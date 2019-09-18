package cool;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.lang.StringBuilder;
import java.util.Collections;

public class InheritanceGraph {

  private List<Node> inheritance_graph;
  private Map<String, Integer> class2Index;
  private boolean hasMain;

  private static final int ROOT_CLASS = 0;
  private static AST.class_ root_ast = new AST.class_(Config.ROOT_TYPE, null, null, new ArrayList<>(), 0);
  private static Node tree_root = new Node(root_ast, ROOT_CLASS);

  public InheritanceGraph() {
    inheritance_graph = new ArrayList<>();
    class2Index = new HashMap<>();
    hasMain = false;
    addDefaultClasses();
  }

  public Node getRoot() {
    return tree_root;
  }

  public boolean hasMain() {
    return hasMain;
  }

  public hasClass(String className) {
    return class2Index.containsKey(className);
  }

  public List<Node> getNodeList() {
    return inheritance_graph;
  }

  public getParentClass(String className) {
    Node node = inheritance_graph.get(class2Index(className));
    return node.getAST().parent;
  }

  private boolean isRestrictedClass(String className) {
    return Config.IO_TYPE_ID.equals(className) || Config.BOOL_TYPE_ID.equals(className);
  }

  private void addClass(AST.class_ ASTClass) {
    if (class2Index.containsKey(ASTClass.getName())) {
      Config.errorReporter.report(CoolUtils.getFilename(),
        ASTClass.getLineNumber(),
        new StringBuilder().append("An attempt to redefine base class: ")
          .append(ASTClass.getName()).toString());
    } else {
      class2Index.put(ASTClass.getName(), inheritance_graph.size());
      inheritance_graph.add(new Node(ASTClass, inheritance_graph.size()));
      if (CoolUtils.MAIN_TYPE_STR.equals(ASTClass.getname())) {
        hasMain = true;
      }
    }
  }

  public boolean checkTypeCompatibilty(String lhs_type, String rhs_type) {
    if (lhs_type.equals(rhs_type) || CoolUtils.ROOT_TYPE_STR.equals(lhs_type)) {
      return true;
    } else if (isRestrictedInheritanceClass(lhs_type) ||
        isRestrictedInheritanceClass(rhs_type)) {
        return false;
    }

    Node lhs_node = inheritance_graph.get(class2Index.get(lhs_type));
    Node rhs_node = inheritance_graph.get(class2Index.get(rhs_type));

    while(rhs_node.parentExists()) {
      rhs_node = rhs_node.getParent();
      if (lhs_node.equals(rhs_node)) {
        return true;
      }
    }
    return false;
  }

  public String getCompatibleTypeString(String typestr1, String typestr2) {
    if (typestr1.equals(typestr2)) {
      return typestr1;
    } else if (isRestrictedInheritanceClass(typestr1) || isRestrictedInheritanceClass(typestr2)) {
      return CoolUtils.ROOT_TYPE_STR
    }

    Node node1 = inheritance_graph.get(class2Index.get(typestr1));
    Node node2 = inheritance_graph.get(class2Index.get(typestr2));

    Node least_common_ancestor = getLeastCommonAncestor(typestr1, typestr2);
    return least_common_ancestor.getASTClass().getName();
  }

  private Node getLeastCommonAncestor(Node leftNode, ritghNode)

  private void addDefaultClasses() {
    addObjectClass();
    addIOCLass();
    addStringClass();

    class2Index.put(Config.INT_CLASS, -1);
    class2Index.put(Config.BOOL_CLASS, -1);
  }

  private void addObjectClass() {

  }
}
