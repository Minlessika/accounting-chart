/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018-2021 Minlessika, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.database.mock;

import com.minlessika.exceptions.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 * Embedded PostgreSQL DataSource.
 * <p>It's based on H2 compatibility with PostgreSQL.
 * We use HikariCP for Connection Pooling.
 * @see <a href="http://h2database.com/html/features.html?highlight=PostgreSQL%20Compatibility%20Mode&search=PostgreSQL%20Compatibility%20Mode#firstFound">H2 - PostgreSQL Compatibility Mode</a>
 * @since 1.0.0
 * @checkstyle AbbreviationAsWordInNameCheck (5 line)
 */
@SuppressWarnings("PMD.StaticAccessToStaticFields")
public final class EmbeddedPostgreSQLDataSource extends DataSourceWrap {

    /**
     * Maximum pool size.
     */
    private static final int MAXIMUM_POOL_SIZE = 25;

    /**
     * Database name.
     */
    private final String dbname;

    /**
     * If it has been initialized.
     */
    private volatile boolean initialized;

    /**
     * Ctor.
     * @param dbname Database name
     */
    public EmbeddedPostgreSQLDataSource(final String dbname) {
        super(makeDataSource(dbname));
        this.dbname = dbname;
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.tryToInitialize();
        return super.getConnection();
    }

    @Override
    public Connection getConnection(
        final String username,
        final String password
    ) throws SQLException {
        this.tryToInitialize();
        return super.getConnection(username, password);
    }

    /**
     * Try to initialize.
     */
    private void tryToInitialize() {
        if (!this.initialized) {
            synchronized (EmbeddedPostgreSQLDataSource.class) {
                if (!this.initialized) {
                    try (
                        Connection connection = super.getConnection()
                    ) {
                        try (Statement s = connection.createStatement()) {
                            s.execute("drop all objects delete files");
                        }
                    } catch (final SQLException ex) {
                        throw new DatabaseException(
                            String.format(
                                "Error while getting database connexion (database %s).",
                                this.dbname
                            ),
                            ex
                        );
                    } finally {
                        this.initialized = true;
                    }
                }
            }
        }
    }

    /**
     * Make data source.
     * @param dbname Database name
     * @return Data source
     */
    private static DataSource makeDataSource(final String dbname) {
        final HikariConfig configdb = new HikariConfig();
        configdb.setJdbcUrl(
            String.format(
                "jdbc:h2:~/%s;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
                dbname
            )
        );
        configdb.setDriverClassName("org.h2.Driver");
        configdb.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        return new HikariDataSource(configdb);
    }
}
