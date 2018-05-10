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
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.GStringExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Counts the number of input in a SmartApp.
 *
 * @author Janine Son
 */
class CountInputRule extends AbstractAstVisitorRule {
    String name = 'CountInput'
    int priority = 2
    Class astVisitorClass = CountInputAstVisitor

}

class CountInputAstVisitor extends AbstractAstVisitor {
    //bool will also catch boolean
    List<String> types = Arrays.asList("capability.", "device.", "bool","decimal",
        "email", "enum", "hub", "icon", "number", "password", "phone", "time", "text");

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {

        def capabilityName = "", capabilityName2 = ""
        //count input
        if (AstUtil.isMethodNamed(call, 'input')) {
            if (call.arguments.expressions[2] instanceof ConstantExpression)
                capabilityName = call.arguments.expressions[2]?.value
            else if (call.arguments.expressions[2] instanceof GStringExpression)
                capabilityName = call.arguments.expressions[2].verbatimText

            //special case
            if (call.method instanceof ConstantExpression){
                if (call.method.value == 'input'){
                    if (call.arguments.expressions.size == 2)
                        capabilityName = call.arguments.expressions[1].value
                }
            }

            if (capabilityName != null) {
                for (String s : types) {
                    if (capabilityName.contains(s))
                        addViolation(call, 'This is an input.')
                }
            }

            if (call.arguments.expressions[0] instanceof NamedArgumentListExpression) {

                if (call.arguments.expressions[0].mapEntryExpressions[2].valueExpression instanceof ConstantExpression) {
                    if (call.arguments.expressions[0].mapEntryExpressions[2].keyExpression.value == 'type')
                        capabilityName2 = call.arguments.expressions[0].mapEntryExpressions[2].valueExpression.value
                }

                if (call.arguments.expressions[0].mapEntryExpressions[1].valueExpression instanceof ConstantExpression) {
                    if (call.arguments.expressions[0].mapEntryExpressions[1].keyExpression.value == 'type')
                        capabilityName2 = call.arguments.expressions[0].mapEntryExpressions[1].valueExpression.value
                }

                if (call.arguments.expressions[0].mapEntryExpressions[1].valueExpression instanceof GStringExpression) {
                    if (call.arguments.expressions[0].mapEntryExpressions[1].keyExpression.value == 'type')
                        capabilityName2 = call.arguments.expressions[0].mapEntryExpressions[1].valueExpression.verbatimText
                }

                if (call.arguments.expressions[0].mapEntryExpressions[2].valueExpression instanceof GStringExpression) {
                    if (call.arguments.expressions[0].mapEntryExpressions[2].keyExpression.value == 'type')
                        capabilityName2 = call.arguments.expressions[0].mapEntryExpressions[2].valueExpression.verbatimText
                }

                //special case
                if (call.arguments.expressions[0].mapEntryExpressions[0].valueExpression instanceof ConstantExpression) {
                    if (call.arguments.expressions[0].mapEntryExpressions[0].keyExpression.value == 'type')
                        capabilityName2 = call.arguments.expressions[0].mapEntryExpressions[0].valueExpression.value
                }



                if (capabilityName2 != null) {
                    for (String s2 : types) {
                        if (capabilityName2.contains(s2))
                            addViolation(call, 'This is an input.')
                    }
                }


            }


        } else {
            if (call.arguments.expressions[2] instanceof ConstantExpression)
                capabilityName = (String) call.arguments.expressions[2]?.value
            else if (call.arguments.expressions[2] instanceof GStringExpression)
                capabilityName = (String) call.arguments.expressions[2].verbatimText

            if (capabilityName != null) {
                for (String s : types) {
                    if (capabilityName.contains(s))
                        addViolation(call, 'This is an input.')
                }
            }

        }

        super.visitMethodCallExpression(call)

    }


    @Override
    void visitDeclarationExpression(DeclarationExpression expression) {
        def capabilityName = ""

        if (expression.rightExpression instanceof MapExpression) {

            if (expression.rightExpression?.mapEntryExpressions[0]?.keyExpression?.value == 'type')
                capabilityName = (String) expression.rightExpression.mapEntryExpressions[0]?.valueExpression?.value

            if (expression.rightExpression?.mapEntryExpressions[1]?.keyExpression?.value == 'type')
                capabilityName = (String) expression.rightExpression.mapEntryExpressions[1]?.valueExpression?.value

            if (expression.rightExpression?.mapEntryExpressions[2]?.keyExpression?.value == 'type')
                capabilityName = (String) expression.rightExpression.mapEntryExpressions[2]?.valueExpression?.value


            if (capabilityName != null){
                for (String s: types) {
                    if (capabilityName.contains(s))
                        addViolation(expression, 'This is a device input.')
                }

            }

        }

        super.visitDeclarationExpression(expression)
    }

}