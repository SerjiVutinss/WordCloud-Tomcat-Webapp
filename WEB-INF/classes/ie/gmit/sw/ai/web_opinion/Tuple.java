package ie.gmit.sw.ai.web_opinion;

public class Tuple<T, U> {

    T _t;
    U _u;

    public Tuple() {
    }

    public Tuple(T t, U u) {
        this._t = t;
        _u = u;
    }

    public T getT() {
        return _t;
    }

    public U getU() {
        return _u;
    }

}