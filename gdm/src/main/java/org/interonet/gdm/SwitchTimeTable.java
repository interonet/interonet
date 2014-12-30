package org.interonet.gdm;

import java.util.*;

public class SwitchTimeTable {

    private static final int TOTALSWITCHESNUMBER = 4;
    Map<Integer, List<Duration>> switchTimeTable;

    public SwitchTimeTable() {
        switchTimeTable = new HashMap<Integer, List<Duration>>();
        for (int i = 0; i < TOTALSWITCHESNUMBER; i++) {
            List<Duration> swTimeLine = new LinkedList<Duration>();
            switchTimeTable.put(i, swTimeLine);
        }
    }

    public List<Integer> checkSWAvailability(int switchesNum, String beginTime, String endTime) {
        Duration orderDur = new Duration(beginTime, endTime);
        List<Integer> availableSwitches = new ArrayList<Integer>();

        for (Map.Entry<Integer, List<Duration>> entry : switchTimeTable.entrySet()) {
            int switchID = entry.getKey();
            List<Duration> switchTimeLine = entry.getValue();

            if (switchTimeLine.size() == 0) {
                availableSwitches.add(switchID);
                continue;
            }

            Duration firstFreeTime = new Duration("00:00", switchTimeLine.get(0).start);
            if (firstFreeTime.contains(orderDur)) {
                availableSwitches.add(switchID);
                continue;
            }

            Duration lastFreeTime = new Duration(switchTimeLine.get(switchTimeLine.size() - 1).end, "23:59");
            if (lastFreeTime.contains(orderDur)) {
                availableSwitches.add(switchID);
                continue;
            }

            for (int i = 0; i < switchTimeLine.size() - 1; i++) {
                Duration freeTime = new Duration(switchTimeLine.get(i).end, switchTimeLine.get(i + 1).start);
                if (freeTime.contains(orderDur)) {
                    availableSwitches.add(switchID);
                    break;
                }

            }


        }
        return (availableSwitches.size() >= switchesNum) ? availableSwitches.subList(0, switchesNum) : null;
    }

    public boolean setOccupied(List<Integer> switchIDs, String beginTime, String endTime) {
        for (Integer switchID : switchIDs) {
            Duration orderDur = new Duration(beginTime, endTime);
            List<Duration> switchTimeLine = switchTimeTable.get(switchID);

            if (switchTimeLine.size() == 0) {
                switchTimeLine.add(new Duration(beginTime, endTime));
                continue;
            }

            Duration firstFreeTime = new Duration("00:00", switchTimeLine.get(0).start);
            if (firstFreeTime.contains(orderDur)) {
                switchTimeLine.add(0, orderDur);
                continue;
            }

            Duration lastFreeTime = new Duration(switchTimeLine.get(switchTimeLine.size() - 1).end, "24:00");
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

        for (Map.Entry<Integer, List<Duration>> entry : switchTimeTable.entrySet()) {
            int switchID = entry.getKey();
            StringBuffer swTimeLineStr = new StringBuffer();
            List<Duration> switchTimeLine = entry.getValue();
            swTimeLineStr.append("SW#:" + switchID + " || ");
            for (int i = 0; i < switchTimeLine.size(); i++)
                swTimeLineStr.append("(" + switchTimeLine.get(i).start + "--->" + switchTimeLine.get(i).end + ")");
            timeTable.append(swTimeLineStr + "\n");
        }
        return timeTable.toString();
    }
}
