target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"
define void @_CN6Object_FN6Object_(%class.Object* %this) {
entry:	
ret void
}
define void @_CN2IO_FN2IO_(%class.IO* %this) {
entry:
	%0 = bitcast %class.IO* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	ret void
}
@Abortdivby0 = private unnamed_addr constant [22 x i8] c"Error: Division by 0\0A\00", align 1
@Abortdisvoid = private unnamed_addr constant [25 x i8] c"Error: Dispatch to void\0A\00", align 1

; C malloc declaration
declare noalias i8* @malloc(i64)
; C exit declaration
declare void @exit(i32)
; C printf declaration
declare i32 @printf(i8*, ...)
; C scanf declaration
declare i32 @scanf(i8*, ...)
; C strlen declaration
declare i64 @strlen(i8*)
; C strcpy declaration
declare i8* @strcpy(i8*, i8*)
; C strcat declaration
declare i8* @strcat(i8*, i8*)
; C strncpy declaration
declare i8* @strncpy(i8*, i8*, i32)

@strformatstr = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@intformatstr = private unnamed_addr constant [3 x i8] c"%d\00", align 1

%class.Object = type {i8*}
%class.Int = type {	%class.Object}
%class.String = type {	%class.Object}
%class.Bool = type {	%class.Object}
%class.IO = type {	%class.Object}
%class.A = type {	%class.Object ,i32}
%class.B = type {	%class.A ,i32 ,i32}
%class.C = type {	%class.B ,i32 ,i32 ,i32 ,[1024 x i8]* ,%class.IO* ,%class.Object*}
%class.Main = type {	%class.Object ,%class.C*}
define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {
entry:
	call void @exit( i32 1 )
	ret %class.Object* null
}

define i8* @_CN6Object_FN9type_name(%class.Object* %this) {
	entry:
	%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0
	%1 = load i8*, i8** %0, align 8
	ret i8* %1
}
define i32 @_CN6String_FN6length_( [1024 x i8]* %this ) {
	entry:
	%0 = bitcast [1024 x i8]* %this to i8*
	%1 = call i64 @strlen( i8* %0 )
	%retval = trunc i64 %1 to i32
	ret i32 %retval
}

define [1024 x i8]* @_CN6String_FN6concat_( [1024 x i8]* %this, [1024 x i8]* %that ) {
entry:
	%retval = call [1024 x i8]* @_CN6String_FN4copy_( [1024 x i8]* %this )
	%0 = bitcast [1024 x i8]* %retval to i8*
	%1 = bitcast [1024 x i8]* %that to i8*
	%2 = call i8* @strcat( i8* %0, i8* %1 )
	ret [1024 x i8]* %retval
}

define [1024 x i8]* @_CN6String_FN4copy_( [1024 x i8]* %this ) {
entry:
	%0 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %0 to [1024 x i8]*
	%1 = bitcast [1024 x i8]* %this to i8*
	%2 = bitcast [1024 x i8]* %retval to i8*
	%3 = call i8* @strcpy( i8* %2, i8* %1)
	ret [1024 x i8]* %retval
}

define [1024 x i8]* @_CN6String_FN6substr_( [1024 x i8]* %this, i32 %start, i32 %len ) {
entry:
	%0 = getelementptr inbounds [1024 x i8], [1024 x i8]* %this, i32 0, i32 %start
	%1 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %1 to [1024 x i8]*
	%2 = bitcast [1024 x i8]* %retval to i8*
	%3 = call i8* @strncpy( i8* %2, i8* %0, i32 %len )
	%4 = getelementptr inbounds [1024 x i8], [1024 x i8]* %retval, i32 0, i32 %len
	store i8 0, i8* %4
	ret [1024 x i8]* %retval
}

define %class.IO* @_CN2IO_FN10out_string_( %class.IO* %this, [1024 x i8]* %str ) {
entry:
	%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %str )
	ret %class.IO* %this
}

define %class.IO* @_CN2IO_FN7out_int_( %class.IO* %this, i32 %int ) {
entry:
	%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )
	ret %class.IO* %this
}

define [1024 x i8]* @_CN2IO_FN9in_string_( %class.IO* %this ) {
entry:
	%0 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %0 to [1024 x i8]*
	%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), [1024 x i8]* %retval )
	ret [1024 x i8]* %retval
}

define i32 @_CN2IO_FN6in_int_( %class.IO* %this ) {
entry:
	%0 = call i8* @malloc( i64 4 )
	%1 = bitcast i8* %0 to i32*
	%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )
	%retval = load i32, i32* %1
	ret i32 %retval
}

define i32 @_CN1C_FN15testIOFunctions_(%class.C* %this ,[1024 x i8]* %s,i32 %c){
entry:
	%s.addr = alloca [1024 x i8]*, align 4
	store [1024 x i8]* %s, [1024 x i8]** %s.addr, align 4
	%c.addr = alloca i32, align 4
	store i32 %c, i32* %c.addr, align 4
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [31 x i8]* @.str0 to [1024 x i8]*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then0, label %if.else0
if.then0:
	%4 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %4)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, [1024 x i8]* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [75 x i8]* @.str1 to [1024 x i8]*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then1, label %if.else1
if.then1:
	%11 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %11)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, [1024 x i8]* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = load [1024 x i8]*, [1024 x i8]** %s.addr, align 4
	%17 = icmp eq %class.IO* %15, null
	br i1 %17, label %if.then2, label %if.else2
if.then2:
	%18 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %18)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%20 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %15, [1024 x i8]* %16)
	%21 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%22 = load %class.IO*, %class.IO** %21, align 4
	%23 = bitcast [67 x i8]* @.str2 to [1024 x i8]*
	%24 = icmp eq %class.IO* %22, null
	br i1 %24, label %if.then3, label %if.else3
if.then3:
	%25 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%26 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %25)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %22, [1024 x i8]* %23)
	%28 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%29 = load %class.IO*, %class.IO** %28, align 4
	%30 = load i32, i32* %c.addr, align 4
	%31 = icmp eq %class.IO* %29, null
	br i1 %31, label %if.then4, label %if.else4
if.then4:
	%32 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%33 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %32)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%34 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %29, i32 %30)
	%35 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%36 = load %class.IO*, %class.IO** %35, align 4
	%37 = bitcast [33 x i8]* @.str3 to [1024 x i8]*
	%38 = icmp eq %class.IO* %36, null
	br i1 %38, label %if.then5, label %if.else5
if.then5:
	%39 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%40 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %39)
	call void @exit(i32 1)
	br label %if.else5
if.else5:
	%41 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %36, [1024 x i8]* %37)
	%42 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%43 = load %class.IO*, %class.IO** %42, align 4
	%44 = icmp eq %class.IO* %43, null
	br i1 %44, label %if.then6, label %if.else6
if.then6:
	%45 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%46 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %45)
	call void @exit(i32 1)
	br label %if.else6
if.else6:
	%47 = call [1024 x i8]* @_CN2IO_FN9in_string_(%class.IO* %43)
	store [1024 x i8]* %47, [1024 x i8]** %s.addr, align 4
	%48 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%49 = load %class.IO*, %class.IO** %48, align 4
	%50 = bitcast [15 x i8]* @.str4 to [1024 x i8]*
	%51 = icmp eq %class.IO* %49, null
	br i1 %51, label %if.then7, label %if.else7
if.then7:
	%52 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%53 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %52)
	call void @exit(i32 1)
	br label %if.else7
if.else7:
	%54 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %49, [1024 x i8]* %50)
	%55 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%56 = load %class.IO*, %class.IO** %55, align 4
	%57 = load [1024 x i8]*, [1024 x i8]** %s.addr, align 4
	%58 = icmp eq %class.IO* %56, null
	br i1 %58, label %if.then8, label %if.else8
if.then8:
	%59 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%60 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %59)
	call void @exit(i32 1)
	br label %if.else8
if.else8:
	%61 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %56, [1024 x i8]* %57)
	%62 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%63 = load %class.IO*, %class.IO** %62, align 4
	%64 = bitcast [27 x i8]* @.str5 to [1024 x i8]*
	%65 = icmp eq %class.IO* %63, null
	br i1 %65, label %if.then9, label %if.else9
if.then9:
	%66 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%67 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %66)
	call void @exit(i32 1)
	br label %if.else9
if.else9:
	%68 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %63, [1024 x i8]* %64)
	%69 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%70 = load %class.IO*, %class.IO** %69, align 4
	%71 = icmp eq %class.IO* %70, null
	br i1 %71, label %if.then10, label %if.else10
if.then10:
	%72 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%73 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %72)
	call void @exit(i32 1)
	br label %if.else10
if.else10:
	%74 = call i32 @_CN2IO_FN6in_int_(%class.IO* %70)
	store i32 %74, i32* %c.addr, align 4
	%75 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%76 = load %class.IO*, %class.IO** %75, align 4
	%77 = bitcast [12 x i8]* @.str6 to [1024 x i8]*
	%78 = icmp eq %class.IO* %76, null
	br i1 %78, label %if.then11, label %if.else11
if.then11:
	%79 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%80 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %79)
	call void @exit(i32 1)
	br label %if.else11
if.else11:
	%81 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %76, [1024 x i8]* %77)
	%82 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%83 = load %class.IO*, %class.IO** %82, align 4
	%84 = load i32, i32* %c.addr, align 4
	%85 = icmp eq %class.IO* %83, null
	br i1 %85, label %if.then12, label %if.else12
if.then12:
	%86 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%87 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %86)
	call void @exit(i32 1)
	br label %if.else12
if.else12:
	%88 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %83, i32 %84)
	%89 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%90 = load %class.IO*, %class.IO** %89, align 4
	%91 = bitcast [13 x i8]* @.str7 to [1024 x i8]*
	%92 = icmp eq %class.IO* %90, null
	br i1 %92, label %if.then13, label %if.else13
if.then13:
	%93 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%94 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %93)
	call void @exit(i32 1)
	br label %if.else13
if.else13:
	%95 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %90, [1024 x i8]* %91)
	ret i32 0
}

define i32 @_CN1C_FN22useInheritedAttributes_(%class.C* %this ){
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [60 x i8]* @.str8 to [1024 x i8]*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then14, label %if.else14
if.then14:
	%4 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %4)
	call void @exit(i32 1)
	br label %if.else14
if.else14:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, [1024 x i8]* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	%10 = load i32, i32* %9, align 4
	%11 = icmp eq %class.IO* %8, null
	br i1 %11, label %if.then15, label %if.else15
if.then15:
	%12 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %12)
	call void @exit(i32 1)
	br label %if.else15
if.else15:
	%14 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %8, i32 %10)
	%15 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%16 = load %class.IO*, %class.IO** %15, align 4
	%17 = bitcast [13 x i8]* @.str9 to [1024 x i8]*
	%18 = icmp eq %class.IO* %16, null
	br i1 %18, label %if.then16, label %if.else16
if.then16:
	%19 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%20 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %19)
	call void @exit(i32 1)
	br label %if.else16
if.else16:
	%21 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %16, [1024 x i8]* %17)
	ret i32 0
}

define i32 @_CN1C_FN19testStringFunctions_(%class.C* %this ,[1024 x i8]* %s1,[1024 x i8]* %s2,i32 %x,i32 %l){
entry:
	%s1.addr = alloca [1024 x i8]*, align 4
	store [1024 x i8]* %s1, [1024 x i8]** %s1.addr, align 4
	%s2.addr = alloca [1024 x i8]*, align 4
	store [1024 x i8]* %s2, [1024 x i8]** %s2.addr, align 4
	%x.addr = alloca i32, align 4
	store i32 %x, i32* %x.addr, align 4
	%l.addr = alloca i32, align 4
	store i32 %l, i32* %l.addr, align 4
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [35 x i8]* @.str10 to [1024 x i8]*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then17, label %if.else17
if.then17:
	%4 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %4)
	call void @exit(i32 1)
	br label %if.else17
if.else17:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, [1024 x i8]* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [16 x i8]* @.str11 to [1024 x i8]*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then18, label %if.else18
if.then18:
	%11 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %11)
	call void @exit(i32 1)
	br label %if.else18
if.else18:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, [1024 x i8]* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = load [1024 x i8]*, [1024 x i8]** %s1.addr, align 4
	%17 = icmp eq %class.IO* %15, null
	br i1 %17, label %if.then19, label %if.else19
if.then19:
	%18 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %18)
	call void @exit(i32 1)
	br label %if.else19
if.else19:
	%20 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %15, [1024 x i8]* %16)
	%21 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%22 = load %class.IO*, %class.IO** %21, align 4
	%23 = bitcast [19 x i8]* @.str12 to [1024 x i8]*
	%24 = icmp eq %class.IO* %22, null
	br i1 %24, label %if.then20, label %if.else20
if.then20:
	%25 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%26 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %25)
	call void @exit(i32 1)
	br label %if.else20
if.else20:
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %22, [1024 x i8]* %23)
	%28 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%29 = load %class.IO*, %class.IO** %28, align 4
	%30 = load [1024 x i8]*, [1024 x i8]** %s2.addr, align 4
	%31 = icmp eq %class.IO* %29, null
	br i1 %31, label %if.then21, label %if.else21
if.then21:
	%32 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%33 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %32)
	call void @exit(i32 1)
	br label %if.else21
if.else21:
	%34 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %29, [1024 x i8]* %30)
	%35 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%36 = load %class.IO*, %class.IO** %35, align 4
	%37 = bitcast [72 x i8]* @.str13 to [1024 x i8]*
	%38 = icmp eq %class.IO* %36, null
	br i1 %38, label %if.then22, label %if.else22
if.then22:
	%39 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%40 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %39)
	call void @exit(i32 1)
	br label %if.else22
if.else22:
	%41 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %36, [1024 x i8]* %37)
	%42 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%43 = load %class.IO*, %class.IO** %42, align 4
	%44 = load i32, i32* %x.addr, align 4
	%45 = icmp eq %class.IO* %43, null
	br i1 %45, label %if.then23, label %if.else23
if.then23:
	%46 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%47 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %46)
	call void @exit(i32 1)
	br label %if.else23
if.else23:
	%48 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %43, i32 %44)
	%49 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%50 = load %class.IO*, %class.IO** %49, align 4
	%51 = bitcast [2 x i8]* @.str14 to [1024 x i8]*
	%52 = icmp eq %class.IO* %50, null
	br i1 %52, label %if.then24, label %if.else24
if.then24:
	%53 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%54 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %53)
	call void @exit(i32 1)
	br label %if.else24
if.else24:
	%55 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %50, [1024 x i8]* %51)
	%56 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%57 = load %class.IO*, %class.IO** %56, align 4
	%58 = load i32, i32* %l.addr, align 4
	%59 = icmp eq %class.IO* %57, null
	br i1 %59, label %if.then25, label %if.else25
if.then25:
	%60 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%61 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %60)
	call void @exit(i32 1)
	br label %if.else25
if.else25:
	%62 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %57, i32 %58)
	%63 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%64 = load %class.IO*, %class.IO** %63, align 4
	%65 = bitcast [13 x i8]* @.str15 to [1024 x i8]*
	%66 = icmp eq %class.IO* %64, null
	br i1 %66, label %if.then26, label %if.else26
if.then26:
	%67 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%68 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %67)
	call void @exit(i32 1)
	br label %if.else26
if.else26:
	%69 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %64, [1024 x i8]* %65)
	%70 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%71 = load %class.IO*, %class.IO** %70, align 4
	%72 = load [1024 x i8]*, [1024 x i8]** %s1.addr, align 4
	%73 = icmp eq [1024 x i8]* %72, null
	br i1 %73, label %if.then27, label %if.else27
if.then27:
	%74 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%75 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %74)
	call void @exit(i32 1)
	br label %if.else27
if.else27:
	%76 = call i32 @_CN6String_FN6length_([1024 x i8]* %72)
	%77 = icmp eq %class.IO* %71, null
	br i1 %77, label %if.then28, label %if.else28
if.then28:
	%78 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%79 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %78)
	call void @exit(i32 1)
	br label %if.else28
if.else28:
	%80 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %71, i32 %76)
	%81 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%82 = load %class.IO*, %class.IO** %81, align 4
	%83 = bitcast [27 x i8]* @.str16 to [1024 x i8]*
	%84 = icmp eq %class.IO* %82, null
	br i1 %84, label %if.then29, label %if.else29
if.then29:
	%85 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%86 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %85)
	call void @exit(i32 1)
	br label %if.else29
if.else29:
	%87 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %82, [1024 x i8]* %83)
	%88 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%89 = load %class.IO*, %class.IO** %88, align 4
	%90 = bitcast [9 x i8]* @.str17 to [1024 x i8]*
	%91 = icmp eq [1024 x i8]* %90, null
	br i1 %91, label %if.then30, label %if.else30
if.then30:
	%92 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%93 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %92)
	call void @exit(i32 1)
	br label %if.else30
if.else30:
	%94 = call i32 @_CN6String_FN6length_([1024 x i8]* %90)
	%95 = icmp eq %class.IO* %89, null
	br i1 %95, label %if.then31, label %if.else31
if.then31:
	%96 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%97 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %96)
	call void @exit(i32 1)
	br label %if.else31
if.else31:
	%98 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %89, i32 %94)
	%99 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%100 = load %class.IO*, %class.IO** %99, align 4
	%101 = bitcast [13 x i8]* @.str18 to [1024 x i8]*
	%102 = icmp eq %class.IO* %100, null
	br i1 %102, label %if.then32, label %if.else32
if.then32:
	%103 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%104 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %103)
	call void @exit(i32 1)
	br label %if.else32
if.else32:
	%105 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %100, [1024 x i8]* %101)
	%106 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%107 = load %class.IO*, %class.IO** %106, align 4
	%108 = load [1024 x i8]*, [1024 x i8]** %s1.addr, align 4
	%109 = load [1024 x i8]*, [1024 x i8]** %s2.addr, align 4
	%110 = icmp eq [1024 x i8]* %108, null
	br i1 %110, label %if.then33, label %if.else33
if.then33:
	%111 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%112 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %111)
	call void @exit(i32 1)
	br label %if.else33
if.else33:
	%113 = call [1024 x i8]* @_CN6String_FN6concat_([1024 x i8]* %108, [1024 x i8]* %109)
	%114 = icmp eq %class.IO* %107, null
	br i1 %114, label %if.then34, label %if.else34
if.then34:
	%115 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%116 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %115)
	call void @exit(i32 1)
	br label %if.else34
if.else34:
	%117 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %107, [1024 x i8]* %113)
	%118 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%119 = load %class.IO*, %class.IO** %118, align 4
	%120 = bitcast [23 x i8]* @.str19 to [1024 x i8]*
	%121 = icmp eq %class.IO* %119, null
	br i1 %121, label %if.then35, label %if.else35
if.then35:
	%122 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%123 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %122)
	call void @exit(i32 1)
	br label %if.else35
if.else35:
	%124 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %119, [1024 x i8]* %120)
	%125 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%126 = load %class.IO*, %class.IO** %125, align 4
	%127 = bitcast [9 x i8]* @.str20 to [1024 x i8]*
	%128 = load [1024 x i8]*, [1024 x i8]** %s2.addr, align 4
	%129 = icmp eq [1024 x i8]* %127, null
	br i1 %129, label %if.then36, label %if.else36
if.then36:
	%130 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%131 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %130)
	call void @exit(i32 1)
	br label %if.else36
if.else36:
	%132 = call [1024 x i8]* @_CN6String_FN6concat_([1024 x i8]* %127, [1024 x i8]* %128)
	%133 = icmp eq %class.IO* %126, null
	br i1 %133, label %if.then37, label %if.else37
if.then37:
	%134 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%135 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %134)
	call void @exit(i32 1)
	br label %if.else37
if.else37:
	%136 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %126, [1024 x i8]* %132)
	%137 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%138 = load %class.IO*, %class.IO** %137, align 4
	%139 = bitcast [13 x i8]* @.str21 to [1024 x i8]*
	%140 = icmp eq %class.IO* %138, null
	br i1 %140, label %if.then38, label %if.else38
if.then38:
	%141 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%142 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %141)
	call void @exit(i32 1)
	br label %if.else38
if.else38:
	%143 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %138, [1024 x i8]* %139)
	%144 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%145 = load %class.IO*, %class.IO** %144, align 4
	%146 = load [1024 x i8]*, [1024 x i8]** %s1.addr, align 4
	%147 = load i32, i32* %x.addr, align 4
	%148 = load i32, i32* %l.addr, align 4
	%149 = icmp eq [1024 x i8]* %146, null
	br i1 %149, label %if.then39, label %if.else39
if.then39:
	%150 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%151 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %150)
	call void @exit(i32 1)
	br label %if.else39
if.else39:
	%152 = call [1024 x i8]* @_CN6String_FN6substr_([1024 x i8]* %146, i32 %147, i32 %148)
	%153 = icmp eq %class.IO* %145, null
	br i1 %153, label %if.then40, label %if.else40
if.then40:
	%154 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%155 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %154)
	call void @exit(i32 1)
	br label %if.else40
if.else40:
	%156 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %145, [1024 x i8]* %152)
	%157 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%158 = load %class.IO*, %class.IO** %157, align 4
	%159 = bitcast [13 x i8]* @.str22 to [1024 x i8]*
	%160 = icmp eq %class.IO* %158, null
	br i1 %160, label %if.then41, label %if.else41
if.then41:
	%161 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%162 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %161)
	call void @exit(i32 1)
	br label %if.else41
if.else41:
	%163 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %158, [1024 x i8]* %159)
	ret i32 0
}

define i32 @_CN1C_FN19testObjectFunctions_(%class.C* %this ){
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [35 x i8]* @.str23 to [1024 x i8]*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then42, label %if.else42
if.then42:
	%4 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %4)
	call void @exit(i32 1)
	br label %if.else42
if.else42:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, [1024 x i8]* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [11 x i8]* @.str24 to [1024 x i8]*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then43, label %if.else43
if.then43:
	%11 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %11)
	call void @exit(i32 1)
	br label %if.else43
if.else43:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, [1024 x i8]* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%15 = load %class.Object*, %class.Object** %14, align 4
	%16 = icmp eq %class.Object* %15, null
	br i1 %16, label %if.then44, label %if.else44
if.then44:
	%17 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%18 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %17)
	call void @exit(i32 1)
	br label %if.else44
if.else44:
	%19 = call %class.Object* @_CN6Object_FN5abort_(%class.Object* %15)
	%20 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%21 = load %class.IO*, %class.IO** %20, align 4
	%22 = bitcast [13 x i8]* @.str25 to [1024 x i8]*
	%23 = icmp eq %class.IO* %21, null
	br i1 %23, label %if.then45, label %if.else45
if.then45:
	%24 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%25 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %24)
	call void @exit(i32 1)
	br label %if.else45
if.else45:
	%26 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %21, [1024 x i8]* %22)
	ret i32 0
}

define i32 @_CN4Main_FN4main_(%class.Main* %this ){
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%1 = load %class.C*, %class.C** %0, align 4
	%2 = icmp eq %class.C* %1, null
	br i1 %2, label %if.then46, label %if.else46
if.then46:
	%3 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%4 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %3)
	call void @exit(i32 1)
	br label %if.else46
if.else46:
	%5 = call i32 @_CN1C_FN22useInheritedAttributes_(%class.C* %1)
	%6 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%7 = load %class.C*, %class.C** %6, align 4
	%8 = bitcast [4 x i8]* @.str26 to [1024 x i8]*
	%9 = icmp eq %class.C* %7, null
	br i1 %9, label %if.then47, label %if.else47
if.then47:
	%10 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%11 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %10)
	call void @exit(i32 1)
	br label %if.else47
if.else47:
	%12 = call i32 @_CN1C_FN15testIOFunctions_(%class.C* %7, [1024 x i8]* %8, i32 6)
	%13 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%14 = load %class.C*, %class.C** %13, align 4
	%15 = bitcast [7 x i8]* @.str27 to [1024 x i8]*
	%16 = bitcast [6 x i8]* @.str28 to [1024 x i8]*
	%17 = icmp eq %class.C* %14, null
	br i1 %17, label %if.then48, label %if.else48
if.then48:
	%18 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %18)
	call void @exit(i32 1)
	br label %if.else48
if.else48:
	%20 = call i32 @_CN1C_FN19testStringFunctions_(%class.C* %14, [1024 x i8]* %15, [1024 x i8]* %16, i32 2, i32 3)
	%21 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%22 = load %class.C*, %class.C** %21, align 4
	%23 = icmp eq %class.C* %22, null
	br i1 %23, label %if.then49, label %if.else49
if.then49:
	%24 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%25 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %24)
	call void @exit(i32 1)
	br label %if.else49
if.else49:
	%26 = call i32 @_CN1C_FN19testObjectFunctions_(%class.C* %22)
	ret i32 0
}

define void @_CN1A_FN1A_(%class.A* %this ) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store i32 1, i32* %1, align 4
ret void
}
define void @_CN1B_FN1B_(%class.B* %this ) {
entry:
	%0 = bitcast %class.B* %this to %class.A*
	call void @_CN1A_FN1A_(%class.A* %0)
	%1 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	store i32 1, i32* %1, align 4
	%2 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
ret void
}
define void @_CN1C_FN1C_(%class.C* %this ) {
entry:
	%0 = bitcast %class.C* %this to %class.B*
	call void @_CN1B_FN1B_(%class.B* %0)
	%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	store i32 1, i32* %1, align 4
	%2 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	%3 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 3
	store i32 3, i32* %3, align 4
	%4 = bitcast [6 x i8]* @.str29 to [1024 x i8]*
	%5 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 4
	store [1024 x i8]* %4, [1024 x i8]** %5, align 4
	%6 = call noalias i8* @malloc(i64 32)
	%7 = bitcast i8* %6 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%7)
	%8 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	store %class.IO* %7, %class.IO** %8, align 4
	%9 = call noalias i8* @malloc(i64 32)
	%10 = bitcast i8* %9 to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object*%10)
	%11 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	store %class.Object* %10, %class.Object** %11, align 4
ret void
}
define void @_CN4Main_FN4Main_(%class.Main* %this ) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = call noalias i8* @malloc(i64 8)
	%2 = bitcast i8* %1 to %class.C*
	call void @_CN1C_FN1C_(%class.C*%2)
	%3 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store %class.C* %2, %class.C** %3, align 4
ret void
}
define i32 @main() {
entry:
	%main = alloca %class.Main, align 8
	call void @_CN4Main_FN4Main_(%class.Main* %main)
	%retval = call i32 @_CN4Main_FN4main_(%class.Main* %main)
	ret i32 0
}
@.str0 = private unnamed_addr constant [31 x i8] c"Calling functions in IO class
\00", align 1

@.str1 = private unnamed_addr constant [75 x i8] c"out_string() : (will print s argument, but class member contains 'Hello')
\00", align 1

@.str2 = private unnamed_addr constant [67 x i8] c"
out_int() : (will print c argument, but class member contains 3)
\00", align 1

@.str3 = private unnamed_addr constant [33 x i8] c"
in_string() : (enter a string)
\00", align 1

@.str4 = private unnamed_addr constant [15 x i8] c"
Got string : \00", align 1

@.str5 = private unnamed_addr constant [27 x i8] c"
in_int() : (enter a int)
\00", align 1

@.str6 = private unnamed_addr constant [12 x i8] c"
Got int : \00", align 1

@.str7 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str8 = private unnamed_addr constant [60 x i8] c"A->B->C, A has a member a with value 1. Printing it from C
\00", align 1

@.str9 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str10 = private unnamed_addr constant [35 x i8] c"Calling functions in String class
\00", align 1

@.str11 = private unnamed_addr constant [16 x i8] c"Input String : \00", align 1

@.str12 = private unnamed_addr constant [19 x i8] c"
To concat with : \00", align 1

@.str13 = private unnamed_addr constant [72 x i8] c"
To get substring at index i of length l, where i and l are given by : \00", align 1

@.str14 = private unnamed_addr constant [2 x i8] c" \00", align 1

@.str15 = private unnamed_addr constant [13 x i8] c"
length() : \00", align 1

@.str16 = private unnamed_addr constant [27 x i8] c"
length() of 'A String' : \00", align 1

@.str17 = private unnamed_addr constant [9 x i8] c"A String\00", align 1

@.str18 = private unnamed_addr constant [13 x i8] c"
concat() : \00", align 1

@.str19 = private unnamed_addr constant [23 x i8] c"
concat() 'A String': \00", align 1

@.str20 = private unnamed_addr constant [9 x i8] c"A String\00", align 1

@.str21 = private unnamed_addr constant [13 x i8] c"
substr() : \00", align 1

@.str22 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str23 = private unnamed_addr constant [35 x i8] c"Calling functions in Object class
\00", align 1

@.str24 = private unnamed_addr constant [11 x i8] c"abort() : \00", align 1

@.str25 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str26 = private unnamed_addr constant [4 x i8] c"AAA\00", align 1

@.str27 = private unnamed_addr constant [7 x i8] c"Hello \00", align 1

@.str28 = private unnamed_addr constant [6 x i8] c"World\00", align 1

@.str29 = private unnamed_addr constant [6 x i8] c"Hello\00", align 1

