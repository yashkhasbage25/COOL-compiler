package java.cool;

class Visitor {

  public void visit(AST.program prog) {
    CoolUtils.inheritanceGraph = new InheritanceGraph();
    for(AST.class_ coolClass: prog.classes) {
      CoolUtils.filename = coolClass.filename;
      CoolUtils.inheritanceGraph.addClass(coolClass);
    }

    if (CoolUtils.inheritanceGraph.checkGraphSemantics()) return;

    updateMangledNames();

    inheritanceGraph.Node rootNode = CoolUtils.inheritanceGraph.getRoot();
    programVisitorDepthFirstHelper(rootNode);
  }

  private void updateMangledNames() {
    for (InheritanceGraph.Node node: Cool.inheritanceGraph.getNodeList()) {
      AST.class_ coolClass = node.getASTClass();
      for (AST.feature f: coolClass.features) {
        if (f instanceof AST.method) {
          AST.method m = (AST.method) f;
          CoolUtils.mangledNameMap.put(CoolUtils.getMangledNameWithClass(coolClass.name, m.name, m.formals), m.typeid);
        }
      }
    }
  }

  private void programVisitorDepthFirstHelper(InheritanceGraph.Node node) {
    CoolUtils.scopeTable.enterScope();
    CoolUtils.methodDefinitionScopeTable.enterScope();
  }
}
