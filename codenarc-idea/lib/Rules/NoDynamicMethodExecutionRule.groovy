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

import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Do not use dynamic method execution.Validate input first.
 *
 * @author Janine Son
 */
class NoDynamicMethodExecutionRule extends AbstractAstVisitorRule {
    String name = 'NoDynamicMethodExecution'
    int priority = 2
    Class astVisitorClass = NoDynamicMethodExecutionAstVisitor
}

class NoDynamicMethodExecutionAstVisitor extends AbstractAstVisitor {
    def methodName
    Set<String> httpCalls = new HashSet<String>(Arrays.asList("httpGet", "httpDelete","httpHead",
        "httpPost","httpPostJson", "httpPutJson"))
    Set<String> paramNames= new HashSet<String>()

    @Override
    void visitMethodCallExpression(MethodCallExpression call){

        if (call.method.hasProperty('value'))
            if (httpCalls.contains(call.method.value))
                if (call.arguments instanceof ArgumentListExpression){
                    if(call.arguments.hasProperty('expressions'))
                        if (call.arguments.expressions[1] instanceof ClosureExpression)
                            if(call.arguments.expressions[1].parameters.length > 0)
                                    paramNames.add(call.arguments.expressions[1].parameters[0].name)
                }

        if(AstUtil.classNodeImplementsType(call.method.getType(), GString))
            if (call.objectExpression instanceof VariableExpression)
                if (paramNames.contains(call.objectExpression.variable))
                    addViolation(call, 'Do not use dynamic method execution.' +
                        ' Validate input first.')


        super.visitMethodCallExpression(call)
    }

}
