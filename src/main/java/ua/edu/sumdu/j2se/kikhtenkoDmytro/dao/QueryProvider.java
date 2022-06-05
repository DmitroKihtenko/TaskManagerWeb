package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;

public class QueryProvider {
    private static final Logger logger =
            LoggerFactory.getLogger(QueryProvider.class);
    private Statement statement;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private DataSource datasource;

    @Autowired
    public QueryProvider(DataSource dataSource) {
        setDatasource(dataSource);
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(@NonNull DataSource datasource) {
        this.datasource = datasource;
    }

    public void connect() throws SQLException {
        if(connection == null) {
            try {
                connection = datasource.getConnection();
                logger.debug("Using connection to " + connection.
                        getMetaData().getURL());
            } catch (Exception e) {
                logger.error("Failed connection to database" +
                        " with error: " + e.getMessage());
                throw new SQLException(e.getMessage());
            }
        } else {
            throw new IllegalStateException("Connection already" +
                    " set");
        }
    }

    public ResultSet execute(@NonNull String sql) throws SQLException {
        if(connection == null) {
            throw new SQLException("Database connection is not set");
        }
        statement = connection.createStatement();
        statement.execute(sql);
        return statement.getResultSet();
    }

    public ResultSet executePrepared() throws SQLException {
        if(preparedStatement == null) {
            throw new IllegalStateException(
                    "Prepared statement is not created"
            );
        }
        preparedStatement.execute();
        return preparedStatement.getResultSet();
    }

    public void prepare(@NonNull String sql) throws SQLException {
        if(connection == null) {
            throw new SQLException("Database connection is not set");
        }
        preparedStatement = connection.prepareStatement(sql);
    }

    public void setPrepareArguments(int index, Object parameter,
                                    SQLType type) throws SQLException {
        if(preparedStatement != null) {
            preparedStatement.setObject(index, parameter,
                    type.getVendorTypeNumber());
        }
    }

    public void setStreamArgument(int index, InputStream stream)
            throws SQLException {
        if(preparedStatement != null) {
            preparedStatement.setBinaryStream(index, stream);
        }
    }

    public void disconnect() {
        statement = null;
        try {
            if(connection != null) {
                connection.close();

                logger.debug("Disconnected");
            }
        } catch (SQLException e) {
            logger.warn("Disconnection error: " + e.getMessage());
        } finally {
            connection = null;
        }
    }
}
