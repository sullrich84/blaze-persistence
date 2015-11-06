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

import java.util.LinkedHashMap;
import java.util.Map;

import com.blazebit.persistence.BaseUpdateCriteriaBuilder;
import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.impl.expression.Expression;
import com.blazebit.persistence.impl.expression.SubqueryExpression;
import com.blazebit.persistence.spi.DbmsStatementType;

/**
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.1.0
 */
public class BaseUpdateCriteriaBuilderImpl<T, X extends BaseUpdateCriteriaBuilder<T, X>, Y> extends AbstractModificationCriteriaBuilder<T, X, Y> implements BaseUpdateCriteriaBuilder<T, X>, SubqueryBuilderListener<X> {

	private final Map<String, Expression> setAttributes = new LinkedHashMap<String, Expression>();
	private SubqueryInternalBuilder<X> currentSubqueryBuilder;
	private String currentAttribute;

	public BaseUpdateCriteriaBuilderImpl(MainQuery mainQuery, boolean isMainQuery, Class<T> clazz, String alias, String cteName, Class<?> cteClass, Y result, CTEBuilderListener listener) {
		super(mainQuery, isMainQuery, DbmsStatementType.UPDATE, clazz, alias, cteName, cteClass, result, listener);
	}

    @Override
    @SuppressWarnings("unchecked")
	public X set(String attributeName, Object value) {
        verifyBuilderEnded();
        checkAttribute(attributeName);
        Expression attributeExpression = parameterManager.addParameterExpression(value);
		setAttributes.put(attributeName, attributeExpression);
		return (X) this;
	}

	@Override
    @SuppressWarnings("unchecked")
    public X setExpression(String attributeName, String expression) {
        verifyBuilderEnded();
        checkAttribute(attributeName);
        Expression attributeExpression = expressionFactory.createScalarExpression(expression);
        setAttributes.put(attributeName, attributeExpression);
        return (X) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SubqueryInitiator<X> set(String attribute) {
        verifySubqueryBuilderEnded();
        this.currentAttribute = attribute;
        return subqueryInitFactory.createSubqueryInitiator((X) this, this);
    }
    
    private void verifySubqueryBuilderEnded() {
        if (currentAttribute != null) {
            throw new BuilderChainingException("An initiator was not ended properly.");
        }
        if (currentSubqueryBuilder != null) {
            throw new BuilderChainingException("An subquery builder was not ended properly.");
        }
    }

    @Override
    public void onBuilderEnded(SubqueryInternalBuilder<X> builder) {
        if (currentAttribute == null) {
            throw new BuilderChainingException("There was an attempt to end a builder that was not started or already closed.");
        }
        if (currentSubqueryBuilder == null) {
            throw new BuilderChainingException("There was an attempt to end a builder that was not started or already closed.");
        }
        setAttributes.put(currentAttribute, new SubqueryExpression(builder));
        currentAttribute = null;
        currentSubqueryBuilder = null;
    }

    @Override
    public void onBuilderStarted(SubqueryInternalBuilder<X> builder) {
        if (currentAttribute == null) {
            throw new BuilderChainingException("There was an attempt to start a builder without an originating initiator.");
        }
        if (currentSubqueryBuilder != null) {
            throw new BuilderChainingException("There was an attempt to start a builder but a previous builder was not ended.");
        }
        currentSubqueryBuilder = builder;
    }

    @Override
    public void onReplaceBuilder(SubqueryInternalBuilder<X> oldBuilder, SubqueryInternalBuilder<X> newBuilder) {
        throw new IllegalArgumentException("Replace not valid!");
    }

    @Override
    public void onInitiatorStarted(SubqueryInitiator<?> initiator) {
        throw new IllegalArgumentException("Initiator started not valid!");
    }

    private void checkAttribute(String attributeName) {
        // Just do that to assert the attribute exists
        JpaUtils.getBasicAttributePath(getMetamodel(), entityType, attributeName);
        Expression attributeExpression = setAttributes.get(attributeName);
        
        if (attributeExpression != null) {
            throw new IllegalArgumentException("The attribute [" + attributeName + "] has already been bound!");
        }
    }

    @Override
	protected void getQueryString1(StringBuilder sbSelectFrom, boolean baseQuery) {
		sbSelectFrom.append("UPDATE ");
		sbSelectFrom.append(entityType.getName()).append(' ');
	    sbSelectFrom.append(entityAlias);
		sbSelectFrom.append(" SET ");

        queryGenerator.setQueryBuffer(sbSelectFrom);
        boolean conditionalContext = queryGenerator.setConditionalContext(false);
        
		for (Map.Entry<String, Expression> attributeEntry : setAttributes.entrySet()) {
			sbSelectFrom.append(attributeEntry.getKey());
			sbSelectFrom.append(" = ");
			attributeEntry.getValue().accept(queryGenerator);
		}

        queryGenerator.setConditionalContext(conditionalContext);
    	appendWhereClause(sbSelectFrom);
	}

}
