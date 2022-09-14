package com.endava.internship.collections;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args){
        Student s1 = new Student("name1", LocalDate.of(2021, 1, 1), "na");
        Student s2 = new Student("name2", LocalDate.of(2021, 1, 1), "na");
        Student s3 = new Student("name3", LocalDate.of(2021, 1, 1), "na");
        System.out.println(s1.equals(s2));
        System.out.println(s1.hashCode() + "  " + s2.hashCode());
        System.out.println("dd");

        StudentMap studentMap = new StudentMap();
        studentMap.put(s1, 1);
        studentMap.put(s2, 2);
        studentMap.put(s3, 3);
        studentMap.put(s3, 4);

        System.out.println(studentMap.get(s1));
        System.out.println(studentMap.get(s2));
        System.out.println(studentMap.get(s3));

        System.out.println(studentMap.containsKey(s1));
        System.out.println(studentMap.containsKey(s2));
        System.out.println(studentMap.containsKey(s3));

        System.out.println(studentMap.containsValue(1));
        System.out.println(studentMap.containsValue(2));
        System.out.println(studentMap.containsValue(4));
        System.out.println(studentMap.containsValue(3));

        System.out.println(studentMap.entrySet());
        System.out.println(studentMap.keySet());
        System.out.println(studentMap.values());

        for(int i =0; i<200; ++i){
            Student student = new Student((""+i), LocalDate.now(), "");
            studentMap.put(student, i);
        }
        System.out.println(studentMap.entrySet());
        System.out.println(studentMap.size());

    }

}
