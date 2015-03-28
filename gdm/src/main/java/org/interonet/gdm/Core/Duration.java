package org.interonet.gdm.Core;

public class Duration {
    public final String start;//"6:00","9:00"
    public final String end;

    public Duration(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(Duration orderDur) {
        int startMins = starttoMinsFromZero();
        int endMins = endtoMinsFromZero();

        int startMinspar = orderDur.starttoMinsFromZero();
        int endMinspar = orderDur.endtoMinsFromZero();

        if (startMins < startMinspar && endMins > endMinspar)
            return true;
        return false;
    }

    public int starttoMinsFromZero() {
        return Integer.parseInt(start.split(":")[0]) * 60 + Integer.parseInt(start.split(":")[1]);

    }

    public int endtoMinsFromZero() {
        return Integer.parseInt(end.split(":")[0]) * 60 + Integer.parseInt(end.split(":")[1]);
    }

    @Override
    public String toString() {
        return "Duration{" + "start='" + start + '\'' +", end='" + end + '\'' +'}';
    }
}