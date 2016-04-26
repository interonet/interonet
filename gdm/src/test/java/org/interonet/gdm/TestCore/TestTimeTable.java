package org.interonet.gdm.TestCore;

import junit.framework.TestCase;
import org.interonet.gdm.Core.TimeTable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTimeTable extends TestCase {
    TimeTable timetable;

    public TestTimeTable() {
        timetable = new TimeTable();
    }

    public void testResourceLimit() throws Exception {
        Map<String, List<Integer>> reservedMap;
        String begin;
        String end;
        ZonedDateTime beginT;
        ZonedDateTime endT;

        beginT = ZonedDateTime.now().plusMinutes(5);
        endT = ZonedDateTime.now().plusMinutes(10);

        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 3);
        map.put("vm", 9);
        reservedMap = timetable.tryToReserve("TestSliceId", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 0);
        assertTrue(reservedMap.get("vm").size() == 0);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        ZonedDateTime.now().plusMinutes(5);
        endT = ZonedDateTime.now().plusMinutes(10);

        map = new HashMap<>();
        map.put("switch", 3);
        map.put("vm", 4);
        reservedMap = timetable.tryToReserve("TestSliceId2", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 3);
        assertTrue(reservedMap.get("vm").size() == 4);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        beginT = ZonedDateTime.now().plusMinutes(5);
        endT = ZonedDateTime.now().plusMinutes(10);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId3", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 0);
        assertTrue(reservedMap.get("vm").size() == 0);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

    }

    public void testTimeSlotInOneDay() throws Exception {
        String begin;
        String end;
        ZonedDateTime beginT;
        ZonedDateTime endT;
        Map<String, List<Integer>> reservedMap;

        begin = "2100-12-03T06:15:30+08:00";
        end = "2100-12-03T07:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId4", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-03T22:15:30+08:00";
        end = "2100-12-03T23:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId5", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-03T13:15:30+08:00";
        end = "2100-12-03T15:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId6", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-03T13:15:30+08:00";
        end = "2100-12-03T14:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId7", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 0);
        assertTrue(reservedMap.get("vm").size() == 0);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

    }

    public void testTimeSlotInMultipleDay() throws Exception {
        String begin;
        String end;
        ZonedDateTime beginT;
        ZonedDateTime endT;
        Map<String, List<Integer>> reservedMap;

        begin = "2100-11-03T06:15:30+08:00";
        end = "2100-12-04T07:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId7", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-04T22:15:30+08:00";
        end = "2100-12-05T23:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId8", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-03T13:15:30+08:00";
        end = "2100-12-03T15:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId9", map, beginT, endT);
        assertTrue(reservedMap.get("switch").size() == 0);
        assertTrue(reservedMap.get("vm").size() == 0);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

    }
}
