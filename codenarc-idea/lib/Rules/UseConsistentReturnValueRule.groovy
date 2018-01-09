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

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.TernaryExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Use consistent return values.
 *
 * @author Janine Son
 */
class UseConsistentReturnValueRule extends AbstractAstVisitorRule {
    String name = 'UseConsistentReturnValue'
    int priority = 2
    Class astVisitorClass = UseConsistentReturnValueAstVisitor
}

class UseConsistentReturnValueAstVisitor extends AbstractAstVisitor {
    def dType
    boolean isDynamicTyped
    @Override
    void visitMethodEx(MethodNode node) {
        dType = null
        isDynamicTyped = false
        super.visitMethodEx(node)
    }

    @Override
    void visitReturnStatement(ReturnStatement statement) {
        if (isFirstVisit(statement)) {
            if (dType.is(null)){
                dType = statement.expression.type
                if (statement.expression instanceof VariableExpression)
                    if (statement.expression.isDynamicTyped)
                        isDynamicTyped = true
            }


            if (dType != statement.expression.type) {
                if (statement.expression instanceof VariableExpression)
                    if (statement.expression.isDynamicTyped)
                        isDynamicTyped = true


                if (isDynamicTyped){
                    //ok
                } else {
                    if (dType.name == "java.lang.String" && statement.expression.type.name == "groovy.lang.GString") {
                        //ok
                    } else if (dType.name == "groovy.lang.GString" && statement.expression.type.name == "java.lang.String") {
                        //ok
                    } else if (statement.expression instanceof TernaryExpression) {
                        if (statement.expression.trueExpression.type.name != "groovy.lang.GString")
                            addViolation(statement, "Use consistent return values.")
                    }else
                            addViolation(statement, "Use consistent return values.")
                }

            }
            super.visitReturnStatement(statement)
        }

    }


}