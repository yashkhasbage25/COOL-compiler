class A{
	a:Int;
};
class B inherits A{
	b: Int;
};
class C{
	b : B <- new B;
	f(): A{
		b
	};
};
class Main {
    main() : Int {
        0
    };
};