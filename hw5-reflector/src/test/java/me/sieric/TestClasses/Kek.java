package me.sieric.TestClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class Kek <T extends Comparable<T> & Runnable> implements Callable {
    private int kek = 0;
    String name = "kek";
    T rrrr;

    @Override
    public Object call() throws Exception {
        return null;
    }

    private class Lol <W> {
        T kek;
        ArrayList<W> lol;
    }

    private static class Heh <W> {
        W[] qrort;
        final W kek = null;
    }

    public void lool(T kek, int lol) {
        System.out.println(lol);
    }

    void draw(List<? extends Kek> c) {
        for (Iterator<? extends Kek> i = c.iterator();
             i.hasNext(); ) {
             Kek s = i.next();
        }
    }

    void kekek(Collection<? extends Object> c) {

    }

    public T heheh(T hohoh) {
        return null;
    }

    public T heheh(String rrr) {
        return null;
    }
    public int rrrrrr() throws Exception {
        return 1;
    }
}