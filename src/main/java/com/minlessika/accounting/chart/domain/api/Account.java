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
 * An account.
 *
 * <p>Account is used to create entries.
 *
 * @since 1.0.0
 */
public interface Account {

    /**
     * Code.
     * @return Code
     */
    String code();

    /**
     * Name.
     * @return Name
     */
    String name();

    /**
     * Is reconciliation allowed.
     * @return Yes or no
     */
    boolean isReconciliationAllowed();

    /**
     * Is deprecated.
     * @return Yes or no
     */
    boolean isDeprecated();

    /**
     * Update informations.
     * @param code Code
     * @param name Name
     * @throws IllegalArgumentException If code already exists
     */
    void update(String code, String name);

    /**
     * Depreciate account.
     * @param enable Depreciate or not
     */
    void depreciate(boolean enable);

    /**
     * Allow reconciliation on account.
     * @param enable Allow or not
     */
    void allowReconciliation(boolean enable);

    /**
     * Clone account.
     * @param code Code of new account
     * @param name Name of new account
     */
    void clone(String code, String name);
}
