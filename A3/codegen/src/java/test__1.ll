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
define void @_CN6Object_FN6Object_(%class.Object %this ) {
}
define void @_CN3Int_FN3Int_(i32 %this ) {
}
define void @_CN6String_FN6String_(i8* %this ) {
}
define void @_CN4Bool_FN4Bool_(i8 %this ) {
}
define void @_CN2IO_FN2IO_(%class.IO %this ) {
}
define void @_CN1D_FN1D_(%class.D %this ) {
%1 = getelementptr inbounds %class.D, %class.D* %this, i32 0, i32 
%2 = alloca i32, align 4
store i32 0, i32* %2, align 4
%3 = load i32, i32* %2, align 4
store i32 %3, i32* %1 , align 4
}
define void @_CN1A_FN1A_(%class.A %this ) {
%1 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 
%2 = alloca i32, align 4
store i32 0, i32* %2, align 4
%3 = load i32, i32* %2, align 4
store i32 %3, i32* %1 , align 4
%4 = getelementptr inbounds %class.A, %class.A* %this, i32 0, i32 
%5 = alloca i32, align 4
store i32 0, i32* %5, align 4
%6 = load i32, i32* %5, align 4
store i32 %6, i32* %4 , align 4
}
define void @_CN1B_FN1B_(%class.B %this ) {
%1 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%2 = alloca [1024 x i8], align 16
%3 = getelementptr inbounds [1024 x i8], [1024 x i8]* %2, i32 0, i32 0
%4 = call i8* @strcpy(i8* %3, i8* getelementptr inbounds ([1 x i8], [1 x i8]* @.str.0, i32 0, i32 0))
%5 = load [1024 x i8], [1024 x i8]* %2, align 16
store i8* %5, i8** %1 , align 4
%6 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%7 = alloca i32, align 1
store i32 0, i32* %7, align 4
%8 = load i32, i32* %7, align 4
store i8 %8, i8* %6 , align 4
%9 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%10 = alloca i32, align 4
store i32 0, i32* %10, align 4
%11 = load i32, i32* %10, align 4
store i32 %11, i32* %9 , align 4
	%11 = getelementptr inbounds %class.B,%class.B* %this, i32 0, i32 0
	store %class.D null, %class.D* %11, align 4
%13 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%14 = alloca i32, align 4
store i32 0, i32* %14, align 4
%15 = load i32, i32* %14, align 4
store i32 %15, i32* %13 , align 4
%16 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%17 = alloca i32, align 4
store i32 0, i32* %17, align 4
%18 = load i32, i32* %17, align 4
store i32 %18, i32* %16 , align 4
%19 = getelementptr inbounds %class.B, %class.B* %this, i32 0, i32 
%20 = alloca i32, align 4
store i32 0, i32* %20, align 4
%21 = load i32, i32* %20, align 4
store i32 %21, i32* %19 , align 4
}
define void @_CN1C_FN1C_(%class.C %this ) {
%1 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%2 = alloca [1024 x i8], align 16
%3 = getelementptr inbounds [1024 x i8], [1024 x i8]* %2, i32 0, i32 0
%4 = call i8* @strcpy(i8* %3, i8* getelementptr inbounds ([1 x i8], [1 x i8]* @.str.1, i32 0, i32 0))
%5 = load [1024 x i8], [1024 x i8]* %2, align 16
store i8* %5, i8** %1 , align 4
%6 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%7 = alloca i32, align 1
store i32 0, i32* %7, align 4
%8 = load i32, i32* %7, align 4
store i8 %8, i8* %6 , align 4
%9 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%10 = alloca i32, align 4
store i32 0, i32* %10, align 4
%11 = load i32, i32* %10, align 4
store i32 %11, i32* %9 , align 4
	%11 = getelementptr inbounds %class.C,%class.C* %this, i32 0, i32 0
	store %class.D null, %class.D* %11, align 4
%13 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%14 = alloca i32, align 4
store i32 0, i32* %14, align 4
%15 = load i32, i32* %14, align 4
store i32 %15, i32* %13 , align 4
%16 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%17 = alloca i32, align 4
store i32 0, i32* %17, align 4
%18 = load i32, i32* %17, align 4
store i32 %18, i32* %16 , align 4
%19 = getelementptr inbounds %class.C, %class.C* %this, i32 0, i32 
%20 = alloca i32, align 4
store i32 0, i32* %20, align 4
%21 = load i32, i32* %20, align 4
store i32 %21, i32* %19 , align 4
}
define void @_CN4Main_FN4Main_(%class.Main %this ) {
}
