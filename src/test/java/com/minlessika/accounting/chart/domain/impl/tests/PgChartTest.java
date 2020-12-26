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

package com.minlessika.accounting.chart.domain.impl.tests;

import com.database.mock.EmbeddedPostgreSQLDataSource;
import com.database.mock.LiquibaseDataSource;
import com.minlessika.accounting.chart.domain.api.Chart;
import com.minlessika.accounting.chart.domain.api.ChartState;
import com.minlessika.accounting.chart.domain.impl.PgChart;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case for {@link PgChart}.
 * @since 1.0.0
 */
@SuppressWarnings("PMD.StaticAccessToStaticFields")
public class PgChartTest {

    /**
     * Data source.
     */
    private static DataSource source;

    @SuppressWarnings("PMD.JUnit4TestShouldUseBeforeAnnotation")
    @BeforeClass
    public static void setUp() throws Exception {
        source = new LiquibaseDataSource(
            new EmbeddedPostgreSQLDataSource("accounting_db"),
            "liquibase/db.changelog-master.xml"
        );
        try (
            Connection connection = source.getConnection();
            Statement s = connection.createStatement()
        ) {
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
    public void givenChartWhenActivateThenReturnStateActive() {
        final Chart chart = new PgChart(source, 1L);
        chart.activate(true);
        MatcherAssert.assertThat(
            chart.state(),
            Matchers.equalTo(ChartState.ACTIVE)
        );
    }

    @Test
    public void givenChartWhenDeactivateThenReturnStateInactive() {
        final Chart chart = new PgChart(source, 1L);
        chart.activate(false);
        MatcherAssert.assertThat(
            chart.state(),
            Matchers.equalTo(ChartState.INACTIVE)
        );
    }

    @Test
    public void givenChartWhenAskForNameThenReturnFullname() {
        final Chart chart = new PgChart(source, 1L);
        MatcherAssert.assertThat(
            chart.name(),
            Matchers.equalTo("Plan comptable SYSCOHADA révisé version 2018")
        );
    }

    @Test
    public void givenChartWhenAskForVersionThenReturnCurrentVersion() {
        final Chart chart = new PgChart(source, 1L);
        MatcherAssert.assertThat(
            chart.version(),
            Matchers.equalTo("2018")
        );
    }
}
