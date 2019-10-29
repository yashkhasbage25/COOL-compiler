%class.Object = type {i8*}
%class.Int = type { %class.Object}
%class.String = type { %class.Object}
%class.Bool = type { %class.Object}
%class.IO = type { %class.Object}
%class.B = type { %class.Object ,i8* ,i8 ,i32}
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
define void @_CN1B_FN1B_(%class.B %this ) {
}
define void @_CN4Main_FN4Main_(%class.Main %this ) {
}
