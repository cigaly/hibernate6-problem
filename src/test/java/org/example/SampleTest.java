package org.example;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SampleTest {

    private static final Logger log = LoggerFactory.getLogger(SampleTest.class);

    @Test
    @Disabled
    void test() {
        try (var factory = createEntityManagerFactory("manager")) {
            Sample sample;
            try (final var manager = factory.createEntityManager()) {
                final var tx = manager.unwrap(Session.class).beginTransaction();

                sample = new Sample();
                sample.setValue("Hello, World!");
                manager.persist(sample);
                tx.commit();
                log.debug("Expected {}", sample);

                manager.unwrap(Session.class).clear();
            }
            try (final var manager = factory.createEntityManager()) {
                final var actual = manager.find(Sample.class, sample.getId());
                log.debug("Actual {}", actual);
                assertNotNull(actual);
                assertEquals(sample.getValue(), actual.getValue());
            }
        }
    }

    @Entity
    @Table(name = "sample")
    public static class Sample {

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id", nullable = false, updatable = false)
        private Integer id;

        @Column(length = 64)
        private String value;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Sample{id=%d, value='%s'}".formatted(id, value);
        }
    }
}
