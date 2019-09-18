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
  private boolean hasMain;

  private static final int ROOT_CLASS = 0;
  private static AST.class_ ROOT_AST_CLASS = new AST.class_(Config.ROOT_TYPE, null, null, new ArrayList<>(), 0);

  public InheritanceGraph() {
    inheritance_graph = new ArrayList<>();
    className2IndexMap = new HashMap<>();
    hasMain = false;
    addDefaultClasses();
  }

  private void addDefaultClasses() {
    addObjectClass();
    addIOCLass();
    addStringClass();

    className2IndexMap.put(Config.INT_CLASS, -1);
    className2IndexMap.put(Config.BOOL_CLASS, -1);
  }

  private void addObjectClass() {

  }
}
