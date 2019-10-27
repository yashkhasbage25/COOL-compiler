package cool;

import java.util.*;
import cool.AST.class_;;

class ClassInfo {

    // this class stores details of all classes encountered.
    // details of objects in functions/scopes and methods of classes are
    // stored in attrInfo and methodInfo resp.

    // the inheritance graph is maintained in Graph object
    public InheritanceGraph Graph;
    public Map<String, class_> ClassNameMap;
    public ScopeTable<Map<String, String>> attrInfo;
    public ScopeTable<Map<String, List<String>>> methodInfo;

    public ClassInfo() {
        Graph = new InheritanceGraph();
        ClassNameMap = new HashMap<String, class_>();

        // this is because all classes are derived from object class
        Graph.addEdge("Object", "Int");
        Graph.addEdge("Object", "String");
        Graph.addEdge("Object", "Bool");
        Graph.addEdge("Object", "IO");
    }

    public void fillDefaultClasses() {
        // initialize attrInfo
        createNewAttrInfo();
        // initialize methodInfo
        createNewMethodInfo();
        // add object class in inheritance graph
        addObjectClass();
        // add IO class in inheritance graph
        addIOClass();
        // add String class in inheritance graph
        addStringClass();
        // add int class in inheritance graph
        addIntClass();
        // add bool class in inheritance graph
        addBoolClass();
    }

    // creates new attrInfo object
    // this same attrInfo object is used everywhere
    public void createNewAttrInfo() {
        attrInfo = new ScopeTable<Map<String, String>>();
        attrInfo.enterScope();
    }

    // creates new methodInfo object
    // this same methodInfo object is used everywhere
    public void createNewMethodInfo() {
        methodInfo = new ScopeTable<Map<String, List<String>>>();
        methodInfo.enterScope();
    }

    // add object class in inheritance graph
    public void addObjectClass() {
        String filename = "";
        // since AST.method constructor uses AST.feature as argument instead of
        // AST.method. So we need to use AST.feature instead of AST.method
        List<AST.feature> objMethods = new ArrayList<AST.feature>();
        // Obejct class has only these methods
        objMethods.add(new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objMethods.add(new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        class_ Object_class = new class_("Object", filename, null, objMethods, 0);
        ClassNameMap.put("Object", Object_class);
        updateEnvironment("Object", Object_class);
    }

    public void addIOClass() {
        String filename = "";
        // IO class methods
        List<AST.feature> ioMethods = new ArrayList<AST.feature>();
        // formals of class methods
        List<AST.formal> outStringFormals = new ArrayList<AST.formal>();
        List<AST.formal> outIntFormals = new ArrayList<AST.formal>();
        // formals of out methods
        outStringFormals.add(new AST.formal("out_string", "String", 0));
        outIntFormals.add(new AST.formal("out_int", "Int", 0));
        // formals of inout methods
        ioMethods.add(new AST.method("out_string", outStringFormals, "IO", new AST.no_expr(0), 0));
        ioMethods.add(new AST.method("out_int", outIntFormals, "IO", new AST.no_expr(0), 0));
        ioMethods.add(new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        ioMethods.add(new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        class_ IO_class = new class_("IO", filename, "Object", ioMethods, 0);
        ClassNameMap.put("IO", IO_class);
        updateEnvironment("IO", IO_class);
    }

    public void addIntClass() {
        String filename = "";
        List<AST.feature> intAttr = new ArrayList<AST.feature>();
        intAttr.add(new AST.attr("_val", "prim_slot", new AST.no_expr(0), 0));
        class_ Int_class = new class_("Int", filename, "Object", intAttr, 0);
        ClassNameMap.put("Int", Int_class);
        updateEnvironment("Int", Int_class);
    }

    public void addBoolClass() {
        String filename = "";
        // the only attribute of bool class
        List<AST.feature> boolAttr = new ArrayList<AST.feature>();
        boolAttr.add(new AST.attr("_val", "prim_slot", new AST.no_expr(0), 0));
        class_ Bool_class = new class_("Bool", filename, "Object", boolAttr, 0);
        ClassNameMap.put("Bool", Bool_class);
        updateEnvironment("Bool", Bool_class);
    }

    public void addStringClass() {
        String filename = "";
        // string class methods and their formals
        List<AST.feature> stringMethods = new ArrayList<AST.feature>();
        List<AST.formal> concatFormals = new ArrayList<AST.formal>();
        concatFormals.add(new AST.formal("arg", "String", 0));
        List<AST.formal> substrFormals = new ArrayList<AST.formal>();
        substrFormals.add(new AST.formal("arg1", "Int", 0));
        substrFormals.add(new AST.formal("arg2", "Int", 0));
        stringMethods.add(new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        stringMethods.add(new AST.method("concat", concatFormals, "String", new AST.no_expr(0), 0));
        stringMethods.add(new AST.method("substr", substrFormals, "String", new AST.no_expr(0), 0));
        class_ String_class = new class_("String", filename, "Object", stringMethods, 0);
        ClassNameMap.put("String", String_class);
        updateEnvironment("String", String_class);
    }

    public void updateEnvironment(String classs, class_ Classname) {
        Map<String, List<String>> methodArgMap = new HashMap<String, List<String>>();
        Map<String, String> attrTypeMap = new HashMap<String, String>();
        for (AST.feature f : Classname.features) {

            if (f instanceof AST.attr) {
                // store name and type of attribute
                AST.attr a = (AST.attr) f;
                attrTypeMap.put(a.name, a.typeid);

            } else if (f instanceof AST.method) {
                // store method name, method arguments and method argument
                // types and return type of method
                AST.method m = (AST.method) f;
                List<String> typeList = new ArrayList<String>();
                for (AST.formal formal : m.formals) {
                    typeList.add(formal.typeid);
                }
                typeList.add(m.typeid);
                methodArgMap.put(m.name, typeList);

            } else {
                // because feature can be either method or attr
                System.out.println("feature is either attribute or method");
            }
        }
        methodInfo.insert(classs, methodArgMap);
        attrInfo.insert(classs, attrTypeMap);
    }

}
