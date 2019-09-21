package cool;

import java.util.*;
import cool.AST.class_;;

class ClassInfo {
    public InheritanceGraph Graph;
    public Map<String, class_> ClassNameMap;

    public ClassInfo() {
        Graph = new InheritanceGraph();
        ClassNameMap = new HashMap<String, class_>();
        Graph.addEdge("Object", "Int");
        Graph.addEdge("Object", "String");
        Graph.addEdge("Object", "Bool");
        Graph.addEdge("Object", "IO");
    }
}
