package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Arrays;

@ThreadSafe
public class UserStorage {
    @GuardedBy("this")
    private User[] arrayUser;
    private int size;

    private final int defaultCapacity = 10;

    public UserStorage() {
        this.arrayUser = new User[defaultCapacity];
        this.size = 0;
    }

    public UserStorage(int initialCapacity) {
        this.arrayUser = new User[initialCapacity > 0 ? initialCapacity : defaultCapacity];
        this.size = 0;
    }

    private synchronized void tryIncrease() {
        if (arrayUser.length == size) {
            double scalingFactor = 1.5;
            arrayUser = Arrays.copyOf(arrayUser, (int) (arrayUser.length * scalingFactor));
        }
    }

    public synchronized boolean add(User user) {
        tryIncrease();
        arrayUser[size++] = user;
        return true;
    }

    public synchronized boolean update(User user) {
        for (int i = 0; i < arrayUser.length; i++) {
            if (arrayUser[i].getId() == user.getId()) {
                arrayUser[i] = user;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean delete(User user) {
        int index = -1;
        for (int i = 0; i < arrayUser.length; i++) {
            if (arrayUser[i].equals(user)) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        int moveCount = size - index - 1;
        if (moveCount > 0) {
            System.arraycopy(arrayUser, index + 1, arrayUser, index, moveCount);
        }
        arrayUser[--size] = null;
        return true;
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        User from = null;
        User to = null;
        for (User user : arrayUser) {
            if (user.getId() == fromId) {
                from = user;
            } else if (user.getId() == toId) {
                to = user;
            }
        }
        if (from == null || to == null || from.getAmount() < amount || from.equals(to)) {
            return false;
        }
        from.setAmount(from.getAmount() - amount);
        to.setAmount(to.getAmount() + amount);
        return true;
    }
}

class User {
    private final int id;
    private int amount;

    public User(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        if (id != user.id) {
            return false;
        }
        return amount == user.amount;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + amount;
        return result;
    }
}