package com.trelloiii.jigul.parser;

import com.trelloiii.jigul.web.server.ConnectionType;

import java.util.Arrays;

public class JsonConfiguration {
    private String[] webPackages;
    private String[] iocPackages;
    private ConnectionType connectionType;
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

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
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

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public JsonConfiguration(String[] webPackages, String[] iocPackages,ConnectionType connectionType, int serverPort) {
        this.webPackages = webPackages;
        this.iocPackages = iocPackages;
        this.serverPort = serverPort;
        this.connectionType=connectionType;
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
