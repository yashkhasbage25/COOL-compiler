package cool;

import cool.AST;
import java.util.*;
import cool.AST.class_;
import cool.VariableMapping;

class CoolUtils {

    // some string constants to be used throughout the cool package
    // these are of the form: default class/type name -> class name
    static final String INT_TYPE_STR = "Int";
    static final String STRING_TYPE_STR = "String";
    static final String OBJECT_TYPE_STR = "Object";
    static final String MAIN_TYPE_STR = "Main";
    static final String IO_TYPE_STR = "IO";
    static final String BOOL_TYPE_STR = "Bool";
    static final String SELF_TYPE_STR = "SELF_TYPE";
    // the only default function name
    static final String MAIN_FN_STR = "main";


    // creates new scope of objects in a new scope
    public static void createNewObjectScope(ScopeTable<Map<String, String>> attrInfo, class_ programClass,
        List<VariableMapping> variableMapping) {
        Map<String, String> newAttrTypeMap = new HashMap<String, String>();
        // put all class attributes in that scope
        newAttrTypeMap.putAll(attrInfo.lookUpGlobal(programClass.name));
        for (int i = 0; i < variableMapping.size(); i++) {
            newAttrTypeMap.put(variableMapping.get(i).getLeftString(), variableMapping.get(i).getRightString());
        }
        attrInfo.enterScope();
        attrInfo.insert(programClass.name, newAttrTypeMap);
    }

    // get formals of a method from the class itself or the parent classes
    public static List<String> getFormalList(String methodName, class_ programClass, ClassInfo classInfo) {
        // check if the method is in the programClass itself
        if (classInfo.methodInfo.lookUpGlobal(programClass.name) != null
                && classInfo.methodInfo.lookUpGlobal(programClass.name).get(methodName) != null) {
            return classInfo.methodInfo.lookUpGlobal(programClass.name).get(methodName);
        }

        // check in parents
        String parentName = classInfo.Graph.parentNameMap.get(programClass.name);
        while (parentName != null) {
            class_ parent = classInfo.ClassNameMap.get(parentName);
            for (AST.feature classFeature : parent.features) {
                if (classFeature instanceof AST.method) {
                    if (((AST.method) classFeature).name.equals(methodName))
                        return classInfo.methodInfo.lookUpGlobal(parent.name).get(methodName);
                }
            }
            parentName = classInfo.Graph.parentNameMap.get(parentName);
        }
        return null;
    }

    // get type of some attribute in programClass or in parent class
    public static String attrType(String attrName, class_ programClass, ClassInfo classInfo) {
        if (classInfo.attrInfo.lookUpGlobal(programClass.name) != null
                && (classInfo.attrInfo.lookUpGlobal(programClass.name)).get(attrName) != null)
            return (classInfo.attrInfo.lookUpGlobal(programClass.name)).get(attrName);
        String parentName = classInfo.Graph.parentNameMap.get(programClass.name);
        while (parentName != null) {
            class_ parent = classInfo.ClassNameMap.get(parentName);
            for (AST.feature classFeature : parent.features) {
                if (classFeature instanceof AST.attr) {
                    if (((AST.attr) classFeature).name.equals(attrName))
                        return classInfo.attrInfo.lookUpGlobal(parent.name).get(attrName);
                }
            }
            parentName = classInfo.Graph.parentNameMap.get(parentName);
        }
        return null;
    }
}
