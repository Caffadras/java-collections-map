package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class StudentMapTest {

    StudentMap studentMap;
    Student dummyStudent;

    @BeforeEach
    void setUp() {
        studentMap = new StudentMap();
        dummyStudent = new Student("Dummy", LocalDate.now(), "na");
    }

    @Test
    public void throwsException_whenCreateWithNegativeInitialCapacity(){
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new StudentMap(-1));
    }


    @Test
    public void sizeZero_initially(){
        assertAll(
                () -> assertThat(studentMap.size()).isEqualTo(0),
                () -> assertThat(studentMap.isEmpty()).isTrue()
        );
    }

    @Test
    public void sizeOne_whenAddOneStudent(){
        studentMap.put(dummyStudent, 1);
        assertAll(
                () -> assertThat(studentMap.size()).isEqualTo(1),
                () -> assertThat(studentMap.isEmpty()).isFalse()
        );
    }

    @Test
    public void containsKey_whenAddOneStudent(){
        studentMap.put(dummyStudent, 1);

        assertThat(studentMap.containsKey(dummyStudent)).isTrue();
    }

    @Test
    public void containsKey_whenAddNullKey(){
        studentMap.put(null, null);

        assertThat(studentMap.containsKey(null)).isTrue();
    }

    @Test
    public void containsValue_whenAddOneStudent(){
        Integer expectedValue = 1;
        studentMap.put(dummyStudent, expectedValue);

        assertThat(studentMap.containsValue(expectedValue)).isTrue();
    }


    @Test
    public void containsLastValue_whenAddSameKeyManyTimes(){
        studentMap.put(dummyStudent, 1);
        studentMap.put(dummyStudent, 2);
        studentMap.put(dummyStudent, 3);

        assertAll(
                () -> assertThat(studentMap.containsValue(3)).isTrue(),
                () -> assertThat(studentMap.get(dummyStudent)).isEqualTo(3)
        );
    }
    @Test
    void shouldReturnNull_whenGettingNonExistingKey(){
        studentMap.put(dummyStudent, 1);
        Student otherStudent = new Student("otherStudent", LocalDate.now(), "");

        assertThat(studentMap.get(otherStudent)).isEqualTo(null);
    }

    @Test
    public void shouldReturnLastValue_whenAddSameKeySecondTime(){
        Integer expectedValue = 1;
        studentMap.put(dummyStudent, expectedValue);

        assertThat(studentMap.put(dummyStudent, 2)).isEqualTo(expectedValue);
    }

    @Test
    public void shouldPutAndGetNullKey(){
        studentMap.put(null, 1);

        assertAll(
                () -> assertThat(studentMap.size()).isEqualTo(1),
                () -> assertThat(studentMap.containsKey(null)).isTrue(),
                () -> assertThat(studentMap.get(null)).isEqualTo(1)
        );
    }

    @Test
    public void shouldPutAndGetNullValue(){
        Integer expectedValue = null;
        studentMap.put(dummyStudent, expectedValue);

        assertAll(
                () -> assertThat(studentMap.size()).isEqualTo(1),
                () -> assertThat(studentMap.containsValue(null)).isTrue(),
                () -> assertThat(studentMap.get(null)).isEqualTo(expectedValue)
        );
    }

    @Test
    public void shouldRemoveStudent(){
        int expectedValue = 1;
        studentMap.put(dummyStudent, expectedValue);

        assertAll(
                () -> assertThat(studentMap.remove(dummyStudent)).isEqualTo(expectedValue),
                () -> assertThat(studentMap.containsKey(dummyStudent)).isFalse(),
                () -> assertThat(studentMap.containsValue(expectedValue)).isFalse()
        );
    }

    @Test
    void shouldRemoveNullKey(){
        int expectedValue = 1;
        studentMap.put(null, expectedValue);

        assertAll(
                () -> assertThat(studentMap.remove(null)).isEqualTo(expectedValue),
                () -> assertThat(studentMap.containsKey(null)).isFalse(),
                () -> assertThat(studentMap.containsValue(expectedValue)).isFalse()
        );
    }

    @Test
    void shouldReturnNull_whenRemovingNonExistingKey(){
        int expectedValue = 1;
        studentMap.put(dummyStudent, expectedValue);

        assertAll(
                () -> assertThat(studentMap.remove(null)).isEqualTo(null),
                () -> assertThat(studentMap.containsKey(dummyStudent)).isTrue(),
                () -> assertThat(studentMap.containsValue(expectedValue)).isTrue()
        );
    }

    @Test
    public void shouldRemoveOnlyOneStudent(){
        int expectedValue = 1;
        Student testStudent1 = new Student("1", LocalDate.now(), "");
        Student testStudent2 = new Student("2", LocalDate.now(), "");

        studentMap.put(dummyStudent, expectedValue);
        studentMap.put(testStudent1, 2);
        studentMap.put(testStudent2, 3);
        assertAll(
                () -> assertThat(studentMap.remove(dummyStudent)).isEqualTo(expectedValue),
                () -> assertThat(studentMap.containsKey(dummyStudent)).isFalse(),
                () -> assertThat(studentMap.containsKey(testStudent1)).isTrue(),
                () -> assertThat(studentMap.containsKey(testStudent2)).isTrue(),
                () -> assertThat(studentMap.containsValue(expectedValue)).isFalse(),
                () -> assertThat(studentMap.size()).isEqualTo(2)
        );
    }

    @Test
    public void throwException_whenGetNotStudentKey(){
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> studentMap.get("Wrong type"));
    }

    @Test
    public void throwException_whenRemoveNotStudentKey(){
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> studentMap.remove("Wrong type"));
    }

    @Test
    public void isEmpty_whenCleared(){
        Student anotherStudent = new Student("another student", LocalDate.now(), "");
        studentMap.put(dummyStudent, 1);
        studentMap.put(anotherStudent, 2);

        studentMap.clear();

        assertThat(studentMap.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_whenPutAllEmptyMap(){
        studentMap.putAll(new HashMap<>());

        assertThat(studentMap.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideStudentList")
    public void sizeIsRight_whenPutAllMap(List<Student> studentList){
        Map<Student, Integer> map = new HashMap<>();
        for (Student student : studentList) {
            map.put(student, 1);
        }
        studentMap.putAll(map);

        assertThat(studentMap.size()).isEqualTo(map.size());
    }

    @ParameterizedTest
    @MethodSource("provideStudentList")
    public void keySetSizeIsRight(List<Student> studentList){
        for (Student student : studentList) {
            studentMap.put(student, 1);
        }

        assertThat(studentMap.keySet().size()).isEqualTo(studentList.size());
    }
    @ParameterizedTest
    @MethodSource("provideStudentList")
    public void valuesCollectionSizeIsRight(List<Student> studentList){
        for (Student student : studentList) {
            studentMap.put(student, 1);
        }

        assertThat(studentMap.values().size()).isEqualTo(studentList.size());
    }

    @ParameterizedTest
    @MethodSource("provideStudentList")
    public void EntrySetSizeIsRight(List<Student> studentList){
        for (Student student : studentList) {
            studentMap.put(student, 1);
        }

        assertThat(studentMap.entrySet().size()).isEqualTo(studentList.size());
    }

    @Test
    public void rehashingDoesNotAffectMapContents(){
        int expectedLength = 100;
        for(int i = 0; i < expectedLength; ++i){
            studentMap.put(new Student("Student " + i, LocalDate.now(), ""), i);
        }

        assertThat(studentMap.size()).isEqualTo(expectedLength);
    }
    public static Stream<Arguments> provideStudentList(){
        List<Student> studentList = new ArrayList<>();
        for(int i = 0; i < 5; ++i){
            studentList.add(new Student("Student " + i, LocalDate.now(), ""));
        }
        return Stream.of(
                Arguments.of(studentList)
        );

    }
}
