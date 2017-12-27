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
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Declare an event handler when subscribing to an event.
 *
 * @author Janine Son
 */
class NoMissingEventHandlerRule extends AbstractAstVisitorRule {
    String name = 'NoMissingEventHandler'
    int priority = 2
    Class astVisitorClass = NoMissingEventHandlerAstVisitor
}

class NoMissingEventHandlerAstVisitor extends AbstractAstVisitor {
    Set<String> methodList = new HashSet<String>(Arrays.asList())

    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        def eventHandler
        if(AstUtil.isMethodNamed(call, 'subscribe', 3)) {
            if (call.arguments.expressions[2] instanceof VariableExpression ) {
                eventHandler = call.arguments.expressions[2].variable.toString()
                //get all method names
                for (def i = 0; i < this.currentClassNode.methodsList.size(); i++) {
                    methodList.add(this.currentClassNode.methodsList[i].name)
                }

                if (!methodList.contains(eventHandler))
                    addViolation(call, 'Event handler not found. Declare an event handler when subscribing to an event.')
            }
        }

        super.visitMethodCallExpression(call)
    }

}
