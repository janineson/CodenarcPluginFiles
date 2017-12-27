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

import org.codehaus.groovy.ast.stmt.EmptyStatement
import org.codehaus.groovy.ast.stmt.SwitchStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Missing default: case statement in switch().
 *
 * @author Janine Son
 */
class MissingSwitchDefaultRule extends AbstractAstVisitorRule {
    String name = 'MissingSwitchDefault'
    int priority = 2
    Class astVisitorClass = MissingSwitchDefaultAstVisitor
}

class MissingSwitchDefaultAstVisitor extends AbstractAstVisitor {
    @Override
    void visitSwitch(SwitchStatement statement) {
        if (statement.defaultStatement instanceof  EmptyStatement){
            addViolation(
                statement, 'Missing default: case statement in switch().')

        }

        super.visitSwitch(statement)
    }
}
