%class.Object = type {i8*}
%class.Int = type { %class.Object}
%class.String = type { %class.Object}
%class.Bool = type { %class.Object}
%class.IO = type { %class.Object}
%class.D = type { %class.Object ,i32}
%class.A = type { %class.D ,i32 ,i32}
%class.B = type { %class.A ,i8* ,i8 ,i32 ,%class.D ,i32 ,i32 ,i32}
%class.C = type { %class.B ,i8* ,i8 ,i32 ,%class.D ,i32 ,i32 ,i32}
%class.Main = type { %class.Object}
define %class.Object* @_CN6Object_FN5abort_( %class.Object* %this ) noreturn {
entry:
	call void @exit( i32 1 )
	ret %class.Object* null
}

define [1024 x i8]* @_CN6Object_FN9type_name_( %class.Object* %this ) {
entry:
	%0 = getelementptr inbounds %class.Object, %class.Object* %this, i32 0, i32 1
	%1 = load [1024 x i8]*, [1024 x i8]** %0
	%retval = call [1024 x i8]* @_CN6String_FN4copy_( [1024 x i8]* %1 )
	ret [1024 x i8]* %retval
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

define i32 @_CN1B_FN2f1_(i32 %x, ){
}

define i32 @_CN1C_FN2f1_(i32 %x, ){
}

define i32 @_CN4Main_FN4main_(){
}

define void @_CN6Object_FN6Object_(%class.Object %this ) {
}
define void @_CN3Int_FN3Int_(i32 %this ) {
	%0 = bitcast %class.Int* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
define void @_CN6String_FN6String_(i8* %this ) {
	%0 = bitcast %class.String* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
define void @_CN4Bool_FN4Bool_(i8 %this ) {
	%0 = bitcast %class.Bool* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
define void @_CN2IO_FN2IO_(%class.IO %this ) {
	%0 = bitcast %class.IO* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
define void @_CN1D_FN1D_(%class.D %this ) {
	%0 = bitcast %class.D* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
define void @_CN1A_FN1A_(%class.A %this ) {
	%0 = bitcast %class.A* %this to %class.D*
	call void @_CN1D_FN1D_(%class.D* %0
}
define void @_CN1B_FN1B_(%class.B %this ) {
	%0 = bitcast %class.B* %this to %class.A*
	call void @_CN1A_FN1A_(%class.A* %0
	%1 = getelementptr inbounds %class.B,%class.B* %this, i32 0, i32 0
	store %class.D null, %class.D* %1, align 4
}
define void @_CN1C_FN1C_(%class.C %this ) {
	%0 = bitcast %class.C* %this to %class.B*
	call void @_CN1B_FN1B_(%class.B* %0
	%1 = getelementptr inbounds %class.C,%class.C* %this, i32 0, i32 0
	store %class.D null, %class.D* %1, align 4
}
define void @_CN4Main_FN4Main_(%class.Main %this ) {
	%0 = bitcast %class.Main* %this to %class.Object*
	call void @_CN6Object_FN6Object_(%class.Object* %0
}
