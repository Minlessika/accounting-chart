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

package com.minlessika.accounting.chart.domain.api;

/**
 * Charts.
 * @since 1.0.0
 */
public interface Charts {

    /**
     * Iterate them all.
     * <p>Ordered by id ascending.
     * @return All charts
     */
    Iterable<Chart> iterate();

    /**
     * Total number of charts.
     * @return Number
     */
    Long size();

    /**
     * Get chart by its ID.
     * @param number ID
     * @return Chart
     */
    Chart get(Long number);

    /**
     * Create a chart.
     * @param type Type
     * @param version Version
     * @return Chart created
     */
    Chart add(ChartType type, String version);
    
    /**
     * Remove a chart.
     * @param id Chart id
     */
    void remove(Long id);
}
