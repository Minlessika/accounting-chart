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

package com.minlessika.accounting.chart.domain.pg;

import com.minlessika.accounting.chart.domain.api.Chart;
import com.minlessika.accounting.chart.domain.api.ChartState;
import com.minlessika.accounting.chart.domain.api.ChartType;
import com.minlessika.accounting.chart.domain.api.Charts;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.ListOutcome;
import com.jcabi.jdbc.SingleOutcome;
import com.minlessika.exceptions.DatabaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Charts from PostgreSQL.
 * @since 1.0.0
 */
public final class PgCharts implements Charts {

    /**
     * Data source.
     */
    private final DataSource source;

    /**
     * Ctor.
     * @param source Data source
     */
    public PgCharts(final DataSource source) {
        this.source = source;
    }

    @Override
    public Iterable<Chart> iterate() {
        try {
            return
                new JdbcSession(source)
                    .sql("SELECT id FROM accounting_chart ORDER BY id ASC")
                    .select(
                        new ListOutcome<>(
                            new ListOutcome.Mapping<Chart>() {
                                @Override
                                public Chart map(final ResultSet rset) throws SQLException {
                                    return
                                        new PgChart(
                                            PgCharts.this.source,
                                            rset.getLong(1)
                                        );
                                }
                            }
                        )
                     );
        } catch (final SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Long size() {
        try {
            return
                new JdbcSession(this.source)
                    .sql("SELECT COUNT(*) FROM accounting_chart")
                    .select(new SingleOutcome<>(Long.class));
        } catch (final SQLException ex) {
            throw new DatabaseException(
                "Error on PgCharts while getting size.",
                ex
            );
        }
    }

    @Override
    public Chart get(Long number) {
        try {
            final Long count =
                new JdbcSession(this.source)
                    .sql("SELECT COUNT(*) FROM accounting_chart WHERE id=?")
                    .set(number)
                    .select(new SingleOutcome<>(Long.class));
            if (count.equals(0L)) {
                throw new IllegalArgumentException(
                    String.format("Chart with ID=%s not found !", number)
                );
            }
            return new PgChart(this.source, number);
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format(
                    "Error on PgCharts while getting chart with ID=%s.",
                    number
                ),
                ex
            );
        }
    }

    @Override
    public Chart add(ChartType type, String version) {
        try {
            final Long id = 
                new JdbcSession(this.source)
                    .sql(
                        String.join(
                            " ",
                            "INSERT INTO accounting_chart (type, state, version)",
                            "VALUES (?, ?, ?);"
                        )
                    )
                    .set(type.name())
                    .set(ChartState.ACTIVE.name())
                    .set(version)
                    .insert(new SingleOutcome<>(Long.class));
            return new PgChart(this.source, id);
        } catch (final SQLException ex) {
            throw new DatabaseException(
                "Error on PgCharts while adding a new Chart.",
                ex
            );
        }
    }

    @Override
    public void remove(Long id) {
        try {
            new JdbcSession(this.source)
                .sql("DELETE FROM accounting_chart WHERE id=?")
                .set(id)
                .execute();
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format(
                    "Error on PgCharts while removing Chart with ID=%s.",
                    id
                ),
                ex
            );
        }
    }
}
