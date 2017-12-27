/*
 * Copyright 2011 the original author or authors.
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
package org.codenarc.rule.basic

import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Avoid using atomicState and state in the same SmartApp.
 *
 * @author Janine Son
 */
class AtomicStateUsageRule extends AbstractAstVisitorRule {
    String name = 'AtomicStateUsage'
    int priority = 2
    Class astVisitorClass = AtomicStateUsageAstVisitor
}

class AtomicStateUsageAstVisitor extends AbstractAstVisitor {
    boolean isAtomic = false


    @Override
    void visitPropertyExpression(PropertyExpression expression) {
        if (expression.objectExpression instanceof  VariableExpression){
            if (expression.objectExpression.variable == 'atomicState')
                isAtomic = true

            if (expression.objectExpression.variable == 'state')
                if (isAtomic)
                    addViolation(
                        expression, 'Avoid using atomicState and state in the same SmartApp.')
        }

        super.visitPropertyExpression(expression)
    }
}
