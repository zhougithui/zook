package com.awake.zoo;

import lombok.Data;

@Data
public class ZkContext {

    private int version;

    public ZkContext(int version){
        this.version = version;
    }
}
