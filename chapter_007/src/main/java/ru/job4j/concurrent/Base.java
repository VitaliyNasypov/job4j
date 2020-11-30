package ru.job4j.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class Base {
    private int id;
    private int version;
    private String name;

    public Base(int id, int version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Base base = (Base) o;
        if (id != base.id) {
            return false;
        }
        if (version == base.version) {
            return name != null ? name.equals(base.name) : base.name == null;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + version;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

class CacheBase {
    private final ConcurrentHashMap<Integer, Base> cacheBase = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return cacheBase.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        cacheBase.computeIfPresent(model.getId(), (k, v) -> {
                    if (v.getVersion() < model.getVersion()) {
                        v = model;
                    }
                    return v;
                }
        );
        if (cacheBase.get(model.getId()).equals(model)) {
            return true;
        }
        throw new OptimisticException("Wrong version");
    }

    public boolean delete(Base model) {
        if (!cacheBase.containsKey(model.getId())) {
            return false;
        }
        cacheBase.put(model.getId(), model);
        return true;
    }

    private static class OptimisticException extends RuntimeException {
        public OptimisticException(String message) {
            super(message);
        }
    }
}