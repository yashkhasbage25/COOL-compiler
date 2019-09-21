package cool;

import java.util.*;
import cool.AST.class_;
import cool.AST;
import cool.CoolUtils;

class TypeChecker {

    boolean errorFlag = false;

    private void reportError(String filename, int lineNo, String error) {
        errorFlag = true;
        System.err.println(filename + ":" + lineNo + ": " + error);
    }

    private boolean nonIntegerExpression(AST.expression e1, AST.expression e2) {
        return !CoolUtils.INT_TYPE_STR.equals(e1.type) || !CoolUtils.INT_TYPE_STR.equals(e2.type);
    }

    TypeChecker(AST.method methodNode, ClassInfo classInfo, class_ programClass) {

        Map<String, List<String>> classMethodName2Args = classInfo.methodInfo.lookUpGlobal(programClass.name);
        if (classMethodName2Args == null) {
            reportError(programClass.filename, programClass.lineNo,
                    "Class " + programClass.name + " was not found in classInfo.methodInfo .");
        } else {
            List<String> formalArgsList = classMethodName2Args.get(methodNode.name);
            if (formalArgsList == null) {
                reportError(programClass.filename, programClass.lineNo, "formalArgsList for method " + methodNode.name
                        + " of Class " + programClass.name + " was not found in classMethodName2Args");
            } else {
                List<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
                variableMapping.add(new VariableMapping("self", programClass.name));
                for (AST.formal methodFormal : methodNode.formals) {
                    String name = methodFormal.name;
                    String type = methodFormal.typeid;
                    variableMapping.add(new VariableMapping(name, type));
                }
                createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
                TypeChecker(methodNode.body, classInfo, programClass);
                classInfo.attrInfo.exitScope();

                String T0_prime_string = methodNode.body.type;

                if (classInfo.ClassNameMap.get(methodNode.typeid) == null) {
                    reportError(programClass.filename, programClass.lineNo, "Return type not specified for method "
                            + methodNode.name + " for Class " + programClass.name);
                } else if (!classInfo.Graph.conforms(T0_prime_string, methodNode.typeid)) {
                    reportError(programClass.filename, programClass.lineNo,
                            "Inferred return type " + T0_prime_string + " method " + methodNode.name
                                    + " does not conform to declared return type " + methodNode.typeid);
                } else {
                    reportError(programClass.filename, programClass.lineNo, "Reached a forbidden point.");
                }
            }
        }
    }

    TypeChecker(AST.attr attrNode, ClassInfo classInfo, class_ programClass) {
        Map<String, String> classAttrName2Type = classInfo.attrInfo.lookUpGlobal(programClass.name);
        if (classAttrName2Type == null) {
            reportError(programClass.filename, programClass.lineNo,
                    "Class " + programClass.name + " was not found in classInfo.attrInfo");
        } else {
            String T0 = classAttrName2Type.get(attrNode.name);
            if (T0 == null) {
                reportError(programClass.name, programClass.lineNo,
                        "Attribute with type '" + T0.toString()
                                + "' in classInfo.attrInfo is not equal to declared type " + attrNode.typeid
                                + ". This maybe caused due to multiple definitions or inherited variables");
            } else {
                if (!(attrNode.value instanceof AST.no_expr)) {
                    List<VariableMapping> variableMappings = new ArrayList<VariableMapping>();

                    createNewObjectScope(classInfo.attrInfo, programClass, variableMappings);
                    TypeCheck(attrNode.value, classInfo, programClass);
                    String T1 = attrNode.value.type;
                    if (!classInfo.Graph.conforms(T1.toString(), T0.toString())) {
                        reportError(programClass.filename, programClass.lineNo,
                                "Inferred type " + T1.toString() + " of initialization of attribute " + attrNode.name
                                        + " does not conform to declared type " + attrNode.typeid);
                    }
                    classInfo.attrInfo.exitScope();
                }
            }
        }
    }

    TypeChecker(AST.plus node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Addition requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.sub node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Subtraction requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.mul node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Multiplication requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.divide node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Division requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.comp node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        if (!CoolUtils.BOOL_TYPE_STR.equals(node.e1.type)) {
            reportError(programClass.filename, programClass.lineNo, "Argument of 'not' should be of type Bool.");
        }
        node.type = CoolUtils.BOOL_TYPE_STR;
    }

    TypeChecker(AST.lt node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "less-than operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.leq node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo,
                    "less-than equal operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.neg node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "negation operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.eq node, ClassInfo classInfo, class_ programClass) { // check again
        TypeChecker(node.e1, classInfo, programClass);
        TypeChecker(node.e2, classInfo, programClass);
        if (!node.e1.type.equals(node.e2.type)) {
            boolean exp1 = CoolUtils.INT_TYPE_STR.equals(node.e1.type) || CoolUtils.BOOL_TYPE_STR.equals(node.e1.type)
                    || CoolUtils.STRING_TYPE_STR.equals(node.e1.type);
            boolean exp2 = CoolUtils.INT_TYPE_STR.equals(node.e2.type) || CoolUtils.BOOL_TYPE_STR.equals(node.e2.type)
                    || CoolUtils.STRING_TYPE_STR.equals(node.e2.type);
            if (exp1 || exp2)
                reportError(programClass.filename, programClass.lineNo, "Illegal comparison with a basic type.");
            // if (exp1 || exp2)
            // reportError(programClass.filename, programClass.lineNo, "Illegal comparison
            // with a basic type.");
        }
        node.type = CoolUtils.BOOL_TYPE_STR;
    }

    TypeChecker(AST.isvoid node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.e1, classInfo, programClass);
        node.type = CoolUtils.BOOL_TYPE_STR;
    }

    TypeChecker(AST.no_expr node, ClassInfo classInfo, class_ programClass) {
        node.type = "_no_type";
    }

    TypeChecker(AST.new_ node, ClassInfo classInfo, class_ programClass) {
        if (classInfo.Graph.checkClass(programClass.name))
            node.type = node.typeid;
        else {
            reportError(programClass.filename, programClass.lineNo, "Undefined Type " + node.typeid);
            node.type = CoolUtils.OBJECT_TYPE_STR;
        }
    }

    TypeChecker(AST.int_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.bool_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.BOOL_TYPE_STR;
    }

    TypeChecker(AST.string_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.STRING_TYPE_STR;
    }

    TypeChecker() {

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
