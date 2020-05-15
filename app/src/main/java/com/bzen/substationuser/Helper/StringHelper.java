package com.bzen.substationuser.Helper;

import java.util.HashMap;
import java.util.Map;

public class StringHelper {


    public HashMap<String, String> StringToHashmap(String data){
        //FORMAT STRING
        //K1/J1-K2/J2-......
        HashMap<String, String> hasil = new HashMap<>();
        String [] datax = data.split("#");
        for(int i = 0; i < datax.length; i++){
            String[] tmp = datax[i].split("/");
            hasil.put(tmp[0],String.valueOf(tmp[1]));
        }
        return hasil;
    }

    public String HashmapToString(HashMap<String,String> data){
        String hasil = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            hasil += key + "/" + value + "#";
        }
        return hasil;
    }
}
