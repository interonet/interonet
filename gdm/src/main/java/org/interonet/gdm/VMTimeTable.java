package org.interonet.gdm;

import java.util.*;

public class VMTimeTable {
    private final int TOTALVMSNUMBER = 8;
    Map<Integer, List<Duration>> vmTimeTable;

    public VMTimeTable() {
        vmTimeTable = new HashMap<Integer, List<Duration>>();
        for (int i = 0; i < TOTALVMSNUMBER; i++) {
            List<Duration> vmTimeLine = new LinkedList<Duration>();
            vmTimeTable.put(i, vmTimeLine);
        }
    }

    public List<Integer> checkVMAvailability(int vmsNum, String beginTime, String endTime) {
        Duration orderDur = new Duration(beginTime, endTime);
        List<Integer> availableVMs = new ArrayList<Integer>();


        for (Map.Entry<Integer, List<Duration>> entry : vmTimeTable.entrySet()) {
            int vmID = entry.getKey();
            List<Duration> vmTimeLine = entry.getValue();

            if (vmTimeLine.size() == 0) {
                availableVMs.add(vmID);
                continue;
            }

            Duration firstFreeTime = new Duration("00:00", vmTimeLine.get(0).start);
            if (firstFreeTime.contains(orderDur)) {
                availableVMs.add(vmID);
                continue;
            }

            Duration lastFreeTime = new Duration(vmTimeLine.get(vmTimeLine.size() - 1).end, "23:59");
            if (lastFreeTime.contains(orderDur)) {
                availableVMs.add(vmID);
                continue;
            }

            for (int i = 0; i < vmTimeLine.size() - 1; i++) {
                Duration freeTime = new Duration(vmTimeLine.get(i).end, vmTimeLine.get(i + 1).start);
                if (freeTime.contains(orderDur)) {
                    availableVMs.add(vmID);
                    break;
                }

            }


        }
        return (availableVMs.size() >= vmsNum) ? availableVMs.subList(0, vmsNum) : null;
    }

    public boolean setOccupied(List<Integer> vmIDs, String beginTime, String endTime) {
        for (Integer vmID : vmIDs) {
            Duration orderDur = new Duration(beginTime, endTime);
            List<Duration> switchTimeLine = vmTimeTable.get(vmID);

            if (switchTimeLine.size() == 0) {
                switchTimeLine.add(new Duration(beginTime, endTime));
                continue;
            }

            Duration firstFreeTime = new Duration("00:00", switchTimeLine.get(0).start);
            if (firstFreeTime.contains(orderDur)) {
                switchTimeLine.add(0, orderDur);
                continue;
            }

            Duration lastFreeTime = new Duration(switchTimeLine.get(switchTimeLine.size() - 1).end, "23:59");
            if (lastFreeTime.contains(orderDur)) {
                switchTimeLine.add(switchTimeLine.size(), orderDur);
                continue;
            }

            for (int i = 0; i < switchTimeLine.size() - 1; i++) {
                Duration freeTime = new Duration(switchTimeLine.get(i).end, switchTimeLine.get(i + 1).start);
                if (freeTime.contains(orderDur)) {
                    switchTimeLine.add(i + 1, orderDur);
                    break;
                }
            }
        }
        return true;

    }

    public String getTimeTable() {
        StringBuffer timeTable = new StringBuffer();

        for (Map.Entry<Integer, List<Duration>> entry : vmTimeTable.entrySet()) {
            int switchID = entry.getKey();
            StringBuffer swTimeLineStr = new StringBuffer();
            List<Duration> switchTimeLine = entry.getValue();
            swTimeLineStr.append("VM#:" + switchID + " || ");
            for (Duration duration : switchTimeLine)
                swTimeLineStr.append("(" + duration.start + "--->" + duration.end + ")");
            timeTable.append(swTimeLineStr + "\n");
        }
        return timeTable.toString();
    }
}
