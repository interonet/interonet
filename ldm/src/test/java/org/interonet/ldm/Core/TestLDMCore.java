package org.interonet.ldm.Core;

import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestLDMCore {
    static LDMCore ldmCore;

    @BeforeClass
    static public void init() {
        ldmCore = new LDMCore();
        ldmCore.start();
    }

    @Test
    public void testCreateAndDeleteTunnel() throws Exception {
        List<SwitchToSwitchTunnel> list = new ArrayList<>();

        SwitchToSwitchTunnel ssTunnel1 = new SwitchToSwitchTunnel();
        ssTunnel1.setSwitchId(1);
        ssTunnel1.setSwitchIdPortNum(1);
        ssTunnel1.setPeerSwitchId(3);
        ssTunnel1.setPeerSwitchIdPortNum(0);
        list.add(ssTunnel1);

        SwitchToSwitchTunnel ssTunnel2 = new SwitchToSwitchTunnel();
        ssTunnel2.setSwitchId(1);
        ssTunnel2.setSwitchIdPortNum(2);
        ssTunnel2.setPeerSwitchId(3);
        ssTunnel2.setPeerSwitchIdPortNum(2);
        list.add(ssTunnel2);

        ldmCore.createSwitchToSwitchTunnel(list);
        ldmCore.deleteSwitchToSwitchTunnel(list);

        List<SwitchToVMTunnel> list2 = new ArrayList<>();
        SwitchToVMTunnel svTunnel1 = new SwitchToVMTunnel();
        svTunnel1.setSwitchId(1);
        svTunnel1.setSwitchPort(0);
        svTunnel1.setVmId(1);
        svTunnel1.setVmPort(0);
        list2.add(svTunnel1);

        SwitchToVMTunnel svTunnel2 = new SwitchToVMTunnel();
        svTunnel2.setSwitchId(3);
        svTunnel2.setSwitchPort(1);
        svTunnel2.setVmId(3);
        svTunnel2.setVmPort(0);
        list2.add(svTunnel2);

        ldmCore.createSwitchToVMTunnel(list2);
        ldmCore.deleteSwitchToVMTunnel(list2);
    }

    @Test
    public void testStartAndStopVM() throws Exception {
        List<Integer> vmIdList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            vmIdList.add(i);
        }
        ldmCore.powerOnVM(vmIdList);
        ldmCore.powerOffVM(vmIdList);
    }
}
