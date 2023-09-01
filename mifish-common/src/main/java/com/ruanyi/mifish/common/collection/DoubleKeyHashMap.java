package com.ruanyi.mifish.common.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-01-22 11:17
 */
public class DoubleKeyHashMap<M, N, V> implements DoubleKeyMap<M, N, V> {

    /**
     * firstTable
     */
    private ConcurrentHashMap<M, DoubleKeyNode<M, N, V>> firstTable = new ConcurrentHashMap<>();

    /**
     * secondTable
     */
    private ConcurrentHashMap<N, DoubleKeyNode<M, N, V>> secondTable = new ConcurrentHashMap<>();

    /**
     * lock
     */
    private final Lock lock = new ReentrantLock();

    /**
     * size
     *
     * @return
     */
    @Override
    public int size() {
        return this.firstTable.size();
    }

    /**
     * isEmpty
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.firstTable.isEmpty();
    }

    /**
     * containsByFirstKey
     *
     * @param firstKey
     * @return
     */
    @Override
    public boolean containsByFirstKey(M firstKey) {
        return this.firstTable.containsKey(firstKey);
    }

    /**
     * containsBySecondKey
     *
     * @param secondKey
     * @return
     */
    @Override
    public boolean containsBySecondKey(N secondKey) {
        return this.secondTable.containsKey(secondKey);
    }

    /**
     * containsValue
     *
     * @param value value whose presence in this map is to be tested
     * @return
     */
    @Override
    public boolean containsValue(V value) {
        if (!isEmpty()) {
            for (Map.Entry<M, DoubleKeyNode<M, N, V>> entry : this.firstTable.entrySet()) {
                DoubleKeyNode<M, N, V> doubleKeyNode = entry.getValue();
                if (Objects.equals(doubleKeyNode.getValue(), value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getByFirstKey
     *
     * @param firstKey
     * @return
     */
    @Override
    public V getByFirstKey(M firstKey) {
        DoubleKeyNode<M, N, V> node = this.firstTable.get(firstKey);
        if (node != null) {
            return node.getValue();
        }
        return null;
    }

    /**
     * getBySecondKey
     *
     * @param secondKey
     * @return
     */
    @Override
    public V getBySecondKey(N secondKey) {
        DoubleKeyNode<M, N, V> node = this.secondTable.get(secondKey);
        if (node != null) {
            return node.getValue();
        }
        return null;
    }

    /**
     * put
     *
     * @param firstKey
     * @param secondKey
     * @param value
     * @return
     */
    @Override
    public V put(M firstKey, N secondKey, V value) {
        if (value == null) {
            return null;
        }
        lock.lock();
        try {
            DoubleKeyNode<M, N, V> node = new DoubleKeyNode(firstKey, secondKey, value);
            DoubleKeyNode<M, N, V> oldNode = this.firstTable.put(firstKey, node);
            this.secondTable.put(secondKey, node);
            if (oldNode != null) {
                return oldNode.getValue();
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * removeByFirstKey
     *
     * @param firstKey
     * @return
     */
    @Override
    public V removeByFirstKey(M firstKey) {
        lock.lock();
        try {
            DoubleKeyNode<M, N, V> node = this.firstTable.remove(firstKey);
            if (node != null) {
                N secondKey = node.getSecondKey();
                this.secondTable.remove(secondKey);
                return node.getValue();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * removeBySecondKey
     *
     * @param secondKey
     * @return
     */
    @Override
    public V removeBySecondKey(N secondKey) {
        lock.lock();
        try {
            DoubleKeyNode<M, N, V> node = this.secondTable.remove(secondKey);
            if (node != null) {
                M firstKey = node.getFirstKey();
                this.firstTable.remove(firstKey);
                return node.getValue();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * putAll
     *
     * @param doubleKeyMap
     */
    @Override
    public void putAll(DoubleKeyMap<? extends M, ? extends N, ? extends V> doubleKeyMap) {
        if (doubleKeyMap == null || doubleKeyMap.isEmpty()) {
            return;
        }
        lock.lock();
        try {
            for (DoubleKeyEntry<? extends M, ? extends N, ? extends V> entry : doubleKeyMap.entrySet()) {
                if (entry != null) {
                    DoubleKeyNode<M, N, V> node =
                        new DoubleKeyNode(entry.getFirstKey(), entry.getSecondKey(), entry.getValue());
                    this.firstTable.put(entry.getFirstKey(), node);
                    this.secondTable.put(entry.getSecondKey(), node);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * clear
     */
    @Override
    public void clear() {
        lock.lock();
        try {
            this.firstTable.clear();
            this.secondTable.clear();
        } finally {
            lock.unlock();
        }
    }

    /**
     * firstKeySet
     *
     * @return
     */
    @Override
    public Set<M> firstKeySet() {
        return this.firstTable.keySet();
    }

    /**
     * secondKeySet
     *
     * @return
     */
    @Override
    public Set<N> secondKeySet() {
        return this.secondTable.keySet();
    }

    /**
     * values
     *
     * @return
     */
    @Override
    public Collection<V> values() {
        Collection<DoubleKeyNode<M, N, V>> cnodes = this.firstTable.values();
        if (cnodes == null || cnodes.isEmpty()) {
            return Lists.newArrayList();
        }
        List<V> lists = new ArrayList<>();
        for (DoubleKeyNode<M, N, V> node : cnodes) {
            if (node != null) {
                lists.add(node.getValue());
            }
        }
        return lists;
    }

    /**
     * entrySet
     *
     * @return
     */
    @Override
    public Set<DoubleKeyEntry<M, N, V>> entrySet() {
        Collection<DoubleKeyNode<M, N, V>> cnodes = this.firstTable.values();
        if (cnodes == null || cnodes.isEmpty()) {
            return Sets.newHashSet();
        }
        Set<DoubleKeyEntry<M, N, V>> sets = Sets.newHashSet();
        for (DoubleKeyNode<M, N, V> node : cnodes) {
            if (node != null) {
                sets.add(node);
            }
        }
        return sets;
    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2018-01-22 11:17
     */
    private static class DoubleKeyNode<M, N, V> implements DoubleKeyEntry<M, N, V> {

        /**
         * firstKey
         */
        M firstKey;

        /**
         * secondKey
         */
        N secondKey;

        /**
         * value
         */
        V value;

        /**
         * DoubleKeyNode
         *
         * @param firstKey
         * @param secondKey
         * @param value
         */
        private DoubleKeyNode(M firstKey, N secondKey, V value) {
            this.firstKey = firstKey;
            this.secondKey = secondKey;
            this.value = value;
        }

        /**
         * getFirstKey
         *
         * @return
         */
        @Override
        public M getFirstKey() {
            return this.firstKey;
        }

        /**
         * getSecondKey
         *
         * @return
         */
        @Override
        public N getSecondKey() {
            return this.secondKey;
        }

        /**
         * getValue
         *
         * @return
         */
        @Override
        public V getValue() {
            return this.value;
        }

        /**
         * equals
         *
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof DoubleKeyMap.DoubleKeyEntry) {
                DoubleKeyNode<?, ?, ?> that = (DoubleKeyNode<?, ?, ?>)o;
                if (Objects.equals(firstKey, that.firstKey) && Objects.equals(secondKey, that.secondKey)
                    && Objects.equals(value, that.value)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * hashCode
         *
         * @return
         */
        @Override
        public int hashCode() {
            int result = firstKey != null ? firstKey.hashCode() : 0;
            result = 31 * result + (secondKey != null ? secondKey.hashCode() : 0);
            return result;
        }
    }
}
