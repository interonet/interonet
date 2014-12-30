package org.interonet.gdm;

public class DayTime {
    public String dayTime;
    public int minsFromZero;

    public DayTime(String time) {
        dayTime = time;
        minsFromZero = Integer.parseInt(time.split(":")[0]) * 60 + Integer.parseInt(time.split(":")[1]);
    }

    public boolean earlyThan(DayTime aTime) {
        return minsFromZero <= aTime.minsFromZero ? true : false;
    }
}
