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

import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.GStringExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.WhileStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Do not use busy loops. Use schedule instead.
 *
 * @author Janine Son
 */
class NoBusyLoopRule extends AbstractAstVisitorRule {
    String name = 'NoBusyLoop'
    int priority = 2
    Class astVisitorClass = NoBusyLoopAstVisitor

}

class NoBusyLoopAstVisitor extends AbstractAstVisitor {
    String varName = ''
    @Override
    void visitWhileLoop(WhileStatement whileStatement) {
        if (isFirstVisit(whileStatement) && AstUtil.isEmptyBlock(whileStatement.loopBlock)) {
            if (whileStatement.booleanExpression.expression instanceof BinaryExpression)
                if (explore(whileStatement.booleanExpression.expression))
                    addViolation(whileStatement, 'Do not use busy loops. Use schedule instead.')
        }
        super.visitWhileLoop(whileStatement)
    }

    @Override
    void visitBinaryExpression(BinaryExpression expression) {
        explore(expression)

        super.visitBinaryExpression(expression)
    }

    boolean explore(BinaryExpression expression) {
        if(expression.rightExpression instanceof MethodCallExpression && expression.leftExpression instanceof VariableExpression)
            if (expression.rightExpression.method instanceof GStringExpression){
                if (expression.rightExpression.method.values == 'now'){
                    varName = expression.leftExpression.variable
                }
            }else {
                if (expression.rightExpression.method.value == 'now'){
                    varName = expression.leftExpression.variable
                }
            }

        if (expression.leftExpression instanceof MethodCallExpression)
            if (expression.leftExpression.method.value == 'now')
                return true

        if (expression.leftExpression instanceof VariableExpression)
            if (expression.leftExpression.variable == varName )
                return true

        return false
        super.visitBinaryExpression(expression)
    }




}
