package ru.spending.storage;

import ru.spending.exception.NotExistStorageException;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.sql.ConnectionFactory;
import ru.spending.sql.SqlHelper;
import ru.spending.util.DateUtil;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

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
            ps.setDate(5, java.sql.Date.valueOf(DateUtil.FORMATTER.format(p.getDate())));
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
    public void update(Payment p) {
        delete(p.getId());
        save(p);
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
    public Map<PaymentType, List<Payment>> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<PaymentType, List<Payment>> map = new HashMap<>();
            for (PaymentType paymentType : PaymentType.values()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM costs WHERE type = ?")) {
                    ps.setString(1, paymentType.name());
                    ResultSet rs = ps.executeQuery();
                    map.put(paymentType, getPaymentList(rs));
                }
            }
            return map;
        });
    }

    @Override
    public Map<PaymentType, List<Payment>> getAllSortedByDate(Date startDate, Date endDate) {
        return sqlHelper.transactionalExecute(conn -> {
            Map<PaymentType, List<Payment>> map = new HashMap<>();
            for (PaymentType paymentType : PaymentType.values()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM costs WHERE type = ? AND date BETWEEN ? AND ?")) {
                    ps.setString(1, paymentType.name());
                    ps.setDate(2, (java.sql.Date) startDate);
                    ps.setDate(3, (java.sql.Date) endDate);
                    ResultSet rs = ps.executeQuery();
                    map.put(paymentType, getPaymentList(rs));
                }
            }
            return map;
        });
    }

    @Override
    public List<Payment> getAllByType(PaymentType paymentType) {
        return sqlHelper.execute("SELECT * FROM costs WHERE type = ?", ps -> {
            ps.setString(1, paymentType.name());
            ResultSet rs = ps.executeQuery();
            return getPaymentList(rs);
        });
    }

    @Override
    public int getSumType(PaymentType paymentType) {
        return sqlHelper.execute("SELECT SUM(prise) FROM costs WHERE type = ?", ps -> {
            ps.setString(1, paymentType.name());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    @Override
    public Map<PaymentType, Integer> getSumMapByType() {
        Map<PaymentType, Integer> map = new HashMap<>();
        for(PaymentType pt : PaymentType.values()) {
            map.put(pt, getSumType(pt));
        }
        return map;
    }

    @Override
    public int getSumAll() {
        return sqlHelper.execute("SELECT SUM(prise) FROM costs", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private Payment restorePayment(ResultSet rs) throws SQLException {
        String paymentID = rs.getString("id").trim();
        PaymentType paymentType = PaymentType.valueOf(rs.getString("type"));
        int prise = rs.getInt("prise");
        String description = rs.getString("description");
        Date date = null;
        try {
            date = DateUtil.FORMATTER.parse(rs.getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String userID = rs.getString("user_id").trim();
        return new Payment(paymentID, paymentType, prise, description, date, userID);
    }

    private List<Payment> getPaymentList(ResultSet rs) throws SQLException {
        List<Payment> list = new ArrayList<>();
        while (rs.next()) {
            list.add(restorePayment(rs));
        }
        return list;
    }
}
