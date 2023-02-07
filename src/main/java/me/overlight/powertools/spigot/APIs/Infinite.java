package me.overlight.powertools.spigot.APIs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Infinite<T> {
    private final List<T> items;
    private int maxRange = 5;

    @SafeVarargs
    public Infinite(T... values) {
        items = new ArrayList<>(Arrays.asList(values));
        while (items.size() > maxRange) items.remove(0);
    }

    @SafeVarargs
    @Deprecated
    public Infinite(Infinite<T> infinite, T... values) {
        items = infinite.getArray();
        while (items.size() > maxRange) items.remove(0);
        items.addAll(new ArrayList<>(Arrays.asList(values)));
    }

    public Infinite(List<T> values) {
        items = values;
        while (items.size() > maxRange) items.remove(0);
    }

    public Infinite() {
        items = new ArrayList<>();
    }

    @SafeVarargs
    @Deprecated
    public Infinite(int maxLength, T... values) {
        this.maxRange = maxLength;
        items = new ArrayList<>(Arrays.asList(values));
        while (items.size() > maxRange) items.remove(0);
    }

    @SafeVarargs
    @Deprecated
    public Infinite(int maxLength, Infinite<T> infinite, T... values) {
        this.maxRange = maxLength;
        items = new ArrayList<>(infinite.getArray());
        items.addAll(new ArrayList<>(Arrays.asList(values)));
        while (items.size() > maxRange) items.remove(0);
    }

    public Infinite(int maxLength, Infinite<T> infinite, Infinite<T> infinite2) {
        this.maxRange = maxLength;
        items = new ArrayList<>(infinite.getArray());
        items.addAll(infinite2.getArray());
        while (items.size() > maxRange) items.remove(0);
    }

    public Infinite(int maxLength, Infinite<T> infinite, List<T> values) {
        this.maxRange = maxLength;
        items = new ArrayList<>(infinite.getArray());
        items.addAll(values);
        while (items.size() > maxRange) items.remove(0);
    }

    public Infinite(int maxLength, List<T> values) {
        this.maxRange = maxLength;
        items = new ArrayList<>(values);
        while (items.size() > maxRange) items.remove(0);
    }

    public Infinite(int maxLength) {
        this.maxRange = maxLength;
        items = new ArrayList<>();
    }

    public void clear() {
        items.clear();
    }

    public T getValue(int index) {
        if (index >= items.size())
            return null;
        if (index < 0)
            return null;
        return items.get(index);
    }

    public T getValueFromLast(int index) {
        if (index >= items.size())
            return null;
        if (index < 0)
            return null;
        return items.get(items.size() - index - 1);
    }

    public boolean contains(T value) {
        return items.contains(value);
    }

    public void add(T value) {
        items.add(value);
        if (items.size() > maxRange)
            items.remove(0);
    }

    @SafeVarargs
    public final void add(T... values) {
        items.addAll(Arrays.asList(values));
        while (items.size() > maxRange) items.remove(0);
    }

    public boolean isFullEquals() {
        List<String> list = getList();
        for (Object obj : list) {
            if (!obj.equals(list.get(0)))
                return false;
        }
        return true;
    }

    public List<String> getList() {
        List<String> output = new ArrayList<>();
        for (T item : items) {
            output.add(item.toString());
        }
        return output;
    }

    public Infinite<T> clone() {
        return new Infinite<>(items);
    }

    private List<T> getArray() {
        return items;
    }

    private boolean compare(String v1, String v2) {
        if (v1.length() != v2.length()) return false;
        for (int i = 0; i < v1.length(); i++) {
            if (v1.charAt(i) != v2.charAt(i))
                return false;
        }
        return true;
    }
}
