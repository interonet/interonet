package org.interonet.gdm.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.Utils.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class SwitchTimeTable {

    private GDMCore core;
    private int totalSwitchesNumber = 1;
    public Map<Integer, List<Duration>> switchTimeTable;

    private Logger logger;

    public SwitchTimeTable(GDMCore core) {
        logger = LoggerFactory.getLogger(SwitchTimeTable.class);
        this.core = core;

        totalSwitchesNumber = Integer.parseInt(core.getConfigurationCenter().getConf("SwitchesNumber"));
        switchTimeTable = new HashMap<>();
        for (int i = 0; i < totalSwitchesNumber; i++) {
            List<Duration> swTimeLine = new LinkedList<>();
            switchTimeTable.put(i, swTimeLine);
        }
    }

    synchronized public List<Integer> checkSWAvailability(int switchesNum, String beginTime, String endTime) {
        Duration orderDur = new Duration(beginTime, endTime);
        List<Integer> availableSwitches = new ArrayList<>();

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

    synchronized public boolean setOccupied(List<Integer> switchIDs, String beginTime, String endTime) {
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


    public String getTimeTable() throws IOException {
        logger.info(this.toString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(switchTimeTable);
    }

    @Override
    public String toString() {
        StringBuilder timeTable = new StringBuilder();
        timeTable.append("\n****************************************************\n");
        for (Map.Entry<Integer, List<Duration>> entry : switchTimeTable.entrySet()) {
            int switchID = entry.getKey();
            StringBuilder swTimeLineStr = new StringBuilder();
            List<Duration> switchTimeLine = entry.getValue();
            swTimeLineStr.append("SW#:").append(switchID).append(" || ");
            for (Duration aSwitchTimeLine : switchTimeLine)
                swTimeLineStr.append("(").append(aSwitchTimeLine.start).append("--->").append(aSwitchTimeLine.end).append(")");
            timeTable.append(swTimeLineStr).append("\n");
        }
        timeTable.append("****************************************************\n");
        return timeTable.toString();
    }
}
