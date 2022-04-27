package ru.spending.storage;

import ru.spending.exception.NotExistStorageException;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.model.User;
import ru.spending.sql.ConnectionFactory;
import ru.spending.sql.SqlHelper;
import ru.spending.util.DateUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionFactory = new ConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        };
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void save(Payment p) {
        sqlHelper.execute("INSERT into costs (id, type, prise, description, date, user_id) " +
                "values (?, ?, ?, ?, ?, ?)", ps -> {
            ps.setString(1, p.getId());
            ps.setString(2, p.getType().name());
            ps.setInt(3, p.getPrise());
            ps.setString(4, p.getDescription() == null ? "" : p.getDescription());
            ps.setTimestamp(5, Timestamp.valueOf(p.getDate().atStartOfDay().format(DateUtil.DTFORMATTER)));
            ps.setString(6, p.getUserID() == null ? "1" : p.getUserID());
            ps.execute();

            return null;
        });
    }

    @Override
    public Payment get(String id) {
        return sqlHelper.execute("SELECT * FROM costs WHERE id = ?", ps -> {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException("Record not exist: " + id);
            }
            return restorePayment(rs);
        });
    }

    @Override
    public User getUserByEmail(String email) {
        return sqlHelper.execute("SELECT * FROM users WHERE email=?", ps -> {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException("User with email " + email + "not exist");
            }
            String id = rs.getString("id").trim();
            String name = rs.getString("name");
            String password = rs.getString("password");
            LocalDate startDatePeriod = LocalDate.parse(rs.getString("start_period_date").split(" ")[0]);
            return new User(id, name, email, password, startDatePeriod);
        });
    }

    @Override
    public User getUserById(String id) {
        return sqlHelper.execute("SELECT * FROM users WHERE id=?", ps -> {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException("User with email " + id + "not exist");
            }
            String email = rs.getString("email").trim();
            String name = rs.getString("name");
            String password = rs.getString("password");
            LocalDate startDatePeriod = LocalDate.parse(rs.getString("start_period_date").split(" ")[0]);
            return new User(id, name, email, password, startDatePeriod);
        });
    }

    @Override
    public void update(Payment p) {
        sqlHelper.execute("UPDATE costs SET type=?, prise=?, description=?, date=?, user_id=? WHERE id=?", ps -> {
            ps.setString(1, p.getType().name());
            ps.setInt(2, p.getPrise());
            ps.setString(3, p.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(p.getDate().atStartOfDay().format(DateUtil.DTFORMATTER)));
            ps.setString(5, p.getUserID());
            ps.setString(6, p.getId());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("Запись " + p.getId() + " не найдена");
            }
            return null;
        });
    }

    @Override
    public void updateUser(User user) {
        sqlHelper.execute("UPDATE users SET name=?, password=?, start_period_date=? WHERE id=?", ps -> {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setTimestamp(3, Timestamp.valueOf(user.getStartPeriodDate().atStartOfDay().format(DateUtil.DTFORMATTER)));
            ps.setString(4, user.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("User with uuid " + user.getUuid() + " not exist");
            }
            return null;
        });
    }

    @Override
    public void delete(String id) {
        sqlHelper.execute("DELETE FROM costs WHERE id = ?", ps -> {
            ps.setString(1, id);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("Record not exist: " + id);
            }
            return null;
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM costs");
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM costs", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    @Override
    public Map<PaymentType, List<Payment>> getAllSorted(LocalDate startDate, LocalDate endDate) {
        return sqlHelper.execute("SELECT * FROM costs ORDER BY date DESC", ps -> {
            ResultSet rs = ps.executeQuery();
            return getPaymentMap(rs);
        });
    }

    @Override
    public Map<PaymentType, List<Payment>> getAllSortedByUser(String userID, LocalDate startDate, LocalDate endDate) {
        return sqlHelper.execute("SELECT * FROM costs WHERE user_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC", ps -> {
            ps.setString(1, userID);
            ps.setTimestamp(2, Timestamp.valueOf(startDate.atStartOfDay().format(DateUtil.DTFORMATTER)));
            ps.setTimestamp(3, Timestamp.valueOf(endDate.atStartOfDay().format(DateUtil.DTFORMATTER)));
            ResultSet rs = ps.executeQuery();
            return getPaymentMap(rs);
        });
    }

    @Override
    public List<Payment> getAllByType(PaymentType paymentType, String userId, LocalDate startDate, LocalDate endDate) {
        return sqlHelper.execute("SELECT * FROM costs WHERE user_id = ? AND type = ? AND date BETWEEN ? AND ? ORDER BY date DESC", ps -> {
            ps.setString(1, userId);
            ps.setString(2, paymentType.name());
            ps.setTimestamp(3, Timestamp.valueOf(startDate.atStartOfDay().format(DateUtil.DTFORMATTER)));
            ps.setTimestamp(4, Timestamp.valueOf(endDate.atStartOfDay().format(DateUtil.DTFORMATTER)));
            ResultSet rs = ps.executeQuery();
            return getPaymentList(rs);
        });
    }

    @Override
    public int getSumType(PaymentType paymentType, String userId) {
        return sqlHelper.execute("SELECT SUM(prise) FROM costs WHERE user_id = ? AND type = ?", ps -> {
            ps.setString(1, userId);
            ps.setString(2, paymentType.name());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    // Восстановление экземпляра класса Payment по данным из БД
    private Payment restorePayment(ResultSet rs) throws SQLException {
        String paymentID = rs.getString("id").trim();
        PaymentType paymentType = PaymentType.valueOf(rs.getString("type"));
        int prise = rs.getInt("prise");
        String description = rs.getString("description");
        LocalDate date = LocalDate.parse(rs.getString("date").split(" ")[0]);
        String userID = rs.getString("user_id").trim();
        return new Payment(paymentID, paymentType, prise, description, date, userID);
    }

    // Создание листа трат по данным из БД
    private List<Payment> getPaymentList(ResultSet rs) throws SQLException {
        List<Payment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(restorePayment(rs));
        }
        return list;
    }

    // Создание мапы листов трат по данным из БД
    private Map<PaymentType, List<Payment>> getPaymentMap(ResultSet rs) throws SQLException {
        Map<PaymentType, List<Payment>> map = new HashMap<>();
        for (PaymentType pt : PaymentType.values()) {
            map.put(pt, new ArrayList<>());
        }
        while (rs.next()) {
            Payment p = restorePayment(rs);
            List<Payment> list = map.get(p.getType());
            list.add(p);
            map.put(p.getType(), list);
        }
        return map;
    }
}
