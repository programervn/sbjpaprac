package com.thaipd.sbjpaprac;

public class ManualTest {
    public static void main(String[] args) {
        System.out.println("--- ManualTest Start ---");
        try {
            System.out.println("Attempting to load com.thaipd.sbjpaprac.entity.Country");
            Class<?> clazz = Class.forName("com.thaipd.sbjpaprac.entity.Country");
            System.out.println("Successfully loaded: " + clazz.getName());

            System.out.println("Attempting to load com.thaipd.sbjpaprac.mapper.ApiMapperImpl");
            Class<?> mapperClass = Class.forName("com.thaipd.sbjpaprac.mapper.ApiMapperImpl");
            System.out.println("Successfully loaded: " + mapperClass.getName());

            System.out.println("Listing methods...");
            for (java.lang.reflect.Method m : mapperClass.getDeclaredMethods()) {
                System.out.println("Method: " + m.getName());
            }

        } catch (Throwable t) {
            System.err.println("Failed to load class: " + t);
            t.printStackTrace();
        }
        System.out.println("--- ManualTest End ---");
    }
}
