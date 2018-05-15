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

import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Counts the number of subscriptions in a SmartApp.
 *
 * @author Janine Son
 */
class CountSubscriptionRule extends AbstractAstVisitorRule {
    String name = 'CountSubscription'
    int priority = 2
    Class astVisitorClass = CountSubscriptionAstVisitor
}

class CountSubscriptionAstVisitor extends AbstractAstVisitor {
    Set<String> subscriptions= new HashSet<String>()
    //check for duplicate
    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        //count subscription
        if(AstUtil.isMethodNamed(call, 'subscribe')){
            if (call.arguments.expressions[0] instanceof VariableExpression){
               // call.arguments.expressions[0]?.name != 'location') &&
               // (call.arguments.expressions[0]?.name != 'app') &&
                if (!(subscriptions.contains(call.arguments.expressions[0]?.name + call.arguments.expressions[1].toString()))){
                    subscriptions.add((String) call.arguments.expressions[0]?.name + call.arguments.expressions[1].toString())
                    addViolation(call, 'This is a subscription.')
                }
            }else if (call.arguments.expressions[0] instanceof PropertyExpression){
                if (!(subscriptions.contains(call.arguments.expressions[0].objectExpression.variable + call.arguments.expressions[0].property.value + call.arguments.expressions[1].toString()))){
                    subscriptions.add((String) call.arguments.expressions[0].objectExpression.variable + call.arguments.expressions[0].property.value + call.arguments.expressions[1].toString())
                        addViolation(call, 'This is a subscription.')
                }
            }

        }

        super.visitMethodCallExpression(call)

    }


}
