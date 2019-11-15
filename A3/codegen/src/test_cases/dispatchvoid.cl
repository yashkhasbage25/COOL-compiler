class A {
	a : Int;
	f1() : Int {
		0
	};
};

class Main {
	a : A;
	main() : Int {
		a@A.f1()
	};
};
