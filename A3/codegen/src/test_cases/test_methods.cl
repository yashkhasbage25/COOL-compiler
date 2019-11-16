class C {
    c : Int;
    getC() : Int {
        c
    };

    setC(cc : Int) : Int {
        c <- cc
    };

    newC() : C {
        new C
    };
};

class B {
    fb(c : C) : Int {
        c@C.setC(c@C.getC() * c@C.getC())
    };
};

class A {
    a : Int;
    bb : B <- new B;
    cc : C <- new C;
    d : Int <- cc@C.getC();
    ccc : C <- cc@C.newC();
    io : IO <- new IO;
    f1(b : Int, c : Int) : Int {{
        cc@C.setC(4);
        b <- cc@C.getC() + 2;
        b <- bb@B.fb(cc);
        io@IO.out_int(cc@C.getC());
        io@IO.out_int(ccc@C.getC());
        0;
    }};
};

class Main {
    a : A <- new A;
    io : IO <- new IO;
    b : Int <- 3;
    main() : Int {{
        io@IO.out_int(b);
        b <- a@A.f1(b, 6);
        io@IO.out_int(b);
        0;
    }};
};
