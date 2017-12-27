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
package org.codenarc.rule.design

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Avoid chained runIn() calls.
 *
 * @author Janine Son
 */
class AvoidChainedRunInCallRule extends AbstractAstVisitorRule {
    String name = 'AvoidChainedRunInCall'
    int priority = 2
    Class astVisitorClass = AvoidChainedRunInCallAstVisitor
}

class AvoidChainedRunInCallAstVisitor extends AbstractAstVisitor {
    def methodName

    @Override
    void visitMethodEx(MethodNode node) {
        methodName = node.name.toString()
        super.visitMethodEx(node)
    }

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if (AstUtil.isMethodCall(call, "runIn", 2)) {
            if (call.arguments.expressions[1].hasProperty('variable'))
                if (call.arguments.expressions[1].variable == methodName)
                    addViolation(call, "Avoid chained runIn() calls.")
        }
    }
}
