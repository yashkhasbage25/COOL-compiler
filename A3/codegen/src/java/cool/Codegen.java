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
		addDefaultClasses(out);

		// build inheritance graph for cool classes
		buildGraph(program,out);

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

	private void addDefaultClasses(PrintWriter out) {
		// add object class in inheritance graph
		addObjectClass(out);
		// add IO class in inheritance graph
		addIOClass(out);
		// add String class in inheritance graph
		addStringClass(out);
		// add int class in inheritance graph
		addIntClass(out);
		// add bool class in inheritance graph
		addBoolClass(out);
	}

	public void addObjectClass(PrintWriter out) {

		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// Object class has only these methods: abort and type_name
		methods.put("abort", new AST.method("abort", new ArrayList<AST.formal>(),
			CoolUtils.OBJECT_TYPE_STR, new AST.no_expr(0), 0));
		methods.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(),
			CoolUtils.OBJECT_TYPE_STR, new AST.no_expr(0), 0));
		IRClassInfo objectClassInfo = new IRClassInfo(CoolUtils.OBJECT_TYPE_STR,
			null, new HashMap<String, AST.attr>(), methods, CoolUtils.OBJECT_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.OBJECT_TYPE_STR, objectClassInfo);
		defineClassString(CoolUtils.OBJECT_TYPE_STR ,out);

	}

	public void addIOClass(PrintWriter out) {

		// IO class methods
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();

		// formals of out_string
		List<AST.formal> outStringFormals = new ArrayList<AST.formal>();
		outStringFormals.add(new AST.formal("out_string", CoolUtils.STRING_TYPE_STR, 0));
		methods.put("out_string", new AST.method("out_string", outStringFormals, CoolUtils.IO_TYPE_STR, new AST.no_expr(0), 0));

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
		defineClassString(CoolUtils.IO_TYPE_STR ,out);
	}

	public void addIntClass(PrintWriter out) {

		// int class methods
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// inherit methods from object class
		methods.putAll(className2IRClassInfoMap.get(CoolUtils.OBJECT_TYPE_STR).classMethods);
		IRClassInfo intClassInfo = new IRClassInfo(CoolUtils.INT_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), new HashMap<String, AST.method>(), CoolUtils.INT_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.INT_TYPE_STR, intClassInfo);
		defineClassString(CoolUtils.INT_TYPE_STR ,out);

	}

	public void addBoolClass(PrintWriter out) {

		// methods in bool class
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();
		// inherit methods from object class
		methods.putAll(className2IRClassInfoMap.get(CoolUtils.OBJECT_TYPE_STR).classMethods);
		IRClassInfo boolClassInfo = new IRClassInfo(CoolUtils.BOOL_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), methods, CoolUtils.BOOL_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.BOOL_TYPE_STR, boolClassInfo);
		defineClassString(CoolUtils.BOOL_TYPE_STR ,out);
	}

	public void addStringClass(PrintWriter out) {

		// string class methods and formals
		HashMap<String, AST.method> methods = new HashMap<String, AST.method>();

		// concat method of string class
		List<AST.formal> concatFormals = new ArrayList<AST.formal>();
		// formal args
		defineClassString(CoolUtils.STRING_TYPE_STR ,out);
		concatFormals.add(new AST.formal("str", CoolUtils.STRING_TYPE_STR, 0));
		methods.put("concat", new AST.method("concat", concatFormals, CoolUtils.STRING_TYPE_STR, new AST.no_expr(0), 0));

		// substr method of string class
		List<AST.formal> substrFormals = new ArrayList<AST.formal>();
		substrFormals.add(new AST.formal("start", CoolUtils.INT_TYPE_STR, 0));
		substrFormals.add(new AST.formal("end", CoolUtils.INT_TYPE_STR, 0));
		methods.put("substr", new AST.method("substr", substrFormals, CoolUtils.STRING_TYPE_STR, new AST.no_expr(0), 0));

		// length method of string class
		// no formals args
		methods.put("length", new AST.method("length", new ArrayList<AST.formal>(), CoolUtils.INT_TYPE_STR, new AST.no_expr(0), 0));
		IRClassInfo stringClassInfo = new IRClassInfo(CoolUtils.INT_TYPE_STR, CoolUtils.OBJECT_TYPE_STR,
				new HashMap<String, AST.attr>(), methods, CoolUtils.STRING_CLASS_SIZE);
		className2IRClassInfoMap.put(CoolUtils.STRING_TYPE_STR, stringClassInfo);
	}

	private void buildGraph(AST.program program,PrintWriter out) {

		// create inheritance graph
		// first, default classes
		addBasicEdges();

		// user defined classes
		for (class_ programClass : program.classes) {
			defineClassString(programClass.name,out);
			stringCounter.incrementIndex();
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
				+ "\tret i32 0\n"
				+ "}");

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

	private void defineClassString(String className, PrintWriter out) {
		globalStr.add("@.str." + className + " = private unnamed_addr " + "constant ["
				+ (className.length() + 1) + " x i8] c\"" + className + "\\00\", align 1");
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
					}

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
					String ret = handleExpr(className2IRClassInfoMap.get(className), method.body, formalsMap, blocks, out);
					System.out.println("267: ret " + ret );
					String rettype = CoolUtils.getIRTypeFromTypeNReg(ret);

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
			if (CoolUtils.isDefaultClass(className))
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
				out.println("\tcall void @" + CoolUtils.getMangledName(parentName, parentName) + "(%class." + parentName
						+ "* %" + registerCounter.getIndex() + ")");
			}

			for (AST.attr classAttr : className2IRClassInfoMap.get(className).classAttrs.values()) {
				System.out.println("constructor assign call of " + classAttr.name+" "+className);
				if (!(classAttr.value instanceof AST.no_expr)) {
					AST.assign assignExpr = new AST.assign(classAttr.name, classAttr.value, 0);
					assignExpr.type = classAttr.typeid;
					handleExpr(className2IRClassInfoMap.get(className),assignExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.INT_TYPE_STR)) {
					AST.assign intExpr = new AST.assign(classAttr.name, new AST.int_const(0, 0), 0);
					intExpr.type = CoolUtils.INT_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), intExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.BOOL_TYPE_STR)) {
					AST.assign boolExpr = new AST.assign(classAttr.name, new AST.bool_const(false, 0), 0);
					boolExpr.type = CoolUtils.BOOL_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), boolExpr, new HashMap<String, String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.STRING_TYPE_STR)) {
					AST.assign stringExpr = new AST.assign(classAttr.name, new AST.string_const("", 0), 0);
					stringExpr.type = CoolUtils.STRING_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), stringExpr, new HashMap<String, String>(), blocks, out);
				} else {
					System.out.println(className + " construnctor");
					String ctype = CoolUtils.printTypes(className);
					out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds " + ctype + "," + ctype
							+ "* %this, i32 0, i32 " + className2IRClassInfoMap.get(className).attrIndex.get(classAttr.name));
					out.println("\tstore " + CoolUtils.printTypes2(classAttr.typeid) + " null, "
							+ CoolUtils.printTypes2(classAttr.typeid) + "* %" + registerCounter.getIndex() + ", align 4");
				}
			}
			out.println("\tret void\n"
				+ "}");
		}
	}

	private void addClassToMap(List<String> dfsOrdering) {
		for (String currClassName : dfsOrdering) {
			if (CoolUtils.isDefaultClass(currClassName))
				continue;

			AST.class_ currAstClass = className2ClassMap.get(currClassName);
			IRClassInfo parentClass = className2IRClassInfoMap.get(inheritanceGraph.parentNameMap.get(currClassName));
			HashMap<String, AST.attr> classAttrName2Attr = new HashMap<String, AST.attr>();
			HashMap<String, AST.method> classMethodName2Method = new HashMap<String, AST.method>();
			classAttrName2Attr.putAll(parentClass.classAttrs);
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

	// IRClassInfo method expr changedformals blocks iut
	private String handleExpr(IRClassInfo classInfo, AST.expression expr, Map<String, String>formalsMap, List<String> blocks, PrintWriter out) {

		if(expr instanceof AST.bool_const) {

			System.out.println("bool cost");
			AST.bool_const boolExpr = (AST.bool_const) expr;
			return "i8 " + (boolExpr.value ? 1 : 0);

		} else if(expr instanceof AST.string_const) {
			System.out.println("string cost");

			AST.string_const stringExpr = (AST.string_const) expr;
			String ty = "[" + (stringExpr.value.length() + 1) + " x i8]";
			globalStr.add("@.str" + stringCounter.getIndex()
				+ " = private unnamed_addr constant " + ty + " c\""
				+ stringExpr.value + "\\00\", align 1\n");
			stringCounter.incrementIndex();
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast "
				+ ty + "* @.str" + stringCounter.prevIndex() + " to i8*");
			return "i8* %" + registerCounter.getIndex();
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
				+ e2.substring(4));
			ifCounter.incrementIndex();
			out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if.then"
				+ ifCounter.getIndex() + ", label %if.else" + ifCounter.getIndex());
			out.println("if.then" + ifCounter.getIndex() + ":");
			blocks.add("if.then" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [22 x i8]* @Abortdivby0 to i8*");
			out.println("\t%" + registerCounter.incrementIndex() + " = call %class.IO* @"
				+ CoolUtils.getMangledName(CoolUtils.IO_TYPE_STR, "out_string")
				+ "(%class.IO* null, i8* %"
				+ registerCounter.prevIndex() + ")");
			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());
			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = sdiv i32 "
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
			out.println("\tcall void @" + CoolUtils.getMangledName(newExpr.typeid, newExpr.typeid)
				+ "(" + type + "%" + registerCounter.getIndex() + ")");
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast %class." + newExpr.typeid
				+ "* %" + registerCounter.prevIndex() + " to %class.Object*");
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds "
				+ "%class.Object, %class.Object* %"	+ registerCounter.prevIndex()
				+ ", i32 0, i32 0");
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds ["
				+ (newExpr.typeid.length() + 1) + " x i8], [" + (newExpr.typeid.length() + 1) + " x i8]* @.str."
				+ newExpr.typeid + ", i32 0, i32 0");
			out.println("\tstore i8* %"+ registerCounter.getIndex() + ", i8** %"
				+ registerCounter.prevIndex() + ", align 8");
			return type + " %" + registerCounter.prevIndex(3);
		} else if(expr instanceof AST.assign) {
			System.out.println("assign");

			AST.assign assignExpr = (AST.assign) expr;
			String e1 = handleExpr(classInfo, assignExpr.e1, formalsMap, blocks, out);
			System.out.println("e1:" + e1);
			String type = CoolUtils.printTypes2(assignExpr.type);
			String stype = CoolUtils.printTypes(classInfo.name);
			String e1type = CoolUtils.getIRTypeFromTypeNReg(e1);
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
			System.out.println("659: predicate " + predicate);
			out.println("\tbr i1 " + predicate.substring(3) + ", label %loop.body"
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
			ifBody = CoolUtils.getRegisterFromTypeNReg(ifBody);
			out.println("\tbr label %if.end" + ifCount);
			out.println("if.else" + ifCount + ":");
			blocks.add("if.else" + ifCount);
			String elseBody = handleExpr(classInfo, condExpr.elsebody, formalsMap, blocks, out);
			String elseBodyLabel = blocks.get(blocks.size() - 1);
			elseBody = CoolUtils.getRegisterFromTypeNReg(elseBody);
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
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [25 x i8]* @Abortdisvoid to i8*");
			out.println("\t%" + registerCounter.incrementIndex()
				+ " = call %class.IO* @" + CoolUtils.getMangledName(CoolUtils.IO_TYPE_STR, "out_string") + "(%class.IO* null, i8* %"
				+ registerCounter.prevIndex() + ")");
			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());

			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());

			String funcName = "@" + CoolUtils.getMangledName(staticDispatchExpr.typeid, staticDispatchExpr.name);
			IRClassInfo callerIRClassInfo = className2IRClassInfoMap.get(CoolUtils.getCoolTypeFromIRType(CoolUtils.getIRTypeFromTypeNReg(caller)));
			while (!CoolUtils.getIRTypeFromTypeNReg(caller).equals(CoolUtils.printTypes2(staticDispatchExpr.typeid))) {
				System.out.println("718: " + callerIRClassInfo.parent);
				String par = CoolUtils.printTypes(callerIRClassInfo.parent);
				String ty = CoolUtils.getIRTypeFromTypeNReg(caller);
				if(CoolUtils.isPointer(ty)) ty = ty.substring(0, ty.length()-1);
				out.println("\t%" + registerCounter.incrementIndex() +
					" = getelementptr inbounds "+ ty + ", " + ty + "* "
					+ CoolUtils.getRegisterFromTypeNReg(caller) + ", i32 0, i32 0");
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
			return "wrong_expr_type. might be dynamic dispatch";
		}
		System.out.println("wrong_expr_type. might be dynamic dispatch");
		return "wrong_expr_type. might be dynamic dispatch";
	}
}
