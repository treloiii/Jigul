package com.trelloiii.jigul.scanner;
import com.trelloiii.jigul.Application;
import com.trelloiii.jigul.parser.ConfigType;

public class Main {
    public static void main(String[] args){
        Application.start(ConfigType.JSON);
    }
}
