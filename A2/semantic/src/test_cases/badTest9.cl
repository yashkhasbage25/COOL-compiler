class A{

};
class B inherits A{

};
class C{
	f1(): B{
		new B
	};
	f2(): B{
		new A
	};
	f3(): A{
		new B
	};
};
class Main {
    main() : Int {
        0
    };
};