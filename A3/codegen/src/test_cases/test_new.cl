class Main inherits IO {
    a : Int;
    b : Int;
    c : String;
    g : String;
    e : A;
    f : A <- new A;
    h : B <- new B;
    i : A;
    main() : Int {
        {
            i <- h.getA();
            out_int(i.getA());
            h.setAVal();
            out_int(i.getA());
            0;
        }
    };
};

class A {
    a : Int;
    getA() : Int {
        a
    };
    setA(x : Int) : Int {
        a <- x
    };
};

class B {
    a : A <- new A;
    getA() : A {
        {
            a.setA(1);
            a;
        }
    };
    setAVal() : Int {
        {
            a.setA(2);
            0;
        }
    };
};
