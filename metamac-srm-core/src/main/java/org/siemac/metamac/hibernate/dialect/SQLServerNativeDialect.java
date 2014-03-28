package org.siemac.metamac.hibernate.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.SQLServerDialect;

public class SQLServerNativeDialect extends SQLServerDialect {

    public SQLServerNativeDialect() {
        super();
        registerColumnType(Types.VARCHAR, "nvarchar($l)");
        registerHibernateType(Types.NVARCHAR, Hibernate.STRING.getName());
    }
}
