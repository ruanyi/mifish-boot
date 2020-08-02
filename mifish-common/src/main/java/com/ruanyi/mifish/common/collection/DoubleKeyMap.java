package com.ruanyi.mifish.common.collection;

import java.util.Collection;
import java.util.Set;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-01-22 11:01
 */
public interface DoubleKeyMap<M, N, V> {

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    int size();

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * containsByFirstKey
     *
     * @param firstKey
     * @return
     */
    boolean containsByFirstKey(M firstKey);

    /**
     * containsBySecondKey
     *
     * @param secondKey
     * @return
     */
    boolean containsBySecondKey(N secondKey);

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    boolean containsValue(V value);

    /**
     * getByFirstKey
     *
     * @param firstKey
     * @return
     */
    V getByFirstKey(M firstKey);

    /**
     * getBySecondKey
     *
     * @param secondKey
     * @return
     */
    V getBySecondKey(N secondKey);

    // Modification Operations


    /**
     * put
     *
     * @param firstKey
     * @param secondKey
     * @param value
     * @return
     */
    V put(M firstKey, N secondKey, V value);

    /**
     * remove
     *
     * @param firstKey
     * @return
     */
    V removeByFirstKey(M firstKey);

    /**
     * removeBySecondKey
     *
     * @param secondKey
     * @return
     */
    V removeBySecondKey(N secondKey);


    // Bulk Operations

    /**
     * putAll
     *
     * @param doubleKeyMap
     */
    void putAll(DoubleKeyMap<? extends M, ? extends N, ? extends V> doubleKeyMap);

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation  is not supported by this map
     */
    void clear();


    // Views

    /**
     * firstKeySet
     *
     * @return
     */
    Set<M> firstKeySet();

    /**
     * secondKeySet
     *
     * @return
     */
    Set<N> secondKeySet();

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    Collection<V> values();

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    Set<DoubleKeyEntry<M, N, V>> entrySet();

    /**
     * DoubleKeyEntry
     *
     * @author: rls
     * @Date: 2018-01-22 11:01
     */
    interface DoubleKeyEntry<M, N, V> {

        /**
         * getFirstKey
         *
         * @return
         */
        M getFirstKey();

        /**
         * getSecondKey
         *
         * @return
         */
        N getSecondKey();

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        V getValue();
    }
}
