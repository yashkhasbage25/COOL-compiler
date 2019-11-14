class Main {

	i : Int;
	io : IO;
	main() : Int {{
		io <- new IO;
		while i < 10 loop
		{
			io@IO.out_int(i);
			io@IO.out_string("\n");
			i <- i+1;
		}
		pool;
        0;
	}};

};
