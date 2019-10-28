package cool;

import cool.CoolUtils;
import java.io.PrintWriter;
import java.util.*;
import cool.AST.class_;

public class Codegen {

	public InheritanceGraph Graph;
	public Map<String, class_> ClassNameMap;
	private HashMap<String, IRClass> nameToIrclassMap;

	public Codegen(AST.program program, PrintWriter out) {

		ClassNameMap = new HashMap<String, class_>();
		Graph = new InheritanceGraph();
		nameToIrclassMap = new HashMap<String, IRClass>();
		addDefaultClasses();
		buildGraph(program);
		buildIRClassMap(out);
		// runCodeGenerator();
	}

	private void buildGraph(AST.program program) {
		addBasicEdges();
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			String programClassName = programClass.name;
			ClassNameMap.put(programClassName, programClass);
			Graph.addEdge(parentClassName, programClassName);
		}
	}

	private void addBasicEdges() {
		Graph.addEdge("Object", "Int");
		Graph.addEdge("Object", "String");
		Graph.addEdge("Object", "Bool");
		Graph.addEdge("Object", "IO");
	}

	private void buildIRClassMap(PrintWriter out) {
		String root = CoolUtils.OBJECT_TYPE_STR;
		List<String> dfsOrdering = new ArrayList<>();
		// gives dfs ordreing to add to classes to map so that parent is already
		// processed
		Graph.DfsOrdering(root);
		dfsOrdering = Graph.getDfsOrder();
		addClassToMap(dfsOrdering);
		PrintStructDeclerations(dfsOrdering, out);
		PrintMethods(dfsOrdering, out);
		PrintConstructor(dfsOrdering, out);
		// System.out.println(dfsOrdering); works fine
	}

	private void PrintStructDeclerations(List<String> dfsOrdering, PrintWriter out) {
		System.out.println(dfsOrdering);
		for (String s : dfsOrdering) {
			out.print("%class." + s + " = type {");
			if (s.equals(CoolUtils.OBJECT_TYPE_STR))
				out.print("i8*");
			else {
				out.print(" %class." + Graph.parentNameMap.get(s));
				for (AST.attr a : nameToIrclassMap.get(s).alist.values()) {
					out.print(" ," + CoolUtils.printTypes(a.typeid));
				}
			}
			out.println("}");

		}
	}

	private void PrintMethods(List<String> dfsOrdering, PrintWriter out) {

	}

	private void PrintConstructor(List<String> dfsOrdering, PrintWriter out) {

	}

	private void addDefaultClasses() {
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

	public void addObjectClass() {
		HashMap<String, AST.method> objMethods = new HashMap<String, AST.method>();
		// Obejct class has only these methods
		objMethods.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		objMethods.put("type_name",
				new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		IRClass irObjectClass = new IRClass("Object", null, new HashMap<String, AST.attr>(), objMethods);
		nameToIrclassMap.put("Object", irObjectClass);

	}

	public void addIOClass() {
		// IO class methods
		HashMap<String, AST.method> ioMethods = new HashMap<String, AST.method>();
		ioMethods = nameToIrclassMap.get("Object").mlist;
		// formals of class methods
		List<AST.formal> outStringFormals = new ArrayList<AST.formal>();
		List<AST.formal> outIntFormals = new ArrayList<AST.formal>();
		// formals of out methods
		outStringFormals.add(new AST.formal("out_string", "String", 0));
		outIntFormals.add(new AST.formal("out_int", "Int", 0));
		// formals of inout methods
		ioMethods.put("out_string", new AST.method("out_string", outStringFormals, "IO", new AST.no_expr(0), 0));
		ioMethods.put("out_int", new AST.method("out_int", outIntFormals, "IO", new AST.no_expr(0), 0));
		ioMethods.put("in_string",
				new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		ioMethods.put("in_int", new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		IRClass irIOClass = new IRClass("IO", "Object", new HashMap<String, AST.attr>(), ioMethods);
		nameToIrclassMap.put("IO", irIOClass);
	}

	public void addIntClass() {
		HashMap<String, AST.method> intMethods = new HashMap<String, AST.method>();
		intMethods = nameToIrclassMap.get("Object").mlist;
		IRClass irIntClass = new IRClass("Int", "Object", new HashMap<String, AST.attr>(), intMethods);
		nameToIrclassMap.put("Int", irIntClass);
	}

	public void addBoolClass() {
		// the only attribute of bool class
		HashMap<String, AST.method> boolMethods = new HashMap<String, AST.method>();
		boolMethods = nameToIrclassMap.get("Object").mlist;
		IRClass irBoolClass = new IRClass("Int", "Object", new HashMap<String, AST.attr>(), boolMethods);
		nameToIrclassMap.put("Bool", irBoolClass);
	}

	public void addStringClass() {
		// string class methods and their formals
		HashMap<String, AST.method> stringMethods = new HashMap<String, AST.method>();
		stringMethods = nameToIrclassMap.get("Object").mlist;
		List<AST.formal> concatFormals = new ArrayList<AST.formal>();
		concatFormals.add(new AST.formal("arg", "String", 0));
		List<AST.formal> substrFormals = new ArrayList<AST.formal>();
		substrFormals.add(new AST.formal("arg1", "Int", 0));
		substrFormals.add(new AST.formal("arg2", "Int", 0));
		stringMethods.put("length",
				new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		stringMethods.put("concat", new AST.method("concat", concatFormals, "String", new AST.no_expr(0), 0));
		stringMethods.put("substr", new AST.method("substr", substrFormals, "String", new AST.no_expr(0), 0));
		IRClass irStringClass = new IRClass("Int", "Object", new HashMap<String, AST.attr>(), stringMethods);
		nameToIrclassMap.put("String", irStringClass);
	}

	private void addClassToMap(List<String> dfsOrdering) {
		for (String s : dfsOrdering) {
			if (CoolUtils.IsDefaultClass(s))
				continue;

			AST.class_ currAstClass = ClassNameMap.get(s);
			IRClass parentClass = nameToIrclassMap.get(Graph.parentNameMap.get(s));
			HashMap<String, AST.attr> curr_alist = parentClass.alist;
			HashMap<String, AST.method> curr_mlist = parentClass.mlist;

			for (AST.feature feature : currAstClass.features) {
				if (feature.getClass() == AST.attr.class) {
					AST.attr attr = (AST.attr) feature;
					curr_alist.put(attr.name, attr);
				}
			}

			for (AST.feature feature : currAstClass.features) {
				if (feature.getClass() == AST.method.class) {
					AST.method method = (AST.method) feature;
					curr_mlist.put(method.name, method);
				}
			}

			IRClass currClass = new IRClass(currAstClass.name, currAstClass.parent, curr_alist, curr_mlist);
			nameToIrclassMap.put(s, currClass);
		}
	}
}
