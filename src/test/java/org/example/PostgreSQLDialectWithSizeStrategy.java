package org.example;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.jdbc.Size;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;

import java.sql.Types;

public class PostgreSQLDialectWithSizeStrategy extends PostgreSQLDialect {

    private final SizeStrategy sizeStrategy = new SizeStrategyImpl() {
        @Override
        public Size resolveSize(
                JdbcType jdbcType,
                JavaType<?> javaType,
                Integer precision,
                Integer scale,
                Long length) {
            final var size = new Size();
            switch (jdbcType.getDdlTypeCode()) {
                case SqlTypes.TIME:
                case SqlTypes.TIMESTAMP:
                case SqlTypes.TIMESTAMP_UTC:
                case SqlTypes.TIME_UTC:
                    length = null;
                    /*size.setPrecision( javaType.getDefaultSqlPrecision( Dialect.this, jdbcType ) );*/
                    if ( scale != null && scale != 0 ) {
                        throw new IllegalArgumentException("scale has no meaning for timestamps");
                    }
                    break;

                case SqlTypes.BINARY:
                case SqlTypes.VARBINARY:
                    length = null;
                    break;

                default:
                    return super.resolveSize(jdbcType, javaType, precision, scale, length);
            }

            if (precision != null) {
                size.setPrecision(precision);
            }
            if (scale != null) {
                size.setScale(scale);
            } else {
                size.setScale(javaType.getDefaultSqlScale(PostgreSQLDialectWithSizeStrategy.this, jdbcType));
            }
            if (length != null && length != 255 /* default for @Column */) {
                size.setLength(length);
            }

            return size;
        }
    };

    @Override
    public SizeStrategy getSizeStrategy() {
        return sizeStrategy;
    }
}
