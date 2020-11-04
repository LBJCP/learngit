package com.fridge.skl.model;

import com.fridge.skl.model.context.*;
import lombok.Data;

@Data
public class Context implements java.io.Serializable {


    private static final long serialVersionUID = 1L;

    private User user;

    private Device device;

    private Skill skill;

    private Location location;

    private ControlDevices[] controlDevices;

    private ExtendApi extendApi;


}
