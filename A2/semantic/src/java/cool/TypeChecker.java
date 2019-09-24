package cool;

import java.util.*;
import cool.AST.class_;
import cool.AST;
import cool.CoolUtils;
import java.util.ArrayList;
import cool.VariableMapping;

class TypeChecker {

    private void reportError(String filename, int lineNo, String error) {
		Semantic.typeCheckErrorFlag = true;
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
                CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
                new TypeChecker(methodNode.body, classInfo, programClass);
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
                        "Attribute with type '" + T0
                                + "' in classInfo.attrInfo is not equal to declared type " + attrNode.typeid
                                + ". This maybe caused due to multiple definitions or inherited variables");
            } else {
                if (!(attrNode.value instanceof AST.no_expr)) {
                    List<VariableMapping> variableMappings = new ArrayList<VariableMapping>();

                    CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMappings);
                    new TypeChecker(attrNode.value, classInfo, programClass);
                    String T1 = attrNode.value.type;
                    if (!classInfo.Graph.conforms(T1, T0)) {
                        reportError(programClass.filename, programClass.lineNo,
                                "Inferred type " + T1 + " of initialization of attribute " + attrNode.name
                                        + " does not conform to declared type " + attrNode.typeid);
                    }
                    classInfo.attrInfo.exitScope();
                }
            }
        }
    }

    TypeChecker(AST.block node, ClassInfo classInfo, class_ programClass) {
        for (AST.expression exp : node.l1) {
            new TypeChecker(exp, classInfo, programClass);
            node.type = exp.type;
        }
    }

    TypeChecker(AST.assign node, ClassInfo classInfo, class_ programClass) {
        String type = CoolUtils.attrType(node.name, programClass, classInfo);
        if (type == null) {
            reportError(programClass.filename, programClass.lineNo, "Undefined Identifier :" + node.name);
            node.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            new TypeChecker(node.e1, classInfo, programClass);
            String type_prime = node.e1.type;
            // if(classInfo.)
        }
    }

    TypeChecker(AST.expression node, ClassInfo classInfo, class_ programClass) {

        if (node instanceof AST.static_dispatch)
            new TypeChecker((AST.static_dispatch) node, classInfo, programClass);

        else if (node instanceof AST.dispatch)
            new TypeChecker((AST.dispatch) node, classInfo, programClass);

        else if (node instanceof AST.assign)
            new TypeChecker((AST.assign) node, classInfo, programClass);

        else if (node instanceof AST.cond)
            new TypeChecker((AST.cond) node, classInfo, programClass);

        else if (node instanceof AST.block)
            new TypeChecker((AST.block) node, classInfo, programClass);

        else if (node instanceof AST.loop)
            new TypeChecker((AST.loop) node, classInfo, programClass);

        else if (node instanceof AST.let)
            new TypeChecker((AST.let) node, classInfo, programClass);

        else if (node instanceof AST.typcase)
            new TypeChecker((AST.typcase) node, classInfo, programClass);

        else if (node instanceof AST.new_)
            new TypeChecker((AST.new_) node, classInfo, programClass);

        else if (node instanceof AST.isvoid)
            new TypeChecker((AST.isvoid) node, classInfo, programClass);

        else if (node instanceof AST.plus)
            new TypeChecker((AST.plus) node, classInfo, programClass);

        else if (node instanceof AST.sub)
            new TypeChecker((AST.sub) node, classInfo, programClass);

        else if (node instanceof AST.mul)
            new TypeChecker((AST.mul) node, classInfo, programClass);

        else if (node instanceof AST.divide)
            new TypeChecker((AST.divide) node, classInfo, programClass);

        else if (node instanceof AST.comp)
            new TypeChecker((AST.comp) node, classInfo, programClass);

        else if (node instanceof AST.lt)
            new TypeChecker((AST.lt) node, classInfo, programClass);

        else if (node instanceof AST.leq)
            new TypeChecker((AST.leq) node, classInfo, programClass);

        else if (node instanceof AST.eq)
            new TypeChecker((AST.eq) node, classInfo, programClass);

        else if (node instanceof AST.neg)
            new TypeChecker((AST.neg) node, classInfo, programClass);

        else if (node instanceof AST.object)
            new TypeChecker((AST.object) node, classInfo, programClass);

        else if (node instanceof AST.bool_const)
            new TypeChecker((AST.bool_const) node, classInfo, programClass);

        else if (node instanceof AST.int_const)
            new TypeChecker((AST.int_const) node, classInfo, programClass);

        else if (node instanceof AST.string_const)
            new TypeChecker((AST.string_const) node, classInfo, programClass);

    }

    TypeChecker(AST.typcase node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.predicate, classInfo, programClass);
        Set<String> branchDeclerations = new HashSet<String>();
        List<String> typcases = new LinkedList<String>();
        for (AST.branch b : node.branches) {
            ArrayList<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
            variableMapping.add(new VariableMapping(b.name, b.type));
            CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
            if (branchDeclerations.contains(b.type)) {
                reportError(programClass.filename, programClass.lineNo, "Same branch exists"); // check
                node.type = "Object";
            } else {
                branchDeclerations.add(b.type);
            }

            AST.expression exp = b.value;
            new TypeChecker(exp, classInfo, programClass);
            String type_case = exp.type;
            typcases.add(type_case);
            classInfo.attrInfo.exitScope();
        }
        if (typcases.isEmpty()) {
            reportError(programClass.filename, programClass.lineNo, "atleast 1 type requires"); // check
        } else {
            String case_type0 = typcases.get(0);
            for (int i = 1; i < typcases.size(); i++) {
                String type1 = typcases.get(i);
                case_type0 = classInfo.Graph.LowestCommonAncestor(type1, case_type0);
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
            new TypeChecker(node.value, classInfo, programClass);
            String T1 = node.value.type;
            if (!classInfo.Graph.conforms(T1, T0_prime_string)) {
                reportError(programClass.filename, programClass.lineNo, "Infered type does not conform to identifier");
            }
        }
        ArrayList<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
        variableMapping.add(new VariableMapping(node.name, type_decl));
        CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
        new TypeChecker(node.body, classInfo, programClass);
        node.type = node.body.type;
        classInfo.attrInfo.exitScope();
    }

    TypeChecker(AST.cond node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.predicate, classInfo, programClass);
        if (!node.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, programClass.lineNo, "Predicate of conditional must be of Bool type");
            node.type = CoolUtils.OBJECT_TYPE_STR;
        }
        new TypeChecker(node.ifbody, classInfo, programClass);
        new TypeChecker(node.elsebody, classInfo, programClass);
        node.type = classInfo.Graph.LowestCommonAncestor(node.ifbody.type, node.elsebody.type); // check again
    }

    TypeChecker(AST.loop node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.predicate, classInfo, programClass);
        if (!node.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, programClass.lineNo,
                    "Infered Predicate of while loop must be of Bool type");
            node.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            new TypeChecker(node.body, classInfo, programClass);
            node.type = CoolUtils.OBJECT_TYPE_STR;
        }

    }

    TypeChecker(AST.plus node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Addition requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.sub node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Subtraction requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.mul node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Multiplication requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.divide node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "Division requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.comp node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        if (!CoolUtils.BOOL_TYPE_STR.equals(node.e1.type)) {
            reportError(programClass.filename, programClass.lineNo, "Argument of 'not' should be of type Bool.");
        }
        node.type = CoolUtils.BOOL_TYPE_STR;
    }

    TypeChecker(AST.lt node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo, "less-than operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.leq node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, programClass.lineNo,
                    "less-than equal operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.neg node, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(node.e1, classInfo, programClass);
        if (nonIntegerExpression(node.e1, node.e1)) {
            reportError(programClass.filename, programClass.lineNo, "negation operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    TypeChecker(AST.eq node, ClassInfo classInfo, class_ programClass) { // check again
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
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
        new TypeChecker(node.e1, classInfo, programClass);
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

    TypeChecker(AST.static_dispatch staticDispatchNode, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(staticDispatchNode.caller, classInfo, programClass);
        String T0 = staticDispatchNode.caller.type;
        List<String> actualTypes = new ArrayList<String>();
        for (AST.expression nodeExpression : staticDispatchNode.actuals) {
            new TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(nodeExpression.type);
        }

        if (!classInfo.Graph.conforms(T0, staticDispatchNode.typeid)) {
            reportError(programClass.filename, staticDispatchNode.lineNo,
                    "Exrpession type " + T0 + " does not conform to declared " + "static dispatch type" + staticDispatchNode.typeid);
        }

        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(staticDispatchNode.typeid);
        if (method2Args == null) {
            reportError(programClass.filename, staticDispatchNode.lineNo,
                    "Class " + programClass.name + " was not found in " + "classInfo.methodInfo");
            staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            List<String> formalTypes = method2Args.get(staticDispatchNode.name);
            if (actualTypes.size() != formalTypes.size() - 1) {
                reportError(programClass.filename, staticDispatchNode.lineNo,
                        "Method " + staticDispatchNode.name + " of class " + programClass.name
                                + " should be called with " + String.valueOf(formalTypes.size() - 1)
                                + " but was called " + "with " + String.valueOf(actualTypes.size()) + " arguments.");
                staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                for (int i = 0; i < actualTypes.size(); i++) {
                    if (!classInfo.Graph.conforms(actualTypes.get(i), formalTypes.get(i))) {
                        reportError(programClass.filename, staticDispatchNode.lineNo,
                                "Inferred type " + actualTypes.get(i) + " does not " + "conform to formal type "
                                        + formalTypes.get(i) + " for dispatch " + staticDispatchNode.name + " of "
                                        + " Class " + programClass.name);
                        staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
                        return;
                    }
                }
                String T_return = formalTypes.get(formalTypes.size() - 1);
                staticDispatchNode.type = T_return;
            }
        }
    }

    TypeChecker(AST.dispatch dispatchNode, ClassInfo classInfo, class_ programClass) {
        new TypeChecker(dispatchNode.caller, classInfo, programClass);
        String T0 = dispatchNode.caller.type;
        List<String> actualTypes = new ArrayList<String>();
        for (AST.expression nodeExpression : dispatchNode.actuals) {
            new TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(nodeExpression.type);
        }
        String T0_prime = T0;

        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(programClass.name);
        if (method2Args == null) {
            reportError(programClass.filename, dispatchNode.lineNo, "Method " + dispatchNode.name + " of Class "
                    + programClass.name + " was not found in classInfo.methodInfo ");
            dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            System.out.println(431);
            List<String> formalTypes = CoolUtils.getFormalList(dispatchNode.name, classInfo.ClassNameMap.get(T0_prime),
                    classInfo);
            System.out.println(439);System.out.println(dispatchNode.name); System.out.println(classInfo.ClassNameMap.get(T0_prime).name);  

            if (formalTypes == null) {
                reportError(programClass.filename, dispatchNode.lineNo,
                        "Dispatch method " + dispatchNode.name + " was not found.");
                dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                for (int i = 0; i < actualTypes.size(); i++) {
                    if (!classInfo.Graph.conforms(actualTypes.get(i), formalTypes.get(i))) {
                        reportError(programClass.filename, dispatchNode.lineNo,
                                "Method dispatch " + dispatchNode.name + " of class " + programClass.name
                                        + " was called with argument number " + String.valueOf(i) + " with type "
                                        + actualTypes.get(i) + " which does not conform with type + "
                                        + formalTypes.get(i));
                        dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
                        return;
                    }
                }
                String T_return = formalTypes.get(formalTypes.size() - 1);
                dispatchNode.type = T_return;
            }
        }
    }
}
