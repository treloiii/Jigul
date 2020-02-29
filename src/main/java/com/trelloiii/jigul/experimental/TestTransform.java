package com.trelloiii.simplereapitinglib.experimental;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.ModifierNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.lang.reflect.Modifier;

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class TestTransform implements ASTTransformation {
    @Override
    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        if(astNodes!=null){
            for(ASTNode node:astNodes){
                if(node instanceof ClassNode){
                    System.out.println("CLASS SOURCE SUKA");
                    ((ClassNode) node).addField("test", Modifier.PUBLIC,(ClassNode) node,null);
                }
            }
        }
    }

}
