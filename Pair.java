package com.company;

public class Pair<T,K> {
    private T first;
    private K second;

    Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    void setFirst(T first) {
        this.first = first;
    }

    void setSecond(K second) {
        this.second = second;
    }

    T getFirst() {
        return first;
    }

    K getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
