/*
 * Copyright 2015 Blazebit.
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
package com.blazebit.persistence.impl;

import com.blazebit.persistence.FinalSetOperationCriteriaBuilder;
import com.blazebit.persistence.LeafOngoingSetOperationCriteriaBuilder;
import com.blazebit.persistence.StartOngoingSetOperationCriteriaBuilder;
import com.blazebit.persistence.spi.SetOperationType;

/**
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.1.0
 */
public class LeafOngoingSetOperationCriteriaBuilderImpl<T> extends AbstractCriteriaBuilder<T, LeafOngoingSetOperationCriteriaBuilder<T>, LeafOngoingSetOperationCriteriaBuilder<T>, StartOngoingSetOperationCriteriaBuilder<T, LeafOngoingSetOperationCriteriaBuilder<T>>> implements LeafOngoingSetOperationCriteriaBuilder<T> {

    public LeafOngoingSetOperationCriteriaBuilderImpl(MainQuery mainQuery, boolean isMainQuery, Class<T> clazz, BuilderListener<Object> listener, FinalSetOperationCriteriaBuilderImpl<T> finalSetOperationBuilder) {
        super(mainQuery, isMainQuery, clazz, null, listener, finalSetOperationBuilder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FinalSetOperationCriteriaBuilder<T> endSet() {
        subListener.verifyBuilderEnded();
        listener.onBuilderEnded(this);
        return (FinalSetOperationCriteriaBuilder<T>) finalSetOperationBuilder;
    }

    @Override
    protected BaseFinalSetOperationCriteriaBuilderImpl<T, ?> createFinalSetOperationBuilder(SetOperationType operator, boolean nested) {
        return createFinalSetOperationBuilder(operator, nested, nested);
    }

    @Override
    protected LeafOngoingSetOperationCriteriaBuilder<T> createSetOperand(BaseFinalSetOperationCriteriaBuilderImpl<T, ?> finalSetOperationBuilder) {
        subListener.verifyBuilderEnded();
        listener.onBuilderEnded(this);
        return createLeaf(finalSetOperationBuilder);
    }

    @Override
    protected StartOngoingSetOperationCriteriaBuilder<T, LeafOngoingSetOperationCriteriaBuilder<T>> createSubquerySetOperand(BaseFinalSetOperationCriteriaBuilderImpl<T, ?> finalSetOperationBuilder, BaseFinalSetOperationCriteriaBuilderImpl<T, ?> resultFinalSetOperationBuilder) {
        subListener.verifyBuilderEnded();
        listener.onBuilderEnded(this);
        LeafOngoingSetOperationCriteriaBuilder<T> leafCb = createLeaf(resultFinalSetOperationBuilder);
        return createOngoing(finalSetOperationBuilder, leafCb);
    }

}