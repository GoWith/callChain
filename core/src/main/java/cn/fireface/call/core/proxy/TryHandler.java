//package cn.fireface.call.core.proxy;
//
//import com.sun.tools.javac.tree.JCTree;
//
///**
// * Created by maoyi on 2018/10/31.
// * don't worry , be happy
// */
//public class TryHandler {
//    JCTree.Kind kind = JCTree.Kind.TRY;
//
//    JCTree.JCStatement execute(JCTree.JCStatement statement){
//        if (statement.getKind()!=kind) {
//            return statement;
//        }
//        statement.getTree().accept(new JCTree.Visitor() {
//            @Override
//            public void visitTry(JCTree.JCTry jcTry) {
//                super.visitTry(jcTry);
//            }
//        });
//    }
//
//    /**
//     *       List<JCTree.JCStatement> statements1 = body.getStatements();
//     JCTree.JCStatement jcStatement = statements1.get(statements1.size() - 1);
//     if (jcStatement.getKind()== JCTree.Kind.TRY) {
//     jcStatement.getTree().accept(new JCTree.Visitor() {
//    @Override
//    public void visitTry(JCTree.JCTry jcTry) {
//    super.visitTry(jcTry);
//    JCTree.JCBlock block = jcTry.getBlock();
//    List<JCTree.JCStatement> jcStatements = nil();
//
//    JCTree.JCFieldAccess selectEnd = treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("cn.fireface.call.core.utils")), names.fromString("LogPool")), names.fromString("endLog"));
//    JCTree.JCMethodInvocation testEnd = treeMaker.Apply(List.<JCTree.JCExpression>nil(), selectEnd, List.<JCTree.JCExpression>of(treeMaker.Literal(key)));
//    JCTree.JCExpressionStatement exec = treeMaker.Exec(testEnd);
//
//    List<JCTree.JCStatement> statements = block.getStatements();
//    for (JCTree.JCStatement statement : statements) {
//    if (statement.getKind()== Tree.Kind.RETURN) {
//    jcStatements=jcStatements.append(exec);
//    }
//    jcStatements.append(statement);
//    }
//    if (jcStatements.size() == statements.size()) {
//    jcStatements = jcStatements.append(exec);
//    }
//    }
//    });
//     }
//
//     */
//}
