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
                    "Attribute ")
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
