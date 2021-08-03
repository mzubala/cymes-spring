package pl.com.bottega.cymes.cinemas;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostgresDBExtension implements BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {
    private final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"));

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        startPostgresDB();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        cleanDB(extensionContext);
    }

    private void cleanDB(ExtensionContext extensionContext) throws SQLException {
        var context = SpringExtension.getApplicationContext(extensionContext);
        var connection = context.getBean(JdbcTemplate.class).getDataSource().getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        List<String> tablesToTruncate = new ArrayList<>();
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tablesToTruncate.add(tableName);
        }
        connection.prepareStatement("TRUNCATE " + tablesToTruncate.stream().collect(Collectors.joining(","))).executeUpdate();
    }

    @Override
    public void close() throws Throwable {
        stopPostgresDB();
    }

    private void startPostgresDB() {
        if (!postgreSQLContainer.isRunning()) {
            postgreSQLContainer.withUsername("app");
            postgreSQLContainer.withPassword("app");
            postgreSQLContainer.withDatabaseName("cinemas");
            postgreSQLContainer.start();
            System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        }
    }

    private void stopPostgresDB() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
    }
}
