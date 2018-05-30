package com.gmail.eksuzyan.pavel.education.market.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.gmail.eksuzyan.pavel.education.market.model.util.DatabaseSettings.JDBC_DRIVER;
import static com.gmail.eksuzyan.pavel.education.market.model.util.DatabaseSettings.createJdbcUrl;

public final class JdbcDatabaseHelper {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcDatabaseHelper.class);

    private static final String CREATE_SCHEMA_PATH = "./distr/db/schema/create_schema.sql";
    private static final String DROP_SCHEMA_PATH = "./distr/db/schema/drop_schema.sql";

    private static final String BATCH_DELIMITER = ";";
    private static final int SUCCESS_CODE = 0;

    private final DatabaseSettings settings;

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new AssertionError("JDBC Driver wasn't found! ");
        }
    }

    public JdbcDatabaseHelper(DatabaseSettings settings) {
        this.settings = Objects.requireNonNull(settings);
    }

    public boolean createSchema() {
        return runScriptByPath(CREATE_SCHEMA_PATH);
    }

    public boolean dropSchema() {
        return runScriptByPath(DROP_SCHEMA_PATH);
    }

    private boolean runScriptByPath(String scriptPath) {
        try (Connection connection = createConnection()) {
            Statement statement = connection.createStatement();

            String script = readScriptContent(scriptPath);

            for (String batch : splitScriptOnBatches(script))
                statement.addBatch(batch);

            int[] resultCodes = statement.executeBatch();

            return IntStream.of(resultCodes).sum() == SUCCESS_CODE;
        } catch (SQLException | IOException e) {
            LOG.error("Something went wrong. ", e);
            return false;
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                createJdbcUrl(settings.getDbHost(), settings.getDbPort(), settings.getDbSid()),
                settings.getDbAdminName(), settings.getDbAdminPwd());
    }

    private static List<String> splitScriptOnBatches(String script) {
        return Stream
                .of(script.split(BATCH_DELIMITER))
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
    }

    private static String readScriptContent(String strPath) throws IOException {
        return readScriptContent(Paths.get(strPath));
    }

    private static String readScriptContent(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
}
