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
package org.codenarc.rule.security

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Subscription must be specific to the Event you are interested in.
 *
 * @author Janine Son
 */
class SpecificSubscriptionRule extends AbstractAstVisitorRule {
    String name = 'SpecificSubscription'
    int priority = 2
    Class astVisitorClass = SpecificSubscriptionAstVisitor
}

class SpecificSubscriptionAstVisitor extends AbstractAstVisitor {
    def eventName


    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        if(AstUtil.isMethodNamed(call, 'subscribe', 3)) {
            eventName = call.arguments.expressions[1]

            if (eventName instanceof ConstantExpression && !eventName.text.contains('.')){
                addViolation(call, 'Subscription must be specific to the Event you are interested in.')
            }

        }

        super.visitMethodCallExpression(call)
    }

}
