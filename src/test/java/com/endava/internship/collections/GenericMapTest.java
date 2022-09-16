package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class GenericMapTest {

    GenericMap<String, Integer> genericMap;
    String dummy = "dummy";
    
    @BeforeEach
    void setUp() {
        genericMap = new GenericMap<>();
    }

    @Test
    public void throwsException_whenCreateWithNegativeInitialCapacity(){
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new GenericMap<String, Integer>(-1));
    }


    @Test
    public void sizeZero_initially(){
        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(0),
                () -> assertThat(genericMap.isEmpty()).isTrue()
        );
    }

    @Test
    public void sizeOne_whenAddOneKey(){
        genericMap.put(dummy, 1);
        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(1),
                () -> assertThat(genericMap.isEmpty()).isFalse()
        );
    }

    @Test
    public void containsKey_whenAddOneKey(){
        genericMap.put(dummy, 1);

        assertThat(genericMap.containsKey(dummy)).isTrue();
    }

    @Test
    public void containsKey_whenAddNullKey(){
        genericMap.put(null, null);

        assertThat(genericMap.containsKey(null)).isTrue();
    }

    @Test
    public void containsValue_whenAddOneKey(){
        Integer expectedValue = 1;
        genericMap.put(dummy, expectedValue);

        assertThat(genericMap.containsValue(expectedValue)).isTrue();
    }


    @Test
    public void containsLastValue_whenAddSameKeyManyTimes(){
        genericMap.put(dummy, 1);
        genericMap.put(dummy, 2);
        genericMap.put(dummy, 3);

        assertAll(
                () -> assertThat(genericMap.containsValue(3)).isTrue(),
                () -> assertThat(genericMap.get(dummy)).isEqualTo(3)
        );
    }
    @Test
    void shouldReturnNull_whenGettingNonExistingKey(){
        genericMap.put(dummy, 1);
        String anotherObject = "another object";

        assertThat(genericMap.get(anotherObject)).isEqualTo(null);
    }

    @Test
    public void shouldReturnLastValue_whenAddSameKeySecondTime(){
        Integer expectedValue = 1;
        genericMap.put(dummy, expectedValue);

        assertThat(genericMap.put(dummy, 2)).isEqualTo(expectedValue);
    }

    @Test
    public void shouldPutAndGetNullKey(){
        genericMap.put(null, 1);

        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(1),
                () -> assertThat(genericMap.containsKey(null)).isTrue(),
                () -> assertThat(genericMap.get(null)).isEqualTo(1)
        );
    }

    @Test
    public void shouldPutAndGetNullValue(){
        Integer expectedValue = null;
        genericMap.put(dummy, expectedValue);

        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(1),
                () -> assertThat(genericMap.containsValue(null)).isTrue(),
                () -> assertThat(genericMap.get(null)).isEqualTo(expectedValue)
        );
    }

    @Test
    public void shouldRemoveKey(){
        int expectedValue = 1;
        genericMap.put(dummy, expectedValue);

        assertAll(
                () -> assertThat(genericMap.remove(dummy)).isEqualTo(expectedValue),
                () -> assertThat(genericMap.containsKey(dummy)).isFalse(),
                () -> assertThat(genericMap.containsValue(expectedValue)).isFalse()
        );
    }

    @Test
    void shouldRemoveNullKey(){
        int expectedValue = 1;
        genericMap.put(null, expectedValue);

        assertAll(
                () -> assertThat(genericMap.remove(null)).isEqualTo(expectedValue),
                () -> assertThat(genericMap.containsKey(null)).isFalse(),
                () -> assertThat(genericMap.containsValue(expectedValue)).isFalse()
        );
    }

    @Test
    void shouldReturnNull_whenRemovingNonExistingKey(){
        int expectedValue = 1;
        genericMap.put(dummy, expectedValue);

        assertAll(
                () -> assertThat(genericMap.remove(null)).isEqualTo(null),
                () -> assertThat(genericMap.containsKey(dummy)).isTrue(),
                () -> assertThat(genericMap.containsValue(expectedValue)).isTrue()
        );
    }

    @Test
    public void shouldRemoveOnlyOneKey(){
        int expectedValue = 1;
        String testObject1 = "object1";
        String testObject2 = "object2";

        genericMap.put(dummy, expectedValue);
        genericMap.put(testObject1, 2);
        genericMap.put(testObject2, 3);
        assertAll(
                () -> assertThat(genericMap.remove(dummy)).isEqualTo(expectedValue),
                () -> assertThat(genericMap.containsKey(dummy)).isFalse(),
                () -> assertThat(genericMap.containsKey(testObject2)).isTrue(),
                () -> assertThat(genericMap.containsKey(testObject2)).isTrue(),
                () -> assertThat(genericMap.containsValue(expectedValue)).isFalse(),
                () -> assertThat(genericMap.size()).isEqualTo(2)
        );
    }

    @Test
    public void shouldReturnNull_whenGetWrongTypeKey(){
        assertThat(genericMap.get(1)).isNull();
    }

    @Test
    public void shouldReturnNull_whenRemoveWrongTypeKey(){
        assertThat(genericMap.remove(1)).isNull();
    }

    @Test
    public void isEmpty_whenCleared(){
        String anotherObject = "another object";
        genericMap.put(dummy, 1);
        genericMap.put(anotherObject, 2);

        genericMap.clear();

        assertThat(genericMap.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_whenPutAllEmptyMap(){
        genericMap.putAll(new HashMap<>());

        assertThat(genericMap.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideObjectList")
    public void sizeIsRight_whenPutAllMap(List<String> objectList){
        Map<String, Integer> map = new HashMap<>();
        for (String object : objectList) {
            map.put(object, 1);
        }
        genericMap.putAll(map);

        assertThat(genericMap.size()).isEqualTo(map.size());
    }

    @ParameterizedTest
    @MethodSource("provideObjectList")
    public void keySetSizeIsRight(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }

        assertThat(genericMap.keySet().size()).isEqualTo(objectList.size());
    }
    @ParameterizedTest
    @MethodSource("provideObjectList")
    public void valuesCollectionSizeIsRight(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }

        assertThat(genericMap.values().size()).isEqualTo(objectList.size());
    }

    @ParameterizedTest
    @MethodSource("provideObjectList")
    public void EntrySetSizeIsRight(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }

        assertThat(genericMap.entrySet().size()).isEqualTo(objectList.size());
    }

    @Test
    public void rehashingDoesNotAffectMapContents(){
        int expectedLength = 100;
        for(int i = 0; i < expectedLength; ++i){
            genericMap.put("string " + i, i);
        }

        assertThat(genericMap.size()).isEqualTo(expectedLength);
    }


    /*
     * * ------------------------- *
     * The way keys and values are stored internally can differ (first element in the bucket,
     * middle element of the bucket, the last element in the bucket).
     * Map should ensure removing those elements in any test case
     * * ------------------------- *
     */
    @ParameterizedTest
    @MethodSource("provideObjectListWithTheSameHashIndex")
    public void shouldRemoveFirstObjectInTheBucket(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }
        //removing first element in the bucket
        String firstObjectInTheBucket = "string 1";
        genericMap.remove(firstObjectInTheBucket);

        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(objectList.size() - 1),
                () -> assertThat(genericMap.containsKey(firstObjectInTheBucket)).isFalse()
        );
    }

    @ParameterizedTest
    @MethodSource("provideObjectListWithTheSameHashIndex")
    public void shouldRemoveMiddleObjectInTheBucket(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }
        //removing middle element in the bucket
        String middleObjectInTheBucket = "string 10";
        genericMap.remove(middleObjectInTheBucket);

        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(objectList.size() - 1),
                () -> assertThat(genericMap.containsKey(middleObjectInTheBucket)).isFalse()
        );
    }

    @ParameterizedTest
    @MethodSource("provideObjectListWithTheSameHashIndex")
    public void shouldRemoveLastObjectInTheBucket(List<String> objectList){
        for (String object : objectList) {
            genericMap.put(object, 1);
        }
        //removing last element in the bucket
        String lastObjectInTheBucket = "string 21";
        genericMap.remove(lastObjectInTheBucket);

        assertAll(
                () -> assertThat(genericMap.size()).isEqualTo(objectList.size() - 1),
                () -> assertThat(genericMap.containsKey(lastObjectInTheBucket)).isFalse()
        );
    }

    public static Stream<Arguments> provideObjectListWithTheSameHashIndex(){
        List<String> objectList = new ArrayList<>();

        //if map has default initial capacity and has not been rehashed,
        //all three objects should produce the same hash index (!but not the same hashcode),
        // and consequently, will be placed in the same bucket in this order.
        objectList.add("string 1");
        objectList.add("string 10");
        objectList.add("string 21");

        return Stream.of(
                Arguments.of(objectList)
        );
    }

    public static Stream<Arguments> provideObjectList(){
        List<String> objectList = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            objectList.add("string " + i);
        }
        return Stream.of(
                Arguments.of(objectList)
        );

    }
}