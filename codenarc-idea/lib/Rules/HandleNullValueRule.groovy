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
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Handle null values. Avoid NullPointerException by using the save navigation (?) operator.
 *
 * @author Janine Son
 */
class HandleNullValueRule extends AbstractAstVisitorRule {
    String name = 'HandleNullValue'
    int priority = 2
    Class astVisitorClass = HandleNullValueAstVisitor
}

class HandleNullValueAstVisitor extends AbstractAstVisitor {
    //todo
    Set<String> propertySet = new HashSet<String>(Arrays.asList("location", "lanEvent"))

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if (call.objectExpression instanceof PropertyExpression)
            explore(call.objectExpression, call)

        super.visitMethodCallExpression(call)
    }


    void explore(PropertyExpression expression, MethodCallExpression call){
        if (expression.hasProperty('objectExpression'))
            if (expression.objectExpression instanceof PropertyExpression)
                explore(expression.objectExpression, call)
            else {
                if (expression.objectExpression.hasProperty('variable'))
                    if (propertySet.contains(expression.objectExpression.variable)  && !call.safe){
                        if (call.objectExpression instanceof PropertyExpression) {
                            if (call.objectExpression.property.value != "helloHome") //exception. http://docs.smartthings.com/en/latest/smartapp-developers-guide/routines.html#execute-routines
                                addViolation(call, 'Handle null values. Avoid NullPointerException by using the safe navigation (?) operator.')
                        }else
                            addViolation(call, 'Handle null values. Avoid NullPointerException by using the safe navigation (?) operator.')
                    }




            }

    }

}
