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
import org.codenarc.util.AstUtil

/**
 * Avoid recurring short schedules unless there is a good reason for it.
 *
 * @author Janine Son
 */
class AvoidRecurringShortSchedulesRule extends AbstractAstVisitorRule {
    String name = 'AvoidRecurringShortSchedules'
    int priority = 2
    Class astVisitorClass = AvoidRecurringShortSchedulesAstVisitor
}

class AvoidRecurringShortSchedulesAstVisitor extends AbstractAstVisitor {

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if (AstUtil.isMethodCall(call, "runIn", 2)) {
            if (call.arguments[0].hasProperty('value'))
                if (Integer.parseInt(call.arguments[0].value.toString()) < 300)
                    addViolation(call, "Avoid recurring short schedules unless there is a good reason for it.")
        }

        if (AstUtil.isMethodCall(call, "runEvery1Minute", 1)) {
                addViolation(call, "Avoid recurring short schedules unless there is a good reason for it.")
        }
    }
}
