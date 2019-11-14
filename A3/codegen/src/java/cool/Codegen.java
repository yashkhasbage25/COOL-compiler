package cool;

import java.util.*;
import cool.CoolUtils;
import cool.AST.class_;
import java.io.PrintWriter;
import cool.Counter;
import cool.IRClassInfo;
import cool.InheritanceGraph;

public class Codegen {

	private List<String> globalStr;
	private Counter stringCounter;
	private Counter registerCounter;
	private Counter ifCounter;
	private Counter loopCounter;
	private InheritanceGraph inheritanceGraph;
	private Map<String, class_> className2ClassMap;
	private HashMap<String, IRClassInfo> className2IRClassInfoMap;

	public Codegen(AST.program program, PrintWriter out) {

		// initialize Codegen variables
		initializeDefaultAttributes();

		// collect default classes and their methods
		addDefaultClasses();

		// build inheritance graph for cool classes
		buildGraph(program);

		// print ir for classes
		printIR(out);

		for (String s : globalStr)
			out.println(s);
	}

	private void initializeDefaultAttributes() {

		ifCounter = new Counter();
		loopCounter = new Counter();
		stringCounter = new Counter(0);
		registerCounter = new Counter();
		inheritanceGraph = new InheritanceGraph();
		globalStr = new ArrayList<String>();
		className2ClassMap = new HashMap<String, class_>();
		className2IRClassInfoMap = new HashMap<String, IRClassInfo>();

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

		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// Object class has only these methods: abort and type_name
		methods.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), CoolUtils.OBJECT_TYPE_STR, new AST.no_expr(0), 0));
		methods.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), CoolUtils.OBJECT_TYPE_STR, new AST.no_expr(0), 0));
		IRClassInfo objectClassInfo = new IRClassInfo(CoolUtils.OBJECT_TYPE_STR, null, new HashMap<String, AST.attr>(), methods, CoolUtils.OBJECT_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.OBJECT_TYPE_STR, objectClassInfo);

	}

	public void addIOClass() {

		// IO class methods
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();

		// formals of out_string
		List<AST.formal> outStringFormals = new ArrayList<AST.formal>();
		outStringFormals.add(new AST.formal("out_string", CoolUtils.STRING_TYPE_STR, 0));
		methods.put("out_string",
				new AST.method("out_string", outStringFormals, CoolUtils.IO_TYPE_STR, new AST.no_expr(0), 0));

		// formals of out_int
		List<AST.formal> outIntFormals = new ArrayList<AST.formal>();
		outIntFormals.add(new AST.formal("out_int", CoolUtils.INT_TYPE_STR, 0));
		methods.put("out_int", new AST.method("out_int", outIntFormals, CoolUtils.IO_TYPE_STR, new AST.no_expr(0), 0));

		// formals of input methods: in_string and in_int
		methods.put("in_string", new AST.method("in_string", new ArrayList<AST.formal>(), CoolUtils.STRING_TYPE_STR,
				new AST.no_expr(0), 0));
		methods.put("in_int",
				new AST.method("in_int", new ArrayList<AST.formal>(), CoolUtils.INT_TYPE_STR, new AST.no_expr(0), 0));

		// store all information in IRClassInfo
		IRClassInfo IOClassInfo = new IRClassInfo(CoolUtils.IO_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), methods, CoolUtils.IO_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.IO_TYPE_STR, IOClassInfo);
	}

	public void addIntClass() {

		// int class methods
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// inherit methods from object class
		methods.putAll(className2IRClassInfoMap.get(CoolUtils.OBJECT_TYPE_STR).classMethods);
		IRClassInfo intClassInfo = new IRClassInfo(CoolUtils.INT_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), new HashMap<String, AST.method>(), CoolUtils.INT_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.INT_TYPE_STR, intClassInfo);

	}

	public void addBoolClass() {

		// methods in bool class
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// inherit methods from object class
		methods.putAll(className2IRClassInfoMap.get(CoolUtils.OBJECT_TYPE_STR).classMethods);
		IRClassInfo boolClassInfo = new IRClassInfo(CoolUtils.BOOL_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), methods, CoolUtils.BOOL_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.BOOL_TYPE_STR, boolClassInfo);

	}

	public void addStringClass() {

		// string class methods and formals
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();

		// concat method of string class
		List<AST.formal> concatFormals = new ArrayList<AST.formal>();
		// formal args
		concatFormals.add(new AST.formal("str", CoolUtils.STRING_TYPE_STR, 0));
		methods.put("concat",
				new AST.method("concat", concatFormals, CoolUtils.STRING_TYPE_STR, new AST.no_expr(0), 0));

		// substr method of string class
		List<AST.formal> substrFormals = new ArrayList<AST.formal>();
		substrFormals.add(new AST.formal("start", CoolUtils.INT_TYPE_STR, 0));
		substrFormals.add(new AST.formal("end", CoolUtils.INT_TYPE_STR, 0));
		methods.put("substr",
				new AST.method("substr", substrFormals, CoolUtils.STRING_TYPE_STR, new AST.no_expr(0), 0));

		// length method of string class
		// no formals args
		methods.put("length",
				new AST.method("length", new ArrayList<AST.formal>(), CoolUtils.INT_TYPE_STR, new AST.no_expr(0), 0));
		IRClassInfo stringClassInfo = new IRClassInfo(CoolUtils.INT_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), methods, CoolUtils.STRING_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.STRING_TYPE_STR, stringClassInfo);
	}

	private void buildGraph(AST.program program) {

		// create inheritance graph
		// first, default classes
		addBasicEdges();

		// user defined classes
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			String programClassName = programClass.name;
			className2ClassMap.put(programClassName, programClass);
			inheritanceGraph.addEdge(parentClassName, programClassName);
		}
	}

	private void addBasicEdges() {

		// default classes
		inheritanceGraph.addEdge(CoolUtils.OBJECT_TYPE_STR, CoolUtils.INT_TYPE_STR);
		inheritanceGraph.addEdge(CoolUtils.OBJECT_TYPE_STR, CoolUtils.STRING_TYPE_STR);
		inheritanceGraph.addEdge(CoolUtils.OBJECT_TYPE_STR, CoolUtils.BOOL_TYPE_STR);
		inheritanceGraph.addEdge(CoolUtils.OBJECT_TYPE_STR, CoolUtils.IO_TYPE_STR);
	}

	private void printIR(PrintWriter out) {

		String root = CoolUtils.OBJECT_TYPE_STR;
		List<String> dfsOrdering = new ArrayList<>();
		// gives dfs ordreing to add to classes to map so that parent is already
		// processed
		inheritanceGraph.DfsOrdering(root);
		dfsOrdering = inheritanceGraph.getDfsOrder();
		addClassToMap(dfsOrdering);
		CoolUtils.printDefaultIR(out);
		PrintStructDeclerations(dfsOrdering, out);
		PrintMethods(dfsOrdering, out);
		PrintConstructor(dfsOrdering, out);
		String mainRetType = className2IRClassInfoMap.get(CoolUtils.MAIN_TYPE_STR).classMethods.get(CoolUtils.MAIN_FN_STR).typeid;
		out.println("define i32 @main() {\n" + "entry:\n"
				+ "\t%main = alloca %class.Main, align 8\n"
				+ "\tcall void @_CN4Main_FN4Main_(%class.Main* %main)\n"
				+ "\t%retval = call " + CoolUtils.printTypes2(mainRetType) + " @_CN4Main_FN4main_(%class.Main* %main)\n"
				+ "\tret i32 0\n" + "}");

	}

	private void PrintStructDeclerations(List<String> dfsOrdering, PrintWriter out) {

		// create structure definitions
		for (String className : dfsOrdering) {
			out.print("%class." + className + " = type {");
			if (className.equals(CoolUtils.OBJECT_TYPE_STR))
				out.print("i8*");
			else {
				out.print("\t%class." + inheritanceGraph.parentNameMap.get(className));
				for (AST.attr classAttrs : className2IRClassInfoMap.get(className).classAttrs.values()) {
					out.print(" ," + CoolUtils.printTypes2(classAttrs.typeid));
				}
			}
			out.println("}");
		}
	}

	private void defineStringConst(AST.expression expr, PrintWriter out) {
		AST.string_const stringConstExpr = (AST.string_const) expr;
		globalStr.add("@.str." + stringCounter.incrementIndex() + " = private unnamed_addr " + "constant ["
				+ (stringConstExpr.value.length() + 1) + " x i8] c\"" + stringConstExpr.value + "\\00\", align 1");
	}

	private void PrintMethods(List<String> dfsOrdering, PrintWriter out) {

		for (String className : dfsOrdering) {
			if (className.equals(CoolUtils.OBJECT_TYPE_STR))
				CoolUtils.PrintMethodsObject(out);
			else if (className.equals(CoolUtils.IO_TYPE_STR))
				CoolUtils.PrintMethodsIO(out);
			else if (className.equals(CoolUtils.STRING_TYPE_STR))
				CoolUtils.PrintMethodsString(out);
			else if (className.equals(CoolUtils.INT_TYPE_STR) || className.equals(CoolUtils.BOOL_TYPE_STR))
				continue;
			else {
				System.out.println(className);
				// HashMap<String, AST.formal> formalMap = new HashMap<String, AST.formal>();
				IRClassInfo classInfo = className2IRClassInfoMap.get(className);
				for (Map.Entry<String, AST.method> entry : classInfo.classMethods.entrySet()) {
					HashMap<String, String> formalsMap = new HashMap<String, String>();
					String methodName = entry.getKey();
					AST.method method = entry.getValue();
					List<String> blocks = new ArrayList<>();
					out.print("define " + CoolUtils.printTypes2(method.typeid) + " @"
							+ CoolUtils.getMangledName(className, methodName) + "(%class." + className + "* %this ");

					for (int i = 0; i < method.formals.size(); i++) {
						out.print("," + CoolUtils.printTypes2(method.formals.get(i).typeid) + " %"
								+ method.formals.get(i).name);
						// formalMap.put(method.formals.get(i).name, method.formals.get(i));
					}

					//formalMap.put("#rettype", new AST.formal("ret", method.typeid, 0));

					out.println("){");
					out.println("entry:");
					for (int i = 0; i < method.formals.size(); i++) {
						String type = CoolUtils.printTypes2(method.formals.get(i).typeid);
						out.println("\t%" + method.formals.get(i).name + ".addr = alloca " + type + ", align 4");
						formalsMap.put(method.formals.get(i).name, "%" + method.formals.get(i).name);
						out.println("\tstore " + type + " %" + method.formals.get(i).name + ", " + type + "* %"
						+ method.formals.get(i).name + ".addr, align 4");
					}
					registerCounter.reset();
					blocks.add("entry");
					// TODO
					String ret = handleExpr(className2IRClassInfoMap.get(className), method.body, formalsMap, blocks, out);
					System.out.println("267: ret " + ret );
					String rettype = CoolUtils.reverseParseTypeValue(ret);
			        // Print return statement
			        if (!rettype.equals(CoolUtils.printTypes2(method.typeid))) {
						System.out.println("271 " + rettype + " " + CoolUtils.printTypes2(method.typeid));
			        	if (rettype.equals("i32")) {
			        		out.println("\t%"+ registerCounter.incrementIndex()
								+ " = call noalias i8* @malloc(i64 8)"); // Object size
			        		out.println("\t%"+ registerCounter.incrementIndex()
								+ " = bitcast i8* %"+ registerCounter.prevIndex()
								+ " to " + CoolUtils.printTypes2(method.typeid));
			        	} else {
			        		out.println("\t%"+ registerCounter.incrementIndex()
								+ " = bitcast " + ret + " to "
								+ CoolUtils.printTypes2(method.typeid));
			        	}
			        	ret = CoolUtils.printTypes2(method.typeid) + " %" + registerCounter.getIndex();
			        }
			        out.println("\tret " + ret);
			        out.println("}\n");
				}
			}
		}
	}

	private void PrintConstructor(List<String> dfsOrdering, PrintWriter out) {
		for (String className : dfsOrdering) {
			if (CoolUtils.IsDefaultClass(className))
				continue;
			System.out.println(className);
			registerCounter.reset();
			int attrCounter = 0;
			String formals = CoolUtils.printTypes2(className) + " %this";
			out.println("define void @" + CoolUtils.getMangledName(className, className) + "(" + formals + " ) {");
			String parentName = inheritanceGraph.parentNameMap.get(className);
			List<String> blocks = new ArrayList<>();
			blocks.add("entry");
			out.println("entry:");
			registerCounter.incrementIndex();
			if (parentName != null) {
				out.println("\t%" + registerCounter.getIndex() + " = bitcast %class." + className + "* %this to %class."
						+ parentName + "*");
				// registerCounter.incrementIndex();
				out.println("\tcall void @" + CoolUtils.getMangledName(parentName, parentName) + "(%class." + parentName
						+ "* %" + registerCounter.getIndex() + ")");
			}
			for (AST.attr classAttr : className2IRClassInfoMap.get(className).classAttrs.values()) {
				System.out.println("constructor assign call of " + classAttr.name+" "+className);
				if (!(classAttr.value instanceof AST.no_expr)) {
					AST.assign assignExpr = new AST.assign(classAttr.name, classAttr.value, 0);
					assignExpr.type = classAttr.typeid;
					// handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);
					// handleExpr(nameToIrclassMap.get(s), null, exp, new ArrayList<>(), out);
					// irclassinfo, formalsList, expr, changedFormals, blocks, out
					// TODO
					handleExpr(className2IRClassInfoMap.get(className),assignExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.INT_TYPE_STR)) {
					AST.assign intExpr = new AST.assign(classAttr.name, new AST.int_const(0, 0), 0);
					intExpr.type = CoolUtils.INT_TYPE_STR;
					// handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);
					handleExpr(className2IRClassInfoMap.get(className), intExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.BOOL_TYPE_STR)) {
					AST.assign boolExpr = new AST.assign(classAttr.name, new AST.bool_const(false, 0), 0);
					boolExpr.type = CoolUtils.BOOL_TYPE_STR;
					// handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);
					handleExpr(className2IRClassInfoMap.get(className), boolExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.STRING_TYPE_STR)) {
					AST.assign stringExpr = new AST.assign(classAttr.name, new AST.string_const("", 0), 0);
					stringExpr.type = CoolUtils.STRING_TYPE_STR;
					// handleClassMethod(nameToIrclassMap.get(s), null, exp, out, false);
					handleExpr(className2IRClassInfoMap.get(className), stringExpr, new HashMap<String, String>(), blocks, out);
				} else {
					System.out.println(className + " construnctor");
					String ctype = CoolUtils.printTypes(className);
					registerCounter.incrementIndex();
					// TODO
					out.println("\t%" + registerCounter.getIndex() + " = getelementptr inbounds " + ctype + "," + ctype
							+ "* %this, i32 0, i32 " + className2IRClassInfoMap.get(className).attrIndex.get(classAttr.name));
					out.println("\tstore " + CoolUtils.printTypes2(classAttr.typeid) + " null, "
							+ CoolUtils.printTypes2(classAttr.typeid) + "* %" + registerCounter.getIndex() + ", align 4");
				}
			}
			out.println("ret void\n}");
		}
	}

	private void addClassToMap(List<String> dfsOrdering) {
		for (String currClassName : dfsOrdering) {
			if (CoolUtils.IsDefaultClass(currClassName))
				continue;

			AST.class_ currAstClass = className2ClassMap.get(currClassName);
			// System.out.println(s + Graph.parentNameMap.get(s));
			IRClassInfo parentClass = className2IRClassInfoMap.get(inheritanceGraph.parentNameMap.get(currClassName));
			// HashMap<String, AST.attr> curr_alist = parentClass.alist;
			HashMap<String, AST.attr> classAttrName2Attr = new HashMap<String, AST.attr>();
			HashMap<String, AST.method> classMethodName2Method = new HashMap<String, AST.method>();
			classAttrName2Attr.putAll(parentClass.classAttrs);
			// curr_mlist.putAll(parentClass.mlist); not needed
			int size = 0;
			for (AST.feature classFeature : currAstClass.features) {
				if (classFeature.getClass() == AST.attr.class) {
					AST.attr classAttr = (AST.attr) classFeature;
					classAttrName2Attr.put(classAttr.name, classAttr);
					if (classAttr.typeid == CoolUtils.INT_TYPE_STR || classAttr.typeid == CoolUtils.BOOL_TYPE_STR)
						size += 4;
					else
						size += 8;
				}
			}

			for (AST.feature classFeature : currAstClass.features) {
				if (classFeature.getClass() == AST.method.class) {
					AST.method classMethod = (AST.method) classFeature;
					classMethodName2Method.put(classMethod.name, classMethod);
				}
			}

			IRClassInfo currClass = new IRClassInfo(currAstClass.name, currAstClass.parent, classAttrName2Attr, classMethodName2Method, size);
			className2IRClassInfoMap.put(currClassName, currClass);
		}
	}

	// deprecated
	// IRClassInfo method expr changedformals blocks iut
	// private String _handleExpr(IRClass IRClass, HashMap<String, AST.formal> formalMap,
	// 	AST.expression expr, List<String> blocks, PrintWriter out) {
	// 	if(expr instanceof AST.bool_const) {
	// 		AST.bool_const e = (AST.bool_const) expr;
	// 		return "i32 " + (e.value ? 1 : 0);
	// 	} else if(e instanceof AST.string_const) {
	// 		globalStr += "@.str" + stringCounter++ + " = private unnamed_addr"
	// 			+ " constant [1024 x i8] c\"" + e.value + "\\00\", align 1\n";
	// 		registerCounter.incrementIndex();
	// 		// out.println("\t%" + registerCounter.getIndex() + " = bitcast "
	// 			// + "[1024 x i8]* @.str" + (stringCounter - 1) + " to [1024]")
	// 		out.println("\t%" + registerCounter.getIndex() + " =  bitcast [1024 x i8]"
	// 			+ "* @.str" + (stringCounter - 1) + " to [1024 x i8]*"
	// 		);
	// 		return "[1024 x i8]* %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.int_const) {
	// 		AST.int_const e = (AST.int_const) expr;
	// 		return "i32 " + e.value;
	// 	} else if(expr instanceof AST.object) {
	// 		AST.object e = (AST.object) expr;
	// 		String e1 = printExpr(cname, method, e.e1, changedFormals, blocks, out);
	// 		int attri = ci.attrList.indexOf(e.name);
	// 		// figure out assign to formal or attr
	// 		if (method != null) {
	// 			for (AST.formal f : method.formals) {
	// 				if (f.name.equals(e.name)) {
	// 					attri = -1;
	// 					break;
	// 				}
	// 			}
	// 		}
	// 		String type = printTypes(e.type);
	// 		String stype = printTypes(cname);
	// 		stype = stype.substring(0, stype.length()-1);
	// 		String e1type = reverseParseTypeValue(e1);
	// 		// Bitcast of types not same
	//         if (!e1type.equals(type)) {
	//         	if (e1type.equals("i32")) {
	//         		out.println("\t%"+(++varCount)+" = call noalias i8* @malloc(i64 8)"); // Object size
	//         		out.println("\t%"+(++varCount)+" = bitcast i8* %"+(varCount-1)+" to "+type);
	//         	} else {
	//         		out.println("\t%"+(++varCount)+" = bitcast "+e1+" to "+type);
	//         	}
	//         	e1 = type+" %"+varCount;
	//         }
	//         // If formal was reassigned
	// 		if (attri == -1) {
	// 			if (changedFormals.indexOf(e.name) == -1) {
	// 				out.println("%"+e.name+".addr = alloca "+type+", align 4");
	// 				changedFormals.add(e.name);
	// 			}
	// 			out.println("\tstore "+e1+", "+type+"* %"+e.name+".addr, align 4");
	// 			return 1;
	// 		} else {
	// 			out.println("\t%"+(++varCount)+" = getelementptr inbounds "+stype+", "+stype+"* %this, i32 0, i32 "+attri);
	// 			out.println("\tstore "+e1+", "+type+"* %"+varCount+", align 4"2);
	// 			return e1;
	// 		}
	// 	} else if(exp2r instanceof AST.comp) {
	// 		AST.comp e = (AST.comp) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = sub nsw i32 1, "
	// 			+ e1.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.eq) {
	// 		AST.eq e = (AST.eq) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		our.println("\t%" + registerCounter.getIndex() + " = icmp eq i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.leq) {
	// 		AST.leq e = (AST.leq) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = icmp sle i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.lt) {
	// 		AST.lt e = (AST.lt) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementCounter();
	// 		out.println("\t%" + registerCounter.getIndex() + " = icmp slt i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.neg) {
	// 		AST.neg e = (AST.new) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = sub nsw i32 0, "
	// 			+ e1.substring(4));
	// 	} else if(expr instanceof AST.divide) {
	// 		AST.divide e = (AST.divide)expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = icmpm eq i32 0, "
	// 			+ e2.substring(4));
	// 		ifCount++;
	// 		out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if."
	// 			+ "then" + ifCount + ", label %if.else" + ifCount);
	// 			out.println("if.then" + ifCount + ":");
	// 		blocks.add("if.then" + ifCount);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = bitcast [22 x i8]*"
	// 			+ " @Abortdivby0 to [1024 x i8]*");
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = call %class.IO* "
	// 			+ "@_CN2IO_FN9out_string(%class.IO* null, [1024 x i8]* %"
	// 			+ registerCounter.prevIndex() + ")");
	// 		out.println("\tcall void @exit(i32 1)");
	// 		out.println("\tbr label %if.else" + ifCount);
	// 		out.println("if.else" + ifCount + ":");
	// 		blocks.add("if.else" + ifCount);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = sdiv i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	//
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.mul) {
	// 		AST.mul e = (AST.mul) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = mul nsw i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.sub) {
	// 		AST.sub e = (AST.sub) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = sub nsw i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.plus) {
	// 		AST.plus e = (AST.plus) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		String e2 = handleExpr(IRClass, formalMap, e.e2, blocks, out);
	// 		registerCounter.incrementCounter();
	// 		out.println("\t%" + registerCounter.getIndex() + " = add nsw i32 "
	// 			+ e1.substring(4) + ", " + e2.substring(4));
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.isvoid) {
	// 		AST.isvoid e = (AST.isvoid) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = icmp eq "
	// 			+ e1 + "null");
	// 		return "i32 %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.new_) {
	// 		AST.new_ e = (AST.new_) expr;
	// 		String type = printTypes(e.typeid);
	// 		int size = className2Size.get(type);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = call noalias i8* @malloc(i64 " + size + ")");
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getindex() + " bitcast i8* %" + registerCounter.prevIndex() + " to " + type);
	// 		registercounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getindex() + " = call i32 @_CN"
	// 			+ e.typeid.length() + e.typeid + "_FN" + e.typeid.length()
	// 			+ e.typeid + "(" + type + " %" + registercounter.prevIndex()
	// 			+ ")");
	// 		return type + " %" + registerCounter.prevIndex(); ///////////////
	// 	} else if(expr instanceof AST.assign) { /////////////////
	// 		AST.assign e = (AST.assign) expr;
	// 		String e1 = handleExpr(IRClass, formalMap, e.e1, blocks, out);
	// 		int attri = IRClass.alist.indexOf(e.name);
	// 		if(method != null) {
	// 			for(AST.formal f: method.formals) {
	// 				if(f.name.equals(e.name)) {
	// 					atrri = -1;
	// 					break;
	// 				}
	// 			}
	// 		}
	// 		String type = printTypes(e.type);
	// 		String stype = printTypes(cname);
	// 		stype = stype.substring(0, stype.length() - 1);
	// 		String e1type = reverseParseTypeValueVar(e1);
	// 		if(!e1type.equals(type)) {
	// 			if(e1type.equals("i32")) {
	// 				registerCounter.incrementIndex();
	//				out.println("\t%" + registerCounter.getIndex() + " = getelementptr "
	// 					"inbounds " + stype + ", " + stype + "* %this, i32 0, i32 " + attri);
	// 				out.println("\tstore " + e1 + ", " + type + "* %"
	// 					+ registerCounter.getIndex() + 2", align 4");
	// 			} else {
	// 				registerC2ounter.incrementIndex();
	// 				out.println("\t%" + registerCounter.getIndex() + " = bitcast " + e1 + " to " + type);
	// 			}
	// 			e1 = type + " %" + registerCounter.getIndex();
	// 		}
	// 		if (attri == -1) {
	// 			if (changedFormals.indexOf(e.name) == -1) {
	// 				out.println("%"+e.name+".addr = alloca "+type+", align 4");
	// 				changedFormals.add(e.name);
	// 			}
	// 			out.println("\tstore "+e1+", "+type+"* %"+e.name+".addr, align 4");
	// 			return e1;
	// 		} else {
	// 			registerCounterincrementIndex();
	// 			out.println("\t%"+ registerCounter.getIndex() + " = getelementptr inbounds "
	// 				+ stype + ", " + stype + "* %this, i32 0, i32 " + attri);
	// 			out.println("\tstore " + e1 + ", " + type + "* %" + registerCounter.getIndex()
	// 				+ ", align 4");
	// 			return e1;
	// 		}2
	// 	} else if(expr instanceof AST.block) {
	// 		AST.block e = (AST.block) expr;
	// 		String re = "";
	// 		for(AST.expression e_: e.l1) {
	// 			re = handleExpr(IRClass, formalMap, ex, blocks, out);
	// 		}
	// 		return re;
	// 	} else if(expr instanceof AST.loop) {
	// 		AST.loop e = (AST.loop) expr;
	// 		int loopcnt = ++loopCount;
	// 		out.println("\tbr label %loop.cond" + loopcnt);
	// 		out.println("loop.cond" + loopcnt + ":");
	// 		out.println("loop.cond" + loopcnt);
	// 		String pred = handleExpr(IRClass, formalMap, e.predict, blocks, out);
	// 		out.println("\tbr i1 " + pred.substring(4) + ", label %loop.body"
	// 			+ loopcnt + " , label %loop.end" + loopcnt);
	// 		out.println("loop.body" + loopcnt + ":");
	// 		blocks.add("loop.body" + loopcnt);
	// 		String body = handleExpr(IRClass, formalMap, e.body, blocks, out);
	// 		out.println("\tbr label %loop.cond" + loopcnt);
	// 		out.println("loop.end" + loopcnt + ":");
	// 		blocks.add("loop.end" + loopcnt);
	// 		return body;
	// 	} else if(expr instanceof AST.cond) {
	// 		AST.cond e = (AST.cond) expr;
	// 		int ifcnt = ++ifCount;
	// 		String pred = handleExpr(IRClass, formalMap, e.predicate, blocks, out)
	// 		out.println("\tbr i1 " + pred.substring(4) + ", label %if.then" + ifcnt + ", label %if.else" + ifcnt);
	// 		out.println("if.then" + ifcnt + ":");
	// 		out.println("if.then" + ifcnt);
	// 		String ifbody = handleExpr(IRClass, formalMap, e.ifbody, blocks, out);
	// 		String ifbodylabel = blocks.get(blocks.size()-1);
	// 		ifbodylabel = reverseParseTypeValueVar(ifbody);
	// 		out.println("\tbr label %if.end" + ifcnt);
	// 		out.println("if.else" + ifcnt + ":");
	// 		blocks.add("if.else" + ifcnt);
	// 		String elsebody = handleExpr(IRClass, formalMap, e.elsebody, blocks, out);
	// 		String elsebodylabel = blocks.get(blocks.size() - 1);
	// 		elsebody = reverseParseTypeValueVar(elsebody);
	// 		out.println("\tbr label %if.end" + ifcnt);
	// 		out.println("if.end" + ifcnt + ":");
	// 		out.println("if.end" + ifcnt);
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = phi " + printTypes(e.type)
	// 			+ " [" + ifbody + ", %" + ifbodylabel + "], [" + elsebody
	// 			+ ", %" + elsebodylabel)
	// 		return parserType(e.type) + " %" + registerCounter.getIndex();
	// 	} else if(expr instanceof AST.static_dispatch) {
	// 		AST.static_dispatch e = (AST.static_dispatch) expr;
	// 		String caller = handleExpr(IRClass, formalMap, e.caller, blocks, out);
	//
	// 		List<String> actuals = new ArrayList<>();
	// 		for(AST.expression actual: e.actuals) {
	// 			String a = handleExpr(IRClass, formalMap, actual, blocks, out);
	// 			actuals.add(a);
	// 		}
	// 		ifCount++;
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = icmp eq "
	// 			+ caller + ", null");
	// 		out.println("\tbr i1 %" + registerCounter.getIndex()
	// 			+ ", label %if.then" + ifCount + ", label %if.else" + ifCount);
	// 		out.println("if.then" + ifCount + ":");
	// 		blocks.add("if.then" + ifCount);
	// 		registerCount.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = bitcast [25 x i8]*"
	// 			+ " @Abortdisvoid to [1024 x i8]*");
	// 		registerCounter.incrementIndex();
	// 		out.println("\t%" + registerCounter.getIndex() + " = call %class.IO* "
	// 			+ "@_CN2IO_FN9out_string(%class.IO* null, [1024 x i8]* %"
	// 			+ registerCounter.prevIndex() +")");
	// 		out.println("\tcall void @exit(i32 1)");
	// 		out.println("\tbr label %if.else" + ifCount);
	// 		out.println("if.else" + ifCount + ":");
	// 		blocks.add("if.else" + ifCount);
	// 		String funcname = "@_CN" + e.typeid + e.typeid.length() + "_FN" + e.name /////////////////////
	// 	} else {
	// 		System.out.println("Unreacheblae code!!!");
	// 	}
	// 	return "";
	// }

	// IRClassInfo method expr changedformals blocks iut
	private String handleExpr(IRClassInfo classInfo, AST.expression expr, Map<String, String>formalsMap, List<String> blocks, PrintWriter out) {
		if(expr instanceof AST.bool_const) {
			System.out.println("bool cost");
			AST.bool_const boolExpr = (AST.bool_const) expr;
			return "i32 " + (boolExpr.value ? 1 : 0);
		} else if(expr instanceof AST.string_const) {
			System.out.println("string cost");
			AST.string_const stringExpr = (AST.string_const) expr;
			String ty = "[" + (stringExpr.value.length() + 1) + " x i8]";
			globalStr.add("@.str" + stringCounter.getIndex()
				+ " = private unnamed_addr constant " + ty + " c\""
				+ stringExpr.value + "\\00\", align 1\n");
			stringCounter.incrementIndex();
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast "
				+ ty + "* @.str" + stringCounter.prevIndex() + " to [1024 x i8]*");
			return "[1024 x i8]* %" + registerCounter.getIndex();
		} else if(expr instanceof AST.int_const) {
			System.out.println("int const");

			AST.int_const intExpr = (AST.int_const) expr;

			return "i32 " + intExpr.value;
		} else if(expr instanceof AST.object) {
			System.out.println("object");

			AST.object objectExpr = (AST.object) expr;
			if((!classInfo.classAttrs.containsKey(objectExpr.name)) || (formalsMap.containsKey(objectExpr.name))) {
				if(! formalsMap.containsKey(objectExpr.name)) {
					System.out.println("forbidden case: objectexpr if-if");
					return CoolUtils.printTypes(objectExpr.type) + " %" + objectExpr.name;
				} else {
					String ty =  CoolUtils.printTypes2(objectExpr.type);
					out.println("\t%" + registerCounter.incrementIndex() + " = load "
						+ ty + ", " + ty + "* %" + objectExpr.name + ".addr, align 4");
					return ty + " %" + registerCounter.getIndex();
				}
			}
			System.out.println("722 classinfo.name: " + classInfo.name);
			String parseTypeName = CoolUtils.printTypes(classInfo.name);
			// TODO
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds "
				+ parseTypeName + ", " + parseTypeName + "* %this, i32 0, i32 " + classInfo.attrIndex.get(objectExpr.name));
			out.println("\t%" + registerCounter.incrementIndex() + " = load "
				+ CoolUtils.printTypes2(objectExpr.type) + ", " + CoolUtils.printTypes2(objectExpr.type)
				+ "* %" + registerCounter.prevIndex() + ", align 4");
			return CoolUtils.printTypes2(objectExpr.type) + " %" + registerCounter.getIndex();
		} else if(expr instanceof AST.comp) {
			System.out.println("comp");

			AST.comp compExpr = (AST.comp) expr;
			String e1 = handleExpr(classInfo, compExpr.e1, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 1, "
				+ e1.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.eq) {
			System.out.println("eq");

			AST.eq eqExpr = (AST.eq) expr;
			String e1 = handleExpr(classInfo, eqExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, eqExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq i32 "
			 + e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.leq) {
			System.out.println("leq");

			AST.leq leqExpr = (AST.leq) expr;
			String e1 = handleExpr(classInfo, leqExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, leqExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp sle i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.lt) {
			System.out.println("lt");

			AST.lt ltExpr = (AST.lt) expr;
			String e1 = handleExpr(classInfo, ltExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, ltExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp slt i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.neg) {
			System.out.println("neg");

			AST.neg negExpr = (AST.neg) expr;
			String e1 = handleExpr(classInfo, negExpr.e1, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 0, " + e1.substring(4));
		} else if(expr instanceof AST.divide) {
			System.out.println("divide");

			AST.divide divideExpr = (AST.divide) expr;
			String e1 = handleExpr(classInfo, divideExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, divideExpr.e2, formalsMap, blocks, out);

			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq i32 0, "
				+ e1.substring(4));
			ifCounter.incrementIndex();
			out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if.then"
				+ ifCounter.getIndex() + ", label %if.else" + ifCounter.getIndex());
			out.println("if.then" + ifCounter.getIndex() + ":");
			blocks.add("if.then" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [22 x i8]* @Abortdivby0 to [1024 x i8]*");
			out.println("\t%" + registerCounter.incrementIndex() + " = call %class.IO* @_CN2IO_FN9out_string(%class.IO* null, [1024 x i8]* %"
				+ registerCounter.prevIndex() + ")");
			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());
			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.getIndex() + " = sdiv i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 " + registerCounter.getIndex();
		} else if(expr instanceof AST.mul) {
			System.out.println("mul");

			AST.mul mulExpr = (AST.mul) expr;
			String e1 = handleExpr(classInfo, mulExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, mulExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = mul nsw i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.sub) {
			System.out.println("sub");

			AST.sub subExpr = (AST.sub) expr;
			String e1 = handleExpr(classInfo, subExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, subExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.plus) {
			System.out.println("plus");

			AST.plus plusExpr = (AST.plus) expr;
			String e1 = handleExpr(classInfo, plusExpr.e1, formalsMap, blocks, out);
			String e2 = handleExpr(classInfo, plusExpr.e2, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = add nsw i32 "
				+ e1.substring(4) + ", " + e2.substring(4));
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.isvoid) {
			System.out.println("isvoid");

			AST.isvoid isvoidExpr = (AST.isvoid) expr;
			String e1 = handleExpr(classInfo, isvoidExpr.e1, formalsMap, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq "
				+ e1 + ", null");
			return "i32 %" + registerCounter.getIndex();
		} else if(expr instanceof AST.new_) {
			System.out.println("new");

			AST.new_ newExpr = (AST.new_) expr;
			String type = CoolUtils.printTypes2(newExpr.typeid);
			int size = classInfo.size;
			out.println("\t%" + registerCounter.incrementIndex() + " = call noalias i8* @malloc(i64 "
				+ size + ")");
			out.println("\t%" + registerCounter.incrementIndex()
				+ " = bitcast i8* %" + registerCounter.prevIndex()
				+ " to " + type);
			out.println("\t%" + registerCounter.incrementIndex() + " = call "
				+ "i32 @" + CoolUtils.getMangledName(newExpr.typeid, newExpr.typeid)
				+ "(" + type + " %" + registerCounter.prevIndex() + ")");
			return type + " %" + registerCounter.prevIndex();
		} else if(expr instanceof AST.assign) {
			System.out.println("assign");

			AST.assign assignExpr = (AST.assign) expr;
			String e1 = handleExpr(classInfo, assignExpr.e1, formalsMap, blocks, out);
			System.out.println("e1:" + e1);
			String type = CoolUtils.printTypes2(assignExpr.type);
			String stype = CoolUtils.printTypes(classInfo.name);
			String e1type = CoolUtils.reverseParseTypeValue(e1);
			System.out.println("assign expr: type" + type);
			System.out.println("assign expr stype "+ stype);
			System.out.println("assign expr e1type: "+e1type);
			if(!e1type.equals(type)) {
				if(e1type.equals(type)) {
					if(e1type.equals("i32")) {
						out.println("\t%" + registerCounter.incrementIndex()
							+ " = call noalias i8* @malloc(i64 8)");
						out.println("\t%" + registerCounter.incrementIndex()
							+ " = bitcast i8* %" + registerCounter.prevIndex()
							+ " to " + type);
					} else {
						out.println("\t%" + registerCounter.incrementIndex()
							+ " = bitcast " + e1 + " to " + type);
					}
					e1 = type + " %" + registerCounter.getIndex();
				}
			}

			if(formalsMap.containsKey(assignExpr.name)) {
				out.println("\tstore " + e1 + ", " + type + "* %" + assignExpr.name
					+ ".addr, align 4");
				return e1;
			} else {
				// TODO
				out.println("\t%"+ registerCounter.incrementIndex()
					+ " = getelementptr inbounds " + stype + ", " + stype
					+ "* %this, i32 0, i32 " + classInfo.attrIndex.get(assignExpr.name));
				out.println("\tstore " + e1 + ", " + type + "* %"
					+ registerCounter.getIndex() + ", align 4");
				return e1;
			}
		} else if(expr instanceof AST.block) {
			System.out.println("block");

			AST.block blockExpr = (AST.block) expr;
			String register = "";
			for(AST.expression exprInBlock: blockExpr.l1) {
				register = handleExpr(classInfo, exprInBlock, formalsMap, blocks, out);
			}
			return register;
		} else if(expr instanceof AST.loop) {
			System.out.println("loop");

			AST.loop loopExpr = (AST.loop) expr;
			out.println("\tbr label %loop.cond" + loopCounter.incrementIndex());
			out.println("loop.cond" + loopCounter.getIndex() + ":");
			blocks.add("loop.cond" + loopCounter.getIndex());
			String predicate = handleExpr(classInfo, loopExpr.predicate, formalsMap, blocks, out);
			out.println("\tbr i1 " + predicate.substring(4) + ", label %loop.body"
				+ loopCounter.getIndex() + " , label %loop.end" + loopCounter.getIndex());
			out.println("loop.body" + loopCounter.getIndex() + ":");
			blocks.add("loop.body" + loopCounter.getIndex());
			String body = handleExpr(classInfo, loopExpr.body, formalsMap, blocks, out);
			out.println("\tbr label %loop.cond" + loopCounter.getIndex());
			out.println("loop.end" + loopCounter.getIndex() + ":");
			blocks.add("loop.end" + loopCounter.getIndex());
			return body;
		} else if(expr instanceof AST.cond) {
			System.out.println("cond");

			AST.cond condExpr = (AST.cond) expr;
			int ifCount = ifCounter.incrementIndex();
			String predicate = handleExpr(classInfo, condExpr.predicate, formalsMap, blocks, out);
			out.println("\tbr i1 " + predicate.substring(4) + ", label %if.then"
				+ ifCount + ", label %if.else" + ifCount);
			out.println("if.then" + ifCount + ":");
			blocks.add("if.then" + ifCount);
			String ifBody = handleExpr(classInfo, condExpr.ifbody, formalsMap, blocks, out);
			String ifBodyLabel = blocks.get(blocks.size() - 1);
			ifBody = CoolUtils.reverseParseTypeValueVar(ifBody);
			out.println("\tbr label %if.end" + ifCount);
			out.println("if.else" + ifCount + ":");
			blocks.add("if.else" + ifCount);
			String elseBody = handleExpr(classInfo, condExpr.elsebody, formalsMap, blocks, out);
			String elseBodyLabel = blocks.get(blocks.size() - 1);
			elseBody = CoolUtils.reverseParseTypeValueVar(elseBody);
			out.println("\tbr label %if.end" + ifCount);
			out.println("if.end" + ifCount + ":");
			blocks.add("if.end" + ifCount);
			out.println("\t%" + registerCounter.incrementIndex() + " = phi "
				+ CoolUtils.printTypes2(condExpr.type) + " [" + ifBody + ", %"
				+ ifBodyLabel + "], [" + elseBody + ", %" + elseBodyLabel + "]");
			return CoolUtils.printTypes2(condExpr.type) + " %" + registerCounter.getIndex();
		} else if(expr instanceof AST.static_dispatch) {
			AST.static_dispatch staticDispatchExpr = (AST.static_dispatch) expr;
			String caller = handleExpr(classInfo, staticDispatchExpr.caller, formalsMap, blocks, out);
			List<String> actuals = new ArrayList<>();
			for(AST.expression actual: staticDispatchExpr.actuals) {
				String a = handleExpr(classInfo, actual, formalsMap, blocks, out);
				actuals.add(a);
			}

			ifCounter.incrementIndex();
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq "
				+ caller + ", null");
			out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if.then"
				+ ifCounter.getIndex() + ", label %if.else" + ifCounter.getIndex());
			out.println("if.then" + ifCounter.getIndex() + ":");
			blocks.add("if.then" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*");
			out.println("\t%" + registerCounter.incrementIndex()
				+ " = call %class.IO* @" + CoolUtils.getMangledName(CoolUtils.IO_TYPE_STR, "out_string") + "(%class.IO* null, [1024 x i8]* %"
				+ registerCounter.prevIndex() + ")");
			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());

			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());

			String funcName = "@" + CoolUtils.getMangledName(staticDispatchExpr.typeid, staticDispatchExpr.name);
			// TODO
			IRClassInfo callerIRClassInfo = className2IRClassInfoMap.get(CoolUtils.reverseParseType(CoolUtils.reverseParseTypeValue(caller)));
			while (!CoolUtils.reverseParseTypeValue(caller).equals(CoolUtils.printTypes2(staticDispatchExpr.typeid))) {
				String par = CoolUtils.printTypes(callerIRClassInfo.parent);
				String ty = CoolUtils.reverseParseTypeValue(caller);
				ty = ty.substring(0, ty.length()-1);
				// TODO
				out.println("\t%" + registerCounter.incrementIndex() +
					" = getelementptr inbounds "+ ty + ", " + ty + "* "
					+ CoolUtils.reverseParseTypeValueVar(caller) + ", i32 0, i32 0");
				caller = par + "* %" + registerCounter.getIndex();
				callerIRClassInfo = className2IRClassInfoMap.get(callerIRClassInfo.parent);
			}

			String actualsStr = caller;
			for(int i = 0; i < actuals.size(); i++) {
				actualsStr += ", " + actuals.get(i);
			}
			out.println("\t%" + registerCounter.incrementIndex() + " = call "
				+ CoolUtils.printTypes2(staticDispatchExpr.type) + " "
				+ funcName + "(" + actualsStr + ")");
			return CoolUtils.printTypes2(staticDispatchExpr.type) + " %" + registerCounter.getIndex();
		} else {
			System.out.println("expression type was not expected");
			System.out.println(expr.type);
			return "";
		}
		System.out.println("no expr type");
		return "";
	}

	// deprecated
	// private void handleClassMethod(IRClass IRClass, HashMap<String, AST.formal> formalMap, AST.expression expr,
	// 		PrintWriter out, boolean lastExpr) {
	// 	// assignment expression
	// 	if (expr.getClass() == AST.assign.class) {
	// 		String reg, varType;
	// 		AST.assign assignExpr = (AST.assign) expr;
	// 		AST.expression exp = assignExpr.e1;
	// 		if (IRClass.alist.containsKey(assignExpr.name)) {
	// 			out.println("%" + registerCounter.incrementIndex() + " = getelementptr inbounds %class." + IRClass.name
	// 					+ ", %class." + IRClass.name + "* %this, i32 0, i32 " + IRClass.attrIndex.get(assignExpr.name));
	// 			reg = "%" + (registerCounter + 1);
	// 			// out.println("??" + assignExpr.name + " " +
	// 			// IRClass.alist.get(assignExpr.name).typeid);
	// 			varType = CoolUtils.printTypes(IRClass.alist.get(assignExpr.name).typeid);
	// 		} else {
	// 			reg = "%" + assignExpr.name;
	// 			varType = CoolUtils.printTypes(formalMap.get(assignExpr.name).typeid);
	// 		}
	// 		// out.println("exp type: " + exp.type);
	// 		handleClassMethod(IRClass, formalMap, exp, out, false);
	// 		out.println("store " + varType + " %" + registerCounter + ", " + varType + "* " + reg + ", align 4"); // check
	// 		if (lastExpr) {
	// 			out.println("ret " + varType + " %" + registerCounter);
	// 			return registerCounter;
	// 		}
	// 	}
	//
	// 	// plus expression
	// 	else if (expr.getClass() == AST.plus.class) {
	// 		AST.plus plusExpr = (AST.plus) expr;
	// 		handleClassMethod(IRClass, formalMap, plusExpr.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, plusExpr.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = add nsw i32 " + reg1 + ", " + reg2);
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(plusExpr.e1.type) + " %" + registerCounter);
	// 	}
	//
	// 	// subtraction expression
	// 	else if (expr.getClass() == AST.sub.class) {
	// 		AST.sub subExpr = (AST.sub) expr;
	// 		handleClassMethod(IRClass, formalMap, subExpr.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, subExpr.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = sub nsw i32 " + reg1 + ", " + reg2);
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(subExpr.e1.type) + " %" + registerCounter);
	// 	}
	//
	// 	// multiplication expression
	// 	else if (expr.getClass() == AST.mul.class) {
	// 		AST.mul mulExpr = (AST.mul) expr;
	// 		handleClassMethod(IRClass, formalMap, mulExpr.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, mulExpr.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = mul nse i32 " + reg1 + ", " + reg2);
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(mulExpr.e1.type) + " %" + registerCounter);
	// 	}
	//
	// 	// division expression
	// 	else if (expr.getClass() == AST.divide.class) {
	// 		AST.divide divideExpr = (AST.divide) expr;
	// 		handleClassMethod(IRClass, formalMap, divideExpr.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, divideExpr.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = sdic i32 " + reg1 + ", " + reg2);
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(divideExpr.e1.type) + " %" + registerCounter);
	// 	}
	//
	// 	// int expression
	// 	else if (expr.getClass() == AST.int_const.class) {
	// 		out.println("%" + registerCounter.incrementIndex() + " = alloca i32, align 4");
	// 		out.println("store i32 " + ((AST.int_const) expr).value + ", i32* %" + registerCounter + ", align 4");
	// 		out.println("%" + registerCounter.incrementIndex() + " = load i32, i32* %" + (registerCounter - 1)
	// 				+ ", align 4");
	//
	// 		if (lastExpr)
	// 			out.println("ret i32 %" + registerCounter);
	// 	}
	//
	// 	// bool expression
	// 	else if (expr.getClass() == AST.bool_const.class) {
	// 		boolean bool = ((AST.bool_const) expr).value;
	// 		int int4bool = 0;
	// 		if (bool)
	// 			int4bool = 1;
	//
	// 		out.println("%" + registerCounter.incrementIndex() + " = alloca i8, align 1"); ////////////////////
	// 		out.println("store i8 " + int4bool + ", i8* %" + registerCounter + ", align 1");
	// 		out.println(
	// 				"%" + registerCounter.incrementIndex() + " = load i8, i8* %" + (registerCounter - 1) + ", align 1");
	//
	// 		if (lastExpr)
	// 			out.println("ret int32 %" + registerCounter);
	// 	}
	//
	// 	// new expression
	// 	else if (expr.getClass() == AST.new_.class) {
	// 		AST.new_ newExpr = (AST.new_) expr;
	// 		out.println("%" + registerCounter.incrementIndex() + " = alloca " + CoolUtils.printTypes2(newExpr.typeid)
	// 				+ ", align 4");
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes2(newExpr.typeid) + "* %" + registerCounter);
	// 	}
	//
	// 	// identifier expression
	// 	else if (expr.getClass() == AST.object.class) {
	// 		String reg;
	// 		AST.object str = (AST.object) expr;
	// 		if (str.name.equals("this")) {
	// 			out.println("%" + registerCounter.incrementIndex() + " = alloca " + CoolUtils.printTypes(str.type)
	// 					+ ", align 4");
	// 			out.println("store " + CoolUtils.printTypes(str.type) + " %this, " + CoolUtils.printTypes(str.type)
	// 					+ "* %" + registerCounter + ", align 4");
	// 			out.println("%" + registerCounter.incrementIndex() + " = load " + CoolUtils.printTypes(str.type) + ", "
	// 					+ CoolUtils.printTypes(str.type) + "* %" + (registerCounter - 1) + ", align 4");
	// 		}
	//
	// 		else if (formalMap.containsKey(str.name)) {
	// 			out.println("%" + registerCounter.incrementIndex() + " = alloca " + CoolUtils.printTypes(str.type)
	// 					+ ", align 4");
	// 			out.println("store " + CoolUtils.printTypes(str.type) + " %" + str.name + ", "
	// 					+ CoolUtils.printTypes(str.type) + "* %" + registerCounter + ", align 4");
	// 			out.println("%" + registerCounter.incrementIndex() + " = load " + CoolUtils.printTypes(str.type) + ", "
	// 					+ CoolUtils.printTypes(str.type) + "* %" + (registerCounter - 1) + ", align 4");
	// 		} else { // it must be defined in attributes.
	// 			out.println("%" + registerCounter.incrementIndex() + " = getelementptr inbounds %class." + IRClass.name
	// 					+ ", %class." + IRClass.name + "* %this, i32 0, i32 " + IRClass.attrIndex.get(str.name));
	// 			out.println("%" + registerCounter.incrementIndex() + " = load  "
	// 					+ CoolUtils.printTypes(IRClass.alist.get(str.name).typeid) + ", "
	// 					+ CoolUtils.printTypes(IRClass.alist.get(str.name).typeid) + "* %" + (registerCounter - 1)
	// 					+ ", align 4");
	//
	// 		}
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(str.type) + " %" + registerCounter);
	// 	}
	//
	// 	// static dispatch
	// 	else if (expr.getClass() == AST.static_dispatch.class) {
	// 		// AST.static_dispatch str = (AST.static_dispatch)expr;
	// 		// List<AST.expression> expression_listsd = new ArrayList<AST.expression>();
	// 		// expression_listsd = str.actuals;
	// 		// for(int i = 0; i < expression_listsd.size(); i++) {
	// 		// expr = expression_listsd.get(i);
	// 		// handleClassMethod(IRClass, formalMap, expr, out, false);
	// 		// }
	// 		// System.out.println("expr.name = " + ((AST.static_dispatch)expr).name);
	// 	}
	//
	// 	// if then else
	// 	else if (expr.getClass() == AST.cond.class) {
	// 		AST.cond str = (AST.cond) expr;
	// 		AST.expression e1 = str.predicate;
	// 		AST.expression e2 = str.ifbody;
	// 		AST.expression e3 = str.elsebody;
	// 		handleClassMethod(IRClass, formalMap, e1, out, false);
	// 		String tag = "" + registerCounter;
	// 		out.println("br i1 %" + registerCounter + ", label %ifbody" + tag + ", label %elsebody" + tag);
	//
	// 		out.println("ifbody" + tag + ":");
	// 		handleClassMethod(IRClass, formalMap, e2, out, false);
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(e2.type) + " %" + registerCounter.incrementIndex());
	// 		out.println("br label %elsebody" + tag);
	//
	// 		out.println("elsebody" + tag + ":");
	// 		handleClassMethod(IRClass, formalMap, e3, out, false);
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(e3.type) + " %" + registerCounter.incrementIndex());
	//
	// 		out.println("br label %thenbody" + tag); ///////////////////
	// 		out.println("thenbody" + tag + ":");
	//
	// 		if (lastExpr)
	// 			out.println(
	// 					"ret " + CoolUtils.printTypes(formalMap.get("#rettype").typeid) + " %" + (registerCounter - 1));
	//
	// 	}
	//
	// 	else if (expr.getClass() == AST.block.class) /////////////
	// 	{
	// 		AST.block str = (AST.block) expr;
	// 		List<AST.expression> listExp = new ArrayList<AST.expression>();
	// 		listExp = str.l1;
	// 		boolean isMethod = false;
	// 		if (lastExpr == true)
	// 			isMethod = true;
	// 		lastExpr = false;
	// 		for (int i = 0; i < listExp.size(); ++i) {
	// 			AST.expression e2 = new AST.expression();
	// 			e2 = listExp.get(i);
	// 			if (i == listExp.size() - 1 && isMethod)
	// 				lastExpr = true;
	//
	// 			handleClassMethod(IRClass, formalMap, e2, out, lastExpr);
	//
	// 		}
	//
	// 	}
	//
	// 	// while loop
	// 	else if (expr.getClass() == AST.loop.class) {
	// 		AST.loop str = (AST.loop) expr;
	// 		AST.expression e1 = str.predicate;
	// 		AST.expression e2 = str.body;
	//
	// 		String tag = "" + registerCounter;
	// 		out.println("br label %" + "predicate" + tag);
	// 		out.println("predicate" + tag + ":");
	// 		handleClassMethod(IRClass, formalMap, e1, out, false);
	// 		out.println("br i1 %" + registerCounter + ", label %loopbody" + tag + ", label %pool" + tag);
	//
	// 		out.println("loopbody" + tag + ":");
	// 		handleClassMethod(IRClass, formalMap, e2, out, false);
	// 		out.println("br label %predicate" + tag);
	//
	// 		out.println("pool" + tag + ":");
	//
	// 		if (lastExpr)
	// 			out.println("ret " + CoolUtils.printTypes(formalMap.get("#rettype").typeid) + " %" + registerCounter);
	//
	// 	}
	//
	// 	else if (expr.getClass() == AST.eq.class) {
	// 		AST.eq str = (AST.eq) expr;
	// 		handleClassMethod(IRClass, formalMap, str.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, str.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = icmp eq " + CoolUtils.printTypes(str.e1.type) + " "
	// 				+ reg1 + ", " + reg2);
	// 	}
	//
	// 	// less than
	// 	else if (expr.getClass() == AST.lt.class) {
	// 		AST.lt str = (AST.lt) expr;
	// 		handleClassMethod(IRClass, formalMap, str.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, str.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = icmp slt " + CoolUtils.printTypes(str.e1.type)
	// 				+ " " + reg1 + ", " + reg2);
	// 	}
	//
	// 	// less than equal to
	// 	else if (expr.getClass() == AST.leq.class) {
	// 		AST.leq str = (AST.leq) expr;
	// 		handleClassMethod(IRClass, formalMap, str.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		handleClassMethod(IRClass, formalMap, str.e2, out, false);
	// 		String reg2 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = icmp sle " + CoolUtils.printTypes(str.e1.type)
	// 				+ " " + reg1 + ", " + reg2);
	// 	}
	//
	// 	// negation (not EXPR)
	// 	else if (expr.getClass() == AST.neg.class) {
	// 		AST.neg str = (AST.neg) expr;
	// 		handleClassMethod(IRClass, formalMap, str.e1, out, false);
	// 		String reg1 = "%" + registerCounter;
	// 		out.println("%" + registerCounter.incrementIndex() + " = icmp ne " + CoolUtils.printTypes(str.e1.type) + " "
	// 				+ reg1 + ", " + reg1);
	// 	}
	//
	// 	// string const
	// 	else if (expr.getClass() == AST.string_const.class) {
	// 		AST.string_const str = (AST.string_const) expr;
	// 		defineStringConst(str, out);
	// 		out.println("%" + registerCounter.incrementIndex() + " = alloca [1024 x i8], align 16");
	// 		out.println("%" + registerCounter.incrementIndex() + " = getelementptr inbounds [1024 x i8], [1024 x i8]* %"
	// 				+ (registerCounter - 1) + ", i32 0, i32 0");
	// 		out.println("%" + registerCounter.incrementIndex() + " = call i8* @strcpy(i8* %" + (registerCounter - 1)
	// 				+ ", i8* getelementptr inbounds ([" + (str.value.length() + 1) + " x i8], ["
	// 				+ (str.value.length() + 1) + " x i8]* @.str." + (stringCounter - 1) + ", i32 0, i32 0))");
	// 		out.println("%" + registerCounter.incrementIndex() + " = load [1024 x i8], [1024 x i8]* %"
	// 				+ (registerCounter - 3) + ", align 16");
	//
	// 		if (lastExpr) {
	// 			out.println("ret " + CoolUtils.printTypes(str.type) + " %" + (registerCounter));
	// 		}
	// 	} else {
	// 		System.out.println("Unreacheblae code.");
	// 	}
	// }
}
