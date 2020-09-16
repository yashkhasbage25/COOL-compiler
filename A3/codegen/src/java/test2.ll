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

%class.Object = type{ i8* }

%class.IO = type{ %class.Object }

%class.A = type{ %class.Object, i32 }

%class.B = type{ %class.A, i32, i32 }

%class.F = type{ %class.B, i32, i32, i32 }

%class.E = type{ %class.A, i32, i32 }

%class.C = type{ %class.Object, i32, i8, i8*, %class.B*, %class.B*, %class.IO*, %class.A*, %class.A*, %class.A*, %class.F*, %class.A* }

%class.Main = type{ %class.Object, %class.A*, %class.C* }

define void @_CN1A_FN1A_(%class.A* %this) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	ret void
}

define void @_CN1B_FN1B_(%class.B* %this) {
entry:
	%0 = bitcast %class.B* %this to %class.A*
	call void @_CN1A_FN1A_(%class.A* %0)
	%1 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	ret void
}

define void @_CN1F_FN1F_(%class.F* %this) {
entry:
	%0 = bitcast %class.F* %this to %class.B*
	call void @_CN1B_FN1B_(%class.B* %0)
	%1 = getelementptr inbounds %class.F, %class.F* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.F, %class.F* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	%3 = getelementptr inbounds %class.F, %class.F* %this, i32 0, i32 3
	store i32 0, i32* %3, align 4
	ret void
}

define void @_CN1E_FN1E_(%class.E* %this) {
entry:
	%0 = bitcast %class.E* %this to %class.A*
	call void @_CN1A_FN1A_(%class.A* %0)
	%1 = getelementptr inbounds %class.E, %class.E* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.E, %class.E* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	ret void
}

define void @_CN1C_FN1C_(%class.C* %this) {
entry:
	%0 = bitcast %class.C* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
	store i8 0, i8* %2, align 4
	%3 = bitcast [1 x i8]* @.str6 to i8*
	%4 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 3
	store i8* %3, i8** %4, align 4
	%5 = getelementptr inbounds %class.C,%class.C* %this, i32 0, i32 4
	store %class.B* null, %class.B** %5, align 4
	%6 = call noalias i8* @malloc(i64 88)
	%7 = bitcast i8* %6 to %class.B*
	call void @_CN1B_FN1B_(%class.B* %7)
	%8 = bitcast %class.B* %7 to %class.Object*
	%9 = getelementptr inbounds %class.Object, %class.Object* %8, i32 0, i32 0
	%10 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %10, i8** %9, align 8
	%11 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	store %class.B* %7, %class.B** %11, align 4
	%12 = call noalias i8* @malloc(i64 88)
	%13 = bitcast i8* %12 to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO* %13)
	%14 = bitcast %class.IO* %13 to %class.Object*
	%15 = getelementptr inbounds %class.Object, %class.Object* %14, i32 0, i32 0
	%16 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.IO, i32 0, i32 0
	store i8* %16, i8** %15, align 8
	%17 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	store %class.IO* %13, %class.IO** %17, align 4
	%18 = call noalias i8* @malloc(i64 88)
	%19 = bitcast i8* %18 to %class.B*
	call void @_CN1B_FN1B_(%class.B* %19)
	%20 = bitcast %class.B* %19 to %class.Object*
	%21 = getelementptr inbounds %class.Object, %class.Object* %20, i32 0, i32 0
	%22 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %22, i8** %21, align 8
	%23 = bitcast %class.B* %19 to %class.A*
	%24 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 7
	store %class.A* %23, %class.A** %24, align 4
	%25 = call noalias i8* @malloc(i64 88)
	%26 = bitcast i8* %25 to %class.A*
	call void @_CN1A_FN1A_(%class.A* %26)
	%27 = bitcast %class.A* %26 to %class.Object*
	%28 = getelementptr inbounds %class.Object, %class.Object* %27, i32 0, i32 0
	%29 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.A, i32 0, i32 0
	store i8* %29, i8** %28, align 8
	%30 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 8
	store %class.A* %26, %class.A** %30, align 4
	%31 = getelementptr inbounds %class.C,%class.C* %this, i32 0, i32 9
	store %class.A* null, %class.A** %31, align 4
	%32 = call noalias i8* @malloc(i64 88)
	%33 = bitcast i8* %32 to %class.F*
	call void @_CN1F_FN1F_(%class.F* %33)
	%34 = bitcast %class.F* %33 to %class.Object*
	%35 = getelementptr inbounds %class.Object, %class.Object* %34, i32 0, i32 0
	%36 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.F, i32 0, i32 0
	store i8* %36, i8** %35, align 8
	%37 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 10
	store %class.F* %33, %class.F** %37, align 4
	%38 = call noalias i8* @malloc(i64 88)
	%39 = bitcast i8* %38 to %class.F*
	call void @_CN1F_FN1F_(%class.F* %39)
	%40 = bitcast %class.F* %39 to %class.Object*
	%41 = getelementptr inbounds %class.Object, %class.Object* %40, i32 0, i32 0
	%42 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.F, i32 0, i32 0
	store i8* %42, i8** %41, align 8
	%43 = bitcast %class.F* %39 to %class.A*
	%44 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 11
	store %class.A* %43, %class.A** %44, align 4
	ret void
}

define void @_CN4Main_FN4Main_(%class.Main* %this) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.Main,%class.Main* %this, i32 0, i32 1
	store %class.A* null, %class.A** %1, align 4
	%2 = call noalias i8* @malloc(i64 16)
	%3 = bitcast i8* %2 to %class.C*
	call void @_CN1C_FN1C_(%class.C* %3)
	%4 = bitcast %class.C* %3 to %class.Object*
	%5 = getelementptr inbounds %class.Object, %class.Object* %4, i32 0, i32 0
	%6 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.C, i32 0, i32 0
	store i8* %6, i8** %5, align 8
	%7 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store %class.C* %3, %class.C** %7, align 4
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

define i32 @_CN1A_FN2f1_(%class.A* %this ) {
entry:
	ret i32 0
}

define i32 @_CN1B_FN2f1_(%class.B* %this ) {
entry:
	ret i32 1
}

define %class.A* @_CN1C_FN26ifWithDifferentReturnTypes_(%class.C* %this , i8 %b) {
entry:
	%b.addr = alloca i8, align 4
	store i8 %b, i8* %b.addr, align 4
	%0 = load i8, i8* %b.addr, align 4
	%1 = trunc i8 %0 to i1
	br i1 %1, label %if.then0, label %if.else0
if.then0:
	%2 = call noalias i8* @malloc(i64 88)
	%3 = bitcast i8* %2 to %class.B*
	call void @_CN1B_FN1B_(%class.B* %3)
	%4 = bitcast %class.B* %3 to %class.Object*
	%5 = getelementptr inbounds %class.Object, %class.Object* %4, i32 0, i32 0
	%6 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %6, i8** %5, align 8
	br label %if.end0
if.else0:
	%7 = call noalias i8* @malloc(i64 88)
	%8 = bitcast i8* %7 to %class.E*
	call void @_CN1E_FN1E_(%class.E* %8)
	%9 = bitcast %class.E* %8 to %class.Object*
	%10 = getelementptr inbounds %class.Object, %class.Object* %9, i32 0, i32 0
	%11 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.E, i32 0, i32 0
	store i8* %11, i8** %10, align 8
	br label %if.end0
if.end0:
	%12 = bitcast %class.B* %3 to %class.A*
	%13 = bitcast %class.E* %8 to %class.A*
	%14 = phi %class.A* [%12, %if.then0], [%13, %if.else0]
	%15 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 9
	store %class.A* %14, %class.A** %15, align 4
	%16 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%17 = load %class.IO*, %class.IO** %16, align 4
	%18 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 9
	%19 = load %class.A*, %class.A** %18, align 4
	%20 = icmp eq %class.A* %19, null
	br i1 %20, label %if.then1, label %if.else1
if.then1:
	%21 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%22 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %21)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%23 = getelementptr inbounds %class.A, %class.A* %19, i32 0, i32 0
	%24 = call i8* @_CN6Object_FN9type_name_(%class.Object* %23)
	%25 = icmp eq %class.IO* %17, null
	br i1 %25, label %if.then2, label %if.else2
if.then2:
	%26 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %26)
	call void @exit(i32 1)
	br label %if.else2
if.else2:
	%28 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %17, i8* %24)
	%29 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 9
	%30 = load %class.A*, %class.A** %29, align 4
	ret %class.A* %30
}

define i32 @_CN1C_FN18testStaticDispatch_(%class.C* %this ) {
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [90 x i8]* @.str7 to i8*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then3, label %if.else3
if.then3:
	%4 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %4)
	call void @exit(i32 1)
	br label %if.else3
if.else3:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, i8* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [46 x i8]* @.str8 to i8*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then4, label %if.else4
if.then4:
	%11 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %11)
	call void @exit(i32 1)
	br label %if.else4
if.else4:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, i8* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%17 = load %class.B*, %class.B** %16, align 4
	%18 = icmp eq %class.B* %17, null
	br i1 %18, label %if.then5, label %if.else5
if.then5:
	%19 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%20 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %19)
	call void @exit(i32 1)
	br label %if.else5
if.else5:
	%21 = call i32 @_CN1B_FN2f1_(%class.B* %17)
	%22 = icmp eq %class.IO* %15, null
	br i1 %22, label %if.then6, label %if.else6
if.then6:
	%23 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%24 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %23)
	call void @exit(i32 1)
	br label %if.else6
if.else6:
	%25 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %15, i32 %21)
	%26 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%27 = load %class.IO*, %class.IO** %26, align 4
	%28 = bitcast [47 x i8]* @.str9 to i8*
	%29 = icmp eq %class.IO* %27, null
	br i1 %29, label %if.then7, label %if.else7
if.then7:
	%30 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%31 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %30)
	call void @exit(i32 1)
	br label %if.else7
if.else7:
	%32 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %27, i8* %28)
	%33 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%34 = load %class.IO*, %class.IO** %33, align 4
	%35 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
	%36 = load %class.B*, %class.B** %35, align 4
	%37 = icmp eq %class.B* %36, null
	br i1 %37, label %if.then8, label %if.else8
if.then8:
	%38 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%39 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %38)
	call void @exit(i32 1)
	br label %if.else8
if.else8:
	%40 = getelementptr inbounds %class.B, %class.B* %36, i32 0, i32 0
	%41 = call i32 @_CN1A_FN2f1_(%class.A* %40)
	%42 = icmp eq %class.IO* %34, null
	br i1 %42, label %if.then9, label %if.else9
if.then9:
	%43 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%44 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %43)
	call void @exit(i32 1)
	br label %if.else9
if.else9:
	%45 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %34, i32 %41)
	%46 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%47 = load %class.IO*, %class.IO** %46, align 4
	%48 = bitcast [58 x i8]* @.str10 to i8*
	%49 = icmp eq %class.IO* %47, null
	br i1 %49, label %if.then10, label %if.else10
if.then10:
	%50 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%51 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %50)
	call void @exit(i32 1)
	br label %if.else10
if.else10:
	%52 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %47, i8* %48)
	%53 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%54 = load %class.IO*, %class.IO** %53, align 4
	%55 = bitcast [47 x i8]* @.str11 to i8*
	%56 = icmp eq %class.IO* %54, null
	br i1 %56, label %if.then11, label %if.else11
if.then11:
	%57 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%58 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %57)
	call void @exit(i32 1)
	br label %if.else11
if.else11:
	%59 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %54, i8* %55)
	%60 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%61 = load %class.IO*, %class.IO** %60, align 4
	%62 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 7
	%63 = load %class.A*, %class.A** %62, align 4
	%64 = icmp eq %class.A* %63, null
	br i1 %64, label %if.then12, label %if.else12
if.then12:
	%65 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%66 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %65)
	call void @exit(i32 1)
	br label %if.else12
if.else12:
	%67 = call i32 @_CN1A_FN2f1_(%class.A* %63)
	%68 = icmp eq %class.IO* %61, null
	br i1 %68, label %if.then13, label %if.else13
if.then13:
	%69 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%70 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %69)
	call void @exit(i32 1)
	br label %if.else13
if.else13:
	%71 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %61, i32 %67)
	%72 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%73 = load %class.IO*, %class.IO** %72, align 4
	%74 = bitcast [58 x i8]* @.str12 to i8*
	%75 = icmp eq %class.IO* %73, null
	br i1 %75, label %if.then14, label %if.else14
if.then14:
	%76 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%77 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %76)
	call void @exit(i32 1)
	br label %if.else14
if.else14:
	%78 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %73, i8* %74)
	%79 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%80 = load %class.IO*, %class.IO** %79, align 4
	%81 = bitcast [47 x i8]* @.str13 to i8*
	%82 = icmp eq %class.IO* %80, null
	br i1 %82, label %if.then15, label %if.else15
if.then15:
	%83 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%84 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %83)
	call void @exit(i32 1)
	br label %if.else15
if.else15:
	%85 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %80, i8* %81)
	%86 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%87 = load %class.IO*, %class.IO** %86, align 4
	%88 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 8
	%89 = load %class.A*, %class.A** %88, align 4
	%90 = icmp eq %class.A* %89, null
	br i1 %90, label %if.then16, label %if.else16
if.then16:
	%91 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%92 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %91)
	call void @exit(i32 1)
	br label %if.else16
if.else16:
	%93 = call i32 @_CN1A_FN2f1_(%class.A* %89)
	%94 = icmp eq %class.IO* %87, null
	br i1 %94, label %if.then17, label %if.else17
if.then17:
	%95 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%96 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %95)
	call void @exit(i32 1)
	br label %if.else17
if.else17:
	%97 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %87, i32 %93)
	%98 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%99 = load %class.IO*, %class.IO** %98, align 4
	%100 = bitcast [47 x i8]* @.str14 to i8*
	%101 = icmp eq %class.IO* %99, null
	br i1 %101, label %if.then18, label %if.else18
if.then18:
	%102 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%103 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %102)
	call void @exit(i32 1)
	br label %if.else18
if.else18:
	%104 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %99, i8* %100)
	%105 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%106 = load %class.IO*, %class.IO** %105, align 4
	%107 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 11
	%108 = load %class.A*, %class.A** %107, align 4
	%109 = icmp eq %class.A* %108, null
	br i1 %109, label %if.then19, label %if.else19
if.then19:
	%110 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%111 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %110)
	call void @exit(i32 1)
	br label %if.else19
if.else19:
	%112 = call i32 @_CN1A_FN2f1_(%class.A* %108)
	%113 = icmp eq %class.IO* %106, null
	br i1 %113, label %if.then20, label %if.else20
if.then20:
	%114 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%115 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %114)
	call void @exit(i32 1)
	br label %if.else20
if.else20:
	%116 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %106, i32 %112)
	%117 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%118 = load %class.IO*, %class.IO** %117, align 4
	%119 = bitcast [58 x i8]* @.str15 to i8*
	%120 = icmp eq %class.IO* %118, null
	br i1 %120, label %if.then21, label %if.else21
if.then21:
	%121 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%122 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %121)
	call void @exit(i32 1)
	br label %if.else21
if.else21:
	%123 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %118, i8* %119)
	%124 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%125 = load %class.IO*, %class.IO** %124, align 4
	%126 = bitcast [47 x i8]* @.str16 to i8*
	%127 = icmp eq %class.IO* %125, null
	br i1 %127, label %if.then22, label %if.else22
if.then22:
	%128 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%129 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %128)
	call void @exit(i32 1)
	br label %if.else22
if.else22:
	%130 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %125, i8* %126)
	%131 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%132 = load %class.IO*, %class.IO** %131, align 4
	%133 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 10
	%134 = load %class.F*, %class.F** %133, align 4
	%135 = icmp eq %class.F* %134, null
	br i1 %135, label %if.then23, label %if.else23
if.then23:
	%136 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%137 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %136)
	call void @exit(i32 1)
	br label %if.else23
if.else23:
	%138 = getelementptr inbounds %class.F, %class.F* %134, i32 0, i32 0
	%139 = getelementptr inbounds %class.B, %class.B* %138, i32 0, i32 0
	%140 = call i32 @_CN1A_FN2f1_(%class.A* %139)
	%141 = icmp eq %class.IO* %132, null
	br i1 %141, label %if.then24, label %if.else24
if.then24:
	%142 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%143 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %142)
	call void @exit(i32 1)
	br label %if.else24
if.else24:
	%144 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %132, i32 %140)
	%145 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%146 = load %class.IO*, %class.IO** %145, align 4
	%147 = bitcast [47 x i8]* @.str17 to i8*
	%148 = icmp eq %class.IO* %146, null
	br i1 %148, label %if.then25, label %if.else25
if.then25:
	%149 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%150 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %149)
	call void @exit(i32 1)
	br label %if.else25
if.else25:
	%151 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %146, i8* %147)
	%152 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%153 = load %class.IO*, %class.IO** %152, align 4
	%154 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 10
	%155 = load %class.F*, %class.F** %154, align 4
	%156 = icmp eq %class.F* %155, null
	br i1 %156, label %if.then26, label %if.else26
if.then26:
	%157 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%158 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %157)
	call void @exit(i32 1)
	br label %if.else26
if.else26:
	%159 = call i32 @_CN1F_FN2f1_(%class.F* %155)
	%160 = icmp eq %class.IO* %153, null
	br i1 %160, label %if.then27, label %if.else27
if.then27:
	%161 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%162 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %161)
	call void @exit(i32 1)
	br label %if.else27
if.else27:
	%163 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %153, i32 %159)
	%164 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%165 = load %class.IO*, %class.IO** %164, align 4
	%166 = bitcast [13 x i8]* @.str18 to i8*
	%167 = icmp eq %class.IO* %165, null
	br i1 %167, label %if.then28, label %if.else28
if.then28:
	%168 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%169 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %168)
	call void @exit(i32 1)
	br label %if.else28
if.else28:
	%170 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %165, i8* %166)
	ret i32 0
}

define i32 @_CN1C_FN20staticDispatchOnNull_(%class.C* %this ) {
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 4
	%1 = load %class.B*, %class.B** %0, align 4
	%2 = icmp eq %class.B* %1, null
	br i1 %2, label %if.then29, label %if.else29
if.then29:
	%3 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%4 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %3)
	call void @exit(i32 1)
	br label %if.else29
if.else29:
	%5 = call i32 @_CN1B_FN2f1_(%class.B* %1)
	ret i32 %5
}

define i32 @_CN1C_FN11checkConsts_(%class.C* %this ) {
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [73 x i8]* @.str19 to i8*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then30, label %if.else30
if.then30:
	%4 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %4)
	call void @exit(i32 1)
	br label %if.else30
if.else30:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, i8* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [7 x i8]* @.str20 to i8*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then31, label %if.else31
if.then31:
	%11 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %11)
	call void @exit(i32 1)
	br label %if.else31
if.else31:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, i8* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = icmp eq %class.IO* %15, null
	br i1 %16, label %if.then32, label %if.else32
if.then32:
	%17 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%18 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %17)
	call void @exit(i32 1)
	br label %if.else32
if.else32:
	%19 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %15, i32 1)
	%20 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%21 = load %class.IO*, %class.IO** %20, align 4
	%22 = bitcast [11 x i8]* @.str21 to i8*
	%23 = icmp eq %class.IO* %21, null
	br i1 %23, label %if.then33, label %if.else33
if.then33:
	%24 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%25 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %24)
	call void @exit(i32 1)
	br label %if.else33
if.else33:
	%26 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %21, i8* %22)
	%27 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%28 = load %class.IO*, %class.IO** %27, align 4
	%29 = bitcast [6 x i8]* @.str22 to i8*
	%30 = icmp eq %class.IO* %28, null
	br i1 %30, label %if.then34, label %if.else34
if.then34:
	%31 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%32 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %31)
	call void @exit(i32 1)
	br label %if.else34
if.else34:
	%33 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %28, i8* %29)
	%34 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%35 = load %class.IO*, %class.IO** %34, align 4
	%36 = bitcast [9 x i8]* @.str23 to i8*
	%37 = icmp eq %class.IO* %35, null
	br i1 %37, label %if.then35, label %if.else35
if.then35:
	%38 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%39 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %38)
	call void @exit(i32 1)
	br label %if.else35
if.else35:
	%40 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %35, i8* %36)
	%41 = trunc i8 1 to i1
	br i1 %41, label %if.then36, label %if.else36
if.then36:
	%42 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%43 = load %class.IO*, %class.IO** %42, align 4
	%44 = bitcast [5 x i8]* @.str24 to i8*
	%45 = icmp eq %class.IO* %43, null
	br i1 %45, label %if.then37, label %if.else37
if.then37:
	%46 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%47 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %46)
	call void @exit(i32 1)
	br label %if.else37
if.else37:
	%48 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %43, i8* %44)
	br label %if.end36
if.else36:
	%49 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%50 = load %class.IO*, %class.IO** %49, align 4
	%51 = bitcast [6 x i8]* @.str25 to i8*
	%52 = icmp eq %class.IO* %50, null
	br i1 %52, label %if.then38, label %if.else38
if.then38:
	%53 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%54 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %53)
	call void @exit(i32 1)
	br label %if.else38
if.else38:
	%55 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %50, i8* %51)
	br label %if.end36
if.end36:
	%56 = phi %class.IO* [%48, %if.else37], [%55, %if.else38]
	%57 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%58 = load %class.IO*, %class.IO** %57, align 4
	%59 = bitcast [13 x i8]* @.str26 to i8*
	%60 = icmp eq %class.IO* %58, null
	br i1 %60, label %if.then39, label %if.else39
if.then39:
	%61 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%62 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %61)
	call void @exit(i32 1)
	br label %if.else39
if.else39:
	%63 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %58, i8* %59)
	ret i32 0
}

define %class.Object* @_CN1C_FN18checkDefaultValues_(%class.C* %this ) {
entry:
	%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%1 = load %class.IO*, %class.IO** %0, align 4
	%2 = bitcast [38 x i8]* @.str27 to i8*
	%3 = icmp eq %class.IO* %1, null
	br i1 %3, label %if.then40, label %if.else40
if.then40:
	%4 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%5 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %4)
	call void @exit(i32 1)
	br label %if.else40
if.else40:
	%6 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %1, i8* %2)
	%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%8 = load %class.IO*, %class.IO** %7, align 4
	%9 = bitcast [7 x i8]* @.str28 to i8*
	%10 = icmp eq %class.IO* %8, null
	br i1 %10, label %if.then41, label %if.else41
if.then41:
	%11 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%12 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %11)
	call void @exit(i32 1)
	br label %if.else41
if.else41:
	%13 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %8, i8* %9)
	%14 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%15 = load %class.IO*, %class.IO** %14, align 4
	%16 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
	%17 = load i32, i32* %16, align 4
	%18 = icmp eq %class.IO* %15, null
	br i1 %18, label %if.then42, label %if.else42
if.then42:
	%19 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%20 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %19)
	call void @exit(i32 1)
	br label %if.else42
if.else42:
	%21 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %15, i32 %17)
	%22 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%23 = load %class.IO*, %class.IO** %22, align 4
	%24 = bitcast [11 x i8]* @.str29 to i8*
	%25 = icmp eq %class.IO* %23, null
	br i1 %25, label %if.then43, label %if.else43
if.then43:
	%26 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%27 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %26)
	call void @exit(i32 1)
	br label %if.else43
if.else43:
	%28 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %23, i8* %24)
	%29 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%30 = load %class.IO*, %class.IO** %29, align 4
	%31 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 3
	%32 = load i8*, i8** %31, align 4
	%33 = icmp eq %class.IO* %30, null
	br i1 %33, label %if.then44, label %if.else44
if.then44:
	%34 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%35 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %34)
	call void @exit(i32 1)
	br label %if.else44
if.else44:
	%36 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %30, i8* %32)
	%37 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%38 = load %class.IO*, %class.IO** %37, align 4
	%39 = bitcast [9 x i8]* @.str30 to i8*
	%40 = icmp eq %class.IO* %38, null
	br i1 %40, label %if.then45, label %if.else45
if.then45:
	%41 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%42 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %41)
	call void @exit(i32 1)
	br label %if.else45
if.else45:
	%43 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %38, i8* %39)
	%44 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
	%45 = load i8, i8* %44, align 4
	%46 = trunc i8 %45 to i1
	br i1 %46, label %if.then46, label %if.else46
if.then46:
	%47 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%48 = load %class.IO*, %class.IO** %47, align 4
	%49 = bitcast [5 x i8]* @.str31 to i8*
	%50 = icmp eq %class.IO* %48, null
	br i1 %50, label %if.then47, label %if.else47
if.then47:
	%51 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%52 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %51)
	call void @exit(i32 1)
	br label %if.else47
if.else47:
	%53 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %48, i8* %49)
	br label %if.end46
if.else46:
	%54 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%55 = load %class.IO*, %class.IO** %54, align 4
	%56 = bitcast [6 x i8]* @.str32 to i8*
	%57 = icmp eq %class.IO* %55, null
	br i1 %57, label %if.then48, label %if.else48
if.then48:
	%58 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%59 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %58)
	call void @exit(i32 1)
	br label %if.else48
if.else48:
	%60 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %55, i8* %56)
	br label %if.end46
if.end46:
	%61 = phi %class.IO* [%53, %if.else47], [%60, %if.else48]
	%62 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 6
	%63 = load %class.IO*, %class.IO** %62, align 4
	%64 = bitcast [42 x i8]* @.str33 to i8*
	%65 = icmp eq %class.IO* %63, null
	br i1 %65, label %if.then49, label %if.else49
if.then49:
	%66 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%67 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %66)
	call void @exit(i32 1)
	br label %if.else49
if.else49:
	%68 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %63, i8* %64)
	%69 = bitcast %class.IO* %68 to %class.Object*
	ret %class.Object* %69
}

define i32 @_CN4Main_FN4main_(%class.Main* %this ) {
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%1 = load %class.C*, %class.C** %0, align 4
	%2 = icmp eq %class.C* %1, null
	br i1 %2, label %if.then50, label %if.else50
if.then50:
	%3 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%4 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %3)
	call void @exit(i32 1)
	br label %if.else50
if.else50:
	%5 = call %class.Object* @_CN1C_FN18checkDefaultValues_(%class.C* %1)
	%6 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%7 = load %class.C*, %class.C** %6, align 4
	%8 = icmp eq %class.C* %7, null
	br i1 %8, label %if.then51, label %if.else51
if.then51:
	%9 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%10 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %9)
	call void @exit(i32 1)
	br label %if.else51
if.else51:
	%11 = call i32 @_CN1C_FN18testStaticDispatch_(%class.C* %7)
	%12 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%13 = load %class.C*, %class.C** %12, align 4
	%14 = icmp eq %class.C* %13, null
	br i1 %14, label %if.then52, label %if.else52
if.then52:
	%15 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%16 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %15)
	call void @exit(i32 1)
	br label %if.else52
if.else52:
	%17 = call i32 @_CN1C_FN11checkConsts_(%class.C* %13)
	%18 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%19 = load %class.C*, %class.C** %18, align 4
	%20 = icmp eq %class.C* %19, null
	br i1 %20, label %if.then53, label %if.else53
if.then53:
	%21 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%22 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %21)
	call void @exit(i32 1)
	br label %if.else53
if.else53:
	%23 = call %class.A* @_CN1C_FN26ifWithDifferentReturnTypes_(%class.C* %19, i8 1)
	%24 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store %class.A* %23, %class.A** %24, align 4
	%25 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%26 = load %class.C*, %class.C** %25, align 4
	%27 = icmp eq %class.C* %26, null
	br i1 %27, label %if.then54, label %if.else54
if.then54:
	%28 = bitcast [25 x i8]* @Abortdisvoid to i8*
	%29 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, i8* %28)
	call void @exit(i32 1)
	br label %if.else54
if.else54:
	%30 = call i32 @_CN1C_FN20staticDispatchOnNull_(%class.C* %26)
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
@.str.B = private unnamed_addr constant [2 x i8] c"B\00", align 1
@.str.E = private unnamed_addr constant [2 x i8] c"E\00", align 1
@.str.F = private unnamed_addr constant [2 x i8] c"F\00", align 1
@.str.C = private unnamed_addr constant [2 x i8] c"C\00", align 1
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
@.str6 = private unnamed_addr constant [1 x i8] c"\00", align 1

@.str7 = private unnamed_addr constant [90 x i8] c"Calling static dispatch on f1(), member of A gives 0, member of B gives 1 np member in F
\00", align 1

@.str8 = private unnamed_addr constant [46 x i8] c"Dynamic type B, static type B, called on B : \00", align 1

@.str9 = private unnamed_addr constant [47 x i8] c"
Dynamic type B, static type B, called on A : \00", align 1

@.str10 = private unnamed_addr constant [58 x i8] c"
Dynamic type B, static type A, called on B : Not allowed\00", align 1

@.str11 = private unnamed_addr constant [47 x i8] c"
Dynamic type B, static type A, called on A : \00", align 1

@.str12 = private unnamed_addr constant [58 x i8] c"
Dynamic type A, static type A, called on B : Not allowed\00", align 1

@.str13 = private unnamed_addr constant [47 x i8] c"
Dynamic type A, static type A, called on A : \00", align 1

@.str14 = private unnamed_addr constant [47 x i8] c"
Dynamic type F, static type A, called on A : \00", align 1

@.str15 = private unnamed_addr constant [58 x i8] c"
Dynamic type F, static type A, called on F : Not allowed\00", align 1

@.str16 = private unnamed_addr constant [47 x i8] c"
Dynamic type F, static type F, called on A : \00", align 1

@.str17 = private unnamed_addr constant [47 x i8] c"
Dynamic type F, static type F, called on F : \00", align 1

@.str18 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str19 = private unnamed_addr constant [73 x i8] c"Displaying consts 1 and 'Hello', and test with if for Bool const 'true'
\00", align 1

@.str20 = private unnamed_addr constant [7 x i8] c"Int : \00", align 1

@.str21 = private unnamed_addr constant [11 x i8] c"
String : \00", align 1

@.str22 = private unnamed_addr constant [6 x i8] c"Hello\00", align 1

@.str23 = private unnamed_addr constant [9 x i8] c"
Bool : \00", align 1

@.str24 = private unnamed_addr constant [5 x i8] c"true\00", align 1

@.str25 = private unnamed_addr constant [6 x i8] c"false\00", align 1

@.str26 = private unnamed_addr constant [13 x i8] c"
Completed

\00", align 1

@.str27 = private unnamed_addr constant [38 x i8] c"Printing default values of each type
\00", align 1

@.str28 = private unnamed_addr constant [7 x i8] c"Int : \00", align 1

@.str29 = private unnamed_addr constant [11 x i8] c"
String : \00", align 1

@.str30 = private unnamed_addr constant [9 x i8] c"
Bool : \00", align 1

@.str31 = private unnamed_addr constant [5 x i8] c"True\00", align 1

@.str32 = private unnamed_addr constant [6 x i8] c"False\00", align 1

@.str33 = private unnamed_addr constant [42 x i8] c"
Other class without new, here class B : \00", align 1

