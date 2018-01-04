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
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * This is the total lines of code.
 *
 * @author Janine Son
 */
class TotalLinesOfCodeRule extends AbstractAstVisitorRule {
    String name = 'TotalLinesOfCode'
    int priority = 2
    Class astVisitorClass = TotalLinesOfCodeAstVisitor

    int maxLines = 0
}

class TotalLinesOfCodeAstVisitor extends AbstractAstVisitor {

    @Override
    void visitClassEx(ClassNode classNode) {
        def numCommentLines = 0
        def numLines = this.sourceCode.getLines().size()
        def numBlankLines = 0
        boolean isComment = false

        for( int i = 0; i< numLines; i++){
            if (isComment){
                numCommentLines++
                if (this.sourceCode.line(i).endsWith("*/"))
                    isComment = false
            }else{
                if (this.sourceCode.line(i).isEmpty())
                    numBlankLines++
                if (this.sourceCode.line(i).startsWith("//"))
                    numCommentLines++

                if (this.sourceCode.line(i).startsWith("/*")){
                    numCommentLines++
                    isComment=true
                }
            }
        }

        numLines = numLines - numCommentLines - numBlankLines

        if (numLines > rule.maxLines)
            addViolation(
                    classNode, "File has $numLines lines")

        super.visitClassEx(classNode)

    }
}