package org.interonet.ldm.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.DocumentException;
import org.interonet.ldm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.ldm.PowerManager.PowerManager;
import org.interonet.ldm.SwitchManager.ISwitchManager;
import org.interonet.ldm.TopologyTransformer.TopologyTransformer;
import org.interonet.ldm.VMM.IVMManager;
import org.interonet.ldm.VMM.VMManager;
import org.interonet.ldm.VMMap.VMIdToMac;
import org.interonet.ldm.VMMap.VMMacToIP;
import org.interonet.ldm.WebService.HttpRequest;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;
import org.libvirt.LibvirtException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class LDMCore {
    private PowerManager powerManager;
    private ISwitchManager switchManager;
    private ConfigurationCenter configurationCenter;
    private IVMManager vMManager;

    private TopologyTransformer topologyTransformer;

    private Logger logger = Logger.getLogger(LDMCore.class.getCanonicalName());

    public void start() {

        // PowerManager
        //powerManager = new PowerManager();

        // ConfigurationCenter initiation.
        configurationCenter = new ConfigurationCenter(this);

        // SwitchManager initiation.
        //switchManager = new SwitchManager(this);

        // VMManager initiation
        vMManager = new VMManager(this);

        // TT initiation.
        topologyTransformer = new TopologyTransformer();

    }

    public IVMManager getVMManager() {
        return vMManager;
    }

    public ISwitchManager getSwitchManager() {
        return switchManager;
    }

    public TopologyTransformer getTopologyTransformer() {
        return topologyTransformer;
    }

    public void createSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> switchToSwitchTunnelList) throws Exception {
        logger.info(switchToSwitchTunnelList.toString());
        for (SwitchToSwitchTunnel tunnel : switchToSwitchTunnelList) {
            int switchPeerPortOnTT = configurationCenter.getTopologyTransformerPortFromPeerPort(tunnel.getSwitchId(), tunnel.getSwitchIdPortNum());
            int athrSwitchPeerPortOnTT = configurationCenter.getTopologyTransformerPortFromPeerPort(tunnel.getPeerSwitchId(), tunnel.getPeerSwitchIdPortNum());
            topologyTransformer.createTunnelSW2SW(switchPeerPortOnTT, athrSwitchPeerPortOnTT);
        }
    }

    public void createSwitchToVMTunnel(List<SwitchToVMTunnel> switchToVMTunnelList) throws Exception {
        logger.info(switchToVMTunnelList.toString());
        for (SwitchToVMTunnel swvmTunnel : switchToVMTunnelList) {
            int switchPeerPortOnTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.getSwitchId(), swvmTunnel.getSwitchPort());
            int vmVlanId = configurationCenter.getVlanIdByVM(swvmTunnel.getVmId(), swvmTunnel.getVmPort());
            topologyTransformer.createTunnelSW2VM(switchPeerPortOnTT, vmVlanId);
        }
    }

    public void powerOnVM(List<Integer> vmIdList) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Map<Integer, FutureTask<String>> futureTaskSet = new HashMap<>();
        for (Integer vmId : vmIdList) {
            FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
                public String call() throws Exception {
                    return vMManager.powerOnVM(vmId);
                }
            });
            threadPool.submit(futureTask);
            futureTaskSet.put(vmId, futureTask);
        }
        for (Map.Entry<Integer, FutureTask<String>> entry : futureTaskSet.entrySet()) {
            Integer vmId = entry.getKey();
            FutureTask<String> startVmFuture = entry.getValue();
            try {
                logger.info("Starting VM #" + vmId + ": " + startVmFuture.get());
            } catch (ExecutionException e) {
                threadPool.shutdown();
                e.printStackTrace();
                throw e;
            }

        }
    }
    public void powerOnDHCP(List<Integer> vmIdList) throws IOException {
        File file = new File("/etc/dhcp/isc-dhcp-server.conf");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file,true);

        for(int i = 0;i<vmIdList.size();i++){
            String str = "host vm"+vmIdList.get(i)+"{\r\n";
            String mac = "hardware ethernet" +VMIdToMac.VMIdToMac().get(vmIdList.get(i))+"\r\n";
            String ip = "fixed-address" +VMMacToIP.VMMacToIP().get(VMIdToMac.VMIdToMac().get(vmIdList.get(i)))+"}\r\n";
            out.write(str.getBytes());
            out.write(mac.getBytes());
            out.write(ip.getBytes());
            out.flush();
        }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Process pro = Runtime.getRuntime().exec(new String("/etc/init.d/isc-dhcp-server restart"));

    }
    public void powerOnMininet(Map<String,Integer> userVM2domVM,Map<String,List<Map<String,String>>> topologyMininet,List<Map<String,String>>mininetMapPort,Map<String,List<String>>deviceID,Map<String,String> controllerConf)throws IOException, InterruptedException, ExecutionException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(8);
        final CountDownLatch countDownLatch = new CountDownLatch(mininetMapPort.size());

        Map<String,List<Map<String,String>>> topologyMininetcopy = topologyMininet;
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                List<String> HttpResult = new ArrayList<>();
                for(Map.Entry<String,List<Map<String,String>>> entry:topologyMininetcopy.entrySet()) {
                    Map<String,Object> topologyMininetInfo = null;
                    String url = new String();
                    for(int i = 0;i<mininetMapPort.size();i++){
                        if(entry.getKey().substring(15).equals(mininetMapPort.get(i).get("target"))){
                            topologyMininetInfo.put("map",mininetMapPort.get(i).get("source"));
                            topologyMininetInfo.put(entry.getKey(),entry.getValue());
                        }
                    }
                    for(Map.Entry<String,List<String>> entry1 : deviceID.entrySet()){
                        if(entry.getKey().substring(15).equals(entry1.getKey().substring(13))||entry.getKey().substring(15).equals(entry1.getKey().substring(9))){
                            topologyMininetInfo.put("deviceID",entry1);
                        }
                    }
                    topologyMininetInfo.put("controllerConf",controllerConf);
                    ObjectMapper mapper = new ObjectMapper();
                    String topologyMinJson = new String();
                    try {
                        topologyMinJson = mapper.writeValueAsString(topologyMininetInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(Map.Entry<String,Integer> entry1 : userVM2domVM.entrySet()){
                        if(entry1.getKey().equals(entry.getKey().substring(12))){

                            String mac = VMIdToMac.VMIdToMac().get(entry1.getKey());
                            String ip = VMMacToIP.VMMacToIP().get(mac);
                            url = "http://"+ip;
                        }
                    }
                    HttpRequest httpRequest = new HttpRequest();
                    String result = httpRequest.sendPost(url,"data="+topologyMinJson);
                   // HttpResult.add(result)
                    if(result.equals( "success")){
                        topologyMininetcopy.remove(entry.getKey());
                        countDownLatch.countDown();
                    }
                }
            }
        };
        ScheduledFuture future1 = service.scheduleAtFixedRate(task1, 0, 1, TimeUnit.SECONDS);
        countDownLatch.await();
        service.shutdown();
    }
    public void deleteSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> swswTunnelList) throws Exception {
        for (SwitchToSwitchTunnel swswTunnel : swswTunnelList) {
            int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswTunnel.getSwitchId(), swswTunnel.getSwitchIdPortNum());
            int athrSwitchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswTunnel.getPeerSwitchId(), swswTunnel.getPeerSwitchIdPortNum());
            topologyTransformer.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
        }
    }

    public void deleteSwitchToVMTunnel(List<SwitchToVMTunnel> swvmTunnelList) throws Exception {
        for (SwitchToVMTunnel swvmTunnel : swvmTunnelList) {
            int switchPeerPortonTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.getSwitchId(), swvmTunnel.getSwitchPort());
            int vmVlanId = configurationCenter.getVlanIdByVM(swvmTunnel.getVmId(), swvmTunnel.getVmPort());
            topologyTransformer.deleteTunnelSW2VM(switchPeerPortonTT, vmVlanId);
        }

    }

    public void powerOffVM(List<Integer> vmIdList) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Map<Integer, FutureTask<String>> futureTaskSet = new HashMap<>();
        for (Integer vmId : vmIdList) {
            FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
                public String call() throws Exception {
                    return vMManager.powerOffVM(vmId);
                }
            });
            threadPool.submit(futureTask);
            futureTaskSet.put(vmId, futureTask);
        }
        for (Map.Entry<Integer, FutureTask<String>> entry : futureTaskSet.entrySet()) {
            Integer vmId = entry.getKey();
            FutureTask<String> stopVmFuture = entry.getValue();
            try {
                logger.info("Stopping VM #" + vmId + ": " + stopVmFuture.get());
            } catch (ExecutionException e) {
                threadPool.shutdown();
                e.printStackTrace();
                throw e;
            }

        }
    }

    @Deprecated
    public String powerOnVM(Integer vmID) throws LibvirtException, DocumentException {
        return vMManager.powerOnVM(vmID);
    }

    @Deprecated
    public String powerOffVM(Integer vmID) {
        return vMManager.powerOffVM(vmID);

    }

    @Deprecated
    public void powerOnSwitch(Integer switchID) throws Exception {
        powerManager.powerOnSwitchById(switchID);
    }

    @Deprecated
    public void powerOffSwitch(Integer switchID) throws Exception {
        powerManager.powerOffSwitchById(switchID);
    }

    @Deprecated
    public void addSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        switchManager.changeSwitchConf(type, switchID, controllerIP, controllerPort);
    }

    /*
    *   customSwitchConf should be like this.
    *
    *  {
    *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
    *     "boot-bin": "http://202.117.15.79/ons_bak/system.bit",
    *     "uImage": "http://202.117.15.79/ons_bak/uImage",
    *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
    *  }
    *
    * */
    @Deprecated
    public void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Exception {
        switchManager.changeSwitchConf(customSwitchConfGDM, switchID, controllerIP, controllerPort);
    }

    public ConfigurationCenter getConfigurationCenter() {
        return configurationCenter;
    }

    @Deprecated
    public void resetSwitchConf(Integer switchID) throws IOException, InterruptedException {
        switchManager.resetSwitchConf(switchID);
    }

    @Deprecated
    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Exception {
        topologyTransformer.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
    }

    @Deprecated
    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        topologyTransformer.createTunnelSW2VM(switchPortPeeronTT, vmID);
    }

    @Deprecated
    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception {
        topologyTransformer.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
    }

    @Deprecated
    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        topologyTransformer.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
    }

}
