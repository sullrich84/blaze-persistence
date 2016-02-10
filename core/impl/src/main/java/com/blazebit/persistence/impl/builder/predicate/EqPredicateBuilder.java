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

package com.blazebit.persistence.impl.builder.predicate;

import com.blazebit.persistence.impl.ParameterManager;
import com.blazebit.persistence.impl.SubqueryInitiatorFactory;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.ExpressionFactory;
import com.blazebit.persistence.impl.predicate.EqPredicate;
import com.blazebit.persistence.impl.predicate.PredicateQuantifier;
import com.blazebit.persistence.impl.predicate.QuantifiableBinaryExpressionPredicate;

/**
 *
 * @author Moritz Becker
 */
public class EqPredicateBuilder<T> extends AbstractQuantifiablePredicateBuilder<T> {

    public EqPredicateBuilder(T result, PredicateBuilderEndedListener listener, Expression leftExpression, boolean wrapNot, SubqueryInitiatorFactory subqueryInitFactory, ExpressionFactory expressionFactory, ParameterManager parameterManager) {
        super(result, listener, leftExpression, wrapNot, subqueryInitFactory, expressionFactory, parameterManager);
    }

    @Override
    protected QuantifiableBinaryExpressionPredicate createPredicate(Expression left, Expression right, PredicateQuantifier quantifier) {
        return new EqPredicate(left, right, quantifier);
    }

}
