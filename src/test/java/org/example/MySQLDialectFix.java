package org.example;

import org.hibernate.dialect.MySQLDialect;

import static org.hibernate.type.SqlTypes.isCharacterType;
import static org.hibernate.type.SqlTypes.isVarcharType;

public class MySQLDialectFix extends MySQLDialect {

    @Override
    public boolean equivalentTypes(int typeCode1, int typeCode2) {
        return super.equivalentTypes(typeCode1, typeCode2)
                || isVarcharType(typeCode1) && isCharacterType(typeCode2);
    }
}
