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
	public int registerCounter = -1;
	public int stringCounter = 0;

	public Codegen(AST.program program, PrintWriter out) {

		ClassNameMap = new HashMap<String, class_>();
		Graph = new InheritanceGraph();
		nameToIrclassMap = new HashMap<String, IRClass>();
		addDefaultClasses();
		buildGraph(program);
		buildIRClassMap(out);
		for(String s:globalStr) out.println(s);

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
		CoolUtils.printDefaultIR(out);
		PrintStructDeclerations(dfsOrdering, out);
		PrintMethods(dfsOrdering, out);
		PrintConstructor(dfsOrdering, out);
		out.println("define i32 @main() {\n"
			+ "entry:\n%main = alloca %class.Main, align 8\n"
			+ "call void @_CN4Main_FN4Main_(%class.Main* %main)\n"
			+ "%retval = call i32 @_CN4Main_FN4main_(%class.Main* %main)\n"
			+ "ret i32 %retval\n}");

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
				HashMap<String, AST.formal> formalMap = new HashMap<String, AST.formal>();
				IRClass currClass = nameToIrclassMap.get(s);
				for (Map.Entry<String, AST.method> entry : currClass.mlist.entrySet()) {
					String methodName = entry.getKey();
					if(methodName.equals("main"))continue;
					AST.method method = entry.getValue();
					out.print("define " + CoolUtils.printTypes(method.typeid) + " @"
							+ CoolUtils.getMangledName(s, methodName) + "(");
					for (int i = 0; i < method.formals.size(); i++) {
						out.print(
								CoolUtils.printTypes(method.formals.get(i).typeid) + " %" + method.formals.get(i).name);
						if (i != method.formals.size()-1)
							out.print(", ");
						formalMap.put(method.formals.get(i).name, method.formals.get(i));
					}
					formalMap.put("#rettype", new AST.formal("ret", method.typeid, 0));
					out.println("){");
					out.println("entry:");
					registerCounter = -1;
					handleClassMethod(currClass, formalMap, method.body, out, true);

					out.println("}\n");

				}
			}
		}
	}

	private void PrintConstructor(List<String> dfsOrdering, PrintWriter out) {
		for (String s : dfsOrdering) {
			if(CoolUtils.IsDefaultClass(s) || s.equals("Main"))continue;
			registerCounter = -1;
			int attrCounter = 0;
			String formals = CoolUtils.printTypes2(s) + " %this";
			out.println("define void @" + CoolUtils.getMangledName(s, s) + "(" + formals + " ) {");
			String parentName = Graph.parentNameMap.get(s);
			out.println("entry:");
			if (parentName != null) {
				out.println(
						"%" + (++registerCounter) + " = bitcast %class." + s + "* %this to %class." + parentName + "*");
				out.println("call void @" + CoolUtils.getMangledName(parentName, parentName) + "(%class." + parentName
						+ "* %" + registerCounter+")");

			}
			for (AST.attr a : nameToIrclassMap.get(s).alist.values()) {
				//out.println(s + " " + a + " " + a.name);
				if (!(a.value instanceof AST.no_expr)) {
					AST.assign exp = new AST.assign(a.name, a.value, 0);
					exp.type = a.typeid;
					handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);
				} else if (a.typeid.equals("Int")) {
					AST.assign exp = new AST.assign(a.name, new AST.int_const(0, 0), 0);
					exp.type = "Int";
					handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else if (a.typeid.equals("Bool")) {
					AST.assign exp = new AST.assign(a.name, new AST.bool_const(false, 0), 0);
					exp.type = "Bool";
					handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else if (a.typeid.equals("String")) {
					AST.assign exp = new AST.assign(a.name, new AST.string_const("", 0), 0);
					exp.type = "String";
					handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);

				} else {
					String ctype = CoolUtils.printTypes(s);
					out.println("%" + ++registerCounter + " = getelementptr inbounds " + ctype + "," + ctype
							+ "* %this, i32 0, i32 " + attrCounter);
					// out.println("a =" + a.typeid);
					// out.println(a);
					out.println("store " + CoolUtils.printTypes2(a.typeid) + " null, " + CoolUtils.printTypes(a.typeid)
							+ "* %" + registerCounter + ", align 4");

				}
			}
			out.println("ret void\n}");
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

	private void handleClassMethod(IRClass IRClass, HashMap<String, AST.formal> formalMap, AST.expression expr, PrintWriter out, boolean lastExpr) {
		// assignment expression
		if(expr.getClass() == AST.assign.class) {
			String reg, varType;
			AST.assign assignExpr = (AST.assign) expr;
			AST.expression exp = assignExpr.e1;
			if(IRClass.alist.containsKey(assignExpr.name)) {
				out.println("%" + ++registerCounter + " = getelementptr inbounds %class."
				+ IRClass.name + ", %class." + IRClass.name +
				"* %this, i32 0, i32 " + IRClass.attrIndex.get(assignExpr.name));
				reg = "%" + (registerCounter+1);
				// out.println("??" + assignExpr.name + " " + IRClass.alist.get(assignExpr.name).typeid);
				varType = CoolUtils.printTypes(IRClass.alist.get(assignExpr.name).typeid);
			} else {
				reg = "%" + assignExpr.name;
				varType = CoolUtils.printTypes(formalMap.get(assignExpr.name).typeid);
			}
			// out.println("exp type: " + exp.type);
			handleClassMethod(IRClass, formalMap, exp, out, false);
			out.println("store " + varType + " %" + registerCounter + ", " + varType +
			"* " + reg + ", align 4"); // check
			if(lastExpr) out.println("ret " + varType + " %" +registerCounter);
		}

		// plus expression
		if(expr.getClass() == AST.plus.class) {
			AST.plus plusExpr = (AST.plus) expr;
			handleClassMethod(IRClass, formalMap, plusExpr.e1, out, false);
			String reg1 = "%" + registerCounter;
			handleClassMethod(IRClass, formalMap, plusExpr.e2, out, false);
			String reg2 = "%" + registerCounter;
			out.println("%" + ++registerCounter + " = add nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + CoolUtils.printTypes(plusExpr.e1.type) + " %" + registerCounter);
		}

		// subtraction expression
		if(expr.getClass() == AST.sub.class) {
			AST.sub subExpr = (AST.sub) expr;
			handleClassMethod(IRClass, formalMap, subExpr.e1, out, false);
			String reg1 = "%" + registerCounter;
			handleClassMethod(IRClass, formalMap, subExpr.e2, out, false);
			String reg2 = "%" + registerCounter;
			out.println("%" + ++registerCounter + " = sub nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + CoolUtils.printTypes(subExpr.e1.type) + " %" + registerCounter);
		}

		// multiplication expression
		if(expr.getClass() == AST.mul.class) {
			AST.mul mulExpr = (AST.mul) expr;
			handleClassMethod(IRClass, formalMap, mulExpr.e1, out, false);
			String reg1 = "%" + registerCounter;
			handleClassMethod(IRClass, formalMap, mulExpr.e2, out, false);
			String reg2 = "%" + registerCounter;
			out.println("%" + ++registerCounter + " = mul nse i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + CoolUtils.printTypes(mulExpr.e1.type) + " %" + registerCounter);
		}

		// division expression
		if(expr.getClass() == AST.divide.class) {
			AST.divide divideExpr = (AST.divide) expr;
			handleClassMethod(IRClass, formalMap, divideExpr.e1, out, false);
			String reg1 = "%" + registerCounter;
			handleClassMethod(IRClass, formalMap, divideExpr.e2, out, false);
			String reg2 = "%" + registerCounter;
			out.println("%" + ++registerCounter + " = sdic i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + CoolUtils.printTypes(divideExpr.e1.type) + " %" +registerCounter);
		}

		// int expression
		if(expr.getClass() == AST.int_const.class) {
			out.println("%" + ++registerCounter + " = alloca i32, align 4");
			out.println("store i32 " + ((AST.int_const) expr).value + ", i32* %" + registerCounter + ", align 4");
			out.println("%" + ++registerCounter + " = load i32, i32* %" + (registerCounter-1) + ", align 4");

			if (lastExpr) out.println("ret i32 %" + registerCounter);
		}

		// bool expression
		if(expr.getClass() == AST.bool_const.class) {
			boolean bool = ((AST.bool_const) expr).value;
			int int4bool = 0;
			if(bool) int4bool = 1;

			out.println("%" + ++registerCounter + " = alloca i8, align 1"); ////////////////////
			out.println("store i8 " + int4bool + ", i8* %" + registerCounter + ", align 1");
			out.println("%" + ++registerCounter + " = load i8, i8* %" + (registerCounter-1) + ", align 1");

			if(lastExpr) out.println("ret int32 %" + registerCounter);
		}

		// new expression
		if(expr.getClass() == AST.new_.class) {
			AST.new_ newExpr = (AST.new_) expr;
			out.println("%" + ++registerCounter + " = alloca " + CoolUtils.printTypes2(newExpr.typeid) + ", align 4");

			if(lastExpr) out.println("ret " + CoolUtils.printTypes2(newExpr.typeid) + "* %" + registerCounter);
		}

		// identifier expression
		if(expr.getClass() == AST.object.class)
		{
			String reg;
			AST.object str = (AST.object)expr;
			if (str.name.equals("self")) {
				out.println("%"+ ++registerCounter +" = alloca " + CoolUtils.printTypes(str.type) + ", align 4");
				out.println("store "+CoolUtils.printTypes(str.type)+" %this, "+CoolUtils.printTypes(str.type)+"* %"+registerCounter+", align 4");
				out.println("%"+ ++registerCounter + " = load "+CoolUtils.printTypes(str.type)+", "+CoolUtils.printTypes(str.type)+"* %"+ (registerCounter-1) + ", align 4");
			}

			else if(formalMap.containsKey(str.name)) {
				out.println("%"+ ++registerCounter +" = alloca " + CoolUtils.printTypes(str.type) + ", align 4");
				out.println("store "+CoolUtils.printTypes(str.type)+" %"+str.name+", "+CoolUtils.printTypes(str.type)+"* %"+registerCounter+", align 4");
				out.println("%"+ ++registerCounter + " = load "+CoolUtils.printTypes(str.type)+", "+CoolUtils.printTypes(str.type)+"* %"+ (registerCounter-1) + ", align 4");
			}
			else { // it must be defined in attributes.
				out.println("%"+ ++registerCounter +" = getelementptr inbounds %class." + IRClass.name + ", %class."+IRClass.name+"* %this, i32 0, i32 "+IRClass.attrIndex.get(str.name));
				out.println("%"+ ++registerCounter +" = load  " + CoolUtils.printTypes(IRClass.alist.get(str.name).typeid) + ", "+ CoolUtils.printTypes(IRClass.alist.get(str.name).typeid) + "* %"+(registerCounter-1)+", align 4");

			}

			if(lastExpr)
				out.println("ret "+CoolUtils.printTypes(str.type) + " %" + registerCounter);
		}

		// dispatch
		else if(expr.getClass() == AST.dispatch.class)
		{
			AST.dispatch str = (AST.dispatch)expr;
			if( !(str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) )
				handleClassMethod(IRClass, formalMap, str.caller, out, false);
			String thisReg = ""+registerCounter;

			String caller = str.caller.type;
			if( (str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) ) {
				caller = IRClass.name;
				thisReg = "this";
			} else {

				// Go through the attrs of the class called, and allocate if there's an initialisation in the attributes node.

				String cname = caller;
				for (Map.Entry<String, AST.attr> attrEntry : nameToIrclassMap.get(cname).alist.entrySet()) {
					AST.attr attrNode = attrEntry.getValue();
					if (attrNode.value.getClass() == AST.new_.class) {
						AST.new_ node = (AST.new_)attrNode.value;
						out.println("%"+ ++registerCounter +" = alloca " + CoolUtils.printTypes2(node.typeid)+ ", align 4");
						out.println("%"+ ++registerCounter  +" = getelementptr inbounds %class." + cname + ", %class."+cname+"* %" + thisReg + ", i32 0, i32 "+nameToIrclassMap.get(cname).attrIndex.get(attrNode.name));
						out.println("store " + CoolUtils.printTypes2(node.typeid)+ "* %"+ (registerCounter - 1) +", " + CoolUtils.printTypes2(node.typeid)+ "** %"+registerCounter+", align 8");
					}

				}
			}

			String methodName = str.name;
			String mType =   CoolUtils.printTypes(nameToIrclassMap.get(caller).mlist.get(methodName).typeid);
			String dispatchStr = "call " + mType + " @"+caller+"_"+methodName + "("+ CoolUtils.printTypes(caller) + " %"+thisReg;

			for(int i = 0; i < str.actuals.size(); i++)
			{
				expr = str.actuals.get(i);
				handleClassMethod(IRClass, formalMap, expr, out, false);
				dispatchStr += ", "+CoolUtils.printTypes(expr.type) + " %"+ registerCounter;
			}

			dispatchStr += ")";

			out.println("%"+ ++registerCounter + " = " + dispatchStr);

			if (lastExpr)
				out.println("ret "+ mType + " %" + registerCounter);
		}

		// if then else
		else if(expr.getClass() == AST.cond.class)
		{
			AST.cond str = (AST.cond)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			handleClassMethod(IRClass, formalMap, e1, out, false);
			String tag = ""+registerCounter;
			out.println("br i1 %"+registerCounter+", label %ifbody"+tag+", label %elsebody"+tag);

			out.println("ifbody"+tag+":");
			handleClassMethod(IRClass, formalMap, e2, out, false);
			if(lastExpr)
				out.println("ret "+CoolUtils.printTypes(e2.type) + " %" + ++registerCounter);
			out.println("br label %elsebody"+tag);

			out.println("elsebody"+tag+":");
			handleClassMethod(IRClass, formalMap, e3, out, false);
			if(lastExpr)
				out.println("ret "+CoolUtils.printTypes(e3.type) + " %" + ++registerCounter);

		    out.println("br label %thenbody"+tag); ///////////////////
			out.println("thenbody"+tag+":");

			if(lastExpr)
			out.println("ret "+CoolUtils.printTypes(formalMap.get("#rettype").typeid) + " %"+(registerCounter-1));

		}

		else if(expr.getClass() == AST.block.class) /////////////
		{
			AST.block str = (AST.block)expr;
			List<AST.expression> listExp = new ArrayList<AST.expression>();
			listExp = str.l1;
			boolean isMethod = false;
			if (lastExpr == true)
				isMethod = true;
			lastExpr = false;
			for(int i = 0; i < listExp.size(); ++i)
	        {
	        	AST.expression e2 = new AST.expression();
				e2 = listExp.get(i);
				if (i == listExp.size()-1 && isMethod)
					lastExpr = true;

				handleClassMethod(IRClass, formalMap, e2, out, lastExpr);

	        }

		}


		// while loop
		else if(expr.getClass() == AST.loop.class)
		{
			AST.loop str = (AST.loop)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.body;

			String tag = ""+registerCounter;
			out.println("br label %"+"predicate"+tag);
			out.println("predicate"+tag+":");
			handleClassMethod(IRClass, formalMap, e1, out, false);
			out.println("br i1 %"+registerCounter+", label %loopbody"+tag+", label %pool"+tag);

			out.println("loopbody"+tag+":");
			handleClassMethod(IRClass, formalMap, e2, out, false);
			out.println("br label %predicate"+tag);

			out.println("pool"+tag+":");

			if(lastExpr)
				out.println("ret "+CoolUtils.printTypes(formalMap.get("#rettype").typeid) + " %" + registerCounter);

		}

		else if(expr.getClass() == AST.eq.class)
		{
			AST.eq str = (AST.eq)expr;
			handleClassMethod(IRClass, formalMap, str.e1, out, false);
			String reg1 = "%"+registerCounter;
			handleClassMethod(IRClass, formalMap, str.e2, out, false);
			String reg2 = "%"+registerCounter;
			out.println("%"+ ++registerCounter +" = icmp eq " + CoolUtils.printTypes(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than
		else if(expr.getClass() == AST.lt.class)
		{
			AST.lt str = (AST.lt)expr;
			handleClassMethod(IRClass, formalMap, str.e1, out, false);
			String reg1 = "%"+registerCounter;
			handleClassMethod(IRClass, formalMap, str.e2, out, false);
			String reg2 = "%"+registerCounter;
			out.println("%"+ ++registerCounter +" = icmp slt " + CoolUtils.printTypes(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than equal to
		else if(expr.getClass() == AST.leq.class)
		{
			AST.leq str = (AST.leq)expr;
			handleClassMethod(IRClass, formalMap, str.e1, out, false);
			String reg1 = "%"+registerCounter;
			handleClassMethod(IRClass, formalMap, str.e2, out, false);
			String reg2 = "%"+registerCounter;
			out.println("%"+ ++registerCounter +" = icmp sle " + CoolUtils.printTypes(str.e1.type) + " "+reg1+", "+reg2);
		}

		// negation (not EXPR)
		else if(expr.getClass() == AST.neg.class)
		{
			AST.neg str = (AST.neg)expr;
			handleClassMethod(IRClass, formalMap, str.e1, out, false);
			String reg1 = "%"+registerCounter;
			out.println("%"+ ++registerCounter +" = icmp ne " + CoolUtils.printTypes(str.e1.type) + " "+reg1+", "+reg1);
		}

		// string const
		else if(expr.getClass() == AST.string_const.class)
		{
			AST.string_const str = (AST.string_const)expr;
			defineStringConst(str,out);
			out.println("%"+ ++registerCounter +" = alloca [1024 x i8], align 16");
			out.println("%"+ ++registerCounter +" = getelementptr inbounds [1024 x i8], [1024 x i8]* %"+(registerCounter-1)+", i32 0, i32 0");
			out.println("%"+ ++registerCounter +" = call i8* @strcpy(i8* %"+ (registerCounter-1) + ", i8* getelementptr inbounds (["+ (str.value.length()+1) +" x i8], ["+ (str.value.length()+1) +" x i8]* @.str."+ (stringCounter-1) +", i32 0, i32 0))");
			out.println("%"+ ++registerCounter +" = load [1024 x i8], [1024 x i8]* %"+(registerCounter-3) + ", align 16");

			if(lastExpr) {
				out.println("ret "+CoolUtils.printTypes(str.type) + " %" + (registerCounter));
			}
		}
	}
}
