package cn.fireface.proxy;

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
import javax.lang.model.util.Elements;
import java.util.Set;

import static com.sun.tools.javac.util.List.nil;

/**
 * Created by maoyi on 2018/10/23.
 * don't worry , be happy
 */
@SupportedAnnotationTypes("proxy.UmpProxy")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
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
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(UmpProxy.class);
        for (Element element : set) {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = nil();
                    List<JCTree> myDefs = nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                            myDefs = myDefs.append(tree);
                        }
                        if(tree.getKind().equals(Tree.Kind.METHOD)){
                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) tree;
                            if (jcMethodDecl.getName().toString().equals("<init>")){
                                myDefs = myDefs.append(tree);
                                continue;
                            }
                            myDefs = myDefs.append(addCall(jcMethodDecl,jcClassDecl));
                        }
                    }
                    jcClassDecl.defs = myDefs;
                    System.out.println(jcClassDecl.toString());
//                    jcVariableDeclList.forEach(jcVariableDecl -> {
//                        messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
//                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
//                    });
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    private JCTree.JCMethodDecl addCall(JCTree.JCMethodDecl jcMethodDecl,JCTree.JCClassDecl jcClassDecl){

        String classFullName = null;
        try {
            classFullName = jcClassDecl.sym.fullname.toString();
        } catch (Exception e) {
            return jcMethodDecl;
        }
        String methodName = jcMethodDecl.getName().toString();
        String key = classFullName+"."+methodName;
        JCTree.JCBlock body = jcMethodDecl.getBody();
        List<JCTree.JCStatement> statements1 = body.getStatements();
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCFieldAccess select = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("utils")), names.fromString("LogPool")), names.fromString("startLog"));
        JCTree.JCMethodInvocation test = treeMaker.Apply(List.<JCTree.JCExpression>nil(), select, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
//        JCTree.JCMethodInvocation test = treeMaker.Apply(List.nil(), select, List.of(treeMaker.Literal("test111")));

        JCTree.JCFieldAccess selectEnd = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("utils")), names.fromString("LogPool")), names.fromString("endLog"));
        JCTree.JCMethodInvocation testEnd = treeMaker.Apply(List.<JCTree.JCExpression>nil(), selectEnd, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
        JCTree.JCExpressionStatement exec = treeMaker.Exec(testEnd);

        List<JCTree.JCStatement> jcStatements = nil();

        for (int i = 0; i < statements1.size(); i++) {
            if(statements1.get(i).toString().contains("return ")||statements1.get(i).toString().contains("return;")){
                jcStatements = jcStatements.append(exec);
            }
            jcStatements = jcStatements.append(statements1.get(i));
        }
        if (jcStatements.size() == statements1.size()) {
            jcStatements = jcStatements.append(exec);
        }

        statements.append(treeMaker.Exec(test)).appendArray(jcStatements.toArray(new JCTree.JCStatement[jcStatements.size()]));

//        JCTree.JCFieldAccess selectEnd = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("utils")), names.fromString("LogPool")), names.fromString("endLog"));
//        JCTree.JCMethodInvocation testEnd = treeMaker.Apply(List.nil(), selectEnd, List.of(treeMaker.Literal(key)));
//        statements.append(treeMaker.Exec(testEnd));

        JCTree.JCBlock body1 = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(jcMethodDecl.getModifiers(),jcMethodDecl.getName(),jcMethodDecl.restype, jcMethodDecl.getTypeParameters(),jcMethodDecl.getParameters(),jcMethodDecl.getThrows(),body1,jcMethodDecl.defaultValue);
    }

//    private JCTree.JCMethodDecl addLog(JCTree.JCMethodDecl jcMethodDecl){
//
//        JCTree.JCBlock body = jcMethodDecl.getBody();
//        List<JCTree.JCStatement> statements1 = body.getStatements();
//        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
//        JCTree.JCFieldAccess select = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("System")), names.fromString("out")), names.fromString("println"));
//        JCTree.JCMethodInvocation test = treeMaker.Apply(nil(), select, List.of(treeMaker.Literal("test111")));
//        statements.append(treeMaker.Exec(test)).appendArray(statements1.toArray(new JCTree.JCStatement[statements1.size()]));
//        JCTree.JCBlock body1 = treeMaker.Block(0, statements.toList());
////        return treeMaker.MethodDef(jcMethodDecl.getModifiers(),names.fromString(jcMethodDecl.getName().toString()+"$Proxy"),jcMethodDecl.restype, jcMethodDecl.getTypeParameters(),jcMethodDecl.getParameters(),jcMethodDecl.getThrows(),jcMethodDecl.body,jcMethodDecl.defaultValue);
//        return treeMaker.MethodDef(jcMethodDecl.getModifiers(),jcMethodDecl.getName(),jcMethodDecl.restype, jcMethodDecl.getTypeParameters(),jcMethodDecl.getParameters(),jcMethodDecl.getThrows(),body1,jcMethodDecl.defaultValue);
//    }
//    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
//        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
//        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
//        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()), jcVariableDecl.vartype, nil(), nil(), nil(), body, null);
//    }
//    private Name getNewMethodName(Name name) {
//        String s = name.toString();
//        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
//    }
}
