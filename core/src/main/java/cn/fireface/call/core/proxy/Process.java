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
 * 过程
 * Created by maoyi on 2018/10/23.
 * don't worry , be happy
 *
 * @author maoyi
 * @date 2023/05/16
 */
@SupportedAnnotationTypes("cn.fireface.call.core.proxy.CallChain")
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class Process extends AbstractProcessor {
    /**
     * 信使
     */
    private Messager messager;
    /**
     * 树
     */
    private JavacTrees trees;
    /**
     * 树制造商
     */
    private TreeMaker treeMaker;
    /**
     * 名字
     */
    private Names names;
    /**
     * 元素跑龙套
     */
    Elements elementUtils;

    /**
     * 初始化
     *
     * @param processingEnv 处理env
     */
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

    /**
     * 过程
     *
     * @param annotations 注释
     * @param roundEnv    一轮env
     * @return boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(CallChain.class);
        Set<? extends Element> set = roundEnv.getRootElements();
        for (Element element : set) {
            JCTree jcTree = trees.getTree(element);
            treeMaker.pos = jcTree.pos;
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = nil();
                    List<JCTree> myDefs = nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.METHOD)) {
                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) tree;
                            if (jcMethodDecl.getName().toString().equals("<init>")) {
                                myDefs = myDefs.append(tree);
                                continue;
                            }
                            myDefs = myDefs.append(addCall(jcMethodDecl, jcClassDecl));
                        } else {
                            myDefs = myDefs.append(tree);
                        }
                    }
                    jcClassDecl.defs = myDefs;
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    /**
     * 执行
     *
     * @param statement 声明
     * @param key       关键
     * @return {@link JCTree.JCStatement}
     */
    JCTree.JCStatement execute(JCTree.JCStatement statement, final String key) {
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
                    aCatch.body = buildNewJcBlock(catchBody, key);
                }
            }

            @Override
            public void visitIf(JCTree.JCIf jcIf) {
                JCTree.JCStatement thenpart = jcIf.thenpart;
                if (check(thenpart.getKind())) {
                    jcIf.thenpart = execute(thenpart, key);
                } else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) thenpart;
                    jcIf.thenpart = buildNewJcBlock(thenBody, key);
                }
                JCTree.JCStatement elseStatement = jcIf.getElseStatement();
                if (elseStatement != null) {
                    if (check(elseStatement.getKind())) {
                        jcIf.elsepart = execute(elseStatement, key);
                    } else {
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
                    jcForLoop.body = execute(body, key);
                } else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) body;
                    jcForLoop.body = buildNewJcBlock(thenBody, key);
                }
            }

            @Override
            public void visitForeachLoop(JCTree.JCEnhancedForLoop jcEnhancedForLoop) {
                JCTree.JCStatement body = jcEnhancedForLoop.body;
                if (check(body.getKind())) {
                    jcEnhancedForLoop.body = execute(body, key);
                } else {
                    JCTree.JCBlock thenBody = (JCTree.JCBlock) body;
                    jcEnhancedForLoop.body = buildNewJcBlock(thenBody, key);
                }
            }

            @Override
            public void visitCatch(JCTree.JCCatch jcCatch) {
                JCTree.JCBlock body = jcCatch.body;
                JCTree.JCBlock body1 = buildNewJcBlock(body, key);
                jcCatch.body = body1;
            }
        });
        return statement;
    }

    /**
     * 建立新jc块
     *
     * @param body 身体
     * @param key  关键
     * @return {@link JCTree.JCBlock}
     */
    private JCTree.JCBlock buildNewJcBlock(JCTree.JCBlock body, String key) {
        List<JCTree.JCStatement> statements = body.getStatements();

        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        for (JCTree.JCStatement jcStatement : statements) {
            if (check(jcStatement.getKind())) {
                JCTree.JCStatement execute = execute(jcStatement, key);
                jcStatements = jcStatements.append(execute);
                continue;
            }
            if (jcStatement.getKind() == Tree.Kind.RETURN) {
                JCTree.JCExpressionStatement jcExpressionStatement = buildEndCall(key);
                jcStatements = jcStatements.append(jcExpressionStatement);
            }
            jcStatements = jcStatements.append(jcStatement);
        }
        return treeMaker.Block(0, jcStatements.toList());
    }

    /**
     * 检查
     *
     * @param kind 类
     * @return boolean
     */
    private boolean check(JCTree.Kind kind) {
        return kind == Tree.Kind.TRY ||
                kind == Tree.Kind.IF ||
                kind == Tree.Kind.FOR_LOOP ||
                kind == Tree.Kind.CATCH;
    }


    /**
     * 构建结束电话
     *
     * @param key 关键
     * @return {@link JCTree.JCExpressionStatement}
     */
    private JCTree.JCExpressionStatement buildEndCall(String key) {
        JCTree.JCFieldAccess selectEnd = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("cn.fireface.call.core.utils")), names.fromString("LogPool")), names.fromString("endLog"));
        JCTree.JCMethodInvocation testEnd = treeMaker.Apply(List.<JCTree.JCExpression>nil(), selectEnd, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
        JCTree.JCExpressionStatement exec = treeMaker.Exec(testEnd);
        return exec;
    }


    /**
     * 添加电话
     *
     * @param jcMethodDecl jc方法decl
     * @param jcClassDecl  jc类decl
     * @return {@link JCTree.JCMethodDecl}
     */
    private JCTree.JCMethodDecl addCall(JCTree.JCMethodDecl jcMethodDecl, JCTree.JCClassDecl jcClassDecl) {

        try {
            String key = getKey(jcMethodDecl, jcClassDecl);

            JCTree.JCBlock body = jcMethodDecl.getBody();
            if (body == null) {
                return jcMethodDecl;
            }
            JCTree.JCExpressionStatement startCallStatement = buildStartCall(key);
            JCTree.JCExpressionStatement endCallStatement = buildEndCall(key);


            jcMethodDecl.body = treeMaker.Block(0, List.of(
                    startCallStatement,
                    treeMaker.Try(
                            body,
                            List.nil(),
//                            List.of(treeMaker.Catch(
//                                    treeMaker.VarDef(
//                                            treeMaker.Modifiers(0),
//                                            //名字
//                                            getNameFromString("e"),
//                                            //类型
//                                            memberAccess("java.lang.Exception"),
//                                            //初始化语句
//                                            null
//                                    ),
//                                    treeMaker.Block(0, List.of(
//                                            treeMaker.Throw(
//                                                    // e 这个字符是catch块中定义的变量
//                                                    treeMaker.Ident(getNameFromString("e"))
//                                            )
//                                    ))
//                            )),
                            treeMaker.Block(0, List.of(endCallStatement))
                    )
            ));
            return jcMethodDecl;

//            ListBuffer<JCTree.JCStatement> statementBuffer = new ListBuffer<>();
//            List<JCTree.JCStatement> OldStatements = body.getStatements();
//            JCTree returnType = jcMethodDecl.getReturnType();
//            if (returnType.type.getKind() == TypeKind.VOID) {
//                statementBuffer.append(startCallStatement).appendArray(OldStatements.toArray(new JCTree.JCStatement[OldStatements.size()])).append(endCallStatement);
//            } else {
//                statementBuffer.append(startCallStatement);
//                for (JCTree.JCStatement jcStatement : OldStatements) {
//                    if (jcStatement.getKind() == Tree.Kind.RETURN) {
//                        statementBuffer.append(endCallStatement).append(jcStatement);
//                        continue;
//                    }
//                    statementBuffer.append(execute(jcStatement, key));
//                }
//            }
//            JCTree.JCBlock body1 = treeMaker.Block(0, statementBuffer.toList());
//            return treeMaker.MethodDef(jcMethodDecl.getModifiers(), jcMethodDecl.getName(), jcMethodDecl.restype, jcMethodDecl.getTypeParameters(), jcMethodDecl.getParameters(), jcMethodDecl.getThrows(), body1, jcMethodDecl.defaultValue);
        } catch (Exception e) {
            return jcMethodDecl;
        }
    }

    /**
     * 得到关键
     *
     * @param jcMethodDecl jc方法decl
     * @param jcClassDecl  jc类decl
     * @return {@link String}
     */
    private String getKey(JCTree.JCMethodDecl jcMethodDecl, JCTree.JCClassDecl jcClassDecl) {
        String classFullName = jcClassDecl.sym.fullname.toString();
        String methodName = jcMethodDecl.getName().toString();
        return classFullName + "." + methodName;
    }

    /**
     * 构建开始叫
     *
     * @param key 关键
     * @return {@link JCTree.JCExpressionStatement}
     */
    private JCTree.JCExpressionStatement buildStartCall(String key) {
        JCTree.JCFieldAccess select = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("cn.fireface.call.core.utils")), names.fromString("LogPool")), names.fromString("startLog"));
        JCTree.JCMethodInvocation test = treeMaker.Apply(List.<JCTree.JCExpression>nil(), select, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
        return treeMaker.Exec(test);
    }

    /**
     * 创建 域/方法 的多级访问, 方法的标识只能是最后一个
     *
     * @param components
     * @return
     */
    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    /**
     * 根据字符串获取Name，（利用Names的fromString静态方法）
     *
     * @param s
     * @return
     */
    private com.sun.tools.javac.util.Name getNameFromString(String s) {
        return names.fromString(s);
    }
}
