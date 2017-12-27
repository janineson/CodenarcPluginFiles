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
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * SmartThings restricted methods calls are not allowed.
 *
 * @author Janine Son
 */
class NoRestrictedMethodCallsRule extends AbstractAstVisitorRule {
    String name = 'NoRestrictedMethodCalls'
    int priority = 2
    Class astVisitorClass = NoRestrictedMethodCallsAstVisitor
}

class NoRestrictedMethodCallsAstVisitor extends AbstractAstVisitor {
    Set<String> restrictedMethodSet = new HashSet<String>(Arrays.asList(
        'addShutdownHook',
        'execute',
        'getClass',
        'getMetaClass',
        'setMetaClass',
        'propertyMissing',
        'methodMissing',
        'invokeMethod',
        'mixin',
        'print',
        'printf',
        'println',
        'sleep'))

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if(call.method.hasProperty('value'))
            if (restrictedMethodSet.contains(call.method.value))
                addViolation(call, 'SmartThings restricted methods calls are not allowed.')

        super.visitMethodCallExpression(call)
    }

}
