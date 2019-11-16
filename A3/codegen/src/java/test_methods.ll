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
%class.C = type {	%class.Object ,i32}
%class.B = type {	%class.Object}
%class.A = type {	%class.Object ,%class.B* ,%class.C* ,i32 ,%class.C* ,i32 ,%class.IO*}
%class.Main = type {	%class.Object ,%class.A* ,i32 ,%class.IO*}
define void @_CN1C_FN1C_(%class.C* %this) {
entry:
	%0 = bitcast %class.C* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	ret void
}
define void @_CN1B_FN1B_(%class.B* %this) {
entry:
	%0 = bitcast %class.B* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	ret void
}
define void @_CN1A_FN1A_(%class.A* %this) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = call noalias i8* @malloc(i64 48)
	%2 = bitcast i8* %1 to %class.B*
	call void @_CN1B_FN1B_(%class.B*%2)
	%3 = bitcast %class.B* %2 to %class.Object*
	%4 = getelementptr inbounds %class.Object, %class.Object* %3, i32 0, i32 0
	%5 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %5, i8** %4, align 8
	%6 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store %class.B* %2, %class.B** %6, align 4
	%7 = call noalias i8* @malloc(i64 48)
	%8 = bitcast i8* %7 to %class.C*
	call void @_CN1C_FN1C_(%class.C*%8)
	%9 = bitcast %class.C* %8 to %class.Object*
	%10 = getelementptr inbounds %class.Object, %class.Object* %9, i32 0, i32 0
	%11 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.C, i32 0, i32 0
	store i8* %11, i8** %10, align 8
	%12 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	store %class.C* %8, %class.C** %12, align 4
	%13 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 3
	store i32 0, i32* %13, align 4
	%14 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%15 = load %class.C*, %class.C** %14, align 4
	%16 = icmp eq %class.C* %15, null
	br i1 %16, label %if.then0, label %if.else0
if.then0:
	%17 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%18 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %17)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%19 = call %class.C* @_CN1C_FN4newC_(%class.C* %15)
	%20 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 4
	store %class.C* %19, %class.C** %20, align 4
	%21 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%22 = load %class.C*, %class.C** %21, align 4
	%23 = icmp eq %class.C* %22, null
	br i1 %23, label %if.then1, label %if.else1
if.then1:
	%24 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%25 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %24)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%26 = call i32 @_CN1C_FN4getC_(%class.C* %22)
	%27 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 5
	store i32 %26, i32* %27, align 4
	%28 = call noalias i8* @malloc(i64 48)
	%29 = bitcast i8* %28 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%29)
	%30 = bitcast %class.IO* %29 to %class.Object*
	%31 = getelementptr inbounds %class.Object, %class.Object* %30, i32 0, i32 0
	%32 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %32, i8** %31, align 8
	%33 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 6
	store %class.IO* %29, %class.IO** %33, align 4
	ret void
}
define void @_CN4Main_FN4Main_(%class.Main* %this) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = call noalias i8* @malloc(i64 24)
	%2 = bitcast i8* %1 to %class.A*
	call void @_CN1A_FN1A_(%class.A*%2)
	%3 = bitcast %class.A* %2 to %class.Object*
	%4 = getelementptr inbounds %class.Object, %class.Object* %3, i32 0, i32 0
	%5 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.A, i32 0, i32 0
	store i8* %5, i8** %4, align 8
	%6 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store %class.A* %2, %class.A** %6, align 4
	%7 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store i32 3, i32* %7, align 4
	%8 = call noalias i8* @malloc(i64 24)
	%9 = bitcast i8* %8 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%9)
	%10 = bitcast %class.IO* %9 to %class.Object*
	%11 = getelementptr inbounds %class.Object, %class.Object* %10, i32 0, i32 0
	%12 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %12, i8** %11, align 8
	%13 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	store %class.IO* %9, %class.IO** %13, align 4
	ret void
}
define %class.Object* @_CN6Object_FN5abort_(%class.Object* %this) noreturn {
entry:
	call void @exit(i32 1)
	ret %class.Object* null
}

define i8* @_CN6Object_FN9type_name_(%class.Object* %this) {
	entry:
	%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0
	%1 = load i8*, i8** %0, align 8
	ret i8* %1
}
define i32 @_CN6String_FN6length_(i8* %this) {
	entry:
	%0 = bitcast i8* %this to i8*
	%1 = call i64 @strlen(i8* %0)
	%retval = trunc i64 %1 to i32
	ret i32 %retval
}

define i8* @_CN6String_FN6concat_(i8* %this, i8* %that) {
entry:
	%retval = call i8* @_CN6String_FN4copy_(i8* %this)
	%0 = bitcast i8* %retval to i8*
	%1 = bitcast i8* %that to i8*
	%2 = call i8* @strcat(i8* %0, i8* %1)
	ret i8* %retval
}

define i8* @_CN6String_FN4copy_(i8* %this) {
entry:
	%0 = call i8* @malloc(i64 1024)
	%retval = bitcast i8* %0 to i8*
	%1 = bitcast i8* %this to i8*
	%2 = bitcast i8* %retval to i8*
	%3 = call i8* @strcpy(i8* %2, i8* %1)
	ret i8* %retval
}

define i8* @_CN6String_FN6substr_(i8* %this, i32 %start, i32 %len) {
entry:
	%0 = getelementptr inbounds i8, i8* %this, i32 %start
	%1 = call i8* @malloc(i64 1024)
	%retval = bitcast i8* %1 to i8*
	%2 = bitcast i8* %retval to i8*
	%3 = call i8* @strncpy(i8* %2, i8* %0, i32 %len)
	%4 = getelementptr inbounds i8, i8* %retval, i32 %len
	store i8 0, i8* %4
	ret i8* %retval
}

define %class.IO* @_CN2IO_FN10out_string_(%class.IO* %this, i8* %str) {
entry:
	%0 = call i32 (i8*, ...) @printf(i8* bitcast ([3 x i8]* @strformatstr to i8*), i8* %str)
	ret %class.IO* %this
}

define %class.IO* @_CN2IO_FN7out_int_(%class.IO* %this, i32 %int) {
entry:
	%0 = call i32 (i8*, ...) @printf(i8* bitcast ([3 x i8]* @intformatstr to i8*), i32 %int)
	ret %class.IO* %this
}

define i8* @_CN2IO_FN9in_string_(%class.IO* %this) {
entry:
	%0 = call i8* @malloc(i64 1024)
	%retval = bitcast i8* %0 to i8*
	%1 = call i32 (i8*, ...) @scanf(i8* bitcast ([3 x i8]* @strformatstr to i8*), i8* %retval)
	ret i8* %retval
}

define i32 @_CN2IO_FN6in_int_(%class.IO* %this) {
entry:
	%0 = call i8* @malloc(i64 4)
	%1 = bitcast i8* %0 to i32*
	%2 = call i32 (i8*, ...) @scanf(i8* bitcast ([3 x i8]* @intformatstr to i8*), i32* %1)
	%retval = load i32, i32* %1
	ret i32 %retval
}

define %class.C* @_CN1C_FN4newC_(%class.C* %this ) {
entry:
	%0 = call noalias i8* @malloc(i64 8)
	%1 = bitcast i8* %0 to %class.C*
	call void @_CN1C_FN1C_(%class.C*%1)
	%2 = bitcast %class.C* %1 to %class.Object*
	%3 = getelementptr inbounds %class.Object, %class.Object* %2, i32 0, i32 0
	%4 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.C, i32 0, i32 0
	store i8* %4, i8** %3, align 8
	ret %class.C* %1
}

define i32 @_CN1C_FN4setC_(%class.C* %this , i32 %cc) {
entry:
	%cc.addr = alloca i32, align 4
	store i32 %cc, i32* %cc.addr, align 4
	%0 = load i32, i32* %cc.addr, align 4
	%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	store i32 %0, i32* %1, align 4
	ret i32 %0
}

define i32 @_CN1C_FN4getC_(%class.C* %this ) {
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	%1 = load i32, i32* %0, align 4
	ret i32 %1
}

define i32 @_CN1B_FN2fb_(%class.B* %this , %class.C* %c) {
entry:
	%c.addr = alloca %class.C*, align 4
	store %class.C* %c, %class.C** %c.addr, align 4
	%0 = load %class.C*, %class.C** %c.addr, align 4
	%1 = load %class.C*, %class.C** %c.addr, align 4
	%2 = icmp eq %class.C* %1, null
	br i1 %2, label %if.then2, label %if.else2
if.then2:
	%3 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%4 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %3)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%5 = call i32 @_CN1C_FN4getC_(%class.C* %1)
	%6 = load %class.C*, %class.C** %c.addr, align 4
	%7 = icmp eq %class.C* %6, null
	br i1 %7, label %if.then3, label %if.else3
if.then3:
	%8 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%9 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %8)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%10 = call i32 @_CN1C_FN4getC_(%class.C* %6)
	%11 = mul nsw i32 %5, %10
	%12 = icmp eq %class.C* %0, null
	br i1 %12, label %if.then4, label %if.else4
if.then4:
	%13 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%14 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %13)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%15 = call i32 @_CN1C_FN4setC_(%class.C* %0, i32 %11)
	ret i32 %15
}

define i32 @_CN1A_FN2f1_(%class.A* %this , i32 %b, i32 %c) {
entry:
	%b.addr = alloca i32, align 4
	store i32 %b, i32* %b.addr, align 4
	%c.addr = alloca i32, align 4
	store i32 %c, i32* %c.addr, align 4
	%0 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%1 = load %class.C*, %class.C** %0, align 4
	%2 = icmp eq %class.C* %1, null
	br i1 %2, label %if.then5, label %if.else5
if.then5:
	%3 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%4 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %3)
	call void @exit(i32 1)
	br label %if.else5
if.else5:
	%5 = call i32 @_CN1C_FN4setC_(%class.C* %1, i32 4)
	%6 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%7 = load %class.C*, %class.C** %6, align 4
	%8 = icmp eq %class.C* %7, null
	br i1 %8, label %if.then6, label %if.else6
if.then6:
	%9 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%10 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %9)
	call void @exit(i32 1)
	br label %if.else6
if.else6:
	%11 = call i32 @_CN1C_FN4getC_(%class.C* %7)
	%12 = add nsw i32 %11, 2
	store i32 %12, i32* %b.addr, align 4
	%13 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%14 = load %class.B*, %class.B** %13, align 4
	%15 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%16 = load %class.C*, %class.C** %15, align 4
	%17 = icmp eq %class.B* %14, null
	br i1 %17, label %if.then7, label %if.else7
if.then7:
	%18 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %18)
	call void @exit(i32 1)
	br label %if.else7
if.else7:
	%20 = call i32 @_CN1B_FN2fb_(%class.B* %14, %class.C* %16)
	store i32 %20, i32* %b.addr, align 4
	%21 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 6
	%22 = load %class.IO*, %class.IO** %21, align 4
	%23 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 2
	%24 = load %class.C*, %class.C** %23, align 4
	%25 = icmp eq %class.C* %24, null
	br i1 %25, label %if.then8, label %if.else8
if.then8:
	%26 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %26)
	call void @exit(i32 1)
	br label %if.else8
if.else8:
	%28 = call i32 @_CN1C_FN4getC_(%class.C* %24)
	%29 = icmp eq %class.IO* %22, null
	br i1 %29, label %if.then9, label %if.else9
if.then9:
	%30 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%31 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %30)
	call void @exit(i32 1)
	br label %if.else9
if.else9:
	%32 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %22, i32 %28)
	%33 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 6
	%34 = load %class.IO*, %class.IO** %33, align 4
	%35 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 4
	%36 = load %class.C*, %class.C** %35, align 4
	%37 = icmp eq %class.C* %36, null
	br i1 %37, label %if.then10, label %if.else10
if.then10:
	%38 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%39 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %38)
	call void @exit(i32 1)
	br label %if.else10
if.else10:
	%40 = call i32 @_CN1C_FN4getC_(%class.C* %36)
	%41 = icmp eq %class.IO* %34, null
	br i1 %41, label %if.then11, label %if.else11
if.then11:
	%42 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%43 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %42)
	call void @exit(i32 1)
	br label %if.else11
if.else11:
	%44 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %34, i32 %40)
	ret i32 0
}

define i32 @_CN4Main_FN4main_(%class.Main* %this ) {
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%3 = load i32, i32* %2, align 4
	%4 = icmp eq %class.IO* %1, null
	br i1 %4, label %if.then12, label %if.else12
if.then12:
	%5 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %5)
	call void @exit(i32 1)
	br label %if.else12
if.else12:
	%7 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %1, i32 %3)
	%8 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%9 = load %class.A*, %class.A** %8, align 4
	%10 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%11 = load i32, i32* %10, align 4
	%12 = icmp eq %class.A* %9, null
	br i1 %12, label %if.then13, label %if.else13
if.then13:
	%13 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%14 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %13)
	call void @exit(i32 1)
	br label %if.else13
if.else13:
	%15 = call i32 @_CN1A_FN2f1_(%class.A* %9, i32 %11, i32 6)
	%16 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store i32 %15, i32* %16, align 4
	%17 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%18 = load %class.IO*, %class.IO** %17, align 4
	%19 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%20 = load i32, i32* %19, align 4
	%21 = icmp eq %class.IO* %18, null
	br i1 %21, label %if.then14, label %if.else14
if.then14:
	%22 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%23 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %22)
	call void @exit(i32 1)
	br label %if.else14
if.else14:
	%24 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %18, i32 %20)
	ret i32 0
}

define i32 @main() {
entry:
	%main = alloca %class.Main, align 8
	call void @_CN4Main_FN4Main_(%class.Main* %main)
	%retval = call i32 @_CN4Main_FN4main_(%class.Main* %main)
	ret i32 0
}
@.str.Object = private unnamed_addr constant [7 x i8] c"Object\00", align 1
@.str.IO = private unnamed_addr constant [3 x i8] c"IO\00", align 1
@.str.String = private unnamed_addr constant [7 x i8] c"String\00", align 1
@.str.Int = private unnamed_addr constant [4 x i8] c"Int\00", align 1
@.str.Bool = private unnamed_addr constant [5 x i8] c"Bool\00", align 1
@.str.C = private unnamed_addr constant [2 x i8] c"C\00", align 1
@.str.B = private unnamed_addr constant [2 x i8] c"B\00", align 1
@.str.A = private unnamed_addr constant [2 x i8] c"A\00", align 1
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
