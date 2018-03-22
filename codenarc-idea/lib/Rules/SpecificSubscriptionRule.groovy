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

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Subscription must be specific to the Event you are interested in.
 *
 * @author Janine Son
 */
class SpecificSubscriptionRule extends AbstractAstVisitorRule {
    String name = 'SpecificSubscription'
    int priority = 2
    Class astVisitorClass = SpecificSubscriptionAstVisitor
}

class SpecificSubscriptionAstVisitor extends AbstractAstVisitor {
    def eventName, inputType
    Set<String> inputTypeList= new HashSet<String>(Arrays.asList("enum", "number","text", "email", "bool", "password",
    "phone", "decimal", "time"))
    Set<String> inputNames= new HashSet<String>()
    Set<String> capabilityOtherAttr= new HashSet<String>(Arrays.asList("airQuality","audioTrackData", "volume", "battery", "numberOfButtons",
        "carbonDioxide", "color", "hue", "saturation", "colorTemperature","fineDustLevel","dustLevel","energy","eta","data","fanSpeed","latitude",
       "longitude","method","accuracy", "altitudeAccuracy", "heading", "speed", "lastUpdateTime",  "illuminance","image",
        "infraredLevel", "activities","currentActivity", "supportedInputSources", "level","status", "trackData","trackDescription","odorLevel",
        "supportedMachineStates", "remainingTime","operationTime","ovenSetpoint","pH","power","refrigerationSetpoint","humidity","lqi",
        "rssi","soundPressureLevel", "phraseSpoken","goal","steps","temperature","coolingSetpoint","coolingSetpointRange",
        "supportedThermostatFanModes","heatingSetpoint","heatingSetpointRange","supportedThermostatModes","thermostatSetpoint",
        "thermostatSetpointRange","schedule", "threeAxis","tvChannel","ultravioletIndex","stream","voltage","button", "sunrise", "sunset",
        "sunriseTime", "sunsetTime","position"))

    @Override
    void visitMethodCallExpression(MethodCallExpression call){

        if(AstUtil.isMethodNamed(call, 'input')) {
            if (call.arguments.expressions[2] instanceof ConstantExpression)
                inputType = call.arguments.expressions[2]?.value

            if (inputTypeList.contains(inputType)){
                if (call.arguments.expressions[1] instanceof ConstantExpression)
                    inputNames.add(call.arguments.expressions[1].value)

            }

        }

        if(AstUtil.isMethodNamed(call, 'subscribe', 3)) {
            //get the event name they want to subscribe to
            if (call.arguments.expressions[1] instanceof ConstantExpression)
                eventName = call.arguments.expressions[1]?.value
            else
                addViolation(call, 'Subscription must be specific to the Event you are interested in.')

            if (eventName != null){
                //if it is not declared as an input name in the preferences subscription OR
                // the attribute is  ENUM  (not other attribute type - they dont need to be specific because it is already specific)
                // and the event is not specific, it is a violation
                if (!inputNames?.contains(eventName)){
                    if (!capabilityOtherAttr.contains(eventName))
                        if (!eventName?.contains('.'))
                            addViolation(call, 'Subscription must be specific to the Event you are interested in.')

                }

            }

        }

        super.visitMethodCallExpression(call)
    }

}
