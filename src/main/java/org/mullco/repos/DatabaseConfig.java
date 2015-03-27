package org.mullco.repos;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    private static BasicDataSource ds;

    public DataSource getDataSource() {
        if (ds == null) {
            ds = new BasicDataSource();
            ds  .setDriverClassName("org.h2.Driver");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setUrl("jdbc:h2:mem:sample;INIT=RUNSCRIPT FROM 'classpath:/create.sql'");
        }

        return ds;
    }
}
