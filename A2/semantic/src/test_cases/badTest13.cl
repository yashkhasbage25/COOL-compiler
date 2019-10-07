class A{
	a:Int;
};
class B inherits A{
	b:Int;
};
class C {
	a: A <- new A;
	f():Int{
		let x : B <- a in {
			0;
		}
	};
};
class Main {
    main() : Int {
        0
    };
};