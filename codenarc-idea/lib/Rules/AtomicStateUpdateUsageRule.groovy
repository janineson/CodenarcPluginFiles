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

import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Modifying collections in Atomic State does not work as it does with State. {Read documentation for reference.[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C
 *
 * @author Janine Son
 */
class AtomicStateUpdateUsageRule extends AbstractAstVisitorRule {
    String name = 'AtomicStateUpdateUsage'
    int priority = 2
    Class astVisitorClass = AtomicStateUpdateUsageAstVisitor
}

class AtomicStateUpdateUsageAstVisitor extends AbstractAstVisitor {
    def propertyVal
    boolean isUpdate = false

    @Override
    void visitBinaryExpression(BinaryExpression expression) {
        if(expression.leftExpression instanceof PropertyExpression){
            if (propertyVal.is(null)) {
                if(expression.leftExpression.objectExpression instanceof VariableExpression){
                    if (expression.leftExpression.objectExpression.variable == 'atomicState')
                        propertyVal = expression.leftExpression.property.value
                }

            } else {

                if (propertyVal == String.valueOf(expression.leftExpression.property.value)) {
                    isUpdate = true
                }
            }

        }


        if (expression.rightExpression instanceof MapExpression){
            if (isUpdate) {
                addViolation(
                    expression, 'Modifying collections in Atomic State does not work as it does with State. Read documentation for reference.')

            }
        }

        super.visitBinaryExpression(expression)
    }
}