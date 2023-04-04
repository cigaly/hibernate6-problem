package org.example;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

import static org.hibernate.type.SqlTypes.OTHER;

public class HyperPostgreSQLDialect extends org.hibernate.dialect.PostgreSQLDialect {

    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);

        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();

        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(OTHER, "interval", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(OTHER, "inet", this));
    }
}
