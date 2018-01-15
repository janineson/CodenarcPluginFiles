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
package org.codenarc.rule.exceptions


import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Verify array index to avoid ArrayIndexOutOfBoundsException
 *
 * @author Janine Son
 */
class VerifyArrayIndexRule extends AbstractAstVisitorRule {
    String name = 'VerifyArrayIndex'
    int priority = 2
    Class astVisitorClass = VerifyArrayIndexAstVisitor
}

class VerifyArrayIndexAstVisitor extends AbstractAstVisitor {
    def varName
    @Override
    void visitIfElse(IfStatement ifElse) {
        def statementExpression = ifElse.booleanExpression.expression

        if(statementExpression instanceof BinaryExpression)
            explore statementExpression

        super.visitIfElse(ifElse)
    }

    void explore(BinaryExpression expression){
        if (expression.leftExpression.hasProperty("method")) {
            if (expression.leftExpression.method.value == "size") {
                expression.leftExpression.objectExpression.each {
                    getVarName(it, false)
                }
            }
        }
    }

    void getVarName(VariableExpression expression, Boolean isCheck){
        if (isCheck){
            if(varName != expression.variable)
                addViolation(expression, "Verify array index.")
        } else
            varName = expression.variable
    }

    void getVarName(MethodCallExpression expression, Boolean isCheck){
        if (expression.hasProperty("objectExpression")){
            expression.each {
                if (isCheck){
                    if(varName != it.objectExpression.value)
                        addViolation(expression, "Verify array index.")
                }else
                    getVarName(it.objectExpression,false)
            }
        }else{
            if (isCheck){
                if(varName != expression.objectExpression.variable)
                    addViolation(expression, "Verify array index.")
            }else
                varName = expression.objectExpression.variable
        }

    }

    void getVarName(ConstantExpression expression, Boolean isCheck){
        if (isCheck){
            if(varName != expression.value)
                addViolation(expression, "Verify array index.")
        } else
            varName = expression.value
    }

    void getVarName(PropertyExpression expression, Boolean isCheck){
        if (isCheck){
            if(varName != expression.text)
                addViolation(expression, "Verify array index.")
        } else
            varName = expression.text
    }
    void getVarName(BinaryExpression expression, Boolean isCheck){
        if (isCheck){
            if(varName != expression.text)
                addViolation(expression, "Verify array index.")
        } else
            varName = expression.text
    }


    @Override
    void visitBinaryExpression(BinaryExpression expression) {
        if (expression.leftExpression.hasProperty("method")) {
            if (expression.leftExpression.method.value == "split" && expression.rightExpression instanceof ConstantExpression)
                if (varName == null)
                    addViolation(expression, "Verify array index.")
                else {
                    expression.leftExpression.objectExpression.each {
                        getVarName(it, true)
                    }
                }
        }
        super.visitBinaryExpression(expression)
    }

}
