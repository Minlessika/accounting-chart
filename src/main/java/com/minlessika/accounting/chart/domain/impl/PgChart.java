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

package com.minlessika.accounting.chart.domain.impl;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.minlessika.accounting.chart.domain.api.Chart;
import com.minlessika.accounting.chart.domain.api.ChartState;
import com.minlessika.accounting.chart.domain.api.ChartType;
import com.minlessika.exceptions.DatabaseException;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * PostgreSQL Chart.
 * @since 1.0.0
 */
public final class PgChart implements Chart {

    /**
     * Unique identifier.
     */
    private final Long id;

    /**
     * DataSource.
     */
    private final DataSource source;

    /**
     * Ctor.
     * @param source Data source
     * @param number Number
     */
    public PgChart(final DataSource source, final Long number) {
        this.source = source;
        this.id = number;
    }

    @Override
    public Long number() {
        return this.id;
    }

    @Override
    public String name() {
        return String.format(
            "Plan comptable %s révisé version %s",
            this.type().toString(),
            this.version()
        );
    }

    @Override
    public ChartType type() {
        try {
            final String type =
                new JdbcSession(this.source)
                    .sql("SELECT type FROM accounting_chart WHERE id=?")
                    .set(this.id)
                    .select(new SingleOutcome<>(String.class));
            return ChartType.valueOf(type);
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format("Error on PgChart (ID : %s) while getting type.", this.id),
                ex
            );
        }
    }

    @Override
    public String version() {
        try {
            return
                new JdbcSession(this.source)
                    .sql("SELECT version FROM accounting_chart WHERE id=?")
                    .set(this.id)
                    .select(new SingleOutcome<>(String.class));
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format("Error on PgChart (ID : %s) while getting version.", this.id),
                ex
            );
        }
    }

    @Override
    public ChartState state() {
        try {
            final String state =
                new JdbcSession(this.source)
                    .sql("SELECT state FROM accounting_chart WHERE id=?")
                    .set(this.id)
                    .select(new SingleOutcome<>(String.class));
            return ChartState.valueOf(state);
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format("Error on PgChart (ID : %s) while getting state.", this.id),
                ex
            );
        }
    }

    @Override
    public void activate(final boolean enable) {
        final ChartState state;
        if (enable) {
            state = ChartState.ACTIVE;
        } else {
            state = ChartState.INACTIVE;
        }
        try {
            new JdbcSession(this.source)
                .sql("UPDATE accounting_chart SET state=? WHERE id=?")
                .set(state.name())
                .set(this.id)
                .execute();
        } catch (final SQLException ex) {
            throw new DatabaseException(
                String.format("Error on PgChart (ID : %s) while activating.", this.id),
                ex
            );
        }
    }

    @Override
    public String toString() {
        return String.format("PostgreSQL Chart with ID %s", this.id);
    }
}
