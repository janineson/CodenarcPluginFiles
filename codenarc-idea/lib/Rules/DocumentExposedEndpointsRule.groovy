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

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.util.AstUtil

/**
 * Document any exposed endpoints.
 *
 * @author Janine Son
 */
class DocumentExposedEndpointsRule extends AbstractAstVisitorRule {
    String name = 'DocumentExposedEndpoints'
    int priority = 2
    Class astVisitorClass = DocumentExposedEndpointsAstVisitor
}

class DocumentExposedEndpointsAstVisitor extends AbstractAstVisitor {
    boolean hasComment = false

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if(call.method.hasProperty('value'))
            if(call.method.value == "mappings" && call.arguments.expressions[0] instanceof ClosureExpression && !hasComment) {
                addViolation(call, 'Document any exposed endpoints.')
            }


    }

    @Override
    void visitClassEx(ClassNode classNode) {
        def numLines = this.sourceCode.getLines().size()

        for( int i = 0; i< numLines; i++){
            if (this.sourceCode.line(i).startsWith("//"))
                hasComment = true

            if (this.sourceCode.line(i).startsWith("/*")){
                hasComment = true
            }
        }

    }
}
