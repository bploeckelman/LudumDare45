package lando.systems.ld45.effects.trail;

import com.badlogic.gdx.utils.Array;

public class FixedList<T> extends Array<T> {

    /**
     * Safely creates a list that is backed by an array which
     * can be directly accessed
     *
     * @param capacity the fixed-size capacity of this list
     * @param type the class type of the elements in this list
     */
    public FixedList(int capacity, Class<T> type) {
        super(false, capacity, type);
    }

    /**
     * Inserts the item into index zero, shifting all items to the right,
     * but without increasing the list's size past its array capacity
     *
     * @param t the element to insert
     */
    public void insert(T t) {
        T[] items = this.items;

        // increase the size if we have a new point (up to the capacity)
        size = Math.min(size + 1, items.length);

        // shift elements right
        for (int i = size - 1; i > 0; --i) {
            items[i] = items[i - 1];
        }

        // insert new item in front
        items[0] = t;
    }

}
