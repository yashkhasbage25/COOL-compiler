target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"
define void @_CN6Object_FN6Object_(%class.Object* %this) {
entry:	
ret void
}
define i32 @_CN2IO_FN2IO_(%class.IO* %this) {
entry:
	%0 = bitcast %class.IO* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	ret i32 0
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
%class.Main = type {	%class.Object ,i32 ,%class.IO*}
define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {
entry:
	call void @exit( i32 1 )
	ret %class.Object* null
}

define i8* @CN6Object_FN9type_name(%class.Object* %this) {
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

define %class.Object* @_CN4Main_FN4main_(%class.Main* %this ){
entry:
	br label %loop.cond0
loop.cond0:
	%0 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%1 = load i32, i32* %0, align 4
	%2 = icmp slt i32 %1, 10
	br i1 %2, label %loop.body0 , label %loop.end0
loop.body0:
	%3 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%4 = load %class.IO*, %class.IO** %3, align 4
	%5 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%6 = load i32, i32* %5, align 4
	%7 = icmp eq %class.IO* %4, null
	br i1 %7, label %if.then0, label %if.else0
if.then0:
	%8 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%9 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %8)
	call void @exit(i32 1)
	br label %if.else0
if.else0:
	%10 = call %class.IO* @_CN2IO_FN7out_int_(%class.IO* %4, i32 %6)
	%11 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	%12 = load %class.IO*, %class.IO** %11, align 4
	%13 = bitcast [2 x i8]* @.str0 to [1024 x i8]*
	%14 = icmp eq %class.IO* %12, null
	br i1 %14, label %if.then1, label %if.else1
if.then1:
	%15 = bitcast [25 x i8]* @Abortdisvoid to [1024 x i8]*
	%16 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* null, [1024 x i8]* %15)
	call void @exit(i32 1)
	br label %if.else1
if.else1:
	%17 = call %class.IO* @_CN2IO_FN10out_string_(%class.IO* %12, [1024 x i8]* %13)
	%18 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	%19 = load i32, i32* %18, align 4
	%20 = add nsw i32 %19, 1
	%21 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 %20, i32* %21, align 4
	br label %loop.cond0
loop.end0:
	%22 = call noalias i8* @malloc(i64 8)
	%23 = bitcast i8* %22 to %class.Object*
	ret %class.Object* %23
}

define void @_CN4Main_FN4Main_(%class.Main* %this ) {
entry:
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0)
	%1 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 1
	store i32 0, i32* %1, align 4
	%2 = call noalias i8* @malloc(i64 16)
	%3 = bitcast i8* %2 to %class.IO*
	%4 = call i32 @_CN2IO_FN2IO_(%class.IO* %3)
	%5 = getelementptr inbounds %class.Main, %class.Main* %this, i32 0, i32 2
	store %class.IO* %3, %class.IO** %5, align 4
ret void
}
define i32 @main() {
entry:
	%main = alloca %class.Main, align 8
	call void @_CN4Main_FN4Main_(%class.Main* %main)
	%retval = call %class.Object* @_CN4Main_FN4main_(%class.Main* %main)
	ret i32 0
}
@.str0 = private unnamed_addr constant [2 x i8] c"
\00", align 1

