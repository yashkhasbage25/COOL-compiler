class A {
    a : Int;
};

class B inherits A {
    b : Int;
};

class C inherits B {
    c : Int;
};

class Main {
    m : C;
    io : IO;

    main() : Int {{
        m <- new C;
        io <- new IO;
        io@IO.out_int(m.c);
        0;
    }};
};
