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

import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Parent and child SmartApp should exist in separate files.
 *
 * @author Janine Son
 */
class SeparateParentChildAppRule extends AbstractAstVisitorRule {
    String name = 'SeparateParentChildApp'
    int priority = 2
    Class astVisitorClass = SeparateParentChildAppAstVisitor
}

class SeparateParentChildAppAstVisitor extends AbstractAstVisitor {
    boolean isChild = false

    @Override
    void visitMethodCallExpression(MethodCallExpression call){
        if(AstUtil.isMethodNamed(call, 'definition')) {
            if (call.arguments.text.contains('parent')){
                isChild = true
            }
        }
        if(AstUtil.isMethodNamed(call, 'app')) {
            if (isChild)
                addViolation(call, 'Parent and child SmartApp should exist in separate files.')

        }
        super.visitMethodCallExpression(call)
    }

}
