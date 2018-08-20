package com.github.pgcomb.date;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class WindowLink<T> implements Collection<T> {

    private LinkedList<T> linkedList = new LinkedList<>();

    private int size;

    public WindowLink(int size) {
        this.size = size;
    }
    public WindowLink(int size,T def) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            push(def);
        }
    }
    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return linkedList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return linkedList.iterator();
    }

    @Override
    public Object[] toArray() {
        return linkedList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return linkedList.toArray(a);
    }

    @Override
    public boolean add(T t){
        push(t);
        return true;
    }

    public synchronized T push(T t){
        linkedList.push(t);
        if (linkedList.size()>size){
            return linkedList.pollLast();
        }else {
            return null;
        }
    }
    @Override
    public boolean remove(Object o) {
        return linkedList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return linkedList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return linkedList.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return linkedList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return linkedList.retainAll(c);
    }

    @Override
    public void clear() {
        linkedList.clear();
    }

    public T get(int index){
        return linkedList.get(index);
    }

    @Override
    public String toString() {
        return linkedList.toString();
    }
}
