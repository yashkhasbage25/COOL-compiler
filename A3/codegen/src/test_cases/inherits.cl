class A {
    a : Int;
    b : String;
    io : IO <- new IO;

    print(str : String) : IO {
        io@IO.out_string(str)
    };
};

class B inherits A {
    c : Int;
    d : Bool;
    setB (str : String) : String {{
        b <- str;
    }};

    getB() : String {
        b
    };
};

class Main {
    b : B <- new B;
    main() : Int {{
        b@B.setB("hello");
        b@A.print(b@B.getB());
        0;
    }};
};
