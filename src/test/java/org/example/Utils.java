package org.example;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import static javax.xml.xpath.XPathConstants.NODESET;

public class Utils {
    public static final String PERSISTENCE_URI = "https://jakarta.ee/xml/ns/persistence";

    public static Connection openDbConnection() throws XPathExpressionException, SQLException {
        final var stream = Utils.class.getClassLoader().getResourceAsStream("META-INF/persistence.xml");

        final var xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(
                new NamespaceContext() {

                    @Override
                    public String getNamespaceURI(String prefix) {
                        return "p".equals(prefix) ? PERSISTENCE_URI : null;
                    }

                    @Override
                    public String getPrefix(String namespaceURI) {
                        return PERSISTENCE_URI.equals(namespaceURI) ? "p" : null;
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
        return DriverManager.getConnection(props.get("url"), props.get("user"), props.get("password"));
    }
}
