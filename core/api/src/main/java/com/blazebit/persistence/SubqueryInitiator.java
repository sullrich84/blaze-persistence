/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blazebit.persistence;

/**
 * An interface used to create subquery builders.
 *
 * @param <T> The builder type that is returned on terminal operations
 * @author Christian Beikov
 * @since 1.0
 */
public interface SubqueryInitiator<T> {

    /**
     * Like {@link SubqueryInitiator#from(java.lang.Class, java.lang.String)} with the
     * alias equivalent to the camel cased result of what {@link Class#getSimpleName()} of the entity class returns.
     *
     * @param entityClass The entity class which should be the entity
     * @return A new subquery builder
     */
    public SubqueryBuilder<T> from(Class<?> entityClass);

    /**
     * Like {@link SubqueryInitiator#fromCte(Class, String, String))} with the
     * alias equivalent to the camel cased result of what {@link Class#getSimpleName()} of the cte class returns.
     *
     * @param cteClass The cte class which should be queried
     * @param cteName The name of the cte which should be queried
     * @return The query builder for chaining calls
     * @since 1.1.0
     */
    public SubqueryBuilder<T> fromCte(Class<?> cteClass, String cteName);

    /**
     * Creates a new subquery builder with the given entity class as entity in the FROM clause with the given alias.
     *
     * @param entityClass The entity class which should be the entity
     * @param alias The alias for the entity
     * @return A new subquery builder
     */
    public SubqueryBuilder<T> from(Class<?> entityClass, String alias);

    /**
     * Creates a new subquery builder with the given cte class as entity in the FROM clause with the given alias.
     *
     * @param cteClass The cte class which should be queried
     * @param cteName The name of the cte which should be queried
     * @param alias The alias for the cte
     * @return The query builder for chaining calls
     * @since 1.1.0
     */
    public SubqueryBuilder<T> fromCte(Class<?> cteClass, String cteName, String alias);

    // TODO: documentation
    public StartOngoingSetOperationSubqueryBuilder<T, LeafOngoingSetOperationSubqueryBuilder<T>> startSet();
}
