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
%class.A = type {	%class.Object ,%class.IO*}
%class.Main = type {	%class.Object ,i32 ,i32 ,%class.IO* ,%class.A*}
define void @_CN1A_FN1A_(%class.A* %this) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = call noalias i8* @malloc(i64 8)
	%2 = bitcast i8* %1 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%2)
	%3 = bitcast %class.IO* %2 to %class.Object*
	%4 = getelementptr inbounds %class.Object, %class.Object* %3, i32 0, i32 0
	%5 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %5, i8** %4, align 8
	%6 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store %class.IO* %2, %class.IO** %6, align 4
	ret void
}
define void @_CN4Main_FN4Main_(%class.Main* %this) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	%3 = call noalias i8* @malloc(i64 32)
	%4 = bitcast i8* %3 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO*%4)
	%5 = bitcast %class.IO* %4 to %class.Object*
	%6 = getelementptr inbounds %class.Object, %class.Object* %5, i32 0, i32 0
	%7 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %7, i8** %6, align 8
	%8 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	store %class.IO* %4, %class.IO** %8, align 4
	%9 = call noalias i8* @malloc(i64 32)
	%10 = bitcast i8* %9 to %class.A*
	call void @_CN1A_FN1A_(%class.A*%10)
	%11 = bitcast %class.A* %10 to %class.Object*
	%12 = getelementptr inbounds %class.Object, %class.Object* %11, i32 0, i32 0
	%13 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.A, i32 0, i32 0
	store i8* %13, i8** %12, align 8
	%14 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 4
	store %class.A* %10, %class.A** %14, align 4
	ret void
}
define %class.Object* @_CN6Object_FN5abort_(%class.Object* %this) noreturn {
entry:
	call void @exit(i32 1)
	ret %class.Object* null
}

define i8* @_CN6Object_FN9typr_name_(%class.Object* %this) {
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

define %class.Object* @_CN1A_FN5print_(%class.A* %this , i32 %a, i32 %b) {
entry:
	%a.addr = alloca i32, align 4
	store i32 %a, i32* %a.addr, align 4
	%b.addr = alloca i32, align 4
	store i32 %b, i32* %b.addr, align 4
	%0 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = load i32, i32* %a.addr, align 4
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then0, label %if.else0
if.then0:
	%4 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %4)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%6 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %1, i32 %2)
	%7 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [2 x i8]* @.str2 to i8*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then1, label %if.else1
if.then1:
	%11 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %11)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, i8* %9)
	%14 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = load i32, i32* %b.addr, align 4
	%17 = icmp eq %class.IO* %15, null
	br i1 %17, label %if.then2, label %if.else2
if.then2:
	%18 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%19 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %18)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%20 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %15, i32 %16)
	%21 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%22 = load %class.IO*, %class.IO** %21, align 4
	%23 = bitcast [2 x i8]* @.str3 to i8*
	%24 = icmp eq %class.IO* %22, null
	br i1 %24, label %if.then3, label %if.else3
if.then3:
	%25 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%26 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %25)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %22, i8* %23)
	%28 = bitcast %class.IO* %27 to %class.Object*
	ret %class.Object* %28
}

define i32 @_CN4Main_FN4main_(%class.Main* %this ) {
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 4, i32* %0, align 4
	%1 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store i32 5, i32* %1, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	%3 = load %class.IO*, %class.IO** %2, align 4
	%4 = bitcast [2 x i8]* @.str4 to i8*
	%5 = icmp eq %class.IO* %3, null
	br i1 %5, label %if.then4, label %if.else4
if.then4:
	%6 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%7 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %6)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%8 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %3, i8* %4)
	%9 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%10 = load i32, i32* %9, align 4
	%11 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%12 = load i32, i32* %11, align 4
	%13 = add nsw i32 %10, %12
	%14 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 %13, i32* %14, align 4
	%15 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 4
	%16 = load %class.A*, %class.A** %15, align 4
	%17 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%18 = load i32, i32* %17, align 4
	%19 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%20 = load i32, i32* %19, align 4
	%21 = icmp eq %class.A* %16, null
	br i1 %21, label %if.then5, label %if.else5
if.then5:
	%22 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%23 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %22)
	call void @exit(i32 1)
	br label %if.else5
if.else5:
	%24 = call %class.Object* @_CN1A_FN5print_(%class.A* %16, i32 %18, i32 %20)
	%25 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%26 = load i32, i32* %25, align 4
	%27 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%28 = load i32, i32* %27, align 4
	%29 = icmp eq i32 0, %28
	br i1 %29, label %if.then6, label %if.else6
if.then6:
	%30 = bitcast [22 x i8]* @Abortdivby0 to i8*
	%31 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %30)
	call void @exit(i32 1)
	br label %if.else6
if.else6:
	%32 = sdiv i32 %26, %28
	%33 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 %32, i32* %33, align 4
	%34 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 4
	%35 = load %class.A*, %class.A** %34, align 4
	%36 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%37 = load i32, i32* %36, align 4
	%38 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%39 = load i32, i32* %38, align 4
	%40 = icmp eq %class.A* %35, null
	br i1 %40, label %if.then7, label %if.else7
if.then7:
	%41 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%42 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %41)
	call void @exit(i32 1)
	br label %if.else7
if.else7:
	%43 = call %class.Object* @_CN1A_FN5print_(%class.A* %35, i32 %37, i32 %39)
	%44 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%45 = load i32, i32* %44, align 4
	%46 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%47 = load i32, i32* %46, align 4
	%48 = sub nsw i32 %45, %47
	%49 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 %48, i32* %49, align 4
	%50 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 4
	%51 = load %class.A*, %class.A** %50, align 4
	%52 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%53 = load i32, i32* %52, align 4
	%54 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%55 = load i32, i32* %54, align 4
	%56 = icmp eq %class.A* %51, null
	br i1 %56, label %if.then8, label %if.else8
if.then8:
	%57 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%58 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %57)
	call void @exit(i32 1)
	br label %if.else8
if.else8:
	%59 = call %class.Object* @_CN1A_FN5print_(%class.A* %51, i32 %53, i32 %55)
	%60 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%61 = load i32, i32* %60, align 4
	%62 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%63 = load i32, i32* %62, align 4
	%64 = mul nsw i32 %61, %63
	%65 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 %64, i32* %65, align 4
	%66 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 4
	%67 = load %class.A*, %class.A** %66, align 4
	%68 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%69 = load i32, i32* %68, align 4
	%70 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%71 = load i32, i32* %70, align 4
	%72 = icmp eq %class.A* %67, null
	br i1 %72, label %if.then9, label %if.else9
if.then9:
	%73 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%74 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %73)
	call void @exit(i32 1)
	br label %if.else9
if.else9:
	%75 = call %class.Object* @_CN1A_FN5print_(%class.A* %67, i32 %69, i32 %71)
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
@.str.A = private unnamed_addr constant [2 x i8] c"A\00", align 1
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
@.str2 = private unnamed_addr constant [2 x i8] c" \00", align 1

@.str3 = private unnamed_addr constant [2 x i8] c"
\00", align 1

@.str4 = private unnamed_addr constant [2 x i8] c"
\00", align 1

