class A {
	a : Int;
};

class B {
	a : A <- new A;
};

class Main inherits IO {
	io : IO <- new IO;
	b : B <- new B;
	main() : Int {{
		-- io@IO.out_string("Hello world!\n\n");
		-- io@IO.out_string(io@Object.type_name());
		io@IO.out_string(b@Object.type_name());
		-- io@IO.out_string("\n");
		2;
	}};
};
