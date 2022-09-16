package com.endava.internship.collections;

import java.util.*;

public class GenericMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private GenericMap.Node<K, V>[] buckets;
    private int size = 0;

    static class Node<K, V> {
        private final K key;
        private V value;
        private GenericMap.Node<K, V> next;

        public Node(K key, V value) {
            this(key,  value, null);
        }

        public Node(K key, V value, GenericMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    public GenericMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public GenericMap(int initialCapacity){
        if (initialCapacity < 0){
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity < DEFAULT_INITIAL_CAPACITY){
            buckets = createBucketArray(DEFAULT_INITIAL_CAPACITY);
        } else {
            buckets = createBucketArray(initialCapacity);
        }
    }

    private int hashIndex(Object key){
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    @SuppressWarnings("unchecked")
    private GenericMap.Node<K, V>[] createBucketArray(int length){
        return (GenericMap.Node<K, V>[]) new GenericMap.Node[length];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (GenericMap.Node<K, V> currentNode : buckets) {
            while (currentNode != null){
                if (Objects.equals(currentNode.getKey(), key)){
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (GenericMap.Node<K, V> currentNode : buckets) {
            while (currentNode != null){
                if (Objects.equals(currentNode.getValue(), value)){
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int bucketIndex = hashIndex(key);
        return getFromBucket(key, bucketIndex);
    }

    private V getFromBucket(Object key, int bucketIndex){
        GenericMap.Node<K, V> currentNode = buckets[bucketIndex];
        while (currentNode != null){
            if (Objects.equals(currentNode.getKey(), key)){
                return currentNode.getValue();
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int bucketIndex = hashIndex(key);
        return putInBucket(key, value, bucketIndex);
    }

    private V putInBucket(K key, V value, int bucketIndex){
        if (bucketIndex < 0 || bucketIndex > buckets.length){
            throw new IndexOutOfBoundsException("Bucket Index out of bounds: " + bucketIndex);
        }

        if (size + 1 >= buckets.length * DEFAULT_LOAD_FACTOR){
            rehash();
        }

        GenericMap.Node<K, V> newNode = new GenericMap.Node<>(key, value);

        if (buckets[bucketIndex] == null){
            buckets[bucketIndex] = newNode;
        }
        else {
            GenericMap.Node<K, V> currentNode = buckets[bucketIndex];
            if (Objects.equals(currentNode.getKey(), key)){
                return rewriteEntry(currentNode, value);
            }
            while (currentNode.next != null){
                if (Objects.equals(currentNode.getKey(), key)){
                    return rewriteEntry(currentNode, value);
                }
                currentNode = currentNode.next;
            }
            currentNode.setNext(newNode);
        }
        ++size;
        return null;
    }

    private V rewriteEntry(GenericMap.Node<K, V> node, V newValue){
        V oldValue = node.getValue();
        node.setValue(newValue);
        return oldValue;
    }

    private void rehash(){
        GenericMap.Node<K, V>[] oldBuckets = buckets;
        size = 0;
        buckets = createBucketArray(buckets.length * 2);

        for (GenericMap.Node<K, V> node : oldBuckets) {
            while (node != null){
                put(node.getKey(), node.getValue());
                node = node.next;
            }
        }
    }

    @Override
    public V remove(Object key) {
        int bucketIndex = hashIndex(key);
        return removeFromBucket(key, bucketIndex);
    }

    private V removeFromBucket(Object key, int bucketIndex){
        GenericMap.Node<K, V> currentNode = buckets[bucketIndex];
        if (currentNode != null){
            if (Objects.equals(currentNode.getKey(), key)){
                buckets[bucketIndex] = currentNode.next;
                --size;
                return currentNode.getValue();
            }
            while (currentNode.next != null){
                if (Objects.equals(currentNode.next.getKey(), key)){
                    V valueToDelete = currentNode.next.getValue();
                    currentNode.next = currentNode.next.next;
                    --size;
                    return valueToDelete;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 0;
        buckets = createBucketArray(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (GenericMap.Node<K, V> bucket : buckets) {
            while (bucket != null){
                keySet.add(bucket.getKey());
                bucket = bucket.next;
            }
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (GenericMap.Node<K, V> bucket : buckets) {
            while (bucket != null){
                values.add(bucket.getValue());
                bucket = bucket.next;
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (GenericMap.Node<K, V> bucket : buckets) {
            while (bucket != null){
                entrySet.add(new AbstractMap.SimpleEntry<>(bucket.getKey(), bucket.getValue()));
                bucket = bucket.next;
            }
        }
        return entrySet;
    }
}
