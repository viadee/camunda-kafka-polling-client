package de.viadee.camunda.kafka.pollingclient.model;

import java.util.Calendar;

public class Car {

    private String model;
    private int value;

    public Car(String model, int value) {
        this.model = model;
        this.value = value;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
