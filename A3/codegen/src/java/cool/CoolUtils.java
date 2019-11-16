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

    static final int INT_CLASS_SIZE = 4;
    static final int BOOL_CLASS_SIZE = 1;
    static final int PTR_SIZE = 8;
    static final int OBJECT_CLASS_SIZE = PTR_SIZE;
    static final int IO_CLASS_SIZE = PTR_SIZE;
    static final int STRING_CLASS_SIZE = PTR_SIZE;


    // input: class name
    // output: %class.<class-name>
    public static String getIRClassType(String className) {
        return "%class." + className;
    }

    // converts cool types to ir types
    // works for all kinds of cool objects
    // including ints, bool, string, classes
    public static String convertCoolType2IRType(String entity) {
        if (entity.equals(INT_TYPE_STR))
            return "i32";
        else if (entity.equals(BOOL_TYPE_STR))
            return "i8";
        else if (entity.equals(STRING_TYPE_STR))
            return "i8*";
        else
            return "%class." + entity + "*";
    }

    // prints default ir
    public static void printDefaultIR(PrintWriter out) {
        // target layout
        out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"");
        // target triple
        out.println("target triple = \"x86_64-unknown-linux-gnu\"");
        // object class constructor
        out.println("define void @_CN6Object_FN6Object_(%class.Object* %this) {\n"
                    + "entry:\n"
                    + "\tret void\n"
                    + "}");
        // io class constructor
        out.println("define void @_CN2IO_FN2IO_(%class.IO* %this) {\n"
                    + "entry:\n"
                    + "\t%0 = bitcast %class.IO* %this to %class.Object*\n"
                    + "\tcall void @_CN6Object_FN6Object_(%class.Object* %0)\n"
                    + "\tret void\n"
                    + "}");

        // abort functions
        out.println("@Abortdivby0 = private unnamed_addr constant [22 x i8] c\"Error: Division by 0\\0A\\00\", align 1\n");
        out.println("@Abortdisvoid = private unnamed_addr constant [25 x i8] c\"Error: Dispatch to void\\0A\\00\", align 1\n");

        // c stdio stdlib functions
        out.println("; C malloc declaration\n"
            + "declare noalias i8* @malloc(i64)\n"
            + "; C exit declaration\n"
            + "declare void @exit(i32)\n"
            + "; C printf declaration\n"
            + "declare i32 @printf(i8*, ...)\n"
            + "; C scanf declaration\n"
            + "declare i32 @scanf(i8*, ...)\n"
            + "; C strlen declaration\n"
            + "declare i64 @strlen(i8*)\n"
            + "; C strcpy declaration\n"
            + "declare i8* @strcpy(i8*, i8*)\n"
            + "; C strcat declaration\n"
            + "declare i8* @strcat(i8*, i8*)\n"
            + "; C strncpy declaration\n"
            + "declare i8* @strncpy(i8*, i8*, i32)\n");

        // string "%s"
        out.println("@strformatstr = private unnamed_addr constant [3 x i8] c\"%s\\00\", align 1\n");
        // string "%d"
        out.println("@intformatstr = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n");
    }

    // object methods
    public static void printMethodsObject(PrintWriter out) {
        out.println("define %class.Object* @" + getMangledName(OBJECT_TYPE_STR, "abort") + "(%class.Object* %this) noreturn {\n"
                + "entry:\n"
                + "\tcall void @exit(i32 1)\n"
                + "\tret %class.Object* null\n"
                + "}\n");

        out.println("define i8* @" + getMangledName(OBJECT_TYPE_STR, "type_name") + "(%class.Object* %this) {\n"
                + "\tentry:\n"
                + "\t%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0\n"
                + "\t%1 = load i8*, i8** %0, align 8\n"
                + "\tret i8* %1\n"
                + "}");
    }

    public static void printMethodsIO(PrintWriter out) {
        out.println("define %class.IO* @" + getMangledName(IO_TYPE_STR, "out_string") + "(%class.IO* %this, i8* %str) {\n"
                + "entry:\n"
                + "\t%0 = call i32 (i8*, ...) @printf(i8* bitcast ([3 x i8]* @strformatstr to i8*), i8* %str)\n"
                + "\tret %class.IO* %this\n"
                + "}\n");

        out.println("define %class.IO* @" + getMangledName(IO_TYPE_STR, "out_int") + "(%class.IO* %this, i32 %int) {\n"
                + "entry:\n"
                + "\t%0 = call i32 (i8*, ...) @printf(i8* bitcast ([3 x i8]* @intformatstr to i8*), i32 %int)\n"
                + "\tret %class.IO* %this\n"
                + "}\n");

        out.println("define i8* @" + getMangledName(IO_TYPE_STR, "in_string") + "(%class.IO* %this) {\n"
                + "entry:\n"
                + "\t%0 = call i8* @malloc(i64 1024)\n"
                + "\t%retval = bitcast i8* %0 to i8*\n"
                + "\t%1 = call i32 (i8*, ...) @scanf(i8* bitcast ([3 x i8]* @strformatstr to i8*), i8* %retval)\n"
                + "\tret i8* %retval\n"
                + "}\n");

        out.println("define i32 @" + getMangledName(IO_TYPE_STR, "in_int") + "(%class.IO* %this) {\n"
                + "entry:\n"
                + "\t%0 = call i8* @malloc(i64 4)\n"
                + "\t%1 = bitcast i8* %0 to i32*\n"
                + "\t%2 = call i32 (i8*, ...) @scanf(i8* bitcast ([3 x i8]* @intformatstr to i8*), i32* %1)\n"
                + "\t%retval = load i32, i32* %1\n"
                + "\tret i32 %retval\n"
                + "}\n");
    }

    public static void printMethodsString(PrintWriter out) {
        out.println("define i32 @" + getMangledName(STRING_TYPE_STR, "length")+ "(i8* %this) {\n"
                + "\tentry:\n"
                + "\t%0 = bitcast i8* %this to i8*\n"
                + "\t%1 = call i64 @strlen(i8* %0)\n"
                + "\t%retval = trunc i64 %1 to i32\n"
                + "\tret i32 %retval\n"
                + "}\n");

        out.println("define i8* @" + getMangledName(STRING_TYPE_STR, "concat") + "(i8* %this, i8* %that) {\n"
                + "entry:\n"
                + "\t%retval = call i8* @" + getMangledName(STRING_TYPE_STR, "copy") + "(i8* %this)\n"
                + "\t%0 = bitcast i8* %retval to i8*\n"
                + "\t%1 = bitcast i8* %that to i8*\n"
                + "\t%2 = call i8* @strcat(i8* %0, i8* %1)\n"
                + "\tret i8* %retval\n"
                + "}\n");

        out.println("define i8* @" + getMangledName(STRING_TYPE_STR, "copy") + "(i8* %this) {\n"
                + "entry:\n"
                + "\t%0 = call i8* @malloc(i64 1024)\n"
                + "\t%retval = bitcast i8* %0 to i8*\n"
                + "\t%1 = bitcast i8* %this to i8*\n"
                + "\t%2 = bitcast i8* %retval to i8*\n"
                + "\t%3 = call i8* @strcpy(i8* %2, i8* %1)\n"
                + "\tret i8* %retval\n"
                + "}\n");

        out.println("define i8* @" + getMangledName(STRING_TYPE_STR, "substr") + "(i8* %this, i32 %start, i32 %len) {\n"
                + "entry:\n"
                + "\t%0 = getelementptr inbounds i8, i8* %this, i32 %start\n"
                + "\t%1 = call i8* @malloc(i64 1024)\n"
                + "\t%retval = bitcast i8* %1 to i8*\n"
                + "\t%2 = bitcast i8* %retval to i8*\n"
                + "\t%3 = call i8* @strncpy(i8* %2, i8* %0, i32 %len)\n"
                + "\t%4 = getelementptr inbounds i8, i8* %retval, i32 %len\n"
                + "\tstore i8 0, i8* %4\n"
                + "\tret i8* %retval\n"
                + "}\n");
    }

    // mangle function names
    public static String getMangledName(String className, String functionName) {
        StringBuilder mangledName = new StringBuilder("");
        mangledName.append("_CN").append(className.length()).append(className).append("_FN")
                .append(functionName.length()).append(functionName).append("_");
        return mangledName.toString();
    }

    // check if a class if deafult class:
    // default classes are: int, string, object, io, bool
    public static boolean isDefaultClass(String className) {
        if (className.equals(INT_TYPE_STR) ||
            className.equals(STRING_TYPE_STR) ||
            className.equals(OBJECT_TYPE_STR) ||
            className.equals(BOOL_TYPE_STR)
            || className.equals(IO_TYPE_STR))
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

    // input: <ir-type> %reg
    // output: <ir-type>
    public static String getIRTypeFromTypeNReg(String typeNReg) {
		return typeNReg.split(" ")[0];
	}

	// input: <ir-type> %reg
    // output: %reg
	public static String getRegisterFromTypeNReg(String typeNReg) {
		String[] strings = typeNReg.split(" ");
		return strings[strings.length-1];
	}

    // input: <ir-type>
    // output: <Cool-type>
    public static String getCoolTypeFromIRType(String IRType) {
        if (IRType.equals("i32")) {
            return INT_TYPE_STR;
        } else if (IRType.equals("i8*")) {
            return STRING_TYPE_STR;
        } else if (IRType.equals("i8")) {
            return BOOL_TYPE_STR;
        } else {
            // System.out.println("getCoolTypeFromIRType: should not have reached here with irtype: " + IRType);
            return IRType.substring(7, IRType.length() - 1);
        }
    }

    // input: <ir-type> OR <ir-type> %reg
    public static boolean isPointer(String register) {
        if(register.indexOf("*") != -1) return true;
        else return false;
    }

    // input: <ir-type> %<reg> to %<reg>
    public static String getRegFromTypeNReg(String typeNReg) {

        return typeNReg.split(" ")[1];
    }
}
