package cool;

import java.util.List;
import java.util.Map;

class TypeChecker {

    private void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}

    TypeChecker(AST.method methodNode, ClassInfo classInfo, class_ programClass) {

        Map<String, List<String>> classMethodName2Args = (Map<String, List<String>>) classInfo.methodInfo.lookUpGlobal(programClass);
        if (classMethodName2Args == null) {
            reportError(programClass.filename,
                program.lineNo,
                "Class " + programClass.name + " was not found in classInfo.methodInfo ."
            );
        } else {
            List<String> formalArgsList = classMethodName2Args.get(methodNode.name);
            if (formalArgsList == null) {
                reportError(programClass.filename,
                    programClass.lineNo,
                    "formalArgsList for method " + methodNode.name + " of Class " +
                    programClass.name + " was not found in classMethodName2Args"
                    );
            } else {
                List<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
                variableMapping.add(new VariableMapping("self", programClass.name));
                for (AST.formal methodFormal: method.formals) {
                    String name = methodFormal.name;
                    String type = methodFormal.typeid;
                    variableMapping.add(new VariableMapping(name, type));
                }
                createNewObjectScope(classInfo.objectInfo, programClass, variableMapping);
                TypeChecker(methodNode.body, classInfo, programClass);
                classInfo.objectInfo.exitScope();

                String T0_prime_string = methodNode.body.type;

                if (classInfo.classNameMapper.get(methodNode.typeid) == null) {
                    reportError(programClass.filename,
                        programClass.lineNo,
                        "Return type not specified for method " + methodNode.name +
                        " for Class " + programClass.name
                    );
                } else if (!classInfo.inheritanceGraph.conforms(T0_prime_string, methodNode.typeid, CoolUtils.OBJECT_TYPE_STR)) {
                    reportError(programClass.filename,
                        programClass.lineNo,
                        "Inferred return type " + T0_prime_string + " method " +
                        methodNode.name + " does not conform to declared return type " +
                        methodNode.typeid
                    );
                } else {
                    reportError(programClass.filename,
                        programClass.lineNo,
                        "Reached a forbidden point."
                    )
                }
            }
        }
    }

    TypeChecker(AST.attr attrNode, ClassInfo classInfo, class_ programClass) {
        Map<String, String> classAttrName2Type = (Map<String, String>) classInfo.objectInfo.lookUpGlobal(programClass.name);
        if (classAttr2NameType == null) {
            reportError(programClass.filename,
                programClass.lineNo,
                "Class " + programClass.name + " was not found in classInfo.objectInfo"
            );
        } else {
            String T0 = classAttrName2Type.get(attrNode.name);
            if (T0 == null) {
                reportError(programClass.name,
                    programClass.lineNo,
                    "Attribute with type '" + T0.toString() +
                    "' in classInfo.objectInfo is not equal to declared type " +
                    attrNode.typeid + ". This maybe caused due to multiple definitions or inherited variables"
                );
            } else {
                if (!(attrNode instanceof AST.no_expr)) {
                    List<VariableMapping> variableMappings = new ArrayList<VariableMapping>();

                    createNewObjectScope(classInfo.objectInfo, programClass, variableMappings);
                    TypeCheck(attrNode.value, classInfo, programClass);
                    String T1 = attrNode.value.type;
                    if (!classInfo.inheritanceGraph.conforms(T1.toString(), T0.toString(), CoolUtils.OBJECT_TYPE_STR)) {
                        reportError(programClass.filename,
                            programClass.lineNo,
                            "Inferred type " + T1.toString() +
                            " of initialization of attribute " + attrName.name +
                            " does not conform to declared type " + attrName.typeid
                        );
                    }
                    classInfo.exitScope();
                }
            }
        }
    }

    TypeChecker(AST.static_dispatch staticDispatchNode, ClassInfo classInfo, class_ programClass) {
        TypeChecker(staticDispatchNode.caller, classInfo, programClass);
        String T0 = staticDispatchNode.caller.type;
        List<String> actualTypes = new ArrayList<String>();
        for (AST.experssion nodeExpression: staticDispatchNode.actuals) {
            TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(expressionNode.type);
        }

        if (!classInfo.graph.conforms(T0, node.typeid, CoolUtils.OBJECT_TYPE_STR)) {
            reportError(programClass.filename,
                staticDispatchNode.lineNo,
                "Exrpession type " + T0 + " does not conform to declared " +
                "static dispatch type" + node.typeid
            );
        }

        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(staticDispatchNode.typeid);
        if (method2Args == null) {
            reportError(classInfo.filename,
                staticDispatchNode.lineNo,
                "Class " + programClass.name + " was not found in " +
                "classInfo.methodInfo"
            );
            staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            if (actualTypes.size() != formalTypes.size() - 1) {
                reportError(programClass.filename,
                    staticDispatchNode.lineNo,
                    "Method " + staticDispatchNode.name + " of class " +
                    programClass.name + " should be called with " +
                    String.valueOf(formalTypes.size() - 1) + " but was called " +
                    "with " + String.valueOf(actualTypes.size()) + " arguments."
                );
                staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                for (int i = 0; i < actualTypes.size(); i++) {
                    if (!classInfo.graph.conforms(actualTypes.get(i), formalTypes.get(i), CoolUtils.OBJECT_TYPE_STR)) {
                        reportError(classInfo.filename,
                            staticDispatchNode.lineNo,
                            "Inferred type " + actualTypes.get(i) + " does not " +
                            "conform to formal type " + formalTypes.get(i) +
                            " for dispatch " + staticDispatchNode.name + " of " +
                            " Class " + programClass.name
                        );
                        staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
                        return;
                    }
                }
                String T_return = formalTypes.get(formalType.size() - 1);
                staticDispatchNode.type = T_return;
            }
        }
    }

    TypeChecker(AST.dispatch dispatchNode, ClassInfo classInfo, class_ programClass) {
        TypeChecker(dispatchNode.caller, classInfo, programClass);
        String T0 = dispatchNode.caller.type;
        List<String> actualTypes = new ArrayList<String>();
        for (AST.expression nodeExpression: dispatchNode.actuals) {
            TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(nodeExpression.type);
        }
        String T0_prime = T0;

        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(programClass.name);
        if (method2Args == null) {
            reportError(programClass.filename,
                dispatchNode.lineNo,
                "Method " +  dispatchNode.name + " of Class " + programClass.name +
                " was not found in classInfo.methodInfo "
            );
            dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            List<String> formalTypes = getFormalList(dispatchNode.name, classInfo.classnameMap.get(T0_prime), classInfo);
            if (formalTypes == null) {
                reportError(programClass.filename,
                    dispatchNode.lineNo,
                    "Dispatch method was " + dispatchNode.name + " not found."
                );
                dispatchNode.type = CollUtils.OBJECT_TYPE_STR;
            } else {
                for (int i = 0; i < actualTypes.size(); i++) {
                    if (!classInfo.graph.conforms(actualTypes.get(i), formalTypes.get(i), CoolUtils.OBJECT_TYPE_STR)) {
                        reportError(programClass.filename,
                            dispatchNode.lineNo,
                            "Method dispatch " + dispatchNode.name + " of class " +
                            programClass.name + " was called with argument number "
                            + String.valueOf(i) + " with type " + actualTypes.get(i) +
                            " which does not conform with type + " + formalTypes.get(i)
                        );
                        dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
                        return;
                    }
                }
                String T_return = formalTypes.get(formalTypes.size() - 1);
                dispatchNode.type = T_return;
            }
        }
    }

    private static class VariableMapping {
        String leftString;
        String rightString;
        VariableMapping(String left, String right) {
            leftString = left;
            rightString = right;
        }
    }
}
