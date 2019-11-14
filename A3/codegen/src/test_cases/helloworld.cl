class Main inherits IO {
	io : IO <- new IO;
	main() : Int {{
		io@IO.out_string("Hello world!\n\n");
		io@IO.out_string(io@Object.type_name());
		-- io@IO.out_string("\n");
		2;
	}};
};
