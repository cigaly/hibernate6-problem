package org.example;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPathExpressionException;
import java.sql.SQLException;
import java.util.ArrayList;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonTest {

    private static final Logger log = LoggerFactory.getLogger(JsonTest.class);

    /*@BeforeAll*/
    static void init() throws XPathExpressionException, SQLException {
        try (var conn = Utils.openDbConnection();
             final var st = conn.createStatement()) {
            st.execute("drop table if exists temporals cascade");
            st.execute("create table temporals (id serial not null, instant timestamp with time zone, primary key (id))");
        }
    }

    @AfterAll
    static void shutdown() throws XPathExpressionException, SQLException {
        try (var conn = Utils.openDbConnection()) {
            final var metaData = conn.getMetaData();
            try (var rs = metaData.getColumns(null, null, null, null)) {
                final var meta = rs.getMetaData();
                final var names = new ArrayList<String>();
                for (int n = 1; n <= meta.getColumnCount(); ++n) {
                    names.add(meta.getColumnName(n));
                }
                while (rs.next()) {
                    if ("public".equalsIgnoreCase(rs.getString("TABLE_SCHEM"))) {
                        for (final var name : names) {
                            final var value = rs.getObject(name);
                            if (!rs.wasNull()) {
                                log.debug("{}: {}", name, value);
                            }
                        }
                        log.debug("");
                    } else {
                        log.debug("Scheme {}", rs.getString("TABLE_SCHEM"));
                    }
                }
            }
        }
    }

    @Test
    void test() {
        try (var factory = createEntityManagerFactory("manager")) {
            JsonData sample;
            try (final var manager = factory.createEntityManager()) {
                final var tx = manager.unwrap(Session.class).beginTransaction();

                sample = new JsonData();
                manager.persist(sample);
                tx.commit();
                log.debug("Expected {}", sample);

                manager.unwrap(Session.class).clear();
            }
            try (final var manager = factory.createEntityManager()) {
                final var actual = manager.find(JsonData.class, sample.getId());
                log.debug("Actual {}", actual);
                assertNotNull(actual);
            }
        }
    }

    @Entity
    @Table(name = "js")
    public static class JsonData {

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id", nullable = false, updatable = false)
        private Integer id;

        @Type(JsonBinaryType.class)
        @Column(
                length = 8192,
                columnDefinition = "jsonb"
        )
        private Object object;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
