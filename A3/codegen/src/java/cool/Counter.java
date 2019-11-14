package cool;

public class Counter {

    private int index = -1;

    public Counter() {
    }

    public Counter(int i) {
        index = i;
    }

    public int getIndex() {
        return index;
    }

    public int incrementIndex() {
        index++;
        return index;
    }

    public int decrementIndex() {
        index--;
        return index;
    }

    public int prevIndex() {
        return index - 1;
    }

    public int prevIndex(int i) {
        return index - i;
    }

    public int nextIndex() {
        return index + 1;
    }

    public int nextIndex(int i) {
        return index + i;
    }

    public void reset() {
        index = -1;
    }
}
