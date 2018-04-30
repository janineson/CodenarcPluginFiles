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

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Counts the number of input in a SmartApp.
 *
 * @author Janine Son
 */
class CountInputRule extends AbstractAstVisitorRule {
    String name = 'CountInput'
    int priority = 2
    Class astVisitorClass = CountInputAstVisitor

}

class CountInputAstVisitor extends AbstractAstVisitor {
    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        def capabilityName
        //count input
        if(AstUtil.isMethodNamed(call, 'input')){
            if (call.arguments.expressions[2] instanceof ConstantExpression)
                capabilityName = call.arguments.expressions[2]?.value

                if (capabilityName?.contains('capability.'))
                    addViolation(call, 'This is a device input.')

        }

        super.visitMethodCallExpression(call)

    }

}
