package org.interonet.gdm.Core;

//import javafx.util.Duration;
import org.interonet.gdm.Core.DateTime.SliceDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.*;

public class TimeTable {
    public class Residue {
        private int xi1;
        private int xi2;
        private ZonedDateTime yi1;
        private ZonedDateTime yi2;
        private int xi3;
        private int xi4;
        //private int s ;
        public int getXi1() {
            return xi1;
        }
        public void setXi1(int xi1) {
            this.xi1 = xi1;
        }
        public int getXi2() {
            return xi2;
        }
        public void setXi2(int xi2) {
            this.xi2 = xi2;
        }
        public ZonedDateTime getYi1() {
            return yi1;
        }
        public void setYi1(ZonedDateTime yi1) {
            this.yi1 = yi1;
        }
        public ZonedDateTime getYi2() {
            return yi2;
        }
        public void setYi2(ZonedDateTime yi2) {
            this.yi2 = yi2;
        }
        public int getXi3() {
            return xi3;
        }
        public void setXi3(int xi3) {
            this.xi3 = xi3;
        }
        public int getXi4() {
            return xi4;
        }
        public void setXi4(int xi4) {
            this.xi4 = xi4;
        }
    }
    private Map<Integer, TreeSet<SliceDuration>> switchTimeTable;
    private Map<Integer, TreeSet<SliceDuration>> vmTimeTable;
    private Logger logger = LoggerFactory.getLogger(TimeTable.class);
    private Map<String, List<Residue>> SliceTimeTable = new HashMap<String,List<Residue>>();
    private Map<String, List<Residue>> SliceBlankTable = new HashMap<String,List<Residue>>();//Map<Integer, TreeSet<SliceDuration>>
    private Residue Res = new Residue();
    private List<Residue> SliceBlankInstance = new ArrayList();
    private List<Residue>SliceBlankInstance2 = new ArrayList();
    private List<Residue>SliceBlankInstance3 = new ArrayList();
    private int count3=0;
    private Residue SliceMiddle = new Residue();
    private Residue ini = new Residue();
    private List<Residue> SliceTimeTableIni = new ArrayList();
    double tightness;
    public TimeTable() {
        switchTimeTable = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            TreeSet<SliceDuration> swTimeLine = new TreeSet<>();
            switchTimeTable.put(i, swTimeLine);
        }
        vmTimeTable = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            TreeSet<SliceDuration> vmTimeLine = new TreeSet<>();
            vmTimeTable.put(i, vmTimeLine);
        }
        Res.setXi1(1);
        Res.setXi2(9);
        Res.setXi3(1);
        Res.setXi4(9);
        SliceTimeTableIni.add(0,ini);
        SliceTimeTable.put("ini",SliceTimeTableIni);
    }

    public TimeTable(int totalSwitchesNumber, int totalVmsNumber) {

        switchTimeTable = new HashMap<>();
        for (int i = 1; i <= totalSwitchesNumber; i++) {
            TreeSet<SliceDuration> swTimeLine = new TreeSet<>();
            switchTimeTable.put(i, swTimeLine);
        }
        vmTimeTable = new HashMap<>();
        for (int i = 1; i <= totalVmsNumber; i++) {
            TreeSet<SliceDuration> vmTimeLine = new TreeSet<>();
            vmTimeTable.put(i, vmTimeLine);
        }
        Res.setXi1(1);
        Res.setXi2(totalVmsNumber);
        Res.setXi3(1);
        Res.setXi4(totalSwitchesNumber);
        SliceTimeTableIni.add(0,ini);
        SliceTimeTable.put("ini",SliceTimeTableIni);
    }


    /*
       resourceRequest={"switch":10,"vm":10}
    */
    synchronized public Map<String, List<Integer>> tryToReserve(String sliceId, Map<String, Integer> resourceRequest, ZonedDateTime reqBegin, ZonedDateTime reqEnd, Duration TimePeriod) throws Exception {
        ZonedDateTime nowTime = ZonedDateTime.now();
        if (reqBegin.isBefore(nowTime)) {
            logger.error("begin time is early than now time, failed");
            logger.error("beginTime = " + reqBegin);
            logger.error("nowTime = " + nowTime);
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "], now= [" + nowTime + "]");
        }
        List<Residue> SliceBlank = new ArrayList();
        Res.setYi1(reqBegin);
        Res.setYi2(reqEnd);
        SliceBlank.add(Res);
        SliceBlankTable.put(sliceId , SliceBlank);
        Residue SliceFinish = new Residue();
        int Tie1=0;
        int Tie2=0;
        int resource=0;
        int resource1=0;
        double tightness2=0;
        int distance=0;
        int distance2=1000;
        double gi;
        double S3;
        count3=0;
        Random RandomIndex = new Random();
        double RandomIndex2 = RandomIndex.nextDouble();
        int reqTolerance = (reqEnd.getYear()-reqBegin.getYear())*518400+(reqEnd.getMonthValue()-reqBegin.getMonthValue())*43200+(reqEnd.getDayOfMonth()-reqBegin.getDayOfMonth())*1440+(reqEnd.getHour()-reqBegin.getHour())*60+(reqEnd.getMinute()-reqBegin.getMinute());
        Long TimePeriodMinutes;
        TimePeriodMinutes = TimePeriod.toMinutes();
        gi = TimePeriodMinutes/reqTolerance;
        for(Map.Entry<String, List<Residue>> entry : SliceTimeTable.entrySet())
        {
            String SliceId = entry.getKey();
            List<Residue> BeforeSlice = entry.getValue();
            Residue BeforeSliceInstance = new Residue();
            BeforeSliceInstance = BeforeSlice.get(0);

            SliceBlankInstance = SliceBlankTable.get(sliceId);
            for(int m=0;m<SliceBlankInstance.size();)
            {

                Residue Res1 = (Residue)SliceBlankInstance.get(m);
                Residue Res2 = new Residue();
                Residue Res3 = new Residue();
                Residue Res4 = new Residue();
                Residue Res5 = new Residue();
                if (((BeforeSliceInstance.getXi1()>=Res1.getXi1()&&BeforeSliceInstance.getXi1()<=Res1.getXi2())||(BeforeSliceInstance.getXi2()>=Res1.getXi1()&&BeforeSliceInstance.getXi2()<=Res1.getXi2()))&&(((BeforeSliceInstance.getYi1().isAfter(Res1.getYi1())||BeforeSliceInstance.getYi1().isEqual(Res1.getYi1()))&&(BeforeSliceInstance.getYi1().isBefore(Res1.getYi2())||BeforeSliceInstance.getYi1().isEqual(Res1.getYi2())))||((BeforeSliceInstance.getYi2().isAfter(Res1.getYi1())||BeforeSliceInstance.getYi2().isEqual(Res1.getYi1()))&&(BeforeSliceInstance.getYi2().isBefore(Res1.getYi2())||BeforeSliceInstance.getYi2().isEqual(Res1.getYi2()))))&&((BeforeSliceInstance.getXi3()>=Res1.getXi3()&&BeforeSliceInstance.getXi3()<=Res1.getXi4())||(BeforeSliceInstance.getXi4()>=Res1.getXi3()&&BeforeSliceInstance.getXi4()<=Res1.getXi4())))
                {
                    SliceBlankInstance.remove(m);//此处正确，分配剩余空间
                    Res2.setXi1(Res1.getXi1());
                    Res2.setYi1(Res1.getYi1());
                    Res2.setXi2(Res1.getXi2());
                    Res2.setYi2(BeforeSliceInstance.getYi1());
                    Res2.setXi3(Res1.getXi3());
                    Res2.setXi4(Res1.getXi4());
                    Res3.setXi1(Res1.getXi1());
                    Res3.setYi1(Res1.getYi1());
                    Res3.setXi2(BeforeSliceInstance.getXi1());
                    Res3.setYi2(Res1.getYi2());
                    Res3.setXi3(Res1.getXi3());
                    Res3.setXi4(BeforeSliceInstance.getXi3());
                    Res4.setXi1(BeforeSliceInstance.getXi2());
                    Res4.setYi1(Res1.getYi1());
                    Res4.setXi2(Res1.getXi2());
                    Res4.setYi2(Res1.getYi2());
                    Res4.setXi3(BeforeSliceInstance.getXi4());
                    Res4.setXi4(Res1.getXi4());
                    Res5.setXi1(Res1.getXi1());
                    Res5.setYi1(BeforeSliceInstance.getYi2());
                    Res5.setXi2(Res1.getXi2());
                    Res5.setYi2(Res1.getYi2());
                    Res5.setXi3(Res1.getXi3());
                    Res5.setXi4(Res1.getXi4());
                    SliceBlankInstance.add(m,Res5);
                    SliceBlankInstance.add(m,Res4);
                    SliceBlankInstance.add(m,Res3);
                    SliceBlankInstance.add(m,Res2);
                    m=m+4;
                }
                else
                {
                    m++;
                    continue;
                }
                if(m==SliceBlankInstance.size())//过如果该矩形没有相交矩形则实施的情况
                {
                    break;
                }
            }
            for(int n=0;n<SliceBlankInstance.size();n++)
            {
                Residue Reserror = (Residue)SliceBlankInstance.get(n);
                if((Reserror.getXi2()-Reserror.getXi1())<=0||(Reserror.getYi2().isBefore(Reserror.getYi1()))||(Reserror.getYi2().isEqual(Reserror.getYi1()))/*(Res12.getYi2()-Res12.getYi1())<=0*/||(Reserror.getXi4()-Reserror.getXi3())<=0)
                {
                    SliceBlankInstance2.add(Reserror);
                    continue;
                }
                for(int q=0;q<SliceBlankInstance.size();q++)
                {
                    Residue Res6 = (Residue)SliceBlankInstance.get(q);//有问题部分
                    if(Reserror.getXi1()>=Res6.getXi1()&&Reserror.getXi2()<=Res6.getXi2()&&(Reserror.getYi1().isAfter(Res6.getYi1())||Reserror.getYi1().isEqual(Res6.getYi1()))/*Res12.getYi1()>=Res13.getYi1()*/&&(Reserror.getYi2().isBefore(Res6.getYi2())||Reserror.getYi2().isEqual(Res6.getYi2()))/*Res12.getYi2()<=Res13.getYi2()*/&&Reserror.getXi3()>=Res6.getXi3()&&Reserror.getXi4()<=Res6.getXi4()&&q!=n)
                    {
                        SliceBlankInstance2.add(Reserror);
                        break;
                    }
                }
            }
            for(int n=0;n<SliceBlankInstance.size();n++)
            {
                Residue Res7 = (Residue)SliceBlankInstance.get(n);
                for(int q=0;q<SliceBlankInstance2.size();q++)
                {
                    Residue Reserror = (Residue)SliceBlankInstance2.get(q);
                    if(Res7.getXi1()==Reserror.getXi1()&&Res7.getXi2()==Reserror.getXi2()&&Res7.getYi1()==Reserror.getYi1()&&Res7.getYi2()==Reserror.getYi2())
                    {
                        count3++;
                        break;
                    }
                }
                if(count3==0)
                {
                    SliceBlankInstance3.add(Res7);
                }
                count3=0;
            }
            SliceBlankInstance.removeAll(SliceBlankInstance);
            for(int n=0;n<SliceBlankInstance3.size();n++)
            {
                Residue Res7 = (Residue)SliceBlankInstance3.get(n);
                SliceBlankInstance.add(Res7);
            }
            SliceBlankInstance3.removeAll(SliceBlankInstance3);
            SliceBlankInstance2.removeAll(SliceBlankInstance2);
            SliceBlankTable.put(sliceId,SliceBlankInstance);
        }


        List<Residue> SliceFinishList = new ArrayList();

        Integer requestSwitchNum = null;
        Integer requestVMNum = null;
        for (Map.Entry<String, Integer> entry : resourceRequest.entrySet()) {
            String resourceName = entry.getKey(); //switch or vm
            if (resourceName.equals("switch")) requestSwitchNum = entry.getValue();
            if (resourceName.equals("vm")) requestVMNum = entry.getValue();
        }
        if (requestSwitchNum == null || requestVMNum == null)
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "]");
        S3=Math.sqrt(requestVMNum*TimePeriodMinutes);


        if(gi<RandomIndex2)
        {
            for(int i= 0;i<SliceBlankInstance.size();i++)
            {
                Residue ResMax = (Residue)SliceBlankInstance.get(i);
                int Res1Tolerance = (ResMax.getYi2().getYear()-ResMax.getYi1().getYear())*518400+(ResMax.getYi2().getMonthValue()-ResMax.getYi1().getMonthValue())*43200+(ResMax.getYi2().getDayOfMonth()-ResMax.getYi1().getDayOfMonth())*1440+(ResMax.getYi2().getHour()-ResMax.getYi1().getHour())*60+(ResMax.getYi2().getMinute()-ResMax.getYi1().getMinute());
                if((ResMax.getXi2()-ResMax.getXi1())>=requestVMNum&&Res1Tolerance>=TimePeriodMinutes&&(ResMax.getXi4()-ResMax.getXi3())>=requestSwitchNum)
                {
                    if(resource>(ResMax.getXi2()-ResMax.getXi1()))
                    {
                        continue;
                    }
                    else if(resource==(ResMax.getXi2()-ResMax.getXi1()))
                    {
                        if(resource1>=(ResMax.getXi4()-ResMax.getXi3()))
                        {
                            continue;
                        }
                        else
                        {

                            SliceFinish.setXi1(ResMax.getXi1());
                            SliceFinish.setYi1(ResMax.getYi2().minusMinutes(TimePeriodMinutes));//-TimePeriod);
                            SliceFinish.setXi2(ResMax.getXi1()+requestVMNum);
                            SliceFinish.setYi2(ResMax.getYi2().minusSeconds(1));
                            SliceFinish.setXi3(ResMax.getXi3());
                            SliceFinish.setXi4(ResMax.getXi3()+requestSwitchNum);
                            resource1=(ResMax.getXi4()-ResMax.getXi3());
                        }
                    }
                    else if(resource<(ResMax.getXi2()-ResMax.getXi1()))
                    {
                        SliceFinish.setXi1(ResMax.getXi1());
                        SliceFinish.setYi1(ResMax.getYi2().minusMinutes(TimePeriodMinutes));//-TimePeriod);
                        SliceFinish.setXi2(ResMax.getXi1()+requestVMNum);
                        SliceFinish.setYi2(ResMax.getYi2().minusSeconds(1));
                        SliceFinish.setXi3(ResMax.getXi3());
                        SliceFinish.setXi4(ResMax.getXi3()+requestSwitchNum);
                        resource=(ResMax.getXi2()-ResMax.getXi1());
                    }
                }
            }
        }
        else
        {
            for(int k=0;k<SliceBlankInstance.size();k++)
            {
                Residue ResMax = (Residue)SliceBlankInstance.get(k);
                int Res1Tolerance = (ResMax.getYi2().getYear()-ResMax.getYi1().getYear())*518400+(ResMax.getYi2().getMonthValue()-ResMax.getYi1().getMonthValue())*43200+(ResMax.getYi2().getDayOfMonth()-ResMax.getYi1().getDayOfMonth())*1440+(ResMax.getYi2().getHour()-ResMax.getYi1().getHour())*60+(ResMax.getYi2().getMinute()-ResMax.getYi1().getMinute());
                if((ResMax.getXi2()-ResMax.getXi1())>=requestVMNum&&Res1Tolerance>=TimePeriodMinutes&&(ResMax.getXi4()-ResMax.getXi3())>=requestSwitchNum)
                {
                    if (requestVMNum != (ResMax.getXi2() - ResMax.getXi1()) &&ResMax.getYi1().plusMinutes(TimePeriodMinutes) != ResMax.getYi2()) {
                        Tie1 = 2;
                    } else if ((requestVMNum == (ResMax.getXi2() - ResMax.getXi1()) &&ResMax.getYi1().plusMinutes(TimePeriodMinutes) != ResMax.getYi2()) || (requestVMNum != (ResMax.getXi2() - ResMax.getXi1()) && (ResMax.getYi1().plusMinutes(TimePeriodMinutes)) == ResMax.getYi2())) {
                        Tie1 = 3;
                    } else if (requestVMNum == (ResMax.getXi2() - ResMax.getXi1()) &&ResMax.getYi1().plusMinutes(TimePeriodMinutes) == ResMax.getYi2()) {
                        Tie1 = 4;
                    }
                    if (Tie2 > Tie1)
                    {
                        continue;
                    }
                    SliceMiddle.setXi1(ResMax.getXi1());
                    SliceMiddle.setYi1(ResMax.getYi1());
                    SliceMiddle.setXi2(ResMax.getXi1() + requestVMNum);
                    SliceMiddle.setYi2(ResMax.getYi1().plusMinutes(TimePeriodMinutes));
                    SliceMiddle.setXi3(ResMax.getXi3());
                    SliceMiddle.setXi4(ResMax.getXi3() + requestSwitchNum);
                    if (Tie2 == Tie1)
                    {
                        for (Map.Entry<String, List<Residue>> entry : SliceTimeTable.entrySet()) {
                            List<Residue> BeforeSlice1 = entry.getValue();
                            Residue BeforeSliceInstance1 = BeforeSlice1.get(0);
                            if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && ((BeforeSliceInstance1.getYi1().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi1().isBefore(SliceMiddle.getYi2())) || (BeforeSliceInstance1.getYi2().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi2().isBefore(SliceMiddle.getYi2())))) {
                                distance = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2();
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && ((BeforeSliceInstance1.getYi1().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi1().isBefore(SliceMiddle.getYi2())) || (BeforeSliceInstance1.getYi2().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi2().isBefore(SliceMiddle.getYi2())))) {
                                distance = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2();
                            } else if (SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2()) && ((BeforeSliceInstance1.getXi1() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi1() < SliceMiddle.getXi2()) || (BeforeSliceInstance1.getXi2() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi2() < SliceMiddle.getXi2()))) {
                                distance = (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi1().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi1().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi1().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi1().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi1().getMinute());
                            } else if (SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1()) && ((BeforeSliceInstance1.getXi1() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi1() < SliceMiddle.getXi2()) || (BeforeSliceInstance1.getXi2() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi2() < SliceMiddle.getXi2()))) {
                                distance = (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());
                            } else if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2())) {
                                distance = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2() + (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi2().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi2().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi2().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi2().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi2().getMinute());
                            } else if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1())) {
                                distance = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2() + (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2())) {
                                distance = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2() + (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi2().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi2().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi2().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi2().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi2().getMinute());
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1())) {
                                distance = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2() + (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());
                            }
                            tightness = 1 - distance2 / S3;
                            if (tightness <= tightness2) {
                                continue;
                            }
                            distance2 = distance;
                            tightness2 = tightness;
                            Tie2 = Tie1;
                            SliceFinish.setXi1(ResMax.getXi1());
                            SliceFinish.setYi1(ResMax.getYi1().plusSeconds(1));
                            SliceFinish.setXi2(ResMax.getXi1() + requestVMNum);
                            SliceFinish.setYi2(ResMax.getYi1().plusMinutes(TimePeriodMinutes));
                            SliceFinish.setXi3(ResMax.getXi3());
                            SliceFinish.setXi4(ResMax.getXi3() + requestVMNum);
                        }
                    } else if (Tie2 < Tie1)
                    {
                        Tie2 = Tie1;
                        SliceFinish.setXi1(ResMax.getXi1());
                        SliceFinish.setYi1(ResMax.getYi1().plusSeconds(1));
                        SliceFinish.setXi2(ResMax.getXi1() + requestVMNum);
                        SliceFinish.setYi2(ResMax.getYi1().plusMinutes(TimePeriodMinutes));
                        SliceFinish.setXi3(ResMax.getXi3());
                        SliceFinish.setXi4(ResMax.getXi3() + requestVMNum);
                    }

                }
            }
        }
        SliceFinishList.add(SliceFinish);
        SliceTimeTable.put(sliceId,SliceFinishList);



        Set<Integer> availableSwitch = new HashSet<>();
        for (int switchId = SliceFinish.getXi3();switchId<SliceFinish.getXi4();switchId++) {
            availableSwitch.add(switchId);
        }
        Set<Integer> availableVM = new HashSet<>();
        for (int vmId = SliceFinish.getXi1();vmId<SliceFinish.getXi2();vmId++) {
            availableVM.add(vmId);
        }
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : switchTimeTable.entrySet()) {
            int switchId = entry.getKey();
            TreeSet<SliceDuration> switchTimeLine = entry.getValue();
        }
        //Set<Integer> availableVM = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : vmTimeTable.entrySet()) {
            int vmId = entry.getKey();
            TreeSet<SliceDuration> vmTimeLine = entry.getValue();
        }


        List<Integer> reservedSwitch = new ArrayList<>();
        List<Integer> reservedVMs = new ArrayList<>();

        if (availableSwitch.size() < requestSwitchNum || availableVM.size() < requestVMNum) {
            Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
            resourceMap.put("switch", reservedSwitch);
            resourceMap.put("vm", reservedVMs);
            System.out.println("ok");
            return resourceMap;
        }

        /*Insert into time table actually */
        for (Integer switchId : availableSwitch) {
            TreeSet<SliceDuration> switchTimeLine = switchTimeTable.get(switchId);
            SliceDuration S = new SliceDuration(sliceId, SliceFinish.getYi1(), SliceFinish.getYi2());
            boolean b= switchTimeLine.add(S);
            if (b) {
                reservedSwitch.add(switchId);
            }
            if (reservedSwitch.size() == requestSwitchNum) break;
        }

        for (Integer vmId : availableVM) {
            TreeSet<SliceDuration> vmTimeLine = vmTimeTable.get(vmId);
            if (vmTimeLine.add(new SliceDuration(sliceId, SliceFinish.getYi1(), SliceFinish.getYi2())))
                reservedVMs.add(vmId);
            if (reservedVMs.size() == requestVMNum) break;
        }

        Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
        resourceMap.put("switch", reservedSwitch);
        resourceMap.put("vm", reservedVMs);
        return resourceMap;

    }
    synchronized public Map<String, List<Integer>> tryToReserve(String sliceId, Map<String, Integer> resourceRequest, ZonedDateTime reqBegin, ZonedDateTime reqEnd) throws Exception {
        ZonedDateTime nowTime = ZonedDateTime.now();
        if (reqBegin.isBefore(nowTime)) {
            logger.error("begin time is early than now time, failed");
            logger.error("beginTime = " + reqBegin);
            logger.error("nowTime = " + nowTime);
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "], now= [" + nowTime + "]");
        }

        Integer requestSwitchNum = null;
        Integer requestVMNum = null;
        for (Map.Entry<String, Integer> entry : resourceRequest.entrySet()) {
            String resourceName = entry.getKey(); //switch or vm
            if (resourceName.equals("switch")) requestSwitchNum = entry.getValue();
            if (resourceName.equals("vm")) requestVMNum = entry.getValue();
        }
        if (requestSwitchNum == null || requestVMNum == null)
            throw new Exception("resourceRequest = [" + resourceRequest + "], reqBegin = [" + reqBegin + "], reqEnd = [" + reqEnd + "]");

        /*Try to find available switches and vms.*/
        Set<Integer> availableSwitch = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : switchTimeTable.entrySet()) {
            int switchId = entry.getKey();
            TreeSet<SliceDuration> switchTimeLine = entry.getValue();
            if (!switchTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
                availableSwitch.add(switchId);
        }
        Set<Integer> availableVM = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : vmTimeTable.entrySet()) {
            int vmId = entry.getKey();
            TreeSet<SliceDuration> vmTimeLine = entry.getValue();
            if (!vmTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
                availableVM.add(vmId);
        }


        List<Integer> reservedSwitch = new ArrayList<>();
        List<Integer> reservedVMs = new ArrayList<>();

        if (availableSwitch.size() < requestSwitchNum || availableVM.size() < requestVMNum) {
            Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
            resourceMap.put("switch", reservedSwitch);
            resourceMap.put("vm", reservedVMs);
            return resourceMap;
        }

        /*Insert into time table actually */
        for (Integer switchId : availableSwitch) {
            TreeSet<SliceDuration> switchTimeLine = switchTimeTable.get(switchId);
            if (switchTimeLine.add(new SliceDuration(sliceId, reqBegin, reqEnd)))
                reservedSwitch.add(switchId);
            if (reservedSwitch.size() == requestSwitchNum) break;
        }

        for (Integer vmId : availableVM) {
            TreeSet<SliceDuration> vmTimeLine = vmTimeTable.get(vmId);
            if (vmTimeLine.add(new SliceDuration(sliceId, reqBegin, reqEnd)))
                reservedVMs.add(vmId);
            if (reservedVMs.size() == requestVMNum) break;
        }

        Map<String, List<Integer>> resourceMap = new HashMap<>(resourceRequest.size());
        resourceMap.put("switch", reservedSwitch);
        resourceMap.put("vm", reservedVMs);
        return resourceMap;

    }
    synchronized public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTableSnapShot() {
        return new HashMap<>(switchTimeTable);
    }

    synchronized public Map<Integer, TreeSet<SliceDuration>> getVmTimeTableSnapShot() {
        return new HashMap<>(vmTimeTable);
    }

}
