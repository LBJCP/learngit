package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class Auth {
    private boolean control;
    private boolean set;
    private boolean view;
}
