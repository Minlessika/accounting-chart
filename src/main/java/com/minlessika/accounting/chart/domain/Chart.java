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

package com.minlessika.accounting.chart.domain;

/**
 * Chart of general accounts.
 *
 * <p>It's used to order accounts.
 *
 * @since 1.0.0
 */
public interface Chart {

    /**
     * Name.
     * @return Name
     */
    String name();

    /**
     * Version.
     * @return Version
     */
    String version();

    /**
     * Chart state.
     * @return State
     */
    ChartState state();

    /**
     * Policy.
     * @return Policy
     */
    ChartPolicy policy();

    /**
     * Entity.
     * @return Entity
     */
    Entity entity();

    /**
     * Select a number of accounts.
     * @param start Index of first {@link Account}
     * @param limit Number of {@link Account} to select
     * @param filter Filter on code or name
     * @return Accounts selected
     */
    Iterable<Account> iterate(int start, int limit, String filter);

    /**
     * Total number of accounts.
     * @return Total
     */
    int size();

    /**
     * Add an account.
     * @param code Code
     * @param name Name
     */
    void add(String code, String name);

    /**
     * Remove an account.
     * @param code Code
     */
    void remove(String code);

    /**
     * Contain code's account.
     * @param code Code
     * @return Yes or not
     */
    boolean contains(String code);

    /**
     * Get account by code.
     * @param code Code
     * @return Account
     * @throws IllegalArgumentException If not found
     */
    Account get(String code);
}
