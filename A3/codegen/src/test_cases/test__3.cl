class A {
    a : Int;
    f1(x : Int) : Int {
        0
    };
};

class B inherits A {
    b : Int;

};

class C inherits B {
    f1(x : Int) : Int {
        1
    };
};

class Main {
	b:B;
    main() : Int {
        b@A.f1(1)
    };
};
