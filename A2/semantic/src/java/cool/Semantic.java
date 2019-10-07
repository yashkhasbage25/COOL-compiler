package cool;

import java.util.*;
import cool.CoolUtils;
import cool.AST.class_;
import cool.TypeChecker;

public class Semantic {
	private boolean errorFlag = false;

	public void reportError(String filename, int lineNo, String error) {
		errorFlag = true;
		System.err.println(filename + ":" + lineNo + ": " + error);
	}

	public boolean getErrorFlag() {
		return errorFlag;
	}

	/*
	 * Don't change code above this line
	 */

	ScopeTable<AST.attr> scopeTable = new ScopeTable<AST.attr>();
	ClassInfo classInfo = new ClassInfo();
	static boolean typeCheckErrorFlag = false;

	// the actual semantic analyzer
	public Semantic(AST.program program) {

		// initialize objects of classinfo
		classInfo.createNewAttrInfo();
		classInfo.createNewMethodInfo();
		classInfo.fillDefaultClasses();

		// collect all class names. traverse through all classes. collect all
		// attributes, and all other information
		// this also includes creating inheritance graph
		collectAndValidateClasses(program);
		// now check for cycles in inheritance graph
		runCycleReporter(program);
		// check for undefined parent for all classes
		checkForUndefinedParent(program);
		// analyze features of classes. find any error in definition, error in
		// repeated argument-names
		analyzeClassFeatures(program);
		// run the actual type checker
		recurseTypeChecker(program);
		// see if there was some error
		errorFlag = errorFlag || typeCheckErrorFlag;
	}

	// this runs cycle checker and reports cycle
	private void runCycleReporter(AST.program program) {
		Set<String> nodes = new HashSet<String>();
		nodes = classInfo.Graph.cyclePresent();
		boolean flag = (nodes.size() != 0);
		errorFlag = errorFlag || flag;
		if (flag) {
			for (String k : nodes)
				reportError(program.classes.get(0).filename, 0,
						"Class " + k + " ,or an ancestor of " + k + " is involved in an inheritance cycle.");
			System.exit(1); // we will have to exit otherwise it would run into infinite loop while taking
							// parents succesively
		}
	}

	// this calls further steps in collecting and validating classes
	// traverse through all classes. collect all information.
	private void collectAndValidateClasses(AST.program program) {
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			String programClassName = programClass.name;
			// default class cannot be redefined
			if (!isDefaultClassRedifined(programClass)) {
				// check if class inheritance is not poosible
				if (!classInheritanceNotPossible(programClass)) {
					// if the above two checks are passed then class can
					// be passed on further analysis
					// problematic classes are not at all passed for further
					// analysis
					classInfo.ClassNameMap.put(programClass.name, programClass);
					classInfo.Graph.addEdge(parentClassName, programClassName);
				}
			}
		}
	}

	private boolean isDefaultClassRedifined(class_ programClass) {
		Boolean isRedifined = false;
		String programClassName = programClass.name;

		// these cannot be inherited
		if (programClassName.equals(CoolUtils.OBJECT_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Object class.");
			errorFlag = true;
			isRedifined = true;
		} else if (programClassName.equals(CoolUtils.INT_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Int class.");
			errorFlag = true;
			isRedifined = true;
		} else if (programClassName.equals(CoolUtils.STRING_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of String class.");
			errorFlag = true;
			isRedifined = true;
		} else if (programClassName.equals(CoolUtils.BOOL_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Bool class.");
			errorFlag = true;
			isRedifined = true;
		} else if (programClassName.equals(CoolUtils.IO_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of IO class.");
			errorFlag = true;
			isRedifined = true;
		}
		return isRedifined;
	}

	// check if the class inheritance attempted is not allowed
	// string, cool and int cannot be inherited
	private boolean classInheritanceNotPossible(class_ programClass) {
		Boolean notPossible = false;
		String parentClassName = programClass.parent;
		if (parentClassName.equals(CoolUtils.SELF_TYPE_STR)) {
			// self cannot be parent class
			reportError(programClass.filename, programClass.lineNo, "Parent class cannot be SELF_TYPE");
			errorFlag = true;
			notPossible = true;
		} else if (parentClassName.equals(CoolUtils.INT_TYPE_STR) || parentClassName.equals(CoolUtils.STRING_TYPE_STR)
				|| parentClassName.equals(CoolUtils.BOOL_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Class " + parentClassName
					+ " can never be inherited. Attempt to inherit by class " + programClass.name);
			errorFlag = true;
			notPossible = true;
		}
		return notPossible;
	}

	// check if the parent is not defined in a class inheritance
	private boolean checkForUndefinedParent(AST.program program) {
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			if (classInfo.ClassNameMap.get(parentClassName) == null) {
				reportError(programClass.filename, programClass.lineNo,
						"Inheritance unsuccessful: " + programClass.name + " could not inherit " + parentClassName
								+ " because " + parentClassName + " was not defined.");
				errorFlag = true;
			}
		}
		return errorFlag;
	}

	// analyze class featrues
	private boolean analyzeClassFeatures(AST.program program) {
		Set<String> classesFoundSet = new HashSet<String>();
		boolean foundMain = false;
		boolean foundmain = false;
		boolean mainHasArgs = false;

		// check for existence of Main class
		for (class_ programClass : program.classes) {
			if (programClass.name.equals(CoolUtils.MAIN_TYPE_STR)) {
				foundMain = true;
			}

			// check for class redefinition
			if (classesFoundSet.contains(programClass.name)) {
				reportError(programClass.filename, programClass.lineNo,
						"Classes cannot be redefined. Class " + programClass.name + " was attempted to be redefined.");
				errorFlag = true;
			} else {
				classesFoundSet.add(programClass.name);
			}

			Map<String, List<String>> classMethodName2Args = new HashMap<String, List<String>>();
			Map<String, String> classAttrName2Type = new HashMap<String, String>();
			// analyze class features
			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr) {
					AST.attr classAttr = (AST.attr) classFeature;
					// check for attribute redefinition
					if (classAttrName2Type.containsKey(classAttr.name)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Attribute " + classAttr.name.toString() + " is redefined.");
						errorFlag = true;
						// check if the inherited attrbute is redefined
					} else if (isAttrInherited(classAttr, programClass, classInfo)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Attribute " + classAttr.name.toString() + " was inherited but still redefined.");
						errorFlag = true;
					} else {
						// if the above two checks are passed by class attribute
						// then attribute can be passed for further analysis
						classAttrName2Type.put(classAttr.name, classAttr.typeid);
					}
				} else if (classFeature instanceof AST.method) {
					AST.method classMethod = (AST.method) classFeature;
					// check for method redefinition
					if (classMethodName2Args.containsKey(classMethod.name)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Method " + classMethod.name + " is redefined.");
						errorFlag = true;
					} else {
						List<String> argTypeList = new ArrayList<String>();
						Set<String> argFormalNames = new HashSet<String>();
						for (AST.formal arg : classMethod.formals) {
							if (argFormalNames.contains(arg.name)) {
								// check if a variable name was repeated in
								// formal arguments
								reportError(programClass.filename, classFeature.lineNo,
										"Formal argument " + arg.name + " was reused in definition of "
												+ classMethod.name + " in class " + programClass.name);
								errorFlag = true;
							} else {
								// formal parameter cannot have type SELF_TYPE
								argFormalNames.add(arg.name);
								if (arg.typeid.equals(CoolUtils.SELF_TYPE_STR)) {
									reportError(programClass.filename, classFeature.lineNo,
											"Formal parameter in definition of " + classMethod.name + " of class "
													+ programClass.name + " cannot have type 'SELF_TYPE'");
									errorFlag = true;
								} else {
									// if the formalparameters pass the above
									// checks then it can be passed on to
									// method details for further semantic
									// checks
									argTypeList.add(arg.typeid);
								}
							}
						}
						// if the method passes all the above checks
						// then it can be passed on for further
						// semantic checks
						argTypeList.add(classMethod.typeid);

						// check for main class and main method in main class
						classMethodName2Args.put(classMethod.name, argTypeList);
						if (programClass.name.equals(CoolUtils.MAIN_TYPE_STR)) {
							foundMain = true;
							if (classMethod.name.equals(CoolUtils.MAIN_FN_STR)) {
								foundmain = true;
								mainHasArgs = (argTypeList.size() == 1);
							}
						}
					}

				} else {
					// a class featrue can only be either a method or attribute
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden line.");
					errorFlag = true;
				}
			}
			String className = programClass.name;
			// System.out.println("225 " + classMethodName2Args);
			classInfo.methodInfo.insert(className, classMethodName2Args);
			classInfo.attrInfo.insert(className, classAttrName2Type);
		}
		// check for compatibility of method defintion of overridden methods
		// check if the method redefinition is wronly definied
		errorFlag |= (!checkMethodInheritanceCompat(program));
		// analyze main class
		return analyzeMainClass(foundMain, foundmain, mainHasArgs, classInfo);
	}

	// analyze main class. check for main class and main method
	private boolean analyzeMainClass(boolean foundMain, boolean foundmain, boolean mainHasArgs, ClassInfo classInfo) {

		if (!foundMain) {
			reportError("", 0, "Class Main not found.");
			errorFlag = true;
		} else {
			if (!foundmain) {
				reportError("", 0, "main method not found in Class Main");
				errorFlag = true;
			} else {
				if (!mainHasArgs) {
					class_ classMain = classInfo.ClassNameMap.get(CoolUtils.MAIN_TYPE_STR);
					reportError(classMain.filename, classMain.lineNo,
							"In Class Main, 'main' method must not contain any arguments. Leave the arguments blank.");
					errorFlag = true;
				}
			}
		}
		return errorFlag;
	}

	// recurse over type checker
	private boolean recurseTypeChecker(AST.program program) {

		for (class_ programClass : program.classes) {
			// recurse over the components of the program
			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr) {
					new TypeChecker((AST.attr) classFeature, classInfo, programClass);
				} else if (classFeature instanceof AST.method) {
					new TypeChecker((AST.method) classFeature, classInfo, programClass);
				} else {
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden point.");
					errorFlag = true;
				}
			}
		}
		return errorFlag;
	}

	// check if an attribute of class is inherited
	private boolean isAttrInherited(AST.attr classAttr, class_ programClass, ClassInfo classInfo) {
		String programClassParent = classInfo.Graph.parentNameMap.get(programClass.name);
		while (programClassParent != null) {
			class_ nextParentClass = classInfo.ClassNameMap.get(programClassParent);
			if (nextParentClass == null)
				// if the parent is not found then there is no chance of inheritance
				return false;
			for (AST.feature classFeatures : nextParentClass.features) {
				if ((classFeatures instanceof AST.attr) && (((AST.attr) classFeatures).name.equals(classAttr.name))) {
					return true;
				}
			}
			programClassParent = classInfo.Graph.parentNameMap.get(programClassParent);
		}
		return false;
	}

	// check for method overridding method redefinition while inheritance
	private boolean checkMethodInheritanceCompat(AST.program program) {
		boolean valid = true;
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			Map<String, List<String>> class2MethodsMap = classInfo.methodInfo.lookUpGlobal(programClass.name);
			while (parentClassName != null) {
				// do this if parent exists
				Map<String, List<String>> classParent2MethodsMap = classInfo.methodInfo.lookUpGlobal(parentClassName);
				for (AST.feature classFeature : programClass.features) {
					if (classFeature instanceof AST.method) {
						AST.method classMethod = (AST.method) classFeature;
						String methodName = classMethod.name;
						if (classParent2MethodsMap.containsKey(methodName)) {
							// match methdo formal argument list type
							List<String> parentMethodArgsList = classParent2MethodsMap.get(methodName);
							List<String> classMethodArgList = class2MethodsMap.get(methodName);
							if (!parentMethodArgsList.equals(classMethodArgList)) {
								reportError(programClass.filename, classMethod.lineNo, "Method " + methodName
										+ " was inherited but was incompatible with method in parent class");
								valid = false;
							}
						}
					}
				}
				parentClassName = classInfo.Graph.parentNameMap.get(parentClassName);
			}
		}
		return valid;
	}
}
