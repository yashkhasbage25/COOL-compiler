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

	// create IRClassInfo for classes
	private void addClassToMap(List<String> dfsOrdering) {
		// iterate over classes
		for (String currClassName : dfsOrdering) {
			// default classes have been added manually
			if (CoolUtils.isDefaultClass(currClassName))
				continue;

			// get current class and its parent class
			AST.class_ currAstClass = className2ClassMap.get(currClassName);
			IRClassInfo parentClass = className2IRClassInfoMap.get(inheritanceGraph.parentNameMap.get(currClassName));
			HashMap<String, AST.attr> classAttrName2Attr = new HashMap<String, AST.attr>();
			HashMap<String, AST.method> classMethodName2Method = new HashMap<String, AST.method>();

			// get all parent attributes
			classAttrName2Attr.putAll(parentClass.classAttrs);
			int size = 0;

			// features of this class
			for (AST.feature classFeature : currAstClass.features) {
				if (classFeature.getClass() == AST.attr.class) {
					AST.attr classAttr = (AST.attr) classFeature;
					classAttrName2Attr.put(classAttr.name, classAttr);
					// int and bool count for size of 4bytes
					if (classAttr.typeid == CoolUtils.INT_TYPE_STR || classAttr.typeid == CoolUtils.BOOL_TYPE_STR)
						size += CoolUtils.INT_CLASS_SIZE;
					else
						// else it is a pointer
						size += CoolUtils.PTR_SIZE;
				}
			}

			// get methods of this class
			for (AST.feature classFeature : currAstClass.features) {
				if (classFeature.getClass() == AST.method.class) {
					AST.method classMethod = (AST.method) classFeature;
					classMethodName2Method.put(classMethod.name, classMethod);
				}
			}

			// collect this information in irclass object
			IRClassInfo currClass = new IRClassInfo(currAstClass.name, currAstClass.parent, classAttrName2Attr, classMethodName2Method, size);
			className2IRClassInfoMap.put(currClassName, currClass);
		}
	}

	private void printIR(PrintWriter out) {
		// this function initiates creation of ir
		String root = CoolUtils.OBJECT_TYPE_STR;
		List<String> dfsOrdering = new ArrayList<>();
		// gives dfs ordreing to add to classes to map so that parent is already
		// processed
		inheritanceGraph.DfsOrdering(root);
		dfsOrdering = inheritanceGraph.getDfsOrder();
		addClassToMap(dfsOrdering);
		// forst, print default ir
		CoolUtils.printDefaultIR(out);
		// print structs
		printStructDeclarations(dfsOrdering, out);
		// print constructors
		printConstructor(dfsOrdering, out);
		// print methods
		printClassMethods(dfsOrdering, out);
		String mainRetType = className2IRClassInfoMap.get(CoolUtils.MAIN_TYPE_STR).classMethods.get(CoolUtils.MAIN_FN_STR).typeid;
		out.println("define i32 @main() {\n"
				+ "entry:\n"
				+ "\t%main = alloca %class.Main, align 8\n"
				+ "\tcall void @" + CoolUtils.getMangledName(CoolUtils.MAIN_TYPE_STR, CoolUtils.MAIN_TYPE_STR) + "(%class.Main* %main)\n"
				+ "\t%retval = call " + CoolUtils.convertCoolType2IRType(mainRetType) + " @" + CoolUtils.getMangledName(CoolUtils.MAIN_TYPE_STR, CoolUtils.MAIN_FN_STR) +"(%class.Main* %main)\n"
				+ "\tret i32 0\n"
				+ "}\n");

	}

	private void printStructDeclarations(List<String> dfsOrdering, PrintWriter out) {

		// create structure definitions
		for (String className : dfsOrdering) {
			out.print("%class." + className + " = type{ ");
			// object is i8*
			if (className.equals(CoolUtils.OBJECT_TYPE_STR))
				out.print("i8*");
			else {
				// parent object
				out.print("%class." + inheritanceGraph.parentNameMap.get(className));
				// get attribute types
				for (AST.attr classAttrs : className2IRClassInfoMap.get(className).classAttrs.values()) {
					out.print(", " + CoolUtils.convertCoolType2IRType(classAttrs.typeid));
				}
			}
			out.println(" }\n");
		}
	}

	// create a string for name of a class
	// this is used for typename method of each class
	// eg.
	// @.str.Main = private unnamed_addr constant [5 x i8] c"Main"
	private void defineClassString(String className, PrintWriter out) {
		globalStr.add("@.str." + className + " = private unnamed_addr " + "constant ["
				+ (className.length() + 1) + " x i8] c\"" + className + "\\00\", align 1");
	}

	// print constructors of non-default classes, includes main
	private void printConstructor(List<String> dfsOrdering, PrintWriter out) {
		for (String className : dfsOrdering) {
			// default classes have no constructor
			if (CoolUtils.isDefaultClass(className))
				continue;

			// reset register value to %0
			registerCounter.reset();
			int attrCounter = 0;
			// method begins
			out.println("define void @" + CoolUtils.getMangledName(className, className) + "("
				+ CoolUtils.convertCoolType2IRType(className) + " %this) {");
			String parentName = inheritanceGraph.parentNameMap.get(className);
			List<String> blocks = new ArrayList<>();
			blocks.add("entry");
			out.println("entry:");
			registerCounter.incrementIndex();
			if (parentName != null) {
				// this does not happen only for object class, because object class
				// has not constructor
				// get parent
				out.println("\t%" + registerCounter.getIndex() + " = bitcast %class." + className + "* %this to %class."
				+ parentName + "*");
				// call parent constructor
				out.println("\tcall void @" + CoolUtils.getMangledName(parentName, parentName) + "(%class." + parentName
				+ "* %" + registerCounter.getIndex() + ")");
			}

			// create attributes
			for (AST.attr classAttr : className2IRClassInfoMap.get(className).classAttrs.values()) {

				if (!(classAttr.value instanceof AST.no_expr)) {
					// if the attribute is an expression
					AST.assign assignExpr = new AST.assign(classAttr.name, classAttr.value, 0);
					assignExpr.type = classAttr.typeid;
					handleExpr(className2IRClassInfoMap.get(className), assignExpr, new HashSet<String>(), blocks, out);

				} else if (classAttr.typeid.equals(CoolUtils.INT_TYPE_STR)) {
					// if the attribute is unassigned int
					AST.assign intExpr = new AST.assign(classAttr.name, new AST.int_const(0, 0), 0);
					intExpr.type = CoolUtils.INT_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), intExpr, new HashSet<String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.BOOL_TYPE_STR)) {
					// if the attribute is unassigned bool
					AST.assign boolExpr = new AST.assign(classAttr.name, new AST.bool_const(false, 0), 0);
					boolExpr.type = CoolUtils.BOOL_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), boolExpr, new HashSet<String>(), blocks, out);
				} else if (classAttr.typeid.equals(CoolUtils.STRING_TYPE_STR)) {
					// if the attribute is unassigned string
					AST.assign stringExpr = new AST.assign(classAttr.name, new AST.string_const("", 0), 0);
					stringExpr.type = CoolUtils.STRING_TYPE_STR;
					handleExpr(className2IRClassInfoMap.get(className), stringExpr, new HashSet<String>(), blocks, out);
				} else {
					// non-default class unassigned
					String attrType = CoolUtils.getIRClassType(className);
					out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds " + attrType + "," + attrType
					+ "* %this, i32 0, i32 " + className2IRClassInfoMap.get(className).attrIndex.get(classAttr.name));
					// set attribute to be null
					out.println("\tstore " + CoolUtils.convertCoolType2IRType(classAttr.typeid) + " null, "
					+ CoolUtils.convertCoolType2IRType(classAttr.typeid) + "* %" + registerCounter.getIndex() + ", align 4");
				}
			}
			out.println("\tret void\n"
			+ "}\n");
		}
	}

	// print methods of classes
	private void printClassMethods(List<String> dfsOrdering, PrintWriter out) {

		for (String className : dfsOrdering) {
			if (className.equals(CoolUtils.OBJECT_TYPE_STR))
				CoolUtils.printMethodsObject(out);
			else if (className.equals(CoolUtils.IO_TYPE_STR))
				CoolUtils.printMethodsIO(out);
			else if (className.equals(CoolUtils.STRING_TYPE_STR))
				CoolUtils.printMethodsString(out);
			else if (className.equals(CoolUtils.INT_TYPE_STR) || className.equals(CoolUtils.BOOL_TYPE_STR))
				// only bool and int do not have any methods
				continue;
			else {
				// get class info
				IRClassInfo classInfo = className2IRClassInfoMap.get(className);
				for (Map.Entry<String, AST.method> methodEntry : classInfo.classMethods.entrySet()) {
					HashSet<String> formalsSet = new HashSet<String>();

					String methodName = methodEntry.getKey();
					AST.method method = methodEntry.getValue();
					List<String> blocks = new ArrayList<>();

					// write function name
					out.print("define " + CoolUtils.convertCoolType2IRType(method.typeid) + " @"
							+ CoolUtils.getMangledName(className, methodName) + "(%class." + className + "* %this ");

					// put arguments
					for (int i = 0; i < method.formals.size(); i++) {
						out.print(", " + CoolUtils.convertCoolType2IRType(method.formals.get(i).typeid) + " %"
								+ method.formals.get(i).name);
					}

					out.println(") {");
					out.println("entry:");

					for(AST.formal methodFormal: method.formals) {
						String formalName = methodFormal.name;
						String formalType = CoolUtils.convertCoolType2IRType(methodFormal.typeid);
						// allocate memory for arg
						out.println("\t%" + formalName + ".addr = alloca " + formalType + ", align 4");

						// put into set of formals
						formalsSet.add(formalName);
						// store that arg in new memory
						out.println("\tstore " + formalType + " %" + formalName + ", "
							+ formalType + "* %" + formalName + ".addr, align 4");
					}
					// now write ir for method
					registerCounter.reset();
					blocks.add("entry");
					String typeNReg = handleExpr(className2IRClassInfoMap.get(className), method.body, formalsSet, blocks, out);
					// get type of register
					String regType = CoolUtils.getIRTypeFromTypeNReg(typeNReg);

					// if return type does not match with return of body
			        if (!regType.equals(CoolUtils.convertCoolType2IRType(method.typeid))) {
						// this happens when the function has only integer as body and ret type as object
			        	if (regType.equals("i32")) {
							// allocate int and type cast it
							// just to be safe
			        		out.println("\t%"+ registerCounter.incrementIndex()
							// TODO
								+ " = call noalias i8* @malloc(i64 8)");
			        		out.println("\t%"+ registerCounter.incrementIndex()
								+ " = bitcast i8* %"+ registerCounter.prevIndex()
								+ " to " + CoolUtils.convertCoolType2IRType(method.typeid));
			        	} else {
			        		out.println("\t%"+ registerCounter.incrementIndex()
								+ " = bitcast " + typeNReg + " to "
								+ CoolUtils.convertCoolType2IRType(method.typeid));
			        	}
			        	typeNReg = CoolUtils.convertCoolType2IRType(method.typeid) + " %" + registerCounter.getIndex();
			        }
			        out.println("\tret " + typeNReg);
			        out.println("}\n");
				}
			}
		}
	}


	// IRClassInfo method expr changedformals blocks iut
	private String handleExpr(IRClassInfo classInfo, AST.expression expr, HashSet<String>formalsSet, List<String> blocks, PrintWriter out) {

		if(expr instanceof AST.bool_const) {

			// bool const
			AST.bool_const boolExpr = (AST.bool_const) expr;
			return "i8 " + (boolExpr.value ? 1 : 0);

		} else if(expr instanceof AST.string_const) {

			// string const
			AST.string_const stringExpr = (AST.string_const) expr;
			String irType = "[" + (stringExpr.value.length() + 1) + " x i8]";
			// create a global string
			globalStr.add("@.str" + stringCounter.getIndex()
				+ " = private unnamed_addr constant " + irType + " c\""
				+ stringExpr.value + "\\00\", align 1\n");
			stringCounter.incrementIndex();
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast "
				+ irType + "* @.str" + stringCounter.prevIndex() + " to i8*");
			return "i8* %" + registerCounter.getIndex();

		} else if(expr instanceof AST.int_const) {

			// int const
			AST.int_const intExpr = (AST.int_const) expr;

			return "i32 " + intExpr.value;

		} else if(expr instanceof AST.object) {

			AST.object objectExpr = (AST.object) expr;
			// if te object is in formals but not in attributes
			if((!classInfo.classAttrs.containsKey(objectExpr.name)) || (formalsSet.contains(objectExpr.name))) {
				// load object from method arguments
				String irType =  CoolUtils.convertCoolType2IRType(objectExpr.type);
				out.println("\t%" + registerCounter.incrementIndex() + " = load "
					+ irType + ", " + irType + "* %" + objectExpr.name + ".addr, align 4");
				return irType + " %" + registerCounter.getIndex();
			}

			String parseTypeName = CoolUtils.getIRClassType(classInfo.name);
			// get pointer to attribute
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds "
				+ parseTypeName + ", " + parseTypeName + "* %this, i32 0, i32 " + classInfo.attrIndex.get(objectExpr.name));
			// load it to register
			out.println("\t%" + registerCounter.incrementIndex() + " = load "
				+ CoolUtils.convertCoolType2IRType(objectExpr.type) + ", " + CoolUtils.convertCoolType2IRType(objectExpr.type)
				+ "* %" + registerCounter.prevIndex() + ", align 4");
			return CoolUtils.convertCoolType2IRType(objectExpr.type) + " %" + registerCounter.getIndex();
		} else if(expr instanceof AST.comp) {

			// comp expression
			AST.comp compExpr = (AST.comp) expr;
			String e1 = handleExpr(classInfo, compExpr.e1, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 1, "
				+ CoolUtils.getRegFromTypeNReg(e1));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.eq) {

			AST.eq eqExpr = (AST.eq) expr;
			String e1 = handleExpr(classInfo, eqExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, eqExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq i32 "
			 + CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.leq) {

			AST.leq leqExpr = (AST.leq) expr;
			String e1 = handleExpr(classInfo, leqExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, leqExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp sle i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " +CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.lt) {

			AST.lt ltExpr = (AST.lt) expr;
			String e1 = handleExpr(classInfo, ltExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, ltExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp slt i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.neg) {

			AST.neg negExpr = (AST.neg) expr;
			String e1 = handleExpr(classInfo, negExpr.e1, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 0, "
				+ CoolUtils.getRegFromTypeNReg(e1));

		} else if(expr instanceof AST.divide) {

			AST.divide divideExpr = (AST.divide) expr;
			String e1 = handleExpr(classInfo, divideExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, divideExpr.e2, formalsSet, blocks, out);

			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq i32 0, "
				+ CoolUtils.getRegFromTypeNReg(e2));

			ifCounter.incrementIndex();

			out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if.then"
				+ ifCounter.getIndex() + ", label %if.else" + ifCounter.getIndex());
			out.println("if.then" + ifCounter.getIndex() + ":");
			blocks.add("if.then" + ifCounter.getIndex());

			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [22 x i8]* @Abortdivby0 to i8*");
			out.println("\t%" + registerCounter.incrementIndex() + " = call %class.IO* @"
				+ CoolUtils.getMangledName(CoolUtils.IO_TYPE_STR, "out_string")
				+ "(%class.IO* null, i8* %"	+ registerCounter.prevIndex() + ")");

			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());
			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = sdiv i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.mul) {

			AST.mul mulExpr = (AST.mul) expr;
			String e1 = handleExpr(classInfo, mulExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, mulExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = mul nsw i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.sub) {

			AST.sub subExpr = (AST.sub) expr;
			String e1 = handleExpr(classInfo, subExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, subExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = sub nsw i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.plus) {

			AST.plus plusExpr = (AST.plus) expr;
			String e1 = handleExpr(classInfo, plusExpr.e1, formalsSet, blocks, out);
			String e2 = handleExpr(classInfo, plusExpr.e2, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = add nsw i32 "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", " + CoolUtils.getRegFromTypeNReg(e2));
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.isvoid) {

			AST.isvoid isvoidExpr = (AST.isvoid) expr;
			String e1 = handleExpr(classInfo, isvoidExpr.e1, formalsSet, blocks, out);
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq "
				+ CoolUtils.getRegFromTypeNReg(e1) + ", null");
			return "i32 %" + registerCounter.getIndex();

		} else if(expr instanceof AST.new_) {

			AST.new_ newExpr = (AST.new_) expr;

			String type = CoolUtils.convertCoolType2IRType(newExpr.typeid);
			int size = classInfo.size;

			// allocate memory
			out.println("\t%" + registerCounter.incrementIndex() + " = call noalias i8* @malloc(i64 "
				+ size + ")");
			// typecast memory
			out.println("\t%" + registerCounter.incrementIndex()
				+ " = bitcast i8* %" + registerCounter.prevIndex()
				+ " to " + type);
			// call object constructor
			out.println("\tcall void @" + CoolUtils.getMangledName(newExpr.typeid, newExpr.typeid)
				+ "(" + type + "%" + registerCounter.getIndex() + ")");
			// set type name
			// get type name attr
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast %class." + newExpr.typeid
				+ "* %" + registerCounter.prevIndex() + " to %class.Object*");
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds "
				+ "%class.Object, %class.Object* %"	+ registerCounter.prevIndex()
				+ ", i32 0, i32 0");
			// get class name string
			out.println("\t%" + registerCounter.incrementIndex() + " = getelementptr inbounds ["
				+ (newExpr.typeid.length() + 1) + " x i8], [" + (newExpr.typeid.length() + 1) + " x i8]* @.str."
				+ newExpr.typeid + ", i32 0, i32 0");
			// set class string to the pointer
			out.println("\tstore i8* %"+ registerCounter.getIndex() + ", i8** %"
				+ registerCounter.prevIndex() + ", align 8");
			return type + " %" + registerCounter.prevIndex(3);

		} else if(expr instanceof AST.assign) {

			AST.assign assignExpr = (AST.assign) expr;

			String e1 = handleExpr(classInfo, assignExpr.e1, formalsSet, blocks, out);
			String irType = CoolUtils.convertCoolType2IRType(assignExpr.type);
			String currClassIRType = CoolUtils.getIRClassType(classInfo.name);
			String retTypeNReg = CoolUtils.getIRTypeFromTypeNReg(e1);

			// if lhs and rhs types dont match
			if(!retTypeNReg.equals(irType)) {
				if(retTypeNReg.equals("i32")) {
					// just to be safe
					out.println("\t%" + registerCounter.incrementIndex()
						+ " = call noalias i8* @malloc(i64 8)");
					out.println("\t%" + registerCounter.incrementIndex()
						+ " = bitcast i8* %" + registerCounter.prevIndex()
						+ " to " + irType);
				} else {
					// normal typecast
					out.println("\t%" + registerCounter.incrementIndex()
						+ " = bitcast " + e1 + " to " + irType);
				}
				e1 = irType + " %" + registerCounter.getIndex();
			}

			// put the result back to formal's register
			if(formalsSet.contains(assignExpr.name)) {

				out.println("\tstore " + e1 + ", " + irType + "* %" + assignExpr.name
					+ ".addr, align 4");
				return e1;

			} else {

				// if the variable is an attribute
				out.println("\t%"+ registerCounter.incrementIndex()
					+ " = getelementptr inbounds " + currClassIRType + ", " + currClassIRType
					+ "* %this, i32 0, i32 " + classInfo.attrIndex.get(assignExpr.name));
				out.println("\tstore " + e1 + ", " + irType + "* %"
					+ registerCounter.getIndex() + ", align 4");
				return e1;

			}
		} else if(expr instanceof AST.block) {

			AST.block blockExpr = (AST.block) expr;
			String register = "";

			// iterate over all list of statements
			for(AST.expression exprInBlock: blockExpr.l1) {
				register = handleExpr(classInfo, exprInBlock, formalsSet, blocks, out);
			}
			return register;

		} else if(expr instanceof AST.loop) {

			AST.loop loopExpr = (AST.loop) expr;

			// condition block
			out.println("\tbr label %loop.cond" + loopCounter.incrementIndex());
			out.println("loop.cond" + loopCounter.getIndex() + ":");
			blocks.add("loop.cond" + loopCounter.getIndex());

			String predicate = handleExpr(classInfo, loopExpr.predicate, formalsSet, blocks, out);

			out.println("\tbr i1 " + CoolUtils.getRegFromTypeNReg(predicate) + ", label %loop.body"
				+ loopCounter.getIndex() + " , label %loop.end" + loopCounter.getIndex());

			// body block
			out.println("loop.body" + loopCounter.getIndex() + ":");
			blocks.add("loop.body" + loopCounter.getIndex());

			String body = handleExpr(classInfo, loopExpr.body, formalsSet, blocks, out);
			out.println("\tbr label %loop.cond" + loopCounter.getIndex());

			// end block
			out.println("loop.end" + loopCounter.getIndex() + ":");
			blocks.add("loop.end" + loopCounter.getIndex());
			return body;

		} else if(expr instanceof AST.cond) {

			AST.cond condExpr = (AST.cond) expr;
			int ifCount = ifCounter.incrementIndex();

			// predicate block
			String predicate = handleExpr(classInfo, condExpr.predicate, formalsSet, blocks, out);
			out.println("\tbr i1 " + CoolUtils.getRegFromTypeNReg(predicate) + ", label %if.then"
				+ ifCount + ", label %if.else" + ifCount);
			out.println("if.then" + ifCount + ":");

			// then block
			blocks.add("if.then" + ifCount);

			String ifBody = handleExpr(classInfo, condExpr.ifbody, formalsSet, blocks, out);
			String ifBodyLabel = blocks.get(blocks.size() - 1);
			ifBody = CoolUtils.getRegisterFromTypeNReg(ifBody);
			out.println("\tbr label %if.end" + ifCount);
			out.println("if.else" + ifCount + ":");
			blocks.add("if.else" + ifCount);

			// else block
			String elseBody = handleExpr(classInfo, condExpr.elsebody, formalsSet, blocks, out);
			String elseBodyLabel = blocks.get(blocks.size() - 1);
			elseBody = CoolUtils.getRegisterFromTypeNReg(elseBody);
			out.println("\tbr label %if.end" + ifCount);
			out.println("if.end" + ifCount + ":");
			blocks.add("if.end" + ifCount);

			// phi block
			out.println("\t%" + registerCounter.incrementIndex() + " = phi "
				+ CoolUtils.convertCoolType2IRType(condExpr.type) + " [" + ifBody + ", %"
				+ ifBodyLabel + "], [" + elseBody + ", %" + elseBodyLabel + "]");
			return CoolUtils.convertCoolType2IRType(condExpr.type) + " %" + registerCounter.getIndex();

		} else if(expr instanceof AST.static_dispatch) {

			AST.static_dispatch staticDispatchExpr = (AST.static_dispatch) expr;
			// <caller-expr>@<type>@<name>(actuals)
			String callerReg = handleExpr(classInfo, staticDispatchExpr.caller, formalsSet, blocks, out);

			List<String> actuals = new ArrayList<>();
			for(AST.expression actual: staticDispatchExpr.actuals) {
				String attrReg = handleExpr(classInfo, actual, formalsSet, blocks, out);
				actuals.add(attrReg);
			}

			ifCounter.incrementIndex();

			// check if the object is null
			out.println("\t%" + registerCounter.incrementIndex() + " = icmp eq "
				+ callerReg + ", null");
			out.println("\tbr i1 %" + registerCounter.getIndex() + ", label %if.then"
				+ ifCounter.getIndex() + ", label %if.else" + ifCounter.getIndex());

			// then block
			// call abort
			out.println("if.then" + ifCounter.getIndex() + ":");
			blocks.add("if.then" + ifCounter.getIndex());
			out.println("\t%" + registerCounter.incrementIndex() + " = bitcast [25 x i8]* @Abortdisvoid to i8*");
			out.println("\t%" + registerCounter.incrementIndex() + " = call %class.IO* @"
				+ CoolUtils.getMangledName(CoolUtils.IO_TYPE_STR, "out_string") + "(%class.IO* null, i8* %"
				+ registerCounter.prevIndex() + ")");
			out.println("\tcall void @exit(i32 1)");
			out.println("\tbr label %if.else" + ifCounter.getIndex());

			// else block
			// normal execution
			out.println("if.else" + ifCounter.getIndex() + ":");
			blocks.add("if.else" + ifCounter.getIndex());

			// function call name
			String funcName = "@" + CoolUtils.getMangledName(staticDispatchExpr.typeid, staticDispatchExpr.name);

			// get irinfo of caller
			IRClassInfo callerIRClassInfo = className2IRClassInfoMap.get(CoolUtils.getCoolTypeFromIRType(CoolUtils.getIRTypeFromTypeNReg(callerReg)));
			while(!CoolUtils.getIRTypeFromTypeNReg(callerReg).equals(CoolUtils.convertCoolType2IRType(staticDispatchExpr.typeid))) {

				// type cast the object to appropriate parent
				String parentIRType = CoolUtils.getIRClassType(callerIRClassInfo.parent);
				String callerTypeNReg = CoolUtils.getIRTypeFromTypeNReg(callerReg);
				if(CoolUtils.isPointer(callerTypeNReg)) callerTypeNReg = callerTypeNReg.substring(0, callerTypeNReg.length()-1);
				out.println("\t%" + registerCounter.incrementIndex() +
					" = getelementptr inbounds "+ callerTypeNReg + ", " + callerTypeNReg + "* "
					+ CoolUtils.getRegisterFromTypeNReg(callerReg) + ", i32 0, i32 0");
				callerReg = parentIRType + "* %" + registerCounter.getIndex();
				callerIRClassInfo = className2IRClassInfoMap.get(callerIRClassInfo.parent);
			}

			String actualsStr = callerReg;
			for(int i = 0; i < actuals.size(); i++) {
				actualsStr += ", " + actuals.get(i);
			}

			out.println("\t%" + registerCounter.incrementIndex() + " = call "
				+ CoolUtils.convertCoolType2IRType(staticDispatchExpr.type) + " "
				+ funcName + "(" + actualsStr + ")");
			return CoolUtils.convertCoolType2IRType(staticDispatchExpr.type) + " %" + registerCounter.getIndex();

		} else {
			System.out.println("expression type was not expected");
			System.out.println(expr.type);
			return "wrong_expr_type. might be dynamic dispatch";
		}
		System.out.println("wrong_expr_type. might be dynamic dispatch");
		return "wrong_expr_type. might be dynamic dispatch";
	}
}
