package com.trelloiii.jigul.json;

import java.util.List;

public class Json {
    private JsonConfiguration configuration;
    private List<Bean> beans;

    public void setConfiguration(JsonConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public JsonConfiguration getConfiguration() {
        return configuration;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public Json(JsonConfiguration configuration, List<Bean> beans) {
        this.configuration = configuration;
        this.beans = beans;
    }

    @Override
    public String toString() {
        return "Json{" +
                "configuration=" + configuration +
                ", beans=" + beans +
                '}';
    }
}
