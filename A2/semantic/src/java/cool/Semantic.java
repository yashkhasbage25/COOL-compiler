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

	public Semantic(AST.program program) {

		classInfo.createNewAttrInfo();
		classInfo.createNewMethodInfo();
		classInfo.fillDefaultClasses();

		collectAndValidateClasses(program);
		// System.out.println("valied classes");
		// if (!errorFlag)
		runCycleReporter(program);
		// System.out.println("cycle check done");
		// if (!errorFlag)
		checkForUndefinedParent(program);
		// System.out.println("undefined parent check done");
		// if (!errorFlag)
		analyzeClassFeatures(program);

		// System.out.println("analyze class feature done");
		// if (!errorFlag)
		recurseTypeChecker(program);
		// System.out.println("recurseive type checker done");
		errorFlag = errorFlag || typeCheckErrorFlag;
		// if (errorFlag)
		// System.exit(1);

	}

	private void runCycleReporter(AST.program program) {
		boolean flag = classInfo.Graph.cyclePresent();
		// System.out.println(flag);
		errorFlag = errorFlag || flag;
		if (flag) {
			reportError(program.classes.get(0).filename, 0, "Cycle detected");
		}
	}

	private void collectAndValidateClasses(AST.program program) {

		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			String programClassName = programClass.name;
			if (!isDefaultClassRedifined(programClass)) {
				if (!classInheritanceNotPossible(programClass)) {
					classInfo.ClassNameMap.put(programClass.name, programClass);
					classInfo.Graph.addEdge(parentClassName, programClassName);
				}
			}
		}
	}

	private boolean isDefaultClassRedifined(class_ programClass) {
		Boolean isRedifined = false;
		String programClassName = programClass.name;
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

	private boolean classInheritanceNotPossible(class_ programClass) {
		Boolean notPossible = false;
		String parentClassName = programClass.parent;
		if (parentClassName.equals(CoolUtils.SELF_TYPE_STR)) {
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

	private boolean analyzeClassFeatures(AST.program program) {
		Set<String> classesFoundSet = new HashSet<String>();
		boolean foundMain = false;
		boolean foundmain = false;
		boolean mainHasArgs = false;

		for (class_ programClass : program.classes) {
			if (programClass.name.equals(CoolUtils.MAIN_TYPE_STR)) {
				foundMain = true;
			}

			if (classesFoundSet.contains(programClass.name)) {
				reportError(programClass.filename, programClass.lineNo,
						"Classes cannot be redefined. Class " + programClass.name + " was attempted to be redefined.");
				errorFlag = true;
			} else {
				classesFoundSet.add(programClass.name);
			}

			Map<String, List<String>> classMethodName2Args = new HashMap<String, List<String>>();
			Map<String, String> classAttrName2Type = new HashMap<String, String>();

			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr) {
					AST.attr classAttr = (AST.attr) classFeature;
					if (classAttrName2Type.containsKey(classAttr.name)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Attribute " + classAttr.name.toString() + " is redefined.");
						errorFlag = true;
					} else if (isAttrInherited(classAttr, programClass, classInfo)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Attribute " + classAttr + " was inherited but still redefined.");
						errorFlag = true;
					} else {
						// System.out.println(classAttr.name);
						classAttrName2Type.put(classAttr.name, classAttr.typeid);
					}
				} else if (classFeature instanceof AST.method) {
					AST.method classMethod = (AST.method) classFeature;

					if (classMethodName2Args.containsKey(classMethod.name)) {
						reportError(programClass.filename, classFeature.lineNo,
								"Method " + classMethod.name + " is redefined.");
						errorFlag = true;
					} else {
						List<String> argTypeList = new ArrayList<String>();
						Set<String> argFormalNames = new HashSet<String>();
						for (AST.formal arg : classMethod.formals) {
							if (argFormalNames.contains(arg.name)) {
								reportError(programClass.filename, classFeature.lineNo,
										"Formal argument " + arg.name + " was reused in definition of "
												+ classMethod.name + " in class " + programClass.name);
								errorFlag = true;
							} else {
								argFormalNames.add(arg.name);
								if (arg.typeid.equals(CoolUtils.SELF_TYPE_STR)) {
									reportError(programClass.filename, classFeature.lineNo,
											"Formal parameter in definition of " + classMethod.name + " of class "
													+ programClass.name + " cannot have type 'SELlineNoF_TYPE'");
									errorFlag = true;
								} else {
									argTypeList.add(arg.typeid);
								}
							}
						}
						argTypeList.add(classMethod.typeid);
						// System.out.println(classMethod.name + " " + argTypeList.toString());
						// if (!checkMethodInheritanceCompat(argTypeList, classMethod.name,
						// programClass, classInfo)) {
						// reportError(programClass.filename, programClass.lineNo, "Method " +
						// classMethod.name
						// + " was inherited but was incompatible with method in parent class");
						// }
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
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden line.");
					errorFlag = true;
				}
			}
			String className = programClass.name;
			classInfo.methodInfo.insert(className, classMethodName2Args);
			classInfo.attrInfo.insert(className, classAttrName2Type);
		}
		errorFlag |= (!checkMethodInheritanceCompat(program));
		return analyzeMainClass(foundMain, foundmain, mainHasArgs, classInfo);
	}

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

	private boolean recurseTypeChecker(AST.program program) {

		for (class_ programClass : program.classes) {
			// System.out.println("class detected:" + programClass.name);
			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr) {
					// System.out.println("attr detected: " + ((AST.attr) classFeature).name);
					new TypeChecker((AST.attr) classFeature, classInfo, programClass);
				} else if (classFeature instanceof AST.method) {
					// System.out.println("method detected: " + ((AST.method) classFeature).name);
					new TypeChecker((AST.method) classFeature, classInfo, programClass);
				} else {
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden point.");
					errorFlag = true;
				}
			}
		}
		return errorFlag;
	}

	private boolean isAttrInherited(AST.attr classAttr, class_ programClass, ClassInfo classInfo) {
		String programClassParent = classInfo.Graph.parentNameMap.get(programClass.name);
		// System.out.println(programClassParent + "???");
		while (programClassParent != null) {
			class_ nextParentClass = classInfo.ClassNameMap.get(programClassParent);
			if (nextParentClass == null)
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

	private boolean checkMethodInheritanceCompat(AST.program program) {
		boolean valid = true;
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			Map<String, List<String>> class2MethodsMap = classInfo.methodInfo.lookUpGlobal(programClass.name);
			while (parentClassName != null) {
				Map<String, List<String>> classParent2MethodsMap = classInfo.methodInfo.lookUpGlobal(parentClassName);
				// for (String methodName : class2MethodsMap.keySet()) {
				// if (classParent2MethodsMap.containsKey(methodName)) {
				// List<String> parentMethodArgsList = classParent2MethodsMap.get(methodName);
				// List<String> classMethodArgList = class2MethodsMap.get(methodName);
				// if (!parentMethodArgsList.equals(classMethodArgList)) {
				// reportError(programClass.filename, programClass.lineNo, "Method " +
				// methodName
				// + " was inherited but was incompatible with method in parent class");
				// valid = false;
				// }
				// }
				// }
				for (AST.feature classFeature : programClass.features) {
					if (classFeature instanceof AST.method) {
						AST.method classMethod = (AST.method) classFeature;
						String methodName = classMethod.name;
						if (classParent2MethodsMap.containsKey(methodName)) {
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
