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

import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * Document external HTTP requests.
 *
 * @author Janine Son
 */
class DocumentExternalHTTPRequestsRule extends AbstractAstVisitorRule {
    String name = 'DocumentExternalHTTPRequests'
    int priority = 2
    Class astVisitorClass = DocumentExternalHTTPRequestsAstVisitor
}

class DocumentExternalHTTPRequestsAstVisitor extends AbstractAstVisitor {
    Set<String> httpCalls = new HashSet<String>(Arrays.asList("httpGet", "httpDelete","httpHead",
        "httpPost","httpPostJson", "httpPutJson"))


    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        if(call.method.hasProperty('value'))
            if(httpCalls.contains(call.method.value)) {
                addViolation(call, 'Document external HTTP requests.')
            }
    }
}