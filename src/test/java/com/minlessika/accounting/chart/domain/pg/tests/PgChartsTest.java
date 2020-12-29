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

package com.minlessika.accounting.chart.domain.pg.tests;

import com.minlessika.accounting.chart.domain.api.Chart;
import com.minlessika.accounting.chart.domain.api.ChartType;
import com.minlessika.accounting.chart.domain.api.Charts;
import com.minlessika.accounting.chart.domain.pg.PgCharts;
import com.minlessika.lightweight.db.EmbeddedPostgreSQLDataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import javax.sql.DataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test case for {@link PgCharts}.
 * @since 1.0.0
 * @checkstyle MagicNumberCheck (500 lines)
 */
@SuppressWarnings("PMD.StaticAccessToStaticFields")
public class PgChartsTest {

    /**
     * Data source.
     */
    private static DataSource source;

    /**
     * Thrown.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @SuppressWarnings("PMD.JUnit4TestShouldUseBeforeAnnotation")
    @BeforeClass
    public static void setUp() throws Exception {
        source = new EmbeddedPostgreSQLDataSource();
        try (
            Connection connection = source.getConnection();
            Statement s = connection.createStatement()
        ) {
            s.execute(
                String.join(
                    " ",
                    "CREATE TABLE accounting_chart (",
                    "   id BIGSERIAL NOT NULL,",
                    "   type VARCHAR(25) NOT NULL,",
                    "   state VARCHAR(10) NOT NULL,",
                    "   version VARCHAR(10) NOT NULL,",
                    "   CONSTRAINT accounting_chart_pkey PRIMARY KEY (id)",
                    ")"
                )
            );
            s.execute(
                String.join(
                    " ",
                    "INSERT INTO accounting_chart (type, state, version)",
                    "VALUES ('SYSCOHADA', 'ACTIVE', '2018');"
                )
            );
        }
    }

    @Test
    public void tryToRetrieveAchart() {
        final Charts charts = new PgCharts(source);
        final Chart chart = charts.get(1L);
        MatcherAssert.assertThat(
            chart.number(),
            Matchers.equalTo(1L)
        );
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("Chart with ID=5 not found !");
        charts.get(5L);
    }

    @Test
    public void tryToAddAnewChart() {
        final String version = "2000";
        final Charts charts = new PgCharts(source);
        final Long size = charts.size();
        final Chart chart = charts.add(ChartType.SYSCOHADA, version);
        MatcherAssert.assertThat(
            chart.number(),
            Matchers.greaterThan(1L)
        );
        MatcherAssert.assertThat(
            chart.type(),
            Matchers.equalTo(ChartType.SYSCOHADA)
        );
        MatcherAssert.assertThat(
            chart.version(),
            Matchers.equalTo(version)
        );
        MatcherAssert.assertThat(
            charts.size(),
            Matchers.equalTo(size + 1)
        );
    }

    @Test
    public void tryToRemoveAchart() {
        final Charts charts = new PgCharts(source);
        final Long size = charts.size();
        final Chart chart = charts.add(ChartType.SYSCOHADA, "2050");
        MatcherAssert.assertThat(
            charts.size(),
            Matchers.equalTo(size + 1L)
        );
        charts.remove(chart.number());
        MatcherAssert.assertThat(
            charts.size(),
            Matchers.equalTo(size)
        );
    }

    @Test
    public void iterateAllCharts() {
        final Charts charts = new PgCharts(source);
        Long count = 0L;
        final Iterator<Chart> iterator = charts.iterate().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            count += 1;
        }
        MatcherAssert.assertThat(
            charts.size(),
            Matchers.equalTo(count)
        );
    }
}
