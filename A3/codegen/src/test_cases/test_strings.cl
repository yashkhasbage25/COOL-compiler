class B {
    b : String;

    setB(c : String) : Int {{
        b <- c;
        c <- b;
        0;
    }};

    getB() : String {
        b
    };
};

class Main {
    a : String <- "hello";
    io : IO <- new IO;
    b : B <- new B;
    main() : Object {{
        io@IO.out_string(a);
        a <- "asdfgh";
        b@B.setB(a);
        io@IO.out_string("\n");
        io@IO.out_string(b@B.getB());
        0;
    }};
};
