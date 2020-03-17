package com.trelloiii.jigul.json;

import java.util.Arrays;

public class JsonConfiguration {
    private String[] webPackages;
    private String[] iocPackages;
    private int serverPort;


    public void setWebPackages(String[] webPackages) {
        this.webPackages = webPackages;
    }

    public void setIocPackages(String[] iocPackages) {
        this.iocPackages = iocPackages;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String[] getWebPackages() {
        return webPackages;
    }

    public String[] getIocPackages() {
        return iocPackages;
    }

    public int getServerPort() {
        return serverPort;
    }

    public JsonConfiguration(String[] webPackages, String[] iocPackages, int serverPort) {
        this.webPackages = webPackages;
        this.iocPackages = iocPackages;
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "JsonConfiguration{" +
                "webPackages=" + Arrays.toString(webPackages) +
                ", iocPackages=" + Arrays.toString(iocPackages) +
                ", serverPort=" + serverPort +
                '}';
    }
}
