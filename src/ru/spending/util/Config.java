package ru.spending.util;

import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.storage.SqlStorage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Properties;

// Утильный конфигурационный класс. Так же служит для наполнения тестовыми данными БД.
public class Config {
    private static final Config INSTANCE = new Config();
    private final SqlStorage sqlStorage;
    private final String homeDir = "C:\\projects\\spending";

//    Для локального запуска через файл properties
//    File PROPS = new File(homeDir, "config\\spending.properties");
//    Для хероку через файл properties
//    String  PROPS = "/spending.properties";

    File POPULATE = new File(homeDir, "config\\populate.sql");

    public static Config getINSTANCE() {
        return INSTANCE;
    }

    private Config() {
        //    Для локального запуска через файл properties
//        try (InputStream is = new FileInputStream(PROPS)) {
        //    Для хероку через файл properties
//        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
//            Properties prop = new Properties();
//            prop.load(is);
//            String url = prop.getProperty("db.url");
//            String user = prop.getProperty("db.user");
//            String password = prop.getProperty("db.password");
//            sqlStorage = new SqlStorage(url, user, password);
//        } catch (IOException e) {
//            throw new IllegalStateException("Invalid config file" + PROPS);
//        }

        // Для Хероку и локального запуска
        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL")); // Для локального запуска задать переменную окружения из хероку
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
            sqlStorage = new SqlStorage(dbUrl, username, password);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid credentials");
        }
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    // Метод для наполнения базы тестовыми данными из файла
    public void refillDB() {
        try (BufferedReader br = new BufferedReader(new FileReader(POPULATE, StandardCharsets.UTF_8))) {
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            sqlStorage.sqlHelper.execute(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String ID = "111222333";
        final String ID_NEW = "12345";
        final Payment PAYMENT1 = new Payment(ID, PaymentType.GAS, 2500, "", LocalDate.now(), "1");
        final Payment PAYMENT2 = new Payment(PaymentType.GAS, 3000, "", LocalDate.of(2022, 1, 2), "1");
        final Payment PAYMENT3 = new Payment(PaymentType.CLOTH, 5124, "", LocalDate.of(2022, 2, 3), "1");
        final Payment PAYMENT4 = new Payment(PaymentType.DINNER, 290, "", LocalDate.of(2022, 0, 24), "1");
        final Payment PAYMENT_NEW = new Payment(ID_NEW, PaymentType.GAS, 2800, "", LocalDate.now(), "1");
        sqlStorage.save(PAYMENT1);
        sqlStorage.save(PAYMENT2);
        sqlStorage.save(PAYMENT3);
        sqlStorage.save(PAYMENT4);
    }
}
