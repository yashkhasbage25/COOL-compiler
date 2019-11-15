class A {
	a : Int;
};

class B {
	a : A <- new A;
};

class C inherits A {
	c : Int;
};

class Main inherits IO {
	io : IO <- new IO;
	b : B <- new B;
	c : C <- new C;
	main() : Int {{
		io@IO.out_string(io@Object.type_name());
		io@IO.out_string(b@Object.type_name());
		io@IO.out_string("\n");
		io@IO.out_string(c@Object.type_name());
		io@IO.out_string("\n");
		2;
	}};
};
