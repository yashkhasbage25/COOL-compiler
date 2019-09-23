package cool;

import java.util.*;
import cool.AST.class_;
import cool.AST;
import cool.CoolUtils;
import java.util.ArrayList;

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

    TypeChecker(AST.block node, ClassInfo classInfo, class_ programClass) {
        for (AST.expression exp : node.l1) {
            TypeChecker(exp, classInfo, programClass);
            node.type = exp.type;
        }
    }

    TypeChecker(AST.assign node, ClassInfo classInfo, class_ programClass) {
        String type = Attrtype(node.name, classInfo, programClass);
        if (type == null) {
            reportError(programClass.filename, programClass.lineNo, "Undefined Identifier :", node.name);
            node.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            TypeChecker(node.e1, classInfo, programClass);
            String type_prime = node.e1.type;
            // if(classInfo.)
        }
    }

    TypeChecker(AST.expression node, ClassInfo classInfo, class_ programClass) {

        if (node instanceof AST.static_dispatch)
            TypeChecker((AST.static_dispatch) node, classInfo, programClass);

        else if (node instanceof AST.dispatch)
            TypeChecker((AST.dispatch) node, classInfo, programClass);

        if (node instanceof AST.assign)
            TypeChecker((AST.assign) node, classInfo, programClass);

        else if (node instanceof AST.cond)
            TypeChecker((AST.cond) node, classInfo, programClass);

        else if (node instanceof AST.block)
            TypeChecker((AST.block) node, classInfo, programClass);

        else if (node instanceof AST.loop)
            TypeChecker((AST.loop) node, classInfo, programClass);

        else if (node instanceof AST.let)
            TypeChecker((AST.let) node, classInfo, programClass);

        else if (node instanceof AST.typcase)
            TypeChecker((AST.typcase) node, classInfo, programClass);

        else if (node instanceof AST.new_)
            TypeChecker((AST.new_) node, classInfo, programClass);

        else if (node instanceof AST.isvoid)
            TypeChecker((AST.isvoid) node, classInfo, programClass);

        else if (node instanceof AST.plus)
            TypeChecker((AST.plus) node, classInfo, programClass);

        else if (node instanceof AST.sub)
            TypeChecker((AST.sub) node, classInfo, programClass);

        else if (node instanceof AST.mul)
            TypeChecker((AST.mul) node, classInfo, programClass);

        else if (node instanceof AST.divide)
            TypeChecker((AST.divide) node, classInfo, programClass);

        else if (node instanceof AST.comp)
            TypeChecker((AST.comp) node, classInfo, programClass);

        else if (node instanceof AST.lt)
            TypeChecker((AST.lt) node, classInfo, programClass);

        else if (node instanceof AST.leq)
            TypeChecker((AST.leq) node, classInfo, programClass);

        else if (node instanceof AST.eq)
            TypeChecker((AST.eq) node, classInfo, programClass);

        else if (node instanceof AST.neg)
            TypeChecker((AST.neg) node, classInfo, programClass);

        else if (node instanceof AST.object)
            TypeChecker((AST.object) node, classInfo, programClass);

        else if (node instanceof AST.bool_const)
            TypeChecker((AST.bool_const) node, classInfo, programClass);

        else if (node instanceof AST.int_const)
            TypeChecker((AST.int_const) node, classInfo, programClass);

        else if (node instanceof AST.string_const)
            TypeChecker((AST.string_const) node, classInfo, programClass);

    }

    TypeChecker(AST.typecase node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.predicate, classInfo, programClass);
        Set<String> branchDeclerations = new HashSet<String>();
        List<String> typecases = new LinkedList<String>();
        for (AST.branch b : node.branchs) {
            ArrayList<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();
            newBindings.add(new Pair<String, String>(b.name, b.type));
            updateObjectEnv(classInfo.attrInfo, classInfo, newBindings);
            if (branchDeclerations.contains(b.type)) {
                reportError(programClass.filename, programClass.lineNo, "Same branch exists"); // check
                node.type = "Object";
            } else {
                branchDeclerations.add(b.type);
            }

            AST.expression exp = b.value;
            TypeChecker(exp, classInfo, programClass);
            String type_case = exp.type;
            typecases.add(type_case);
            classInfo.attrInfo.exitScope();
        }
        if (typecases.isEmpty()) {
            reportError(programClass.filename, programClass.lineNo, "atleast 1 type requires"); // check
        } else {
            String case_type0 = typecases.get(0);
            for (int i = 1; i < typecases.size(); i++) {
                String type1 = typecases.get(i);
                case_type0 = classInfo.Graph.LowestCommonAncestor(type1, type0);
                // check again
            }
            node.type = case_type0;
        }
    }

    TypeChecker(AST.let node, ClassInfo classInfo, class_ programClass) {
        String T0_prime_string;
        String type_decl = node.typeid;
        T0_prime_string = type_decl; // both are Same
        if (!(node.value instanceof AST.no_expr)) {
            TypeChecker(node.value, classInfo, programClass);
            String T1 = node.value.type;
            if (!classInfo.Graph.conforms(T1, T0_prime_string)) {
                reportError(programClass.filename, programClass.lineNo, "Infered type does not conform to identifier");
            }
        }
        ArrayList<Pair<String, String>> newBindings = new ArrayList<Pair<String, String>>();
        newBindings.add(new Pair<String, String>(node.name, type_decl));
        updateObjectEnv(c.objectEnv, curr, newBindings);
        TypeChecker(node.body, classInfo, programClass);
        node.type = node.body.type;
        classInfo.attrInfo.exitScope();
    }

    TypeChecker(AST.cond node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.predicate, classInfo, programClass);
        if (!node.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, programClass.lineNo, "Predicate of conditional must be of Bool type");
            node.type = CoolUtils.OBJECT_TYPE_STR;
        }
        TypeChecker(node.ifbody, classInfo, programClass);
        TypeChecker(node.elsebody, classInfo, programClass);
        node.type = classInfo.Graph.LowestCommonAncestor(node.ifbody.type, node.elsebody.type); // check again
    }

    TypeChecker(AST.loop node, ClassInfo classInfo, class_ programClass) {
        TypeChecker(node.predicate, classInfo, programClass);
        if (!node.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, programClass.lineNo,
                    "Infered Predicate of while loop must be of Bool type");
            node.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            TypeChecker(node.body, classInfo, programClass);
            node.type = CoolUtils.OBJECT_TYPE_STR;
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
