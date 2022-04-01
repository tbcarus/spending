package ru.spending.storage;

import org.junit.Before;
import org.junit.Test;
import ru.spending.exception.ExistStorageException;
import ru.spending.exception.NotExistStorageException;
import ru.spending.model.Payment;
import ru.spending.model.PaymentType;
import ru.spending.util.Config;
import ru.spending.util.DateUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SqlStorageTest {
    protected SqlStorage storage = Config.getINSTANCE().getSqlStorage();

    private static final String ID = "111222333";
    private static final String ID_NEW = "12345";
    private static final Payment PAYMENT1 = new Payment(ID, PaymentType.GAS, 2500, "", LocalDate.now(), "1");
    private static final Payment PAYMENT2 = new Payment(PaymentType.GAS, 3000, "", LocalDate.of(2022, 1, 2), "1");
    private static final Payment PAYMENT3 = new Payment(PaymentType.CLOTH, 5124, "", LocalDate.of(2022, 2, 3), "1");
    private static final Payment PAYMENT4 = new Payment(PaymentType.DINNER, 290, "", LocalDate.of(2022, 0, 24), "1");
    private static final Payment PAYMENT_NEW = new Payment(ID_NEW, PaymentType.GAS, 2800, "", LocalDate.now(), "1");


    @Before
    public void setUp() {
        Config.getINSTANCE().refillDB();
        storage.save(PAYMENT1);
        storage.save(PAYMENT2);
        storage.save(PAYMENT3);
        storage.save(PAYMENT4);
    }

    @Test
    public void save() {
        storage.save(PAYMENT_NEW);
        assertGet(PAYMENT_NEW);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(PAYMENT1);
    }

    @Test
    public void get() {
        Payment payment = storage.get(ID);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("Not Exist payment");
    }

    @Test
    public void update() {
        storage.save(PAYMENT_NEW);
        Payment paymentUpdated = new Payment(ID_NEW, PaymentType.CHILDREN, 5800, "Кроссовки", LocalDate.now(), "2");
        storage.update(paymentUpdated);
        assertGet(paymentUpdated);
    }

    @Test
    public void delete() {
        storage.delete(ID);
        assertSize(11);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("Not Exist payment");
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void size() {
        assertSize(12);
    }

    @Test
    public void getAllSorted() {
        Map<PaymentType, List<Payment>> map = storage.getAllSorted();
        int size = 0;
        for (PaymentType pt : map.keySet()) {
            size += map.get(pt).size();
        }
        assertSize(size);
    }

    @Test
    public void getAllByType() {
        List<Payment> list = storage.getAllByType(PaymentType.GAS);
        assertEquals(3, list.size());
    }

    @Test
    public void getSumType() {
        assertEquals(8500, storage.getSumType(PaymentType.GAS));
    }

    public void assertGet(Payment p) {
        assertEquals(p, storage.get(p.getId()));
    }

    public void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}