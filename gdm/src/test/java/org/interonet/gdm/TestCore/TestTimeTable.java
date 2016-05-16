package org.interonet.gdm.TestCore;

import junit.framework.TestCase;
import org.interonet.gdm.Core.TimeTable;

import java.time.Duration;
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
        Duration TimePeriod = Duration.ofHours(0).plus(Duration.ofMinutes(35).plus(Duration.ofSeconds(8)));
        beginT = ZonedDateTime.now().plusMinutes(5);
        endT = ZonedDateTime.now().plusMinutes(90);

        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 3);
        map.put("vm", 3);
        reservedMap = timetable.tryToReserve("TestSliceId", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 3);
        assertTrue(reservedMap.get("vm").size() == 3);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        beginT = ZonedDateTime.now().plusMinutes(5);
        endT = ZonedDateTime.now().plusMinutes(100);

        map = new HashMap<>();
        map.put("switch", 2);
        map.put("vm", 3);
        reservedMap = timetable.tryToReserve("TestSliceId2", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 2);
        assertTrue(reservedMap.get("vm").size() == 3);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        beginT = ZonedDateTime.now().plusMinutes(100);
        endT = ZonedDateTime.now().plusMinutes(200);

        map = new HashMap<>();
        map.put("switch",5);
        map.put("vm", 6);
        reservedMap = timetable.tryToReserve("TestSliceId3", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 5);
        assertTrue(reservedMap.get("vm").size() == 6);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

    }

    public void testTimeSlotInOneDay() throws Exception {
        String begin;
        String end;
        ZonedDateTime beginT;
        ZonedDateTime endT;
        Map<String, List<Integer>> reservedMap;
        Duration TimePeriod = Duration.ofHours(0).plus(Duration.ofMinutes(55).plus(Duration.ofSeconds(8)));
        begin = "2100-12-03T06:15:30+08:00";
        end = "2100-12-03T07:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId4", map, beginT, endT,TimePeriod);
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
        reservedMap = timetable.tryToReserve("TestSliceId5", map, beginT, endT,TimePeriod);
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
        reservedMap = timetable.tryToReserve("TestSliceId6", map, beginT, endT,TimePeriod);
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
        reservedMap = timetable.tryToReserve("TestSliceId7", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
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
        Duration TimePeriod = Duration.ofHours(0).plus(Duration.ofMinutes(22).plus(Duration.ofSeconds(8)));
        Map<String, Integer> map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId7", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-04T22:15:30+08:00";
        end = "2100-12-05T23:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 3);
        map.put("vm", 4);
        reservedMap = timetable.tryToReserve("TestSliceId8", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 3);
        assertTrue(reservedMap.get("vm").size() == 4);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

        begin = "2100-12-03T13:15:30+08:00";
        end = "2100-12-03T15:15:30+08:00";
        beginT = ZonedDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        endT = ZonedDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        map = new HashMap<>();
        map.put("switch", 8);
        map.put("vm", 8);
        reservedMap = timetable.tryToReserve("TestSliceId9", map, beginT, endT,TimePeriod);
        assertTrue(reservedMap.get("switch").size() == 8);
        assertTrue(reservedMap.get("vm").size() == 8);
        //timetable.printReadableSwitchTimeTable();
        //timetable.printReadableVMTimeTable();

    }
}
