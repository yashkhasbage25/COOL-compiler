package cool;

import java.util.*;
import cool.AST.class_;
import cool.AST;
import cool.CoolUtils;
import java.util.ArrayList;
import cool.VariableMapping;

class TypeChecker {

    // this sets error flag of semantic class. this is done so that the
    // information of detecting error is transferred from TypeChecker class
    // to Semantic class
    private void reportError(String filename, int lineNo, String error) {
        Semantic.typeCheckErrorFlag = true;
        System.err.println(filename + ":" + lineNo + ": " + error);
    }

    // check if both expressions are integers, because expression are allowed
    // only for integers
    private boolean nonIntegerExpression(AST.expression leftExpression, AST.expression rightExpression) {
        return !CoolUtils.INT_TYPE_STR.equals(leftExpression.type) || !CoolUtils.INT_TYPE_STR.equals(rightExpression.type);
    }

    // type checker for AST.method
    TypeChecker(AST.method methodNode, ClassInfo classInfo, class_ programClass) {

        Map<String, List<String>> classMethodName2Args = classInfo.methodInfo.lookUpGlobal(programClass.name);
        // check if a class name was not found in classInfo. However,
        // this is unlikely
        if (classMethodName2Args == null) {
            reportError(programClass.filename, methodNode.lineNo,
                    "Class " + programClass.name + " was not found in classInfo.methodInfo .");
        } else {
            List<String> formalArgsList = classMethodName2Args.get(methodNode.name);
            // check if a method was not detected earlier
            // However, this is unlikely
            if (formalArgsList == null) {
                reportError(programClass.filename, methodNode.lineNo, "formalArgsList for method " + methodNode.name
                        + " of Class " + programClass.name + " was not found in classMethodName2Args");
            } else {
                // introduce identifiers ans their types
                List<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
                variableMapping.add(new VariableMapping("self", programClass.name));
                for (AST.formal methodFormal : methodNode.formals) {
                    String name = methodFormal.name;
                    String type = methodFormal.typeid;
                    variableMapping.add(new VariableMapping(name, type));
                }
                CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
                // recurse over method body
                new TypeChecker(methodNode.body, classInfo, programClass);
                classInfo.attrInfo.exitScope();
                // check type of method vody
                String T0_prime = methodNode.body.type;

                if (classInfo.ClassNameMap.get(methodNode.typeid) == null) {
                    reportError(programClass.filename, methodNode.lineNo, "Return type not specified for method "
                            + methodNode.name + " for Class " + programClass.name);
                } else if (!classInfo.Graph.conforms(T0_prime, methodNode.typeid)) {
                    reportError(programClass.filename, methodNode.lineNo,
                            "Inferred return type " + T0_prime + " method " + methodNode.name
                                    + " does not conform to declared return type " + methodNode.typeid);
                }
            }
        }
    }

    // type check for attributes
    TypeChecker(AST.attr attrNode, ClassInfo classInfo, class_ programClass) {

        Map<String, String> classAttrName2Type = classInfo.attrInfo.lookUpGlobal(programClass.name);
        // check if a class was not already found
        if (classAttrName2Type == null) {
            reportError(programClass.filename, attrNode.lineNo,
                    "Class " + programClass.name + " was not found in classInfo.attrInfo");
        } else {
            String T0 = classAttrName2Type.get(attrNode.name);
            // get type of the attribute
            if (T0 == null) {
                // if the type of attribute was not found earlier
                // TODO
                reportError(programClass.name, attrNode.lineNo,
                        "Attribute with type '" + T0 + "' in classInfo.attrInfo is not equal to declared type "
                                + attrNode.typeid
                                + ". This maybe caused due to multiple definitions or inherited variables");
            } else if (!classInfo.Graph.checkClass(T0)) {
                // TODO
                reportError(programClass.name, attrNode.lineNo,
                        "Type " + T0 + " for attribute " + attrNode.name + " has not been declared");
            } else {
                // if the attribute is non trivial
                if (!(attrNode.value instanceof AST.no_expr)) {
                    List<VariableMapping> variableMappings = new ArrayList<VariableMapping>();

                    CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMappings);
                    // recurse over value of attribute
                    new TypeChecker(attrNode.value, classInfo, programClass);
                    String T1 = attrNode.value.type;
                    // conform type of attribute
                    if (!classInfo.Graph.conforms(T1, T0)) {
                        reportError(programClass.filename, attrNode.lineNo,
                                "Inferred type " + T1 + " of initialization of attribute " + attrNode.name
                                        + " does not conform to declared type " + attrNode.typeid);
                    }
                    classInfo.attrInfo.exitScope();
                }
            }
        }
    }

    // type checker for block
    TypeChecker(AST.block blockNode, ClassInfo classInfo, class_ programClass) {
        for (AST.expression nodeExpression : blockNode.l1) {
            new TypeChecker(nodeExpression, classInfo, programClass);
            blockNode.type = nodeExpression.type;
        }
    }

    // type checker for assign node
    TypeChecker(AST.assign assignNode, ClassInfo classInfo, class_ programClass) {
        Map<String, String> variableMap = classInfo.attrInfo.lookUpGlobal(programClass.name);
        // check if variable map for this class was not earlier found
        if (variableMap == null) {
            reportError(programClass.filename, assignNode.lineNo,
                    "class " + programClass.name + " not found in" + " classInfo.attrinfo");
        } else {
            String type = CoolUtils.attrType(assignNode.name, programClass, classInfo);
            // check for undefined identifier
            if (type == null) {
                // undefined identifier will have not type
                reportError(programClass.filename, assignNode.lineNo, "Undefined Identifier :" + assignNode.name);
                assignNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {

                // recurse type checker of expression 1 of assignNode
                new TypeChecker(assignNode.e1, classInfo, programClass);
                String type_prime = assignNode.e1.type;
                // check type conformation of assignment expression
                if (classInfo.Graph.conforms(assignNode.e1.type, type)) {
                    assignNode.type = type_prime;
                } else {
                    reportError(programClass.filename, assignNode.lineNo, "LHS : " + type + " RHS :" + type_prime
                            + " Type of expression does not conform to type of attribute for identifier " + assignNode.name);
                    assignNode.type = assignNode.e1.type;
                }
            }
        }
    }

    // type checker for expression
    TypeChecker(AST.expression expressionNode, ClassInfo classInfo, class_ programClass) {

        if (expressionNode instanceof AST.static_dispatch)
            new TypeChecker((AST.static_dispatch) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.dispatch)
            new TypeChecker((AST.dispatch) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.assign)
            new TypeChecker((AST.assign) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.cond)
            new TypeChecker((AST.cond) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.block)
            new TypeChecker((AST.block) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.loop)
            new TypeChecker((AST.loop) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.let)
            new TypeChecker((AST.let) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.typcase)
            new TypeChecker((AST.typcase) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.new_)
            new TypeChecker((AST.new_) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.isvoid)
            new TypeChecker((AST.isvoid) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.plus)
            new TypeChecker((AST.plus) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.sub)
            new TypeChecker((AST.sub) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.mul)
            new TypeChecker((AST.mul) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.divide)
            new TypeChecker((AST.divide) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.comp)
            new TypeChecker((AST.comp) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.lt)
            new TypeChecker((AST.lt) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.leq)
            new TypeChecker((AST.leq) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.eq)
            new TypeChecker((AST.eq) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.neg)
            new TypeChecker((AST.neg) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.object)
            new TypeChecker((AST.object) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.bool_const)
            new TypeChecker((AST.bool_const) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.int_const)
            new TypeChecker((AST.int_const) expressionNode, classInfo, programClass);

        else if (expressionNode instanceof AST.string_const)
            new TypeChecker((AST.string_const) expressionNode, classInfo, programClass);

    }

    TypeChecker(AST.typcase typcaseNode, ClassInfo classInfo, class_ programClass) {

        // recurse over predicate of typecase node
        new TypeChecker(typcaseNode.predicate, classInfo, programClass);

        Set<String> branchDeclerations = new HashSet<String>();
        List<String> typcases = new LinkedList<String>();

        // ananlyze branches
        for (AST.branch typcaseBranch : typcaseNode.branches) {
            ArrayList<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
            // create scopes for every branch
            variableMapping.add(new VariableMapping(typcaseBranch.name, typcaseBranch.type));
            CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
            // check for branch repeatition
            if (branchDeclerations.contains(typcaseBranch.type)) {
                reportError(programClass.filename, typcaseNode.lineNo, "Same branch exists"); // check
                typcaseNode.type = "Object";
            } else {
                branchDeclerations.add(typcaseBranch.type);
            }

            AST.expression branchExpression = typcaseBranch.value;
            //recurse type checker over expression of branch
            new TypeChecker(branchExpression, classInfo, programClass);
            String type_case = branchExpression.type;
            typcases.add(type_case);
            classInfo.attrInfo.exitScope();
        }
        // there should be atleast one branch
        if (typcases.isEmpty()) {
            reportError(programClass.filename, typcaseNode.lineNo, "atleast 1 type required"); // check
        } else {
            String case_type0 = typcases.get(0);
            for (int i = 1; i < typcases.size(); i++) {
                String type1 = typcases.get(i);
                case_type0 = classInfo.Graph.LowestCommonAncestor(type1, case_type0);
                // check again
            }
            typcaseNode.type = case_type0;
        }
    }

    TypeChecker(AST.let letNode, ClassInfo classInfo, class_ programClass) {
        String T0_prime;
        String type_decl = letNode.typeid;
        T0_prime = type_decl; // both are Same
        if (!(letNode.value instanceof AST.no_expr)) {
            // recurse type checker for value of let nonde
            new TypeChecker(letNode.value, classInfo, programClass);
            String T1 = letNode.value.type;
            // check for type conformation of lhs and rhs of let node
            if (!classInfo.Graph.conforms(T1, T0_prime)) {
                reportError(programClass.filename, letNode.lineNo,
                        "Infered type " + T1 + " does not conform to identifier declared type" + T0_prime);
            }
        }
        // create new scopes in let block
        ArrayList<VariableMapping> variableMapping = new ArrayList<VariableMapping>();
        variableMapping.add(new VariableMapping(letNode.name, type_decl));
        CoolUtils.createNewObjectScope(classInfo.attrInfo, programClass, variableMapping);
        // recurse typecheker over body of let node
        new TypeChecker(letNode.body, classInfo, programClass);
        letNode.type = letNode.body.type;
        classInfo.attrInfo.exitScope();
    }

    // type checker for condition letNode
    TypeChecker(AST.cond condNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker for predicate
        new TypeChecker(condNode.predicate, classInfo, programClass);
        if (!condNode.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, condNode.lineNo, "Predicate of conditional must be of Bool type");
            condNode.type = CoolUtils.OBJECT_TYPE_STR;
        }
        // recurse type checker of if body
        new TypeChecker(condNode.ifbody, classInfo, programClass);
        // recurse type checker of else body
        new TypeChecker(condNode.elsebody, classInfo, programClass);
        // deciding type of conditional statement
        condNode.type = classInfo.Graph.LowestCommonAncestor(condNode.ifbody.type, condNode.elsebody.type); // check again
    }

    // type checker for loop Node
    TypeChecker(AST.loop loopNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker over predicate of loop
        new TypeChecker(loopNode.predicate, classInfo, programClass);
        // predicate should be bool
        if (!loopNode.predicate.type.equals(CoolUtils.BOOL_TYPE_STR)) {
            reportError(programClass.filename, loopNode.lineNo, "Infered Predicate of while loop must be of Bool type");
            loopNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            // recurse type checker for body of loop node
            new TypeChecker(loopNode.body, classInfo, programClass);
            loopNode.type = CoolUtils.OBJECT_TYPE_STR;
        }
    }

    // type checker for plus node
    TypeChecker(AST.plus plusNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker over expressions of plus node
        new TypeChecker(plusNode.e1, classInfo, programClass);
        new TypeChecker(plusNode.e2, classInfo, programClass);
        // check if expression type is integer
        if (nonIntegerExpression(plusNode.e1, plusNode.e2)) {
            reportError(programClass.filename, plusNode.lineNo, "Addition requires non-Int arguments");
        }
        plusNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for sub node
    TypeChecker(AST.sub subNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker over e1 and e2
        new TypeChecker(subNode.e1, classInfo, programClass);
        new TypeChecker(subNode.e2, classInfo, programClass);
        // e1 and e2 should be int
        if (nonIntegerExpression(subNode.e1, subNode.e2)) {
            reportError(programClass.filename, subNode.lineNo, "Subtraction requires non-Int arguments");
        }
        subNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for mul node
    TypeChecker(AST.mul mulNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker over e1 and e2
        new TypeChecker(mulNode.e1, classInfo, programClass);
        new TypeChecker(mulNode.e2, classInfo, programClass);
        // e1 and e2 should be int
        if (nonIntegerExpression(mulNode.e1, mulNode.e2)) {
            reportError(programClass.filename, mulNode.lineNo, "Multiplication requires non-Int arguments");
        }
        mulNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for divide node
    TypeChecker(AST.divide divideNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker over e1 and e2
        new TypeChecker(divideNode.e1, classInfo, programClass);
        new TypeChecker(divideNode.e2, classInfo, programClass);
        // type if e1 and e2 should be int
        if (nonIntegerExpression(divideNode.e1, divideNode.e2)) {
            reportError(programClass.filename, divideNode.lineNo, "Division requires non-Int arguments");
        }
        divideNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for comp node
    TypeChecker(AST.comp compNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker for comp node
        new TypeChecker(compNode.e1, classInfo, programClass);
        // type should be bool
        if (!CoolUtils.BOOL_TYPE_STR.equals(compNode.e1.type)) {
            reportError(programClass.filename, compNode.lineNo, "Argument of 'not' should be of type Bool.");
        }
        compNode.type = CoolUtils.BOOL_TYPE_STR;
    }

    // type checker for lt node
    TypeChecker(AST.lt ltNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker for lt node
        new TypeChecker(ltNode.e1, classInfo, programClass);
        new TypeChecker(ltNode.e2, classInfo, programClass);
        // type of e1 and e2 should be int
        if (nonIntegerExpression(ltNode.e1, ltNode.e2)) {
            reportError(programClass.filename, ltNode.lineNo, "less-than operator requires non-Int arguments");
        }
        ltNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for leq
    TypeChecker(AST.leq node, ClassInfo classInfo, class_ programClass) {
        // recurse type checker for e1 and e2 of leq node
        new TypeChecker(node.e1, classInfo, programClass);
        new TypeChecker(node.e2, classInfo, programClass);
        // type of e1 and e2 should be int
        if (nonIntegerExpression(node.e1, node.e2)) {
            reportError(programClass.filename, node.lineNo, "less-than equal operator requires non-Int arguments");
        }
        node.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for neg node
    TypeChecker(AST.neg negNode, ClassInfo classInfo, class_ programClass) {
        // recurse type checker for e1 of node
        new TypeChecker(negNode.e1, classInfo, programClass);
        // type of e1 should be int
        if (nonIntegerExpression(negNode.e1, negNode.e1)) {
            reportError(programClass.filename, negNode.lineNo, "negation operator requires non-Int arguments");
        }
        negNode.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for eq node
    TypeChecker(AST.eq eqNode, ClassInfo classInfo, class_ programClass) { // check again
        // type checker for eq node
        new TypeChecker(eqNode.e1, classInfo, programClass);
        new TypeChecker(eqNode.e2, classInfo, programClass);
        // check for type of e1 and e2
        if (!eqNode.e1.type.equals(eqNode.e2.type)) {
            // comparison is only allowed for int bool and string
            boolean exp1 = CoolUtils.INT_TYPE_STR.equals(eqNode.e1.type) || CoolUtils.BOOL_TYPE_STR.equals(eqNode.e1.type)
                    || CoolUtils.STRING_TYPE_STR.equals(eqNode.e1.type);
            boolean exp2 = CoolUtils.INT_TYPE_STR.equals(eqNode.e2.type) || CoolUtils.BOOL_TYPE_STR.equals(eqNode.e2.type)
                    || CoolUtils.STRING_TYPE_STR.equals(eqNode.e2.type);
            if (exp1 && exp2)
                reportError(programClass.filename, eqNode.lineNo, "Illegal comparison with a basic type.");
            else
                reportError(programClass.filename, eqNode.lineNo,
                        "Illegal comparison with a basic type with non basic types.");
        }
        eqNode.type = CoolUtils.BOOL_TYPE_STR;
    }

    // type checker for isvoid node
    TypeChecker(AST.isvoid isvoidNode, ClassInfo classInfo, class_ programClass) {
        // recurse over e1 of node
        new TypeChecker(isvoidNode.e1, classInfo, programClass);
        isvoidNode.type = CoolUtils.BOOL_TYPE_STR;
    }

    // type checker for no_expr node
    TypeChecker(AST.no_expr no_exprNode, ClassInfo classInfo, class_ programClass) {
        no_exprNode.type = "_no_type";
    }

    // type checker for new_ node
    TypeChecker(AST.new_ new_Node, ClassInfo classInfo, class_ programClass) {
        if (classInfo.Graph.checkClass(programClass.name))
            new_Node.type = new_Node.typeid;
        else {
            reportError(programClass.filename, new_Node.lineNo, "Undefined Type " + new_Node.typeid);
            new_Node.type = CoolUtils.OBJECT_TYPE_STR;
        }
    }

    // type checker for int const
    TypeChecker(AST.int_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.INT_TYPE_STR;
    }

    // type checker for bool const
    TypeChecker(AST.bool_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.BOOL_TYPE_STR;
    }

    // type checker for string const
    TypeChecker(AST.string_const e1, ClassInfo classInfo, class_ programClass) {
        e1.type = CoolUtils.STRING_TYPE_STR;
    }

    // type checker for static dispatch
    TypeChecker(AST.static_dispatch staticDispatchNode, ClassInfo classInfo, class_ programClass) {

        // recurse type checker for caller of node
        new TypeChecker(staticDispatchNode.caller, classInfo, programClass);
        String T0 = staticDispatchNode.caller.type;
        List<String> actualTypes = new ArrayList<String>();

        // recurse over expressions of node
        for (AST.expression nodeExpression : staticDispatchNode.actuals) {
            new TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(nodeExpression.type);
        }

        // check type conformation
        if (!classInfo.Graph.conforms(T0, staticDispatchNode.typeid)) {
            reportError(programClass.filename, staticDispatchNode.lineNo,
                    "Exrpession type " + T0 + " does not conform to declared " + "static dispatch type "
                            + staticDispatchNode.typeid + "for method " + staticDispatchNode.name);
        }
        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(staticDispatchNode.typeid);
        // check if the class was not already seen
        if (method2Args == null) {
            reportError(programClass.filename, staticDispatchNode.lineNo, "Class " + staticDispatchNode.typeid
                    + " was not found in " + "classInfo.methodInfo for method" + staticDispatchNode.name);
            staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            // check calling semantics
            // check number of arguments while calling
            List<String> formalTypes = method2Args.get(staticDispatchNode.name);
            if (actualTypes.size() != formalTypes.size() - 1) {
                reportError(programClass.filename, staticDispatchNode.lineNo,
                        "Method " + staticDispatchNode.name + " of class " + programClass.name
                                + " should be called with " + String.valueOf(formalTypes.size() - 1)
                                + " but was called " + "with " + String.valueOf(actualTypes.size()) + " arguments.");
                staticDispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                // check type conformation of arguments during call
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

    // type checker for dispatch
    TypeChecker(AST.dispatch dispatchNode, ClassInfo classInfo, class_ programClass) {

        // recurse type checker for caller of ndoe
        new TypeChecker(dispatchNode.caller, classInfo, programClass);
        String T0 = dispatchNode.caller.type;

        // get argument ypes during call
        List<String> actualTypes = new ArrayList<String>();
        for (AST.expression nodeExpression : dispatchNode.actuals) {
            new TypeChecker(nodeExpression, classInfo, programClass);
            actualTypes.add(nodeExpression.type);
        }
        String T0_prime = T0;

        Map<String, List<String>> method2Args = classInfo.methodInfo.lookUpGlobal(programClass.name);
        // check if class was not seen earlier
        if (method2Args == null) {
            reportError(programClass.filename, dispatchNode.lineNo, "Method " + dispatchNode.name + " of Class "
                    + programClass.name + " was not found in classInfo.methodInfo ");
            dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {

            List<String> formalTypes = CoolUtils.getFormalList(dispatchNode.name, classInfo.ClassNameMap.get(T0_prime),
                    classInfo);

            // check if class details were stored earlier
            if (formalTypes == null) {
                reportError(programClass.filename, dispatchNode.lineNo,
                        "Dispatch method " + dispatchNode.name + " was not found.");
                dispatchNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                // conform argument types during call
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

    // type checker for
    TypeChecker(AST.object objectNode, ClassInfo classInfo, class_ programClass) {
        Map<String, String> varMap = classInfo.attrInfo.lookUpGlobal(programClass.name);
        // check if the class was not seen earlier
        if (varMap == null) {
            reportError(programClass.filename, objectNode.lineNo,
                    "Class " + programClass.name + "not in classInfo.attrinfo ");
            objectNode.type = CoolUtils.OBJECT_TYPE_STR;
        } else {
            String type = CoolUtils.attrType(objectNode.name, programClass, classInfo);
            // check if the attribute type was not observed earlier
            if (type == null) {
                reportError(programClass.filename, objectNode.lineNo,
                        "Identifier " + objectNode.name + " in class " + programClass.name + "is undefined");
                objectNode.type = CoolUtils.OBJECT_TYPE_STR;
            } else {
                objectNode.type = type;
            }
        }

    }
}
