package cool;

import java.util.*;
import cool.AST.class_;;

class ClassTable {
    public Graph InheritanceGraph;
    public Map<String, class_> ClassNameMap;

    public ClassTable() {
        inheritanceGraph = new InheritanceGraph();
        ClassNameMap = new HashMap<String, class_>();
        inheritanceGraph.addEdge("Object", "Int");
        inheritanceGraph.addEdge("Object", "String");
        inheritanceGraph.addEdge("Object", "Bool");
        inheritanceGraph.addEdge("Object", "IO");
    }
}
