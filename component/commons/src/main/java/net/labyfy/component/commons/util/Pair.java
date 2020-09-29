package net.labyfy.component.commons.util;

public class Pair<U, V> {

    private U first;
    private V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public U getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    public Pair<V, U> swap() {
        return new Pair<>(second, first);
    }

    public U first() {
        return this.getFirst();
    }

    public V second() {
        return this.getSecond();
    }

    public U a() {
        return this.getFirst();
    }

    public V b() {
        return this.getSecond();
    }

    public U x() {
        return this.getFirst();
    }

    public V y() {
        return this.getSecond();
    }

    public U u() {
        return this.getFirst();
    }

    public V v() {
        return this.getSecond();
    }

}
