package com.fridge.skl.model;


import lombok.Data;

@Data
public class Slot implements java.io.Serializable {


    private static final long serialVersionUID = 1L;

    private String name;


    private String[] values;
    private String[] originalValues;

}
