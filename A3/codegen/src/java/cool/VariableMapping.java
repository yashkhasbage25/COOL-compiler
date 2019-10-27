package cool;

class VariableMapping {

    // this class is similar to C++ Pair<>
    String leftString;
    String rightString;

    // this class is used to store identifier name binidings
    // eg. while matching formal arguments and actual arguments
    // of a class method
    VariableMapping(String left, String right) {
        leftString = left;
        rightString = right;
    }

    // this returns leftString
    public String getLeftString() {
        return leftString;
    }

    // this returns rightString
    public String getRightString() {
        return rightString;
    }
}
