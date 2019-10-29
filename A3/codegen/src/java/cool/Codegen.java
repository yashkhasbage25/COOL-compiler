package cool;

import cool.CoolUtils;
import java.io.PrintWriter;
import java.util.*;
import cool.AST.class_;

public class Codegen {

	public InheritanceGraph Graph;
	public Map<String, class_> ClassNameMap;
	private HashMap<String, IRClass> nameToIrclassMap;
	public List<String> globalStr = new ArrayList<String>();
	public int registerCounter = 0;
	public int stringCounter = 0;

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

	private void defineStringConst(AST.expression expr, PrintWriter out) {
		AST.string_const str_obj = (AST.string_const) expr;
		globalStr.add("@.str." + stringCounter++ + " = private unnamed_addr constant [" + (str_obj.value.length() + 1)
				+ " x i8] c\"" + str_obj.value + "\\00\", align 1");
	}

	private void PrintMethods(List<String> dfsOrdering, PrintWriter out) {
		for (String s : dfsOrdering) {
			System.out.println(s);
			if (s.equals("Object"))
				CoolUtils.PrintMethodsObject(out);
			else if (s.equals("IO"))
				CoolUtils.PrintMethodsIO(out);
			else if (s.equals("String"))
				CoolUtils.PrintMethodsString(out);
			else if (s.equals("Int") || s.equals("Bool"))
				continue;
			else {
				HashMap<String, AST.formal> fmap = new HashMap<String, AST.formal>();
				IRClass currClass = nameToIrclassMap.get(s);
				for (Map.Entry<String, AST.method> entry : currClass.mlist.entrySet()) {
					String methodName = entry.getKey();
					AST.method method = entry.getValue();
					out.print("define " + CoolUtils.printTypes(method.typeid) + " @"
							+ CoolUtils.getMangledName(s, methodName) + "(");
					for (int i = 0; i < method.formals.size(); i++) {
						out.print(
								CoolUtils.printTypes(method.formals.get(i).typeid) + " %" + method.formals.get(i).name);
						if (i != method.formals.size())
							out.print(", ");
						fmap.put(method.formals.get(i).name, method.formals.get(i));
					}
					fmap.put("#rettype", new AST.formal("ret", method.typeid, 0));
					out.println("){");
					registerCounter = 0;
					// ProcessMethod(currClass, fmap, method.body, out, true);

					out.println("}\n");

				}
			}
		}
	}

	private void PrintConstructor(List<String> dfsOrdering, PrintWriter out) {
		for (String s : dfsOrdering) {
			registerCounter = 0;
			int attrCounter = 0;
			String formals = CoolUtils.printTypes(s) + " %this";
			out.println("define void @" + CoolUtils.getMangledName(s, s) + "(" + formals + " ) {");
			String parentName = Graph.parentNameMap.get(s);
			if (parentName != null) {
				out.println(
						"\t%" + (registerCounter) + " = bitcast %class." + s + "* %this to %class." + parentName + "*");
				out.println("\tcall void @" + CoolUtils.getMangledName(parentName, parentName) + "(%class." + parentName
						+ "* %" + registerCounter);
				registerCounter++;

			}
			for (AST.attr a : nameToIrclassMap.get(s).alist.values()) {
				System.out.println(s + " " + a);
				if (!(a.value instanceof AST.no_expr)) {
					AST.assign exp = new AST.assign(a.name, a.value, 0);
					exp.type = a.typeid;
					// ProcessMethod(nameToIrclassMap.get(s), null, exp, out, false);
				} else if (a.typeid.equals("Int")) {
					AST.assign exp = new AST.assign(a.name, new AST.int_const(0, 0), 0);
					exp.type = "Int";
					// ProcessMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else if (a.typeid.equals("Bool")) {
					AST.assign exp = new AST.assign(a.name, new AST.bool_const(false, 0), 0);
					exp.type = "Bool";
					// ProcessMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else if (a.typeid.equals("String")) {
					AST.assign exp = new AST.assign(a.name, new AST.string_const("", 0), 0);
					exp.type = "String";
					// ProcessMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else {
					String ctype = CoolUtils.printTypes(s);
					out.println("\t%" + (registerCounter) + " = getelementptr inbounds " + ctype + "," + ctype
							+ "* %this, i32 0, i32 " + attrCounter);
					out.println("\tstore " + CoolUtils.printTypes(a.typeid) + " null, " + CoolUtils.printTypes(a.typeid)
							+ "* %" + registerCounter + ", align 4");
					registerCounter++;
				}
			}
			out.println("}");
		}
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
		IRClass irIntClass = new IRClass("Int", "Object", new HashMap<String, AST.attr>(),
				new HashMap<String, AST.method>());
		nameToIrclassMap.put("Int", irIntClass);
	}

	public void addBoolClass() {
		// the only attribute of bool class
		HashMap<String, AST.method> boolMethods = new HashMap<String, AST.method>();
		boolMethods = nameToIrclassMap.get("Object").mlist;
		IRClass irBoolClass = new IRClass("Bool", "Object", new HashMap<String, AST.attr>(),
				new HashMap<String, AST.method>());
		nameToIrclassMap.put("Bool", irBoolClass);
	}

	public void addStringClass() {
		// string class methods and their formals
		HashMap<String, AST.method> stringMethods = new HashMap<String, AST.method>();
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
			// System.out.println(s + Graph.parentNameMap.get(s));
			IRClass parentClass = nameToIrclassMap.get(Graph.parentNameMap.get(s));
			// HashMap<String, AST.attr> curr_alist = parentClass.alist;
			HashMap<String, AST.attr> curr_alist = new HashMap<String, AST.attr>();
			HashMap<String, AST.method> curr_mlist = new HashMap<String, AST.method>();
			curr_alist.putAll(parentClass.alist);
			// curr_mlist.putAll(parentClass.mlist); not needed

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
