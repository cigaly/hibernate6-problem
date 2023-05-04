package org.example;

import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumValidateTest {

    @Test
    void test() {
        final var factory = Persistence.createEntityManagerFactory("manager");
        final var manager = factory.createEntityManager();
        final var tx = manager.unwrap(Session.class).beginTransaction();

        final var e1 = new E();
        e1.setYear(2023);
        e1.setSignPosition(SignPosition.BEFORE_NO_SPACE);
        e1.setDay(25);
        manager.persist(e1);
        tx.commit();

        final var actual = manager.find(E.class, e1.getId());
        assertNotNull(actual);
        assertEquals(e1.getDay(), actual.getDay());
        assertEquals(e1.getSignPosition(), actual.getSignPosition());
        assertEquals(e1.getYear(), actual.getYear());
        System.out.println(actual);
    }
}
