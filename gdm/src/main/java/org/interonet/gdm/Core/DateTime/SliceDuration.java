package org.interonet.gdm.Core.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public class SliceDuration implements Comparable<SliceDuration> {
    private String sliceId;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Logger logger = LoggerFactory.getLogger(SliceDuration.class);

    public SliceDuration(String sliceId, ZonedDateTime start, ZonedDateTime end) throws Exception {
        this.sliceId = sliceId;
        this.start = start;
        this.end = end;
        if (sliceId == null) {
            logger.error("sliceId is null");
            throw new Exception("sliceId is null when creating SliceDuration.");
        }
        if (start.isAfter(end)) {
            logger.error("start time is after than end time");
            throw new Exception("start = [" + start + "], end = [" + end + "]");
        }
    }

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public boolean contains(SliceDuration other) {
        return start.isBefore(other.start) && end.isAfter(other.end);
    }

    @Override
    public String toString() {
        return "SliceDuration{" +
                "sliceId='" + sliceId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public int compareTo(SliceDuration other) {
        //this(13:00-16:00)<other(17:00-19:00)
        //this(13:00-18:00)>(11:00-12:00)
        //this(13:00-18:00)=(17:00-19:00)->intersect
        if (end.isBefore(other.start)) return -1;
        else if (start.isAfter(other.end)) return 1;
        else return 0;
    }
}