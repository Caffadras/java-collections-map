package com.endava.internship.collections;

import java.util.*;

public class StudentMap implements Map<Student, Integer> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node[] buckets;
    private int size = 0;

    static class Node {
        private final Student key;
        private Integer value;
        private Node next;

        public Node(Student key, Integer value) {
            this(key,  value, null);
        }

        public Node(Student key, Integer value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Student getKey() {
            return key;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public StudentMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public StudentMap(int initialCapacity){
        if (initialCapacity < 0){
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity < DEFAULT_INITIAL_CAPACITY){
            buckets = new Node[DEFAULT_INITIAL_CAPACITY];
        } else {
            buckets = new Node[initialCapacity];
        }
    }

    private int hashIndex(Student key){
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
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
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Integer){
            for (Node currentNode : buckets) {
                if (currentNode != null){
                    do{
                        if (currentNode.getValue().equals(value)){
                            return true;
                        }
                        currentNode = currentNode.next;
                    } while (currentNode != null && currentNode.next != null);
                }
            }
            return false;
        }
        throw new IllegalArgumentException("Value is not instance of Integer: " + value);
    }

    @Override
    public Integer get(Object key) {
        if (key instanceof Student){
            int bucketIndex = hashIndex((Student) key);
            return getFromBucket((Student) key, bucketIndex);
        }
        throw new IllegalArgumentException("Key is not instance of Student: " + key);
    }

    private Integer getFromBucket(Student key, int bucketIndex){
        Node currentNode = buckets[bucketIndex];
        while (currentNode != null){
            if (currentNode.getKey().equals(key)){
                return currentNode.getValue();
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public Integer put(Student key, Integer value) {
        int bucketIndex = hashIndex(key);
        return putInBucket(key, value, bucketIndex);
    }

    private Integer putInBucket(Student key, Integer value, int bucketIndex){
        if (bucketIndex < 0 || bucketIndex > buckets.length){
            throw new IndexOutOfBoundsException("Bucket Index out of bounds: " + bucketIndex);
        }

        if (size + 1 >= buckets.length * DEFAULT_LOAD_FACTOR){
            rehash();
        }

        Node newNode = new Node(key, value);
        if (buckets[bucketIndex] == null){
            buckets[bucketIndex] = newNode;
        }
        else {
            Node currentNode = buckets[bucketIndex];
            if (currentNode.getKey().equals(key)){
                return rewriteEntry(currentNode, value);
            }
            while (currentNode.next != null){
                if (currentNode.getKey().equals(key)){
                    return rewriteEntry(currentNode, value);
                }
                currentNode = currentNode.next;
            }
            currentNode.setNext(newNode);
        }
        ++size;
        return null;
    }

    private Integer rewriteEntry(Node node, Integer newValue){
        Integer oldValue = node.getValue();
        node.setValue(newValue);
        return oldValue;
    }

    private void rehash(){
        Node[] oldBuckets = buckets;
        size = 0;
        buckets = new Node[buckets.length * 2];

        for (Node node : oldBuckets) {
            while (node != null){
                put(node.getKey(), node.getValue());
                node = node.next;
            }
        }
    }

    @Override
    public Integer remove(Object key) {
        if (key instanceof Student){
            int bucketIndex = hashIndex((Student) key);
            return removeFromBucket((Student) key, bucketIndex);
        }

        throw new IllegalArgumentException("Key is not instance of Student: " + key);
    }

    private Integer removeFromBucket(Student key, int bucketIndex){
        Node currentNode = buckets[bucketIndex];
        if (currentNode != null){
            if (currentNode.getKey().equals(key)){
                buckets[bucketIndex] = currentNode.next;
                --size;
                return currentNode.getValue();
            }
            while (currentNode.next != null){
                if (currentNode.next.getKey().equals(key)){
                    Integer valueToDelete = currentNode.next.getValue();
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
    public void putAll(Map<? extends Student, ? extends Integer> map) {
        for (Map.Entry<? extends Student, ? extends Integer> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 0;
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public Set<Student> keySet() {
        Set<Student> keySet = new HashSet<>();
        for (Node bucket : buckets) {
            while (bucket != null){
                keySet.add(bucket.getKey());
                bucket = bucket.next;
            }
        }
        return keySet;
    }

    @Override
    public Collection<Integer> values() {
        Collection<Integer> values = new ArrayList<>();
        for (Node bucket : buckets) {
            while (bucket != null){
                values.add(bucket.getValue());
                bucket = bucket.next;
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<Student, Integer>> entrySet() {
        Set<Map.Entry<Student, Integer>> entrySet = new HashSet<>();
        for (Node bucket : buckets) {
            while (bucket != null){
                entrySet.add(new AbstractMap.SimpleEntry<>(bucket.getKey(), bucket.getValue()));
                bucket = bucket.next;
            }
        }
        return entrySet;
    }
}

