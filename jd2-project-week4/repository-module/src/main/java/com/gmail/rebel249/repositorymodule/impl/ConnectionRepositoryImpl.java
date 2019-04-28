package com.gmail.rebel249.repositorymodule.impl;

import com.gmail.rebel249.repositorymodule.ConnectionRepository;
import com.gmail.rebel249.repositorymodule.exception.ConnectionStateException;
import com.gmail.rebel249.repositorymodule.exception.IllegalDatabaseDriverRepositoryException;
import com.gmail.rebel249.repositorymodule.exception.IllegalFormatStatementRepositoryException;
import com.gmail.rebel249.repositorymodule.exception.IllegalURISyntaxException;
import com.gmail.rebel249.repositorymodule.property.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Stream;

public class ConnectionRepositoryImpl implements ConnectionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionRepositoryImpl.class);
    private static final String DATABASE_DRIVER_MESSAGE = "Driver not found ";
    private static final String CONNECTION_MESSAGE = "Cannot create connection using properties";
    private static final String ILLEGAL_STATEMENT_MESSAGE = "Cannot create statement";
    private static final String URI_SYNTAX_MESSAGE = "A string describing the parse error";
    private final DatabaseProperties databaseProperties;

    public ConnectionRepositoryImpl(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalDatabaseDriverRepositoryException(DATABASE_DRIVER_MESSAGE + databaseProperties.getDriver());
        }
    }

    @Override
    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", databaseProperties.getUsername());
            properties.setProperty("password", databaseProperties.getPassword());
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "cp1251");
            return DriverManager.getConnection(databaseProperties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException(CONNECTION_MESSAGE);
        }
    }

    @PostConstruct
    private void createDatabaseTables() {
        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                Statement statement = connection.createStatement();
                URL url = getClass().getResource("/" + databaseProperties.getInitFile());
                Path dest = Paths.get(url.toURI());
                String databaseCreationFile = dest.toString();
                String[] databaseInitFile = getQueries(databaseCreationFile);
                for (String databaseFile : databaseInitFile) {
                    statement.addBatch(databaseFile);
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionStateException(CONNECTION_MESSAGE);
            } catch (URISyntaxException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalURISyntaxException(URI_SYNTAX_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException(CONNECTION_MESSAGE);
        }
    }

    private String[] getQueries(String databaseCreationFile) throws SQLException {
        try (Stream<String> fileStream = Files.lines(Paths.get(databaseCreationFile))) {
            return fileStream.reduce(String::concat).orElse("").split(";");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalFormatStatementRepositoryException(ILLEGAL_STATEMENT_MESSAGE);
        }
    }
}
