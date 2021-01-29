package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;
import java.util.NoSuchElementException;

@ThreadSafe
public class SingleLockList<E> implements Iterable<E> {
    @GuardedBy("this")
    private final DynamicList<E> dynamicList;

    public SingleLockList() {
        this.dynamicList = new DynamicList<>();
    }

    public synchronized void add(E value) {
        dynamicList.add(value);
    }

    public synchronized E get(int index) {
        return dynamicList.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        synchronized (this) {
            return copyList(dynamicList).iterator();
        }
    }

    private DynamicList<E> copyList(DynamicList<E> dynamicList) {
        DynamicList<E> dynamicListCopy = new DynamicList<>();
        for (E e : dynamicList) {
            dynamicListCopy.add(e);
        }
        return dynamicListCopy;
    }
}

class DynamicList<E> implements Iterable<E> {
    private ListElement<E> head; // Указатель на начало списка
    private ListElement<E> tail; // Указатель на конец списка
    private int size = -1;

    public void add(E elem) {
        ListElement<E> newTail = new ListElement<>(null, elem);
        if (head == null) {
            head = newTail;
            tail = head;
        } else {
            tail.setNext(newTail);
            tail = newTail;
        }
        size++;
    }

    public E get(int index) {
        ListElement<E> current;
        if (index >= 0 && index <= size) {
            int indexHead = 0;
            current = head;
            while (indexHead++ != index) {
                current = current.getNext();
            }
        } else {
            throw new NoSuchElementException();
        }
        return current.getValue();
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<E> {
        private ListElement<E> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E tmp = current.getValue();
            current = current.getNext();
            return tmp;
        }
    }
}

class ListElement<E> {
    private ListElement<E> next;
    private final E value;

    public ListElement(ListElement<E> next, E value) {
        this.next = next;
        this.value = value;
    }

    public ListElement<E> getNext() {
        return next;
    }

    void setNext(ListElement<E> next) {
        this.next = next;
    }

    E getValue() {
        return value;
    }
}