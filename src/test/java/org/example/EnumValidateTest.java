package org.example;

import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import static javax.xml.xpath.XPathConstants.NODESET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumValidateTest {

    public static final String PERSISTENCE_UDI = "https://jakarta.ee/xml/ns/persistence";

    @BeforeAll
    static void init() throws XPathExpressionException, SQLException {

        final var stream = EnumValidateTest.class.getClassLoader().getResourceAsStream("META-INF/persistence.xml");
        final var xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(
                new NamespaceContext() {

                    @Override
                    public String getNamespaceURI(String prefix) {
                        return "p".equals(prefix) ? PERSISTENCE_UDI : null;
                    }

                    @Override
                    public String getPrefix(String namespaceURI) {
                        return PERSISTENCE_UDI.equals(namespaceURI) ? "p" : null;
                    }

                    @Override
                    public Iterator<String> getPrefixes(String namespaceURI) {
                        return null;
                    }
                });

        final var expr = xpath.compile("/p:persistence/p:persistence-unit/p:properties/p:property[starts-with(@name,'jakarta.persistence.jdbc.')]");
        final var nodes = (NodeList) expr.evaluate(new InputSource(stream), NODESET);

        final var props = new HashMap<String, String>();
        for (int n = 0; n < nodes.getLength(); ++n) {
            final var item = (Element) nodes.item(n);
            props.put(item.getAttribute("name").substring("jakarta.persistence.jdbc.".length()), item.getAttribute("value"));
        }
        try (var conn = DriverManager.getConnection(props.get("url"), props.get("user"), props.get("password"));
             final var st = conn.createStatement()) {
            st.execute("drop table if exists en");
            st.execute("create table en (day integer, id integer not null auto_increment, year integer, signPosition enum ('AFTER_NO_SPACE','AFTER_WITH_SPACE','BEFORE_NO_SPACE','BEFORE_WITH_SPACE'), primary key (id))");
        }
    }

    @Test
    void test() {
        try (var factory = Persistence.createEntityManagerFactory("manager");
             final var manager = factory.createEntityManager()) {
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
}
