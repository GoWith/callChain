package cn.fireface.call.core.proxy;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import java.util.Set;

import static com.sun.tools.javac.util.List.nil;

/**
 * Created by maoyi on 2018/10/23.
 * don't worry , be happy
 */
@SupportedAnnotationTypes("cn.fireface.call.core.proxy.CallChain")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class Process extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println("init");
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        elementUtils = processingEnv.getElementUtils();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(CallChain.class);
        Set<? extends Element> set = roundEnv.getRootElements();
        for (Element element : set) {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = nil();
                    List<JCTree> myDefs = nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if(tree.getKind().equals(Tree.Kind.METHOD)){
                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) tree;
                            if (jcMethodDecl.getName().toString().equals("<init>")){
                                myDefs = myDefs.append(tree);
                                continue;
                            }
                            myDefs = myDefs.append(addCall(jcMethodDecl,jcClassDecl));
                        }else {
                            myDefs=myDefs.append(tree);
                        }
                    }
                    jcClassDecl.defs = myDefs;
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    JCTree.JCStatement execute(JCTree.JCStatement statement, final String key){
        if (!check(statement.getKind())) {
            return statement;
        }
        statement.accept(new JCTree.Visitor() {
            @Override
            public void visitTry(JCTree.JCTry jcTry) {
                JCTree.JCBlock tryBody = jcTry.body;
                jcTry.body = buildNewJcBlock(tryBody, key);
                List<JCTree.JCCatch> catches = jcTry.getCatches();
                for (JCTree.JCCatch aCatch : catches) {
                    JCTree.JCBlock catchBody = aCatch.body;
                    aCatch.body= buildNewJcBlock(catchBody, key);
                }
            }

            @Override
            public void visitIf(JCTree.JCIf jcIf) {
                JCTree.JCStatement thenpart = jcIf.thenpart;
                if (check(thenpart.getKind())) {
                    jcIf.thenpart = execute(thenpart,key);
                }else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) thenpart;
                    jcIf.thenpart= buildNewJcBlock(thenBody, key);
                }
                JCTree.JCStatement elseStatement = jcIf.getElseStatement();
                if (elseStatement!=null) {
                    if (check(elseStatement.getKind())) {
                        jcIf.elsepart = execute(elseStatement,key);
                    }else {
                        JCTree.JCBlock elseBody = (JCTree.JCBlock) elseStatement;
                        jcIf.elsepart = buildNewJcBlock(elseBody, key);
                    }
                }
                System.out.println("");
            }


            @Override
            public void visitForLoop(JCTree.JCForLoop jcForLoop) {
                JCTree.JCStatement body = jcForLoop.body;
                if (check(body.getKind())) {
                    jcForLoop.body = execute(body,key);
                }else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) body;
                    jcForLoop.body= buildNewJcBlock(thenBody, key);
                }
            }

            @Override
            public void visitForeachLoop(JCTree.JCEnhancedForLoop jcEnhancedForLoop) {
                JCTree.JCStatement body = jcEnhancedForLoop.body;
                if (check(body.getKind())) {
                    jcEnhancedForLoop.body = execute(body,key);
                }else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) body;
                    jcEnhancedForLoop.body= buildNewJcBlock(thenBody, key);
                }
            }

            @Override
            public void visitCatch(JCTree.JCCatch jcCatch) {
                JCTree.JCBlock body = jcCatch.body;
                JCTree.JCBlock body1 = buildNewJcBlock(body, key);
                jcCatch.body=body1;
            }
        });
        return statement;
    }

    private JCTree.JCBlock buildNewJcBlock(JCTree.JCBlock body, String key) {
        List<JCTree.JCStatement> statements = body.getStatements();

        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        for (JCTree.JCStatement jcStatement : statements) {
            if (check(jcStatement.getKind())) {
                JCTree.JCStatement execute = execute(jcStatement, key);
                jcStatements=jcStatements.append(execute);
                continue;
            }
            if (jcStatement.getKind()== Tree.Kind.RETURN) {
                JCTree.JCExpressionStatement jcExpressionStatement = buildEndCall(key);
                jcStatements=jcStatements.append(jcExpressionStatement);
            }
            jcStatements=jcStatements.append(jcStatement);
        }
        return treeMaker.Block(0, jcStatements.toList());
    }

    private boolean check(JCTree.Kind kind){
        return kind== Tree.Kind.TRY ||
                kind == Tree.Kind.IF||
                kind == Tree.Kind.FOR_LOOP||
                kind == Tree.Kind.CATCH;
    }




    private JCTree.JCExpressionStatement buildEndCall(String key){
        JCTree.JCFieldAccess selectEnd = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("cn.fireface.call.core.utils")), names.fromString("LogPool")), names.fromString("endLog"));
        JCTree.JCMethodInvocation testEnd = treeMaker.Apply(List.<JCTree.JCExpression>nil(), selectEnd, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
        JCTree.JCExpressionStatement exec = treeMaker.Exec(testEnd);
        return exec;
    }


    private JCTree.JCMethodDecl addCall(JCTree.JCMethodDecl jcMethodDecl,JCTree.JCClassDecl jcClassDecl){

        try {
            String key = getKey(jcMethodDecl, jcClassDecl);

            JCTree.JCBlock body = jcMethodDecl.getBody();
            ListBuffer<JCTree.JCStatement> statementBuffer = new ListBuffer<>();
            JCTree.JCExpressionStatement startCallStatement = buildStartCall(key);
            JCTree.JCExpressionStatement endCallStatement = buildEndCall(key);
            List<JCTree.JCStatement> OldStatements = body.getStatements();
            JCTree returnType = jcMethodDecl.getReturnType();
            if (returnType.type.getKind() == TypeKind.VOID) {
                statementBuffer.append(startCallStatement).appendArray(OldStatements.toArray(new JCTree.JCStatement[OldStatements.size()])).append(endCallStatement);
            }else {
                statementBuffer.append(startCallStatement);
                for (JCTree.JCStatement jcStatement : OldStatements) {
                    if(jcStatement.getKind()== Tree.Kind.RETURN){
                        statementBuffer.append(endCallStatement).append(jcStatement);
                        continue;
                    }
                    statementBuffer.append(execute(jcStatement,key));
                }
            }
            JCTree.JCBlock body1 = treeMaker.Block(0, statementBuffer.toList());
            return treeMaker.MethodDef(jcMethodDecl.getModifiers(),jcMethodDecl.getName(),jcMethodDecl.restype, jcMethodDecl.getTypeParameters(),jcMethodDecl.getParameters(),jcMethodDecl.getThrows(),body1,jcMethodDecl.defaultValue);
        } catch (Exception e) {
            return jcMethodDecl;
        }
    }

    private String getKey(JCTree.JCMethodDecl jcMethodDecl, JCTree.JCClassDecl jcClassDecl) {
        String classFullName = jcClassDecl.sym.fullname.toString();
        String methodName = jcMethodDecl.getName().toString();
        return classFullName+"."+methodName;
    }

    private JCTree.JCExpressionStatement buildStartCall(String key) {
        JCTree.JCFieldAccess select = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("cn.fireface.call.core.utils")), names.fromString("LogPool")), names.fromString("startLog"));
        JCTree.JCMethodInvocation test = treeMaker.Apply(List.<JCTree.JCExpression>nil(), select, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
        return treeMaker.Exec(test);
    }
}
