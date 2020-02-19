package com.trelloiii.simplereapitinglib.scanner;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String pkg="com.trelloiii.simplereapitinglib.scanner";
        ClassScanner scanner=ClassScanner.createScanner();
        System.out.println(Arrays.toString(scanner.scan(pkg).toArray()));
    }
}
