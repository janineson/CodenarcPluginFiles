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
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * In SmartThings, defining global constant variables will not work. Use a getter method instead.
 *
 * @author Janine Son
 */
class NoGlobalVariableRule extends AbstractAstVisitorRule {
    String name = 'NoGlobalVariable'
    int priority = 2
    Class astVisitorClass = NoGlobalVariableAstVisitor
}

class NoGlobalVariableAstVisitor extends AbstractAstVisitor {
    boolean isOutsideMethod = true

    @Override
     void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
       if (!isConstructor) {
           if (node.name == 'run' || node.name == 'main')
               isOutsideMethod = true
           else
               isOutsideMethod = false
       }
        super.visitConstructorOrMethod(node, isConstructor)
    }

    @Override
    void visitClosureExpression(ClosureExpression expression) {
        isOutsideMethod = false

        super.visitClosureExpression(expression)
    }

    @Override
    void visitDeclarationExpression(DeclarationExpression expression) {
        if(isOutsideMethod && expression.leftExpression instanceof VariableExpression)
            addViolation(
                expression, 'In SmartThings, defining global constant variables will not work. Use a getter method instead.')

        super.visitDeclarationExpression(expression)
    }
}
