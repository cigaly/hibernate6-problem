package org.example;

import io.hypersistence.utils.hibernate.type.basic.Inet;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.Period;

public class PeriodTest {

    @Test
    void test() {
        final var factory = Persistence.createEntityManagerFactory("manager");
        final var manager = factory.createEntityManager();
        final var tx = manager.unwrap(Session.class).beginTransaction();

        final var p1 = new P();
        p1.setPeriod(Period.ofWeeks(3));
        p1.setInet(new Inet("192.168.56.42"));
        manager.persist(p1);
        tx.commit();
    }
}
