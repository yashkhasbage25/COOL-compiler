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
			if (s.equals("Object"))
				CoolUtils.PrintMethodsObject(out);
			else if (s.equals("IO"))
				CoolUtils.PrintMethodsIO(out);
			else if (s.equals("String"))
				CoolUtils.PrintMethodsString(out);
			else if (s.equals("Int") || s.equals("Bool"))
				return;
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

	private void handleClassMethod(IRClass irClass, HashMap<STring, AST.formal> formalMap, AST.expression expr, PrintWriter out, boolean lastExpr) {
		// assignment expression
		if(expr.getClass() == AST.assign.class) {
			AST.assign assignExpr = (AST.assign) expr;
			AST.expression exp = assignExpr.e1;
			if(irClass.alist.containsKey(assignExpr.name)) {
				out.println("%" + ++regNo + " = getelementptr inbounds %class."
				+ irClass.name + ", %class." + irClass.name +
				"* %this, i32 0, i32 " + irClass.attrOffset.get(assignExpr.name));
				reg = "%" + regNo;
				varType = IRType(irClass.alist.get(assignExpr.name).typeid);
			} else {
				reg = "%" + assignExpr.name;
				varType = IRType(formalMap.get(assignExpr.name).typeid);
			}

			handleClassMethod(irClass, fmap, exp, out, false);
			out.println("store " + varType + " %" + regNo + ", " + varType +
			"* " + reg + ", align 4");
			if(lastExpr) out.println("ret " + varType + " %" +regNo);
		}

		// plus expression
		if(expr.getClass() == AST.plus.class) {
			AST.plus plusExpr = (AST.plus) expr;
			handleClassMethod(irClass, formalMap, plusExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, plusExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = add nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + IRType(plusExpr.e1.type) + " %" + regNo);
		}

		// subtraction expression
		if(expr.getClass() == AST.sub.class) {
			AST.subExpr = (AST.sub) expr;
			handleClassMethod(irClass, formalMap, subExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, subExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = sub nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + IRType(subExpr.e1.type) + " %" + regNo);
		}

		// multiplication expression
		if(expr.getClass() == AST.mul.class) {
			AST.mulExpr = (AST.mul) expr;
			handleClassMethod(irClass, formalMap, mulExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, mulExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = mul nse i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + IRType(mulExpr.e1.type) + " %" + regNo);
		}

		// division expression
		if(expr.getClass() == AST.divide.class)) {
			AST.divide divideExpr = (AST.divide) expr;
			handleClassMethod(irClass, formalMap, divideExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, divideExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = sdic i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + IRType(divideExpr.e1.type) + " %" +regNo);
		}

		// int expression
		if(expr.getClass() == AST.int_const.class) {
			out.println("%" + ++regNo + " = alloca i32, align 4");
			out.println("store i32 " + ((AST.int_const) expr).value + ", i32* %" + regNo + ", align 4");
			out.println("%" + ++regNo + " = load i32, i32* %" + (regNo-1) + ", align 4");

			if (lastExpr) out.println("ret i32 %" + regNo);
		}

		// bool expression
		if(expr.getClass() == AST.bool_const.class) {
			boolean bool = ((AST.bool_const) expr).value;
			int int4bool = 0;
			if(bool) int4bool = 1;

			out.println("%" + ++regNo + " = alloca i32, align 1"); ////////////////////
			out.println("store i32 " + int4bool + ", i32* %" + regNo + ", align 4");
			out.println("%" + ++regNo + " = load i32, i32* %" + (regNo-1) + ", align 4");

			if(lastExpr) out.println("ret int32 %" + regNo);
		}

		// new expression
		if(expr.getClass() == AST.new_.class) {
			AST.new_ newExpr = (AST.new_) expr;
			out.println("%" + ++regNo + " = alloca " + IRType2(newExpr.typeid) + ", align 4");

			if(lastExpr) out.println("ret " + IRType2(newExpr.typeid) + "* %" + regNo);
		}

		// identifier expression
		if(expr.getClass() == AST.object.class)
		{
			String reg;
			AST.object str = (AST.object)expr;
			if (str.name.equals("self")) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %this, "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}

			else if(fmap.containsKey(str.name)) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %"+str.name+", "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}
			else { // it must be defined in attributes.
				out.println("%"+ ++regNo +" = getelementptr inbounds %class." + irclass.name + ", %class."+irclass.name+"* %this, i32 0, i32 "+irclass.attrOffset.get(str.name));
				out.println("%"+ ++regNo +" = load  " + IRType(irclass.alist.get(str.name).typeid) + ", "+ IRType(irclass.alist.get(str.name).typeid) + "* %"+(regNo-1)+", align 4");

			}

			if(lastExpr)
				out.println("ret "+IRType(str.type) + " %" + regNo);
		}

		// dispatch
		else if(expr.getClass() == AST.dispatch.class)
		{
			AST.dispatch str = (AST.dispatch)expr;
			if( !(str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) )
				ProcessMethod(irclass, fmap, str.caller, out, false);
			String thisReg = ""+regNo;

			String caller = str.caller.type;
			if( (str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) ) {
				caller = irclass.name;
				thisReg = "this";
			} else {

				// Go through the attrs of the class called, and allocate if there's an initialisation in the attributes node.

				String cname = caller;
				for (Entry<String, AST.attr> attrEntry : IRclassTable.getAttrs(cname).entrySet()) {
					AST.attr attrNode = attrEntry.getValue();
					if (attrNode.value.getClass() == AST.new_.class) {
						AST.new_ node = (AST.new_)attrNode.value;
						out.println("%"+ ++regNo +" = alloca " + IRType2(node.typeid)+ ", align 4");
						out.println("%"+ ++regNo  +" = getelementptr inbounds %class." + cname + ", %class."+cname+"* %" + thisReg + ", i32 0, i32 "+IRclassTable.getIRClass(cname).attrOffset.get(attrNode.name));
						out.println("store " + IRType2(node.typeid)+ "* %"+ (regNo - 1) +", " + IRType2(node.typeid)+ "** %"+regNo+", align 8");
					}

				}
			}

			String methodName = str.name;
			String mType =   IRType(IRclassTable.getIRClass(caller).mlist.get(methodName).typeid);
			String dispatchStr = "call " + mType + " @"+caller+"_"+methodName + "("+ IRType(caller) + " %"+thisReg;

			for(int i = 0; i < str.actuals.size(); i++)
			{
				expr = str.actuals.get(i);
				ProcessMethod(irclass, fmap, expr, out, false);
				dispatchStr += ", "+IRType(expr.type) + " %"+ regNo;
			}

			dispatchStr += ")";

			out.println("%"+ ++regNo + " = " + dispatchStr);

			if (lastExpr)
				out.println("ret "+ mType + " %" + regNo);
		}

		// if then else
		else if(expr.getClass() == AST.cond.class)
		{
			AST.cond str = (AST.cond)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			ProcessMethod(irclass, fmap, e1, out, false);
			String tag = ""+regNo;
			out.println("br i1 %"+regNo+", label %ifbody"+tag+", label %elsebody"+tag);

			out.println("ifbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);
			if(lastExpr)
				out.println("ret "+IRType(e2.type) + " %" + regNo++);
			out.println("br label %elsebody"+tag);

			out.println("elsebody"+tag+":");
			ProcessMethod(irclass, fmap, e3, out, false);
			if(lastExpr)
				out.println("ret "+IRType(e3.type) + " %" + regNo++);

		    out.println("br label %thenbody"+tag); ///////////////////
			out.println("thenbody"+tag+":");

			if(lastExpr)
			out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %"+(regNo-1));

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

				ProcessMethod(irclass, fmap, e2, out, lastExpr);

	        }

		}


		// while loop
		else if(expr.getClass() == AST.loop.class)
		{
			AST.loop str = (AST.loop)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.body;

			String tag = ""+regNo;
			out.println("br label %"+"predicate"+tag);
			out.println("predicate"+tag+":");
			ProcessMethod(irclass, fmap, e1, out, false);
			out.println("br i1 %"+regNo+", label %loopbody"+tag+", label %pool"+tag);

			out.println("loopbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);
			out.println("br label %predicate"+tag);

			out.println("pool"+tag+":");

			if(lastExpr)
				out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %" + regNo);

		}

		else if(expr.getClass() == AST.eq.class)
		{
			AST.eq str = (AST.eq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp eq " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than
		else if(expr.getClass() == AST.lt.class)
		{
			AST.lt str = (AST.lt)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp slt " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than equal to
		else if(expr.getClass() == AST.leq.class)
		{
			AST.leq str = (AST.leq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp sle " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// negation (not EXPR)
		else if(expr.getClass() == AST.neg.class)
		{
			AST.neg str = (AST.neg)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp ne " + IRType(str.e1.type) + " "+reg1+", "+reg1);
		}

		// string const
		else if(expr.getClass() == AST.string_const.class)
		{
			AST.string_const str = (AST.string_const)expr;
			defineStringConst(str,out);
			out.println("%"+ ++regNo +" = alloca [1024 x i8], align 16");
			out.println("%"+ ++regNo +" = getelementptr inbounds [1024 x i8], [1024 x i8]* %"+(regNo-1)+", i32 0, i32 0");
			out.println("%"+ ++regNo +" = call i8* @strcpy(i8* %"+ (regNo-1) + ", i8* getelementptr inbounds (["+ (str.value.length()+1) +" x i8], ["+ (str.value.length()+1) +" x i8]* @.str."+ (strCount-1) +", i32 0, i32 0))");
			out.println("%"+ ++regNo +" = load [1024 x i8], [1024 x i8]* %"+(regNo-3) + ", align 16");

			if(lastExpr) {
				out.println("ret "+IRType(str.type) + " %" + (regNo));
			}
		}
	}
}
