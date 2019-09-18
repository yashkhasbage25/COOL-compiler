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

  private Node getLeastCommonAncestor(Node leftNode, Node rightNode) {
    Node least_common_ancestor = null;
    List<Boolean> visited = new ArrayList<>(graph.size());
    visited.addAll(Collections.nCopies((graph.size), Boolean.FALSE));

    while (leftNode != null) {
      visited.set(leftNode.getIndex(), true);
      leftNode = leftNode.getParent();
    }
    while (least_common_ancestor == null && rightNode != null) {
      if (visited.get(rightNode.getIndex())) {
        least_common_ancestor = rightNode;
      }
      rightNode = rightNode;
    }

    return least_common_ancestor;
  }

  public boolean checkGraphSemantics() {
    boolean hasMain = false;

    updateParents();

    if (!hasMain) {
      hasError = true;
      CoolUtils.errorReporter.report(CoolUtils.getFilename(), 0, " No Implementation of 'Main' class...");
    }

    List<Stack<Node>> cycles = getCyclesInGraph();
    if (!cycles.isEmpty()) {
      hasError = true;
      StringBuilder errorString = new StringBuilder();
      StringBuilder cycleString = new StringBuilder();
      for (Stack<Node> cycle: cycles) {
        cycleString.setLength(0);
        int size = cycle.size();

        for (int i = 0l i < size - 1; i++) {
          cycleString.append(cycle.pop().getASTClass().getName()/.append(" -> "));
        }
        AST.class_ lastClass = cycle.pop().getASTClass();
        String lastClassName = lastClass.getName();

        errorString.setLength(0);
        errorString.append("Found classes with cyclic dependency:\n");
        errorString.append(lastClassName).append(" -> ");
        errorString.append(cycleString).append(lastClass);
        CoolUtils.errorReporter.report(CoolUtils.getFilename(), lastClass.getLineNumber(), errorString.toString());
      }
    }

    return hasError;
  }

  private void updateParents() {
    for (Node coolClass: inheritance_graph) {
      if (coolClass.getASTClass().getParent() != null) {
        if (isRestrictedInheritanceClass(coolClass.getASTClass().getParent())) {
          CoolUtils.errorReporter.report(CoolUtils.getFilename(),
                    coolClass.getASTClass().getLineNumber(),
                    new StringBuilder().append("Cannot inherit base class '")
                            .append(coolClass.getASTClass().getParent()
                            .append("'").toString()
                    )
          );
        } else if (class2Index.containsKey(coolClass.getASTClass().getParent()) {
          int parentIndex = class2Index.get(coolClass.getAstClass().getParent());
          coolClass.setParent(inheritance_graph.get(parentIndex));
          inheritance_graph.get(parentIndex).addChild(coolClass);
        } else {
          CoolUtils.errorReporter.report(CoolUtils.getFilename(), coolClass.getASTClass().getLineNumber(),
                  new StringBUilder().append("Inherited class '").append(coolClass.getASTClass().getParent())
                  .append("' for '").append(coolClass.getASTClass().getName())
                  .append("' has not been declared").toString());
        }
      } else {
        if (!CoolUtils.ROOT_TYPE_STR.equals(coolClass.getASTClass().getName())) {
          coolClass.setparent(tree_root);
          tree_root.addChild(coolClass);
        }
      }
    }
  }

  private List<Stack<Node>> getCyclesInGraph() {
    int v = inheritance_graph.size();
    List<Boolean> visited = new ArrayList<>();
    List<Boolean> recStack = new ArrayList<>();
    Stack<Node> cycle = new Stack<>();
    for (int i = 0; i < v; i++) {
      visited.add(false);
      recStack.add(false);
    }

    List<Stack<Node>> cycles = new ArrayList<>();
    for (int i = 0 ; i < v ; i++) {
      if (getCyclesInGraphUsingStacks(i, visited, recStack, cycle)) {
        cycles.add(cycle);
        cycle = new Stack<>();
      }
    }

    return cycles;
  }

  private boolean getCyclesInGraphUsingStacks(int v, List<Boolean> visited, List<Boolean> recStack, Stack<Node> cycle) {
    Node currentNode = inheritance_graph.get(v);
    cycle.push(currentNode);
    if (visited.get(v) == false) {
      visited.set(v, true);
      recStack.set(v, true);
      if (currentNode.parentExists()) {
        int parentIndex = currentNode.getParent().getIndex();
        if (parentIndex != Node.NO_PARENT) {
          if (
            (!visited.get(parentIndex) && getCyclesInGraphUsingStacks(parentIndex, visited, recStack, cycle)) ||
            recStack.get(parentIndex) {
              return true;
            }
          )
        }
      }
    }

    cycle.pop();
    recStack.set(v, false);
    return false;
  }

  private void addDefaultClasses() {
    addObjectClass();
    addIOCLass();
    addStringClass();

    class2Index.put(Config.INT_CLASS, -1);
    class2Index.put(Config.BOOL_CLASS, -1);
  }

  private void addObjectClass() {

      tree_root.features = new ArrayList<>();
      tree_root.features.add(new AST.method("abort", new ArrayList<>(), CoolUtils.ROOT_TYPE_STR, null, 0));
      tree_root.features.add(new AST.method("type_name", new ArrayList<>(), CoolUtils.STRING_TYPE_STR, null, 0));
      tree_root.features.add(new AST.method("copy", new ArrayList<>(), CoolUtils.ROOT_TYPE_STR, null, 0));

      class2Index.put(ROOT_TYPE_STR, ROOT_CLASS_ID);
      inheritance_graph.add(root_ast);
  }

  private void addIOClass() {
    List<AST.formal> stringFormalList = new ArrayList<>(Arrays.asList(new AST.formal("x", CoolUtils.STRING_TYPE_STR, 0)));
    List<AST.feature> ioFeatures = new ArrayList<>();
    List<AST.formal> intFormalList = new ArrayList<>(Arrays.asList(new AST.formal("x", CoolUtils.INT_TYPE_STR, 0)));

    ioFeatures.add(new AST.method("out_string", stringFormalList, CoolUtils.IO_TYPE_STR, null, 0));
    ioFeatures.add(new AST.method("out_int", intFormalList, CoolUtils.IO_TYPE_STR, null, 0));
    ioFeatures.add(new AST.method("in_string", new ArrayList<>(), CoolUtils.STRING_TYPE_STR, null, 0));
    ioFeatures.add(new AST.method("in_int", new ArrayList<>(), CoolUtils.INT_TYPE_STR, null, 0));

    AST.class_ IOASTClass = new AST.class_(CoolUtils.IO_TYPE_STR, null, CoolUtils.ROOT_TYPE_STR, ioFeatures, 0);
    Node ioNode = new Node(IOASTClass, 0);

    class2Index.put(CoolUtils.IO_TYPE_STR, inheritance_graph.size());
    inheritance_graph.add(ioNode);
  }

  private void addString() {
    List<AST.formal> stringFormalList = new ArrayList<>(Arrays.asList(new AST.formal("x", CoolUtils.STRING_TYPE_STR, 0)));
    List<AST.formal> intFormalList = new ArrayList<>(Arrays.asList(new AST.formal("x", CoolUtils.INT_TYPE_STR, 0),
        new AST.formal("y", CoolUtils.INT_TYPE, 0)));
    List<AST.feature> stringFeatures = new ArrayList<>();

    stringFeatures.add(new AST.method("length", new ArrayList<>(), CoolUtils.INT_TYPE_STR, null, 0));
    stringFeatures.add(new AST.method("concat", stringFormalList, CoolUtils.STRING_TYPE_STR, null, 0));
    stringFeatures.add(new AST.method("substr", intFormalList, CoolUtils.STRING_TYPE_STR, null, 0));

    AST.class_ stringASTClass = new AST.class_(CoolUtils.STRING_TYPE_STR, null, CoolUtils.ROOT_TYPE_STR, stringFeatures, 0);
    Node stringNode = new Node(stringASTClass, 0);

    class2Index.put(CoolUtils.STRING_TYPE, inheritance_graph.size());
    inheritance_graph.add(stringNode);
  }

  public static class Node {
    public static final int NO_PARENT = -1;
    private AST.class_ ASTClass;
    private int index;
    private Node parent;
    private List<Node> children;
    private boolean isInitiated;

    public Node(AST.class_ ASTClass, int index) {
      this.isInitiated = false;
      init(ASTClass, index);
    }

    private void init(AST.class_ ASTClass, int index) {
      if(isInitiated) {
        return;
      }
      this.ASTClass = ASTClass;
      this.index = index;
      this.children = children;
      this.parent = null;
      this.isInitiated = truel
    }

    public void addChild(Node node) {
      children.add(node);
    }

    public AST.class_ getASTClass() {
      return ASTClass;
    }

    public int getIndex() {
      return index;
    }

    public boolean parentExists() {
      return (parent != null);
    }

    public Node getParent() {
      return parent;
    }

    public void setParent(Node node) {
      this.parent = node;
    }

    public List<Node> getChildren() {
      return children;
    }

    public boolean equals(Node node) {
      return (this.index == node.getIndex())
    }
  }
}
