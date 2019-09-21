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

	public Semantic(AST.program program) {

		classInfo.fillDefaultClasses();
		classInfo.createNewObjectInfo();
		classInfo.createNewMethodInfo();

		collectAndValidateClasses(program);
		runCycleReporter(program);
		checkForUndefinedParent(program);

		analyzeClassFeatures(program);

		recurseTypeChecker(program);
	}

	private void runCycleReporter(AST.program program) {

	};

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

		boolean foundError = false;
		String programClassName = programClass.name;
		if (programClassName.equals(CoolUtils.OBJECT_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Object class.");
			foundError = true;
		} else if (programClassName.equals(CoolUtils.INT_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Int class.");
			foundError = true;
		} else if (programClassName.equals(CoolUtils.STRING_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of String class.");
			foundError = true;
		} else if (programClassName.equals(CoolUtils.BOOL_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of Bool class.");
			foundError = true;
		} else if (programClassName.equals(CoolUtils.IO_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Redefinition of IO class.");
			foundError = true;
		}
		return foundError;
	}

	private boolean classInheritanceNotPossible(class_ programClass) {

		boolean foundError = false;
		String parentClassName = programClass.parent;
		if (parentClassName.equals(CoolUtils.SELF_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Parent class cannot be SELF_TYPE");
			foundError = true;
		} else if (parentClassName.equals(CoolUtils.INT_TYPE_STR) || parentClassName.equals(CoolUtils.STRING_TYPE_STR)
				|| parentClassName.equals(CoolUtils.BOOL_TYPE_STR)) {
			reportError(programClass.filename, programClass.lineNo, "Class " + parentClassName
					+ " can never be inherited. Attempt to inherit by class " + programClass.name);
			foundError = true;
		}
		return foundError;
	}

	private boolean checkForUndefinedParent(AST.program program) {
		boolean foundError = false;
		for (class_ programClass : program.classes) {
			String parentClassName = programClass.parent;
			if (classInfo.ClassNameMap.get(parentClassName) == null) {
				reportError(programClass.filename, programClass.lineNo,
						"Inheritance unsuccessful: " + programClass.name + " could not inherit " + parentClassName
								+ " because " + parentClassName + " was not defined.");
				foundError = true;
			}
		}
		return foundError;
	}

	private boolean analyzeClassFeatures(AST.program program) {
		Set<String> classesFoundSet = new HashSet<String>();
		boolean foundMain = false;
		boolean foundmain = false;
		boolean mainHasArgs = false;
		boolean foundError = false;

		for (class_ programClass : program.classes) {
			if (programClass.equals(CoolUtils.MAIN_TYPE_STR)) {
				foundMain = true;
			}

			if (classesFoundSet.contains(programClass.name)) {
				reportError(programClass.filename, programClass.lineNo,
						"Classes cannot be redefined. Class " + programClass.name + " was attempted to be redefined.");
				foundError = true;
			} else {
				classesFoundSet.add(programClass.name);
			}

			Map<String, List<String>> classMethodName2Args = new HashMap<String, List<String>>();
			Map<String, String> classAttrName2Type = new HashMap<String, String>();

			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr) {
					AST.attr classAttr = (AST.attr) classFeature;
					if (classAttrName2Type.containsKey(classAttr)) {
						reportError(programClass.filename, programClass.lineNo,
								"Attribute " + classAttr.name.toString() + " is redefined.");
						foundError = true;
					} else if (isAttrInherited(classAttr, programClass, classInfo)) {
						reportError(programClass.filename, programClass.lineNo,
								"Attribute " + classAttr + " was inherited but still redefined.");
						foundError = true;
					} else {
						classAttrName2Type.put(classAttr.name, classAttr.typeid);
					}
				} else if (classFeature instanceof AST.method) {
					AST.method classMethod = (AST.method) classFeature;
					if (classAttrName2Type.containsKey(classMethod.name)) {
						reportError(programClass.filename, programClass.lineNo,
								"Method " + classMethod.name + " is redefined.");
						foundError = true;
					} else {
						List<String> argTypeList = new ArrayList<String>();
						Set<String> argFormalNames = new HashSet<String>();
						for (AST.formal arg : classMethod.formals) {
							if (argFormalNames.contains(arg.name)) {
								reportError(programClass.filename, programClass.lineNo,
										"Formal argument " + arg.name + " was reused in definition of "
												+ classMethod.name + " in class " + programClass.name);
								foundError = true;
							} else {
								argFormalNames.add(arg.name);
								if (arg.typeid.equals(CoolUtils.SELF_TYPE_STR)) {
									reportError(programClass.filename, programClass.lineNo,
											"Formal parameter in definition of " + classMethod.name + " of class "
													+ programClass.name + " cannot have type 'SELlineNoF_TYPE'");
									foundError = true;
								} else {
									argTypeList.add(arg.typeid);
								}
								argTypeList.add(classMethod.typeid);
								classMethodName2Args.put(classMethod.name, argTypeList);
								if (programClass.name.equals(CoolUtils.MAIN_TYPE_STR)) {
									foundMain = true;
									if (classMethod.name.equals(CoolUtils.MAIN_FN_STR)) {
										foundmain = true;
										mainHasArgs = (argTypeList.size() == 1);
									}
								}
							}
						}
					}

				} else {
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden line.");
					foundError = true;
				}
			}
			String className = programClass.name;
			classInfo.methodInfo.insert(className, classMethodName2Args);
			classInfo.attrInfo.insert(className, classAttrName2Type);
		}

		return analyzeMainClass(foundMain, foundmain, mainHasArgs, classInfo);
	}

	private boolean analyzeMainClass(boolean foundMain, boolean foundmain, boolean mainHasArgs, ClassInfo classInfo) {
		boolean foundError = false;
		if (!foundMain) {
			reportError("", 0, "Class Main not found.");
			foundError = true;
		} else {
			if (!foundmain) {
				reportError("", 0, "main method not found in Class Main");
				foundError = true;
			} else {
				if (!mainHasArgs) {
					class_ classMain = classInfo.ClassNameMap.get(CoolUtils.MAIN_TYPE_STR);
					reportError(classMain.filename, classMain.lineNo,
							"In Class Main, 'main' method must not contain any arguments. Leave the arguments blank.");
					foundError = true;
				}
			}
		}
		return foundError;
	}

	private boolean recurseTypeChecker(AST.program program) {

		boolean foundError = false;
		for (class_ programClass : program.classes) {
			for (AST.feature classFeature : programClass.features) {
				if (classFeature instanceof AST.attr)
					new TypeChecker((AST.attr) classFeature, classInfo, programClass);
				else if (classFeature instanceof AST.method)
					new TypeChecker((AST.method) classFeature, classInfo, programClass);
				else {
					reportError(programClass.filename, programClass.lineNo, "Reached a forbidden point.");
					foundError = true;
				}
			}
		}
		return foundError;
	}

	private boolean isAttrInherited(AST.attr classAttr, class_ programClass, ClassInfo classInfo) {
		String programClassParent = classInfo.Graph.parentNameMap.get(programClass.name);
		while (programClassParent != null) {
			class_ nextParentClass = classInfo.ClassNameMap.get(programClassParent);
			for (AST.feature classFeatures : nextParentClass.features) {
				if ((classFeatures instanceof AST.attr) && (((AST.attr) classFeatures).name.equals(classAttr))) {
					return true;
				}
			}
			programClassParent = classInfo.Graph.parentNameMap.get(programClassParent);
		}
		return false;
	}
}
