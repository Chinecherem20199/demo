package com.example.demo.viewmodel;

import javax.validation.constraints.NotNull;

public class RoleViewModel {
    @NotNull
     String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
