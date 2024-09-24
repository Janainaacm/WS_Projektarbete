package com.example.ws_projektarbete.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SimpleWeather {
    @JsonProperty("data")
    private List<SimpleObservation> data;

    public List<SimpleObservation> getData() {
        return data;
    }

    public void setData(List<SimpleObservation> data) {
        this.data = data;
    }
}
