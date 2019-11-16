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
%class.B = type {	%class.Object ,i8*}
%class.Main = type {	%class.Object ,i8* ,%class.B* ,%class.IO*}
define void @_CN1B_FN1B_(%class.B* %this) {
entry:
	%0 = bitcast %class.B* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = bitcast [1 x i8]* @.str2 to i8*
	%2 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	store i8* %1, i8** %2, align 4
	ret void
}
define void @_CN4Main_FN4Main_(%class.Main* %this) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = bitcast [6 x i8]* @.str3 to i8*
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i8* %1, i8** %2, align 4
	%3 = call noalias i8* @malloc(i64 24)
	%4 = bitcast i8* %3 to %class.B*
	call void @_CN1B_FN1B_(%class.B*%4)
	%5 = bitcast %class.B* %4 to %class.Object*
	%6 = getelementptr inbounds %class.Object, %class.Object* %5, i32 0, i32 0
	%7 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %7, i8** %6, align 8
	%8 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store %class.B* %4, %class.B** %8, align 4
	%9 = call noalias i8* @malloc(i64 24)
	%10 = bitcast i8* %9 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%10)
	%11 = bitcast %class.IO* %10 to %class.Object*
	%12 = getelementptr inbounds %class.Object, %class.Object* %11, i32 0, i32 0
	%13 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %13, i8** %12, align 8
	%14 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	store %class.IO* %10, %class.IO** %14, align 4
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

define i8* @_CN1B_FN4getB_(%class.B* %this ) {
entry:
	%0 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	%1 = load i8*, i8** %0, align 4
	ret i8* %1
}

define i32 @_CN1B_FN4setB_(%class.B* %this , i8* %c) {
entry:
	%c.addr = alloca i8*, align 4
	store i8* %c, i8** %c.addr, align 4
	%0 = load i8*, i8** %c.addr, align 4
	%1 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	store i8* %0, i8** %1, align 4
	%2 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	%3 = load i8*, i8** %2, align 4
	store i8* %3, i8** %c.addr, align 4
	ret i32 0
}

define %class.Object* @_CN4Main_FN4main_(%class.Main* %this ) {
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%3 = load i8*, i8** %2, align 4
	%4 = icmp eq %class.IO* %1, null
	br i1 %4, label %if.then0, label %if.else0
if.then0:
	%5 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %5)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%7 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, i8* %3)
	%8 = bitcast [7 x i8]* @.str4 to i8*
	%9 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i8* %8, i8** %9, align 4
	%10 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%11 = load %class.B*, %class.B** %10, align 4
	%12 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%13 = load i8*, i8** %12, align 4
	%14 = icmp eq %class.B* %11, null
	br i1 %14, label %if.then1, label %if.else1
if.then1:
	%15 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%16 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %15)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%17 = call i32 @_CN1B_FN4setB_(%class.B* %11, i8* %13)
	%18 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%19 = load %class.IO*, %class.IO** %18, align 4
	%20 = bitcast [2 x i8]* @.str5 to i8*
	%21 = icmp eq %class.IO* %19, null
	br i1 %21, label %if.then2, label %if.else2
if.then2:
	%22 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%23 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %22)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%24 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %19, i8* %20)
	%25 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%26 = load %class.IO*, %class.IO** %25, align 4
	%27 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%28 = load %class.B*, %class.B** %27, align 4
	%29 = icmp eq %class.B* %28, null
	br i1 %29, label %if.then3, label %if.else3
if.then3:
	%30 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%31 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %30)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%32 = call i8* @_CN1B_FN4getB_(%class.B* %28)
	%33 = icmp eq %class.IO* %26, null
	br i1 %33, label %if.then4, label %if.else4
if.then4:
	%34 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%35 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %34)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%36 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %26, i8* %32)
	%37 = call noalias i8* @malloc(i64 8)
	%38 = bitcast i8* %37 to %class.Object*
	ret %class.Object* %38
}

define i32 @main() {
entry:
	%main = alloca %class.Main, align 8
	call void @_CN4Main_FN4Main_(%class.Main* %main)
	%retval = call %class.Object* @_CN4Main_FN4main_(%class.Main* %main)
	ret i32 0
}
@.str.Object = private unnamed_addr constant [7 x i8] c"Object\00", align 1
@.str.IO = private unnamed_addr constant [3 x i8] c"IO\00", align 1
@.str.String = private unnamed_addr constant [7 x i8] c"String\00", align 1
@.str.Int = private unnamed_addr constant [4 x i8] c"Int\00", align 1
@.str.Bool = private unnamed_addr constant [5 x i8] c"Bool\00", align 1
@.str.B = private unnamed_addr constant [2 x i8] c"B\00", align 1
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
@.str2 = private unnamed_addr constant [1 x i8] c"\00", align 1

@.str3 = private unnamed_addr constant [6 x i8] c"hello\00", align 1

@.str4 = private unnamed_addr constant [7 x i8] c"asdfgh\00", align 1

@.str5 = private unnamed_addr constant [2 x i8] c"
\00", align 1

