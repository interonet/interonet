package org.interonet.ldm.VMMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuang on 2016/9/27.
 */
public class VMIdToMac {
    public static Map<Integer,String> VMIdToMac(){
        int a = 0;
        Map<Integer,String> VMIdToMac = new HashMap<>();
        for(int i = 0;i < 8; i++){
            String Mac = new String();
            Mac = "00:50:56:40:00:0"+i;
            a = i+1;
            VMIdToMac.put(a,Mac);
        }
        return VMIdToMac;
    }
}
