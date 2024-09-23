package com.example.ws_projektarbete.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherCondition {

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("code")
    private int code;

    @JsonProperty("description")
    private String description;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
