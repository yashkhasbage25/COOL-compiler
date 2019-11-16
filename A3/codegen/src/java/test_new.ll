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
%class.Main = type {	%class.IO ,i32 ,i32 ,i8* ,%class.A* ,%class.A* ,i8* ,%class.B* ,%class.A*}
%class.A = type {	%class.Object ,i32}
%class.B = type {	%class.Object ,%class.A*}
define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {
entry:
	call void @exit( i32 1 )
	ret %class.Object* null
}

define i8* @_CN6Object_FN9type_name_(%class.Object* %this) {
	entry:
	%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0
	%1 = load i8*, i8** %0, align 8
	ret i8* %1
}
define i32 @_CN6String_FN6length_( i8* %this ) {
	entry:
	%0 = bitcast i8* %this to i8*
	%1 = call i64 @strlen( i8* %0 )
	%retval = trunc i64 %1 to i32
	ret i32 %retval
}

define i8* @_CN6String_FN6concat_( i8* %this, i8* %that ) {
entry:
	%retval = call i8* @_CN6String_FN4copy_( i8* %this )
	%0 = bitcast i8* %retval to i8*
	%1 = bitcast i8* %that to i8*
	%2 = call i8* @strcat( i8* %0, i8* %1 )
	ret i8* %retval
}

define i8* @_CN6String_FN4copy_( i8* %this ) {
entry:
	%0 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %0 to i8*
	%1 = bitcast i8* %this to i8*
	%2 = bitcast i8* %retval to i8*
	%3 = call i8* @strcpy( i8* %2, i8* %1)
	ret i8* %retval
}

define i8* @_CN6String_FN6substr_( i8* %this, i32 %start, i32 %len ) {
entry:
	%0 = getelementptr inbounds i8, i8* %this, i32 %start
	%1 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %1 to i8*
	%2 = bitcast i8* %retval to i8*
	%3 = call i8* @strncpy( i8* %2, i8* %0, i32 %len )
	%4 = getelementptr inbounds i8, i8* %retval, i32 %len
	store i8 0, i8* %4
	ret i8* %retval
}

define %class.IO* @_CN2IO_FN10out_string_( %class.IO* %this, i8* %str ) {
entry:
	%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), i8* %str )
	ret %class.IO* %this
}

define %class.IO* @_CN2IO_FN7out_int_( %class.IO* %this, i32 %int ) {
entry:
	%0 = call i32 (i8*, ...) @printf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32 %int )
	ret %class.IO* %this
}

define i8* @_CN2IO_FN9in_string_( %class.IO* %this ) {
entry:
	%0 = call i8* @malloc( i64 1024 )
	%retval = bitcast i8* %0 to i8*
	%1 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @strformatstr to i8* ), i8* %retval )
	ret i8* %retval
}

define i32 @_CN2IO_FN6in_int_( %class.IO* %this ) {
entry:
	%0 = call i8* @malloc( i64 4 )
	%1 = bitcast i8* %0 to i32*
	%2 = call i32 (i8*, ...) @scanf( i8* bitcast ( [3 x i8]* @intformatstr to i8* ), i32* %1 )
	%retval = load i32, i32* %1
	ret i32 %retval
}

define i32 @_CN4Main_FN4main_(%class.Main* %this ){
entry:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 8
	store wrong_expr_type. might be dynamic dispatch, %class.A** %0, align 4
	ret i32 0
}

define i32 @_CN1A_FN4setA_(%class.A* %this ,i32 %x){
entry:
	%x.addr = alloca i32, align 4
	store i32 %x, i32* %x.addr, align 4
	%0 = load i32, i32* %x.addr, align 4
	%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store i32 %0, i32* %1, align 4
	ret i32 %0
}

define i32 @_CN1A_FN4getA_(%class.A* %this ){
entry:
	%0 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	%1 = load i32, i32* %0, align 4
	ret i32 %1
}

define i32 @_CN1B_FN7setAVal_(%class.B* %this ){
entry:
	ret i32 0
}

define %class.A* @_CN1B_FN4getA_(%class.B* %this ){
entry:
	%0 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
	%1 = load %class.A*, %class.A** %0, align 4
	ret %class.A* %1
}

define void @_CN4Main_FN4Main_(%class.Main* %this ) {
entry:
	%0 = bitcast %class.Main* %this to %class.IO*
	call void @_CN2IO_FN2IO_(%class.IO* %0)
	%1 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store i32 0, i32* %2, align 4
	%3 = bitcast [1 x i8]* @.str3 to i8*
	%4 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 3
	store i8* %3, i8** %4, align 4
	%5 = getelementptr inbounds %class.Main,%class.Main* %this, i32 0, i32 4
	store %class.A* null, %class.A** %5, align 4
	%6 = call noalias i8* @malloc(i64 64)
	%7 = bitcast i8* %6 to %class.A*
	call void @_CN1A_FN1A_(%class.A*%7)
	%8 = bitcast %class.A* %7 to %class.Object*
	%9 = getelementptr inbounds %class.Object, %class.Object* %8, i32 0, i32 0
	%10 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.A, i32 0, i32 0
	store i8* %10, i8** %9, align 8
	%11 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 5
	store %class.A* %7, %class.A** %11, align 4
	%12 = bitcast [1 x i8]* @.str4 to i8*
	%13 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 6
	store i8* %12, i8** %13, align 4
	%14 = call noalias i8* @malloc(i64 64)
	%15 = bitcast i8* %14 to %class.B*
	call void @_CN1B_FN1B_(%class.B*%15)
	%16 = bitcast %class.B* %15 to %class.Object*
	%17 = getelementptr inbounds %class.Object, %class.Object* %16, i32 0, i32 0
	%18 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.B, i32 0, i32 0
	store i8* %18, i8** %17, align 8
	%19 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 7
	store %class.B* %15, %class.B** %19, align 4
	%20 = getelementptr inbounds %class.Main,%class.Main* %this, i32 0, i32 8
	store %class.A* null, %class.A** %20, align 4
	ret void
}
define void @_CN1A_FN1A_(%class.A* %this ) {
entry:
	%0 = bitcast %class.A* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	ret void
}
define void @_CN1B_FN1B_(%class.B* %this ) {
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
@.str.Main = private unnamed_addr constant [5 x i8] c"Main\00", align 1
@.str.A = private unnamed_addr constant [2 x i8] c"A\00", align 1
@.str.B = private unnamed_addr constant [2 x i8] c"B\00", align 1
@.str3 = private unnamed_addr constant [1 x i8] c"\00", align 1

@.str4 = private unnamed_addr constant [1 x i8] c"\00", align 1

