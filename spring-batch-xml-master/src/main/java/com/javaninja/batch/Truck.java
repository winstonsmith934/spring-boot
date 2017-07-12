package com.javaninja.batch;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author norris.shelton
 */
@XmlRootElement
public class Truck implements ResourceAware {
    private Resource resource;
    private String make;
    private String model;
    private String color;
    private int doors;
    private boolean extendedCab;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public boolean isExtendedCab() {
        return extendedCab;
    }

    public void setExtendedCab(boolean extendedCab) {
        this.extendedCab = extendedCab;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @XmlTransient
    public Resource getResource() {
        return resource;
    }

}
