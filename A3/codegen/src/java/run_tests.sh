make
bash codegen ../test_cases/$1.cl
clang $1.ll
./a.out
