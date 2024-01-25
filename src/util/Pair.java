package src.util;

public class Pair<T1, T2> {

    private T1 _first;
    private T2 _second;

    public Pair(T1 first, T2 second) {
        _first = first;
        _second = second;
    }

    public T1 first() {
        return _first;
    }

    public T2 second() {
        return _second;
    }

}
