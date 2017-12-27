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

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Do not hard-code SMS messages.
 *
 * @author Janine Son
 */
class NoHardcodeSMSRule extends AbstractAstVisitorRule {
    String name = 'NoHardcodeSMS'
    int priority = 2
    Class astVisitorClass = NoHardcodeSMSAstVisitor
}

class NoHardcodeSMSAstVisitor extends AbstractAstVisitor {
    Set<String> contactName = new HashSet<String>()

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {

        if(AstUtil.isMethodNamed(call, 'input')) {
            def arg1 = call.arguments.expressions[2]
            if (arg1 instanceof ConstantExpression)
                if (arg1.value.equals('contact'))
                    contactName.add(call.arguments.expressions[1].value)
        }


        if(AstUtil.isMethodNamed(call, 'sendNotificationToContacts', 2)) {
            def attributeName = call.arguments.expressions[1]
            if (attributeName instanceof ConstantExpression)
                addViolation(call, 'Do not hard-code SMS messages.')
            if (attributeName instanceof VariableExpression)
                if (!contactName.contains(attributeName.variable))
                    addViolation(call, 'Do not hard-code SMS messages.')
        }


        super.visitMethodCallExpression(call)
    }


}
