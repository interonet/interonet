package org.interonet.ldm.VMMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuang on 2016/9/27.
 */
public class VMMacToIP {
    public static Map<String,String> VMMacToIP(){
        Map<String,String> VMMacToIP = new HashMap<>();
        for(int i = 0;i<8;i++){
            String mac = new String();
            mac = "00:50:56:40:00:0"+i;
            String ip = new String();
            ip = "10.0.0.4"+i;
            VMMacToIP.put(mac,ip);
        }
        return VMMacToIP;
    }
}
