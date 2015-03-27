package org.interonet.ldm.VMM;

public class BridgeAndVlan implements IBridgeAndVlan {

    @Override
    public String createBridge() {
        String command = "";
        for (int i = 1; i < 9; i++) {
            command = command + "brctl addbr br" + i + ";ifconfig br" + i + " up;";
        }
        Channel channel = new Channel("root","xjtu420","202.117.15.94", 22);
        String result = channel.setChannel(command);
        return result;

    }

    @Override
    public String createVLAN() {
        String command = "modprobe 8021q;";
        for (int i = 1; i < 9; i++) {
            command = command + "vconfig add eth1 1" + i + ";ifconfig eth1.1" + i + " up;";
        }
        Channel channel = new Channel("root","xjtu420","202.117.15.94", 22);
        String result = channel.setChannel(command);
        return result;
    }

    @Override
    public String addBridgeToVlan() {
        String command = "";
        for (int i = 1; i < 9; i++) {
            command = command + "brctl addif br" + i + " eth1.1" + i + ";";
        }
        Channel channel = new Channel("root","xjtu420","202.117.15.94", 22);
        String result = channel.setChannel(command);
        return result;
    }

    @Override
    public void bridgeAndvlan() {
        this.createBridge();
        this.createVLAN();
        this.addBridgeToVlan();


    }
}
