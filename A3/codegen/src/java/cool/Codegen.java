package cool;

import java.io.PrintWriter;

public class Codegen{
	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
        out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
	}

	private void handleClassMethod(IRClass irClass, HashMap<STring, AST.formal> formalMap, AST.expression expr, PrintWriter out, boolean lastExpr) {
		// assignment expression
		if(expr.getClass() == AST.assign.class) {
			AST.assign assignExpr = (AST.assign) expr;
			AST.expression exp = assignExpr.e1;
			if(irClass.alist.containsKey(assignExpr.name)) {
				out.println("%" + ++regNo + " = getelementptr inbounds %class."
				+ irClass.name + ", %class." + irClass.name +
				"* %this, i32 0, i32 " + irClass.attrOffset.get(assignExpr.name));
				reg = "%" + regNo;
				varType = IRType(irClass.alist.get(assignExpr.name).typeid);
			} else {
				reg = "%" + assignExpr.name;
				varType = IRType(formalMap.get(assignExpr.name).typeid);
			}

			handleClassMethod(irClass, fmap, exp, out, false);
			out.println("store " + varType + " %" + regNo + ", " + varType +
			"* " + reg + ", align 4");
			if(lastExpr) out.println("ret " + varType + " %" +regNo);
		}

		// plus expression
		if(expr.getClass() == AST.plus.class) {
			AST.plus plusExpr = (AST.plus) expr;
			handleClassMethod(irClass, formalMap, plusExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, plusExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = add nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + IRType(plusExpr.e1.type) + " %" + regNo);
		}

		// subtraction expression
		if(expr.getClass() == AST.sub.class) {
			AST.subExpr = (AST.sub) expr;
			handleClassMethod(irClass, formalMap, subExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, subExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = sub nsw i32 " + reg1 + ", " + reg2);

			if(lastExpr) out.println("ret " + IRType(subExpr.e1.type) + " %" + regNo);
		}

		// multiplication expression
		if(expr.getClass() == AST.mul.class) {
			AST.mulExpr = (AST.mul) expr;
			handleClassMethod(irClass, formalMap, mulExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, mulExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = mul nse i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + IRType(mulExpr.e1.type) + " %" + regNo);
		}

		// division expression
		if(expr.getClass() == AST.divide.class)) {
			AST.divide divideExpr = (AST.divide) expr;
			handleClassMethod(irClass, formalMap, divideExpr.e1, out, false);
			String reg1 = "%" + regNo;
			handleClassMethod(irClass, formalMap, divideExpr.e2, out, false);
			String reg2 = "%" + regNo;
			out.println("%" + ++regNo + " = sdic i32 " + reg1 + ", " + reg2);

			if (lastExpr) out.println("ret " + IRType(divideExpr.e1.type) + " %" +regNo);
		}

		// int expression
		if(expr.getClass() == AST.int_const.class) {
			out.println("%" + ++regNo + " = alloca i32, align 4");
			out.println("store i32 " + ((AST.int_const) expr).value + ", i32* %" + regNo + ", align 4");
			out.println("%" + ++regNo + " = load i32, i32* %" + (regNo-1) + ", align 4");

			if (lastExpr) out.println("ret i32 %" + regNo);
		}

		// bool expression
		if(expr.getClass() == AST.bool_const.class) {
			boolean bool = ((AST.bool_const) expr).value;
			int int4bool = 0;
			if(bool) int4bool = 1;

			out.println("%" + ++regNo + " = alloca i32, align 1"); ////////////////////
			out.println("store i32 " + int4bool + ", i32* %" + regNo + ", align 4");
			out.println("%" + ++regNo + " = load i32, i32* %" + (regNo-1) + ", align 4");

			if(lastExpr) out.println("ret int32 %" + regNo);
		}

		// new expression
		if(expr.getClass() == AST.new_.class) {
			AST.new_ newExpr = (AST.new_) expr;
			out.println("%" + ++regNo + " = alloca " + IRType2(newExpr.typeid) + ", align 4");

			if(lastExpr) out.println("ret " + IRType2(newExpr.typeid) + "* %" + regNo);
		}

		// identifier expression
		if(expr.getClass() == AST.object.class)
		{
			String reg;
			AST.object str = (AST.object)expr;
			if (str.name.equals("self")) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %this, "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}

			else if(fmap.containsKey(str.name)) {
				out.println("%"+ ++regNo +" = alloca " + IRType(str.type) + ", align 4");
				out.println("store "+IRType(str.type)+" %"+str.name+", "+IRType(str.type)+"* %"+regNo+", align 4");
				out.println("%"+ ++regNo + " = load "+IRType(str.type)+", "+IRType(str.type)+"* %"+ (regNo-1) + ", align 4");
			}
			else { // it must be defined in attributes.
				out.println("%"+ ++regNo +" = getelementptr inbounds %class." + irclass.name + ", %class."+irclass.name+"* %this, i32 0, i32 "+irclass.attrOffset.get(str.name));
				out.println("%"+ ++regNo +" = load  " + IRType(irclass.alist.get(str.name).typeid) + ", "+ IRType(irclass.alist.get(str.name).typeid) + "* %"+(regNo-1)+", align 4");

			}

			if(lastExpr)
				out.println("ret "+IRType(str.type) + " %" + regNo);
		}

		// dispatch
		else if(expr.getClass() == AST.dispatch.class)
		{
			AST.dispatch str = (AST.dispatch)expr;
			if( !(str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) )
				ProcessMethod(irclass, fmap, str.caller, out, false);
			String thisReg = ""+regNo;

			String caller = str.caller.type;
			if( (str.caller.getClass() == AST.object.class && ((AST.object)str.caller).name.equals("self")) ) {
				caller = irclass.name;
				thisReg = "this";
			} else {

				// Go through the attrs of the class called, and allocate if there's an initialisation in the attributes node.

				String cname = caller;
				for (Entry<String, AST.attr> attrEntry : IRclassTable.getAttrs(cname).entrySet()) {
					AST.attr attrNode = attrEntry.getValue();
					if (attrNode.value.getClass() == AST.new_.class) {
						AST.new_ node = (AST.new_)attrNode.value;
						out.println("%"+ ++regNo +" = alloca " + IRType2(node.typeid)+ ", align 4");
						out.println("%"+ ++regNo  +" = getelementptr inbounds %class." + cname + ", %class."+cname+"* %" + thisReg + ", i32 0, i32 "+IRclassTable.getIRClass(cname).attrOffset.get(attrNode.name));
						out.println("store " + IRType2(node.typeid)+ "* %"+ (regNo - 1) +", " + IRType2(node.typeid)+ "** %"+regNo+", align 8");
					}

				}
			}

			String methodName = str.name;
			String mType =   IRType(IRclassTable.getIRClass(caller).mlist.get(methodName).typeid);
			String dispatchStr = "call " + mType + " @"+caller+"_"+methodName + "("+ IRType(caller) + " %"+thisReg;

			for(int i = 0; i < str.actuals.size(); i++)
			{
				expr = str.actuals.get(i);
				ProcessMethod(irclass, fmap, expr, out, false);
				dispatchStr += ", "+IRType(expr.type) + " %"+ regNo;
			}

			dispatchStr += ")";

			out.println("%"+ ++regNo + " = " + dispatchStr);

			if (lastExpr)
				out.println("ret "+ mType + " %" + regNo);
		}

		// if then else
		else if(expr.getClass() == AST.cond.class)
		{
			AST.cond str = (AST.cond)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.ifbody;
			AST.expression e3 = str.elsebody;
			ProcessMethod(irclass, fmap, e1, out, false);
			String tag = ""+regNo;
			out.println("br i1 %"+regNo+", label %ifbody"+tag+", label %elsebody"+tag);

			out.println("ifbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);
			if(lastExpr)
				out.println("ret "+IRType(e2.type) + " %" + regNo++);
			out.println("br label %elsebody"+tag);

			out.println("elsebody"+tag+":");
			ProcessMethod(irclass, fmap, e3, out, false);
			if(lastExpr)
				out.println("ret "+IRType(e3.type) + " %" + regNo++);

		    out.println("br label %thenbody"+tag); ///////////////////
			out.println("thenbody"+tag+":");

			if(lastExpr)
			out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %"+(regNo-1));

		}

		else if(expr.getClass() == AST.block.class) /////////////
		{
			AST.block str = (AST.block)expr;
			List<AST.expression> listExp = new ArrayList<AST.expression>();
			listExp = str.l1;
			boolean isMethod = false;
			if (lastExpr == true)
				isMethod = true;
			lastExpr = false;
			for(int i = 0; i < listExp.size(); ++i)
	        {
	        	AST.expression e2 = new AST.expression();
				e2 = listExp.get(i);
				if (i == listExp.size()-1 && isMethod)
					lastExpr = true;

				ProcessMethod(irclass, fmap, e2, out, lastExpr);

	        }

		}


		// while loop
		else if(expr.getClass() == AST.loop.class)
		{
			AST.loop str = (AST.loop)expr;
			AST.expression e1 = str.predicate;
			AST.expression e2 = str.body;

			String tag = ""+regNo;
			out.println("br label %"+"predicate"+tag);
			out.println("predicate"+tag+":");
			ProcessMethod(irclass, fmap, e1, out, false);
			out.println("br i1 %"+regNo+", label %loopbody"+tag+", label %pool"+tag);

			out.println("loopbody"+tag+":");
			ProcessMethod(irclass, fmap, e2, out, false);
			out.println("br label %predicate"+tag);

			out.println("pool"+tag+":");

			if(lastExpr)
				out.println("ret "+IRType(fmap.get("#rettype").typeid) + " %" + regNo);

		}

		else if(expr.getClass() == AST.eq.class)
		{
			AST.eq str = (AST.eq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp eq " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than
		else if(expr.getClass() == AST.lt.class)
		{
			AST.lt str = (AST.lt)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp slt " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// less than equal to
		else if(expr.getClass() == AST.leq.class)
		{
			AST.leq str = (AST.leq)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			ProcessMethod(irclass, fmap, str.e2, out, false);
			String reg2 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp sle " + IRType(str.e1.type) + " "+reg1+", "+reg2);
		}

		// negation (not EXPR)
		else if(expr.getClass() == AST.neg.class)
		{
			AST.neg str = (AST.neg)expr;
			ProcessMethod(irclass, fmap, str.e1, out, false);
			String reg1 = "%"+regNo;
			out.println("%"+ ++regNo +" = icmp ne " + IRType(str.e1.type) + " "+reg1+", "+reg1);
		}

		// string const
		else if(expr.getClass() == AST.string_const.class)
		{
			AST.string_const str = (AST.string_const)expr;
			defineStringConst(str,out);
			out.println("%"+ ++regNo +" = alloca [1024 x i8], align 16");
			out.println("%"+ ++regNo +" = getelementptr inbounds [1024 x i8], [1024 x i8]* %"+(regNo-1)+", i32 0, i32 0");
			out.println("%"+ ++regNo +" = call i8* @strcpy(i8* %"+ (regNo-1) + ", i8* getelementptr inbounds (["+ (str.value.length()+1) +" x i8], ["+ (str.value.length()+1) +" x i8]* @.str."+ (strCount-1) +", i32 0, i32 0))");
			out.println("%"+ ++regNo +" = load [1024 x i8], [1024 x i8]* %"+(regNo-3) + ", align 16");

			if(lastExpr) {
				out.println("ret "+IRType(str.type) + " %" + (regNo));
			}
		}
	}
}
