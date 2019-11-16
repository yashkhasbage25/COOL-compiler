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
%class.Main = type {	%class.IO ,%class.B* ,%class.C* ,%class.IO*}
%class.A = type {	%class.Object ,i32}
%class.C = type {	%class.A ,i32 ,i32}
%class.B = type {	%class.Object ,%class.A*}
define void @_CN4Main_FN4Main_(%class.Main* %this) {
entry:
	%0 = bitcast %class.Main* %this to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO* %0)
	%1 = call noalias i8* @malloc(i64 24)
	%2 = bitcast i8* %1 to %class.B*
	call void @_CN1B_FN1B_(%class.B*%2)
	%3 = bitcast %class.B* %2 to %class.Object*
	%4 = getelementptr inbounds %class.Object, %class.Object* %3, i32 0, i32 0
	%5 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %5, i8** %4, align 8
	%6 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store %class.B* %2, %class.B** %6, align 4
	%7 = call noalias i8* @malloc(i64 24)
	%8 = bitcast i8* %7 to %class.C*
	call void @_CN1C_FN1C_(%class.C*%8)
	%9 = bitcast %class.C* %8 to %class.Object*
	%10 = getelementptr inbounds %class.Object, %class.Object* %9, i32 0, i32 0
	%11 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.C, i32 0, i32 0
	store i8* %11, i8** %10, align 8
	%12 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store %class.C* %8, %class.C** %12, align 4
	%13 = call noalias i8* @malloc(i64 24)
	%14 = bitcast i8* %13 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%14)
	%15 = bitcast %class.IO* %14 to %class.Object*
	%16 = getelementptr inbounds %class.Object, %class.Object* %15, i32 0, i32 0
	%17 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %17, i8** %16, align 8
	%18 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	store %class.IO* %14, %class.IO** %18, align 4
	ret void
}
define void @_CN1A_FN1A_(%class.A* %this) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	ret void
}
define void @_CN1C_FN1C_(%class.C* %this) {
entry:
	%0 = bitcast %class.C* %this to %class.A*
	call void @_CN1A_FN1A_(%class.A* %0)
	%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	ret void
}
define void @_CN1B_FN1B_(%class.B* %this) {
entry:
	%0 = bitcast %class.B* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = call noalias i8* @malloc(i64 8)
	%2 = bitcast i8* %1 to %class.A*
	call void @_CN1A_FN1A_(%class.A*%2)
	%3 = bitcast %class.A* %2 to %class.Object*
	%4 = getelementptr inbounds %class.Object, %class.Object* %3, i32 0, i32 0
	%5 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.A, i32 0, i32 0
	store i8* %5, i8** %4, align 8
	%6 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	store %class.A* %2, %class.A** %6, align 4
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

define i32 @_CN4Main_FN4main_(%class.Main* %this ) {
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%3 = load %class.IO*, %class.IO** %2, align 4
	%4 = icmp eq %class.IO* %3, null
	br i1 %4, label %if.then0, label %if.else0
if.then0:
	%5 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %5)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%7 = getelementptr inbounds %class.IO, %class.IO* %3, i32 0, i32 0
	%8 = call i8* @_CN6Object_FN9type_name_(%class.Object* %7)
	%9 = icmp eq %class.IO* %1, null
	br i1 %9, label %if.then1, label %if.else1
if.then1:
	%10 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%11 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %10)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, i8* %8)
	%13 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%14 = load %class.IO*, %class.IO** %13, align 4
	%15 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%16 = load %class.B*, %class.B** %15, align 4
	%17 = icmp eq %class.B* %16, null
	br i1 %17, label %if.then2, label %if.else2
if.then2:
	%18 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %18)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%20 = getelementptr inbounds %class.B, %class.B* %16, i32 0, i32 0
	%21 = call i8* @_CN6Object_FN9type_name_(%class.Object* %20)
	%22 = icmp eq %class.IO* %14, null
	br i1 %22, label %if.then3, label %if.else3
if.then3:
	%23 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%24 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %23)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%25 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %14, i8* %21)
	%26 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%27 = load %class.IO*, %class.IO** %26, align 4
	%28 = bitcast [2 x i8]* @.str4 to i8*
	%29 = icmp eq %class.IO* %27, null
	br i1 %29, label %if.then4, label %if.else4
if.then4:
	%30 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%31 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %30)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%32 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %27, i8* %28)
	%33 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%34 = load %class.IO*, %class.IO** %33, align 4
	%35 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%36 = load %class.C*, %class.C** %35, align 4
	%37 = icmp eq %class.C* %36, null
	br i1 %37, label %if.then5, label %if.else5
if.then5:
	%38 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%39 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %38)
	call void @exit(i32 1)
	br label %if.else5
if.else5:
	%40 = getelementptr inbounds %class.C, %class.C* %36, i32 0, i32 0
	%41 = getelementptr inbounds %class.A, %class.A* %40, i32 0, i32 0
	%42 = call i8* @_CN6Object_FN9type_name_(%class.Object* %41)
	%43 = icmp eq %class.IO* %34, null
	br i1 %43, label %if.then6, label %if.else6
if.then6:
	%44 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%45 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %44)
	call void @exit(i32 1)
	br label %if.else6
if.else6:
	%46 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %34, i8* %42)
	%47 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%48 = load %class.IO*, %class.IO** %47, align 4
	%49 = bitcast [2 x i8]* @.str5 to i8*
	%50 = icmp eq %class.IO* %48, null
	br i1 %50, label %if.then7, label %if.else7
if.then7:
	%51 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%52 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %51)
	call void @exit(i32 1)
	br label %if.else7
if.else7:
	%53 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %48, i8* %49)
	ret i32 2
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
@.str.A = private unnamed_addr constant [2 x i8] c"A\00", align 1
@.str.B = private unnamed_addr constant [2 x i8] c"B\00", align 1
@.str.C = private unnamed_addr constant [2 x i8] c"C\00", align 1
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
@.str4 = private unnamed_addr constant [2 x i8] c"
\00", align 1

@.str5 = private unnamed_addr constant [2 x i8] c"
\00", align 1

