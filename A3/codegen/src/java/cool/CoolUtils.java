package cool;

import cool.AST;
import java.util.*;
import cool.AST.class_;
import java.io.PrintWriter;
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
    // the only default function name
    static final String MAIN_FN_STR = "main";

    public static String printTypes(String s) {
        if (s.equals(INT_TYPE_STR))
            return "i32";
        else if (s.equals(BOOL_TYPE_STR))
            return "i8";
        else if (s.equals(STRING_TYPE_STR))
            return "i8*";
        else
            return "%class." + s;

    }

    public static String printTypes2(String s) {
        if (s.equals(INT_TYPE_STR))
            return "i32";
        else if (s.equals(BOOL_TYPE_STR))
            return "i8";
        else if (s.equals(STRING_TYPE_STR))
            return "i8*";
        else
            return "%class." + s+"*";

    }

    public static void PrintMethodsObject(PrintWriter out) {
        out.println("define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {\n" + "entry:\n"
                + "\tcall void @exit( i32 1 )\n" + "\tret %class.Object* null\n" + "}\n");

        out.println("define i8* @CN6Object_FN9type_name(%class.Object* %this) {\n"
                + "\tentry:"
                + "\t%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0"
                + "\t%1 = load i8*, i8** %0, align 8"
                + "\tret i8* %1"
                + "}");
    }

    public static void PrintMethodsIO(PrintWriter out) {
        out.println("define %class.IO* @_CN2IO_FN10out_string_( %class.IO* %this, [1024 x i8]* %str ) {\n" + "entry:\n"
                + "\t%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %str )\n"
                + "\tret %class.IO* %this\n" + "}\n");

        out.println("define %class.IO* @_CN2IO_FN7out_int_( %class.IO* %this, i32 %int ) {\n" + "entry:\n"
                + "\t%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )\n"
                + "\tret %class.IO* %this\n" + "}\n");

        out.println("define [1024 x i8]* @_CN2IO_FN9in_string_( %class.IO* %this ) {\n" + "entry:\n"
                + "\t%0 = call i8* @malloc( i64 1024 )\n" + "\t%retval = bitcast i8* %0 to [1024 x i8]*\n"
                + "\t%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %retval )\n"
                + "\tret [1024 x i8]* %retval\n" + "}\n");

        out.println("define i32 @_CN2IO_FN6in_int_( %class.IO* %this ) {\n" + "entry:\n"
                + "\t%0 = call i8* @malloc( i64 4 )\n" + "\t%1 = bitcast i8* %0 to i32*\n"
                + "\t%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )\n"
                + "\t%retval = load i32, i32* %1\n" + "\tret i32 %retval\n" + "}\n");
    }

    public static void PrintMethodsString(PrintWriter out) {
        out.println("define i32 @_CN6String_FN6length_( [1024 x i8]* %this ) {\n" + "\tentry:\n"
                + "\t%0 = bitcast [1024 x i8]* %this to i8*\n" + "\t%1 = call i64 @strlen( i8* %0 )\n"
                + "\t%retval = trunc i64 %1 to i32\n" + "\tret i32 %retval\n" + "}\n");

        out.println("define [1024 x i8]* @_CN6String_FN6concat_( [1024 x i8]* %this, [1024 x i8]* %that ) {\n"
                + "entry:\n" + "\t%retval = call [1024 x i8]* @_CN6String_FN4copy_( [1024 x i8]* %this )\n"
                + "\t%0 = bitcast [1024 x i8]* %retval to i8*\n" + "\t%1 = bitcast [1024 x i8]* %that to i8*\n"
                + "\t%2 = call i8* @strcat( i8* %0, i8* %1 )\n" + "\tret [1024 x i8]* %retval\n" + "}\n");

        out.println("define [1024 x i8]* @_CN6String_FN4copy_( [1024 x i8]* %this ) {\n" + "entry:\n"
                + "\t%0 = call i8* @malloc( i64 1024 )\n" + "\t%retval = bitcast i8* %0 to [1024 x i8]*\n"
                + "\t%1 = bitcast [1024 x i8]* %this to i8*\n" + "\t%2 = bitcast [1024 x i8]* %retval to i8*\n"
                + "\t%3 = call i8* @strcpy( i8* %2, i8* %1)\n" + "\tret [1024 x i8]* %retval\n" + "}\n");

        out.println("define [1024 x i8]* @_CN6String_FN6substr_( [1024 x i8]* %this, i32 %start, i32 %len ) {\n"
                + "entry:\n" + "\t%0 = getelementptr inbounds [1024 x i8], [1024 x i8]* %this, i32 0, i32 %start\n"
                + "\t%1 = call i8* @malloc( i64 1024 )\n" + "\t%retval = bitcast i8* %1 to [1024 x i8]*\n"
                + "\t%2 = bitcast [1024 x i8]* %retval to i8*\n"
                + "\t%3 = call i8* @strncpy( i8* %2, i8* %0, i32 %len )\n"
                + "\t%4 = getelementptr inbounds [1024 x i8], [1024 x i8]* %retval, i32 0, i32 %len\n"
                + "\tstore i8 0, i8* %4\n" + "\tret [1024 x i8]* %retval\n" + "}\n");
    }

    public static String getMangledName(String className, String functionName) {
        StringBuilder mangledName = new StringBuilder("");
        mangledName.append("_CN").append(className.length()).append(className).append("_FN")
                .append(functionName.length()).append(functionName).append("_");
        return mangledName.toString();
    }

    public static boolean IsDefaultClass(String s) {
        if (s.equals(INT_TYPE_STR) || s.equals(STRING_TYPE_STR) || s.equals(OBJECT_TYPE_STR) || s.equals(BOOL_TYPE_STR)
                || s.equals(IO_TYPE_STR))
            return true;
        return false;
    }

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
