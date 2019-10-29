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
define void @_CN4Main_FN4Main_(%class.Main* %this) {
 entry:
 %0 = bitcast %class.Main* %this to %class.Object*
 call void @_CN6Object_FN6Object_(%class.Object* %0)
 ret void}
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

define i32 @_CN4Main_FN4main_(%class.Main* %this) {entry:
ret i32 0
}
%class.Object = type {i8*}
%class.Int = type { %class.Object}
%class.String = type { %class.Object}
%class.Bool = type { %class.Object}
%class.IO = type { %class.Object}
%class.A = type { %class.Object ,i32}
%class.B = type { %class.A ,i32 ,i32}
%class.C = type { %class.B ,i32 ,i32 ,i32 ,[1024 x i8] ,%class.IO ,%class.Object}
%class.Main = type { %class.Object ,%class.C}
define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {
entry:
	call void @exit( i32 1 )
	ret %class.Object* null
}

define i8* @CN6Object_FN9type_name(%class.Object* %this) {
	entry:	%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 0	%1 = load i8*, i8** %0, align 8	ret i8* %1}
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

define i32 @_CN1C_FN15testIOFunctions_([1024 x i8] %s, i32 %c){
entry:
%0 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 3
store [1024 x i8] %0, [1024 x i8]* %1, align 4
%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
store i32 %1, i32* %2, align 4
%2 = alloca i32, align 4
store i32 0, i32* %2, align 4
%3 = load i32, i32* %2, align 4
ret i32 %3
}

define i32 @_CN1C_FN22useInheritedAttributes_(){
entry:
%0 = alloca i32, align 4
store i32 0, i32* %0, align 4
%1 = load i32, i32* %0, align 4
ret i32 %1
}

define i32 @_CN1C_FN19testStringFunctions_([1024 x i8] %s1, [1024 x i8] %s2, i32 %x, i32 %l){
entry:
%0 = alloca i32, align 4
store i32 0, i32* %0, align 4
%1 = load i32, i32* %0, align 4
ret i32 %1
}

define i32 @_CN1C_FN19testObjectFunctions_(){
entry:
%0 = alloca i32, align 4
store i32 0, i32* %0, align 4
%1 = load i32, i32* %0, align 4
ret i32 %1
}

define void @_CN1A_FN1A_(%class.A* %this ) {
entry:
%0 = bitcast %class.A* %this to %class.Object*
call void @_CN6Object_FN6Object_(%class.Object* %0)
%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 0
%2 = alloca i32, align 4
store i32 1, i32* %2, align 4
%3 = load i32, i32* %2, align 4
store i32 %3, i32* %2, align 4
ret void
}
define void @_CN1B_FN1B_(%class.B* %this ) {
entry:
%0 = bitcast %class.B* %this to %class.A*
call void @_CN1A_FN1A_(%class.A* %0)
%1 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 0
%2 = alloca i32, align 4
store i32 1, i32* %2, align 4
%3 = load i32, i32* %2, align 4
store i32 %3, i32* %2, align 4
%4 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 1
%5 = alloca i32, align 4
store i32 0, i32* %5, align 4
%6 = load i32, i32* %5, align 4
store i32 %6, i32* %5, align 4
ret void
}
define void @_CN1C_FN1C_(%class.C* %this ) {
entry:
%0 = bitcast %class.C* %this to %class.B*
call void @_CN1B_FN1B_(%class.B* %0)
%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 0
%2 = alloca i32, align 4
store i32 1, i32* %2, align 4
%3 = load i32, i32* %2, align 4
store i32 %3, i32* %2, align 4
%4 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 1
%5 = alloca i32, align 4
store i32 0, i32* %5, align 4
%6 = load i32, i32* %5, align 4
store i32 %6, i32* %5, align 4
%7 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 2
%8 = alloca i32, align 4
store i32 3, i32* %8, align 4
%9 = load i32, i32* %8, align 4
store i32 %9, i32* %8, align 4
%10 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 3
%11 = alloca [1024 x i8], align 16
%12 = getelementptr inbounds [1024 x i8], [1024 x i8]* %11, i32 0, i32 0
%13 = call i8* @strcpy(i8* %12, i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.0, i32 0, i32 0))
%14 = load [1024 x i8], [1024 x i8]* %11, align 16
store [1024 x i8] %14, [1024 x i8]* %11, align 4
%15 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 4
%16 = alloca %class.IO*, align 4
store %class.IO %16, %class.IO* %16, align 4
%17 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 5
%18 = alloca %class.Object*, align 4
store %class.Object %18, %class.Object* %18, align 4
ret void
}
define i32 @main() {
entry:
%main = alloca %class.Main, align 8
call void @_CN4Main_FN4Main_(%class.Main* %main)
%retval = call i32 @_CN4Main_FN4main_(%class.Main* %main)
ret i32 %retval
}
@.str.0 = private unnamed_addr constant [6 x i8] c"Hello\00", align 1
