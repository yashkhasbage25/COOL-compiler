class A {
    f1() : Int {
        0
    };
};

class Main {

    a : A <- new A;
    main() : Int {
        {
            (1)@Object.abort();
            0;
        }
    };
};
