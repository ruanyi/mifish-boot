package com.mifish.common.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description:
 * <p>
 * https://stackoverflow.com/questions/363681/how-to-generate-random-integers-within-a-specific-range-in-java
 *
 * @author: rls
 * @Date: 2017-08-26 14:26
 */
public final class RandomUtil {

    /**
     * 生成在[min,max]之间的随机整数，
     * <p>
     * In Java 1.7 or later, the standard way to do this is as follows:
     *
     * @param min
     *            the min
     * @param max
     *            the max
     * @return int
     */
    public static int randomRange(int min, int max) {
        if (min > max) {
            return -1;
        }
        if (min == max) {
            return min;
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * <p>
     * Returns a random integer within the specified range.
     * </p>
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @return the random integer, -1 if {@code startInclusive > endExclusive} or if {@code startInclusive} is negative
     */
    public static int randomInt(final int startInclusive, final int endExclusive) {
        if (startInclusive > endExclusive) {
            return -1;
        }
        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        return ThreadLocalRandom.current().nextInt(startInclusive, endExclusive);
    }

    /**
     * 随机取 list 中一个数
     *
     * @param <T>
     *            the type parameter
     * @param list
     *            the list
     * @return the t
     */
    public static <T> T randomSelectOne(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    /**
     * Random get n record list.
     *
     * @param <T>
     *            the type parameter
     * @param list
     *            the list
     * @param n
     *            the n
     * @return the list
     */
    public static <T> List<T> randomGetNRecord(List<T> list, int n) {
        if (n <= 0 || list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int listSize = list.size();
        if (n > listSize) {
            return list;
        }

        List<T> resultList = new ArrayList<>();
        int tryCount = 2 * n;
        Set<Integer> indexSet = new HashSet<>();
        for (int i = 0; i < tryCount; i++) {
            int randomIndex = new Random().nextInt(listSize);
            if (!indexSet.contains(randomIndex)) {
                indexSet.add(randomIndex);
                resultList.add(list.get(randomIndex));
            }

            if (resultList.size() >= n) {
                return resultList;
            }
        }

        for (int i = 0; i < listSize; i++) {
            if (!indexSet.contains(i)) {
                indexSet.add(i);
                resultList.add(list.get(i));
            }

            if (resultList.size() >= n) {
                return resultList;
            }
        }

        return resultList;
    }

    /**
     * forbit instance
     */
    private RandomUtil() {

    }
}
