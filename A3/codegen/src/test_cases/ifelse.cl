class Main {
    num: Int;
    io : IO <- new IO;
    main() : IO {
        if num = 1 then
            io.out_string("its 1\n")
        else
            if num = 2 then
                io.out_string("its 2\n")
            else
                io.out_string("its neither 1 or 2\n")
            fi
        fi
    };
};
