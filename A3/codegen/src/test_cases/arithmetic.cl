class A {
    io : IO <- new IO;

    print(a : Int, b : Int) : Object {{
        io@IO.out_int(a);
        io@IO.out_string(" ");
        io@IO.out_int(b);
        io@IO.out_string("\n");
    }};
};

class Main {
    a : Int;
    b : Int;
    io : IO <- new IO;
    printer : A <- new A;

    main() : Int {{
        a <- 4;
        b <- 5;
        io@IO.out_string("\n");
        a <- a + b;
        printer@A.print(a, b);
        a <- a / b;
        printer@A.print(a, b);
        a <- a - b;
        printer@A.print(a, b);
        a <- a * b;
        printer@A.print(a, b);
        0;
    }};
};
