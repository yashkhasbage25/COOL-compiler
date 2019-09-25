(* Rules for conditionals, including working of joins *)

class A {
    a : Int;
};

class B inherits A {
    b : Int;
};

class C {
    c : Int;
};

class D {
    a : A;
    b : B;
    c : C;
    d : Int;
    e : Int; 
    f : Bool;
    f1() : Int {
        {
            if d = e then a else b fi;
            if f then a else c fi;
            if f then d else e fi;
            if d = e then e else f fi;
            0;
        }
    };
};

class E inherits B {
    e : Int;
};

class F inherits B {
    f : Int;
};

class G {
    e : E;
    f : F;
    a : Bool;
    f1() : B {
        if a then e else f fi
    };
};

class Main {
    main() : Int {
        0
    };
};
