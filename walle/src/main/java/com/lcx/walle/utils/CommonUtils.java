package com.lcx.walle.utils;

import java.util.UUID;

public class CommonUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().toLowerCase().replace("-","");
    }
}
