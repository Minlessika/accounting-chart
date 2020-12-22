/*
 * Copyright (c) 2018-2020, Minlessika Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of Minlessika nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.minlessika.accounting.chart.domain;

/**
 * Chart of accounts.
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
     * Add a general account.
     * @param code Code
     * @param name Name
     */
    void add(String code, String name);

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
