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

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * No. of event handlers.
 *
 * @author Janine Son
 */
class CountEventHandlerRule extends AbstractAstVisitorRule {
    String name = 'CountEventHandler'
    int priority = 2
    Class astVisitorClass = CountEventHandlerAstVisitor
}

class CountEventHandlerAstVisitor extends AbstractAstVisitor {
    Set<String> methodList = new HashSet<String>(Arrays.asList())
    Set<String> subscriptions= new HashSet<String>()

    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        def eventHandler
        //check for duplicate
        //check if 2 args only. then handler is the 2nd arg
        if(AstUtil.isMethodNamed(call, 'subscribe', 2)) {
                if (call.arguments.expressions[1] instanceof VariableExpression ) {
                    eventHandler = call.arguments.expressions[1].variable.toString()
                    checkHandlerExists(call, eventHandler, subscriptions)
                }
                if (call.arguments.expressions[1] instanceof ConstantExpression ) {
                    eventHandler = call.arguments.expressions[1].value
                    checkHandlerExists(call, eventHandler, subscriptions)
                }

        }else{
            if(AstUtil.isMethodNamed(call, 'subscribe')) {
                //normal case is 3 args. handler is always the third arg
                //if args are 3 or more
                if (call.arguments.expressions[2] instanceof VariableExpression ) {
                    eventHandler = call.arguments.expressions[2].variable.toString()
                    checkHandlerExists(call, eventHandler, subscriptions)
                }

                if (call.arguments.expressions[2] instanceof ConstantExpression ) {
                    eventHandler = call.arguments.expressions[2].value
                    checkHandlerExists(call, eventHandler, subscriptions)
                }
            }


        }

        super.visitMethodCallExpression(call)
    }


    void checkHandlerExists(MethodCallExpression call, def eventHandler, Set<String> subscriptions){

        //get all method names
        for (def i = 0; i < this.currentClassNode.methodsList.size(); i++) {
            methodList.add(this.currentClassNode.methodsList[i].name)
        }

        if (!(subscriptions.contains(eventHandler) && methodList.contains(eventHandler))){
            subscriptions.add((String) eventHandler)
            addViolation(call, 'This is an event handler.')
        }

    }
}
