package com.gmail.eksuzyan.pavel.education.market.model.util;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;

import java.util.Objects;
import java.util.Properties;

@SuppressWarnings("WeakerAccess")
public class DatabaseSettings extends Properties {

    private static final String DB_HOST = "com.gmail.eksuzyan.pavel.market.model.db.host";
    private static final String DB_PORT = "com.gmail.eksuzyan.pavel.market.model.db.port";
    private static final String DB_SID = "com.gmail.eksuzyan.pavel.market.model.db.sid";
    private static final String DB_USER_NAME = "com.gmail.eksuzyan.pavel.market.model.db.user.name";
    private static final String DB_USER_PWD = "com.gmail.eksuzyan.pavel.market.model.db.user.pwd";
    private static final String DB_ADMIN_NAME = "com.gmail.eksuzyan.pavel.market.model.db.admin.name";
    private static final String DB_ADMIN_PWD = "com.gmail.eksuzyan.pavel.market.model.db.admin.pwd";

    private static final String PERSISTENCE_DRIVER = "javax.persistence.jdbc.driver";
    private static final String PERSISTENCE_URL = "javax.persistence.jdbc.url";
    private static final String PERSISTENCE_USERNAME = "javax.persistence.jdbc.user";
    private static final String PERSISTENCE_PASSWORD = "javax.persistence.jdbc.password";

    private static final String JDBC_URL_PATTERN = "jdbc:mysql://%s:%d/%s?useSSL=false";
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final Configuration configuration;

    public DatabaseSettings(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration);

        put(DB_HOST, "localhost");
        put(DB_PORT, String.valueOf(3306));
        put(DB_SID, "market_prod");
        put(DB_USER_NAME, "market-user");
//        put(DB_USER_PWD, "market-user123");
        put(DB_ADMIN_NAME, "market-admin");
//        put(DB_ADMIN_PWD, "market-admin123");

        this.configuration.addDefaults(this);
    }

    public static String createJdbcUrl(String host, int port, String sid) {
        return String.format(JDBC_URL_PATTERN, host, port, sid);
    }

    public Properties getJpaProperties() {
        Properties props = new Properties();

        props.put(PERSISTENCE_DRIVER, JDBC_DRIVER);
        props.put(PERSISTENCE_USERNAME, getDbUsername());
        props.put(PERSISTENCE_PASSWORD, getDbPassword());
        props.put(PERSISTENCE_URL, createJdbcUrl(getDbHost(), getDbPort(), getDbSid()));

        return props;
    }

    public String getDbHost() {
        return configuration.getStringProperty(DB_HOST);
    }

    public int getDbPort() {
        return configuration.getIntProperty(DB_PORT);
    }

    public String getDbSid() {
        return configuration.getStringProperty(DB_SID);
    }

    public String getDbUsername() {
        return configuration.getStringProperty(DB_USER_NAME);
    }

    public String getDbPassword() {
        return configuration.getStringProperty(DB_USER_PWD, "market-user123");
    }

    public String getDbAdminName() {
        return configuration.getStringProperty(DB_ADMIN_NAME);
    }

    public String getDbAdminPwd() {
        return configuration.getStringProperty(DB_ADMIN_PWD, "market-admin123");
    }
}
