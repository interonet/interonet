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
    int flagR=0;
    float f1;
    float f2;
    float fi;
    //int flag = 0;
    //int count=0;
    //int S1;
    //int S2=0;
    //float gi;
    //int d=0;
    //int d2=1000;
    double C;
    //double C2=0;
    //double S3;
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
        int ziyuan=0;
        int ziyuan1=0;
        double C2=0;
        int d=0;
        int d2=1000;
        //int S2=0;
        int count=0;
        int flag=0;
        double gi;
        double S3;
        count3=0;
        Random RandomIndex = new Random();
        double RandomIndex2 = RandomIndex.nextDouble();
        int reqTolerance = (reqEnd.getYear()-reqBegin.getYear())*518400+(reqEnd.getMonthValue()-reqBegin.getMonthValue())*43200+(reqEnd.getDayOfMonth()-reqBegin.getDayOfMonth())*1440+(reqEnd.getHour()-reqBegin.getHour())*60+(reqEnd.getMinute()-reqBegin.getMinute());
        Long TimePeriodMinutes;
        TimePeriodMinutes = TimePeriod.toMinutes();
        gi = TimePeriodMinutes/reqTolerance;
        //System.out.println(sliceId);
        // S3=Math.sqrt(requestVMNum*TimePeriodMinutes);
        for(Map.Entry<String, List<Residue>> entry : SliceTimeTable.entrySet())
        {
            String SliceId = entry.getKey();
            List<Residue> BeforeSlice = entry.getValue();
            Residue BeforeSliceInstance = new Residue();
            BeforeSliceInstance = BeforeSlice.get(0);

            SliceBlankInstance = SliceBlankTable.get(sliceId);//System.out.println("O1"+O1.getXi2());
            for(int m=0;m<SliceBlankInstance.size();)
            {
                //System.out.println(sliceId);

                Residue Res3 = (Residue)SliceBlankInstance.get(m);
                //System.out.println(Res3.getXi2());
                //System.out.println("Xi1   "+Res3.getXi1()+" "+"Yi1   "+Res3.getYi1()+"Xi2   "+Res3.getXi2()+"Yi2   "+Res3.getYi2()+"Xi3 "+Res3.getXi3()+"Xi4"+Res3.getXi4());
                //System.out.print("空闲空间有几个"+residue.size());
                Residue Res6 = new Residue();
                Residue Res7 = new Residue();
                Residue Res8 = new Residue();
                Residue Res9 = new Residue();
                if (((BeforeSliceInstance.getXi1()>=Res3.getXi1()&&BeforeSliceInstance.getXi1()<=Res3.getXi2())||(BeforeSliceInstance.getXi2()>=Res3.getXi1()&&BeforeSliceInstance.getXi2()<=Res3.getXi2()))&&(((BeforeSliceInstance.getYi1().isAfter(Res3.getYi1())||BeforeSliceInstance.getYi1().isEqual(Res3.getYi1()))&&(BeforeSliceInstance.getYi1().isBefore(Res3.getYi2())||BeforeSliceInstance.getYi1().isEqual(Res3.getYi2())))||((BeforeSliceInstance.getYi2().isAfter(Res3.getYi1())||BeforeSliceInstance.getYi2().isEqual(Res3.getYi1()))&&(BeforeSliceInstance.getYi2().isBefore(Res3.getYi2())||BeforeSliceInstance.getYi2().isEqual(Res3.getYi2()))))&&((BeforeSliceInstance.getXi3()>=Res3.getXi3()&&BeforeSliceInstance.getXi3()<=Res3.getXi4())||(BeforeSliceInstance.getXi4()>=Res3.getXi3()&&BeforeSliceInstance.getXi4()<=Res3.getXi4())))
                {
                    //System.out.println("no");
                    SliceBlankInstance.remove(m);//此处正确，分配剩余空间
                    //residue1.remove(count2);
                    Res6.setXi1(Res3.getXi1());
                    Res6.setYi1(Res3.getYi1());
                    Res6.setXi2(Res3.getXi2());
                    Res6.setYi2(BeforeSliceInstance.getYi1());
                    Res6.setXi3(Res3.getXi3());
                    Res6.setXi4(Res3.getXi4());
                    Res7.setXi1(Res3.getXi1());
                    Res7.setYi1(Res3.getYi1());
                    Res7.setXi2(BeforeSliceInstance.getXi1());
                    Res7.setYi2(Res3.getYi2());
                    Res7.setXi3(Res3.getXi3());
                    Res7.setXi4(BeforeSliceInstance.getXi3());
                    Res8.setXi1(BeforeSliceInstance.getXi2());
                    Res8.setYi1(Res3.getYi1());
                    Res8.setXi2(Res3.getXi2());
                    Res8.setYi2(Res3.getYi2());
                    Res8.setXi3(BeforeSliceInstance.getXi4());
                    Res8.setXi4(Res3.getXi4());
                    Res9.setXi1(Res3.getXi1());
                    Res9.setYi1(BeforeSliceInstance.getYi2());
                    Res9.setXi2(Res3.getXi2());
                    Res9.setYi2(Res3.getYi2());
                    Res9.setXi3(Res3.getXi3());
                    Res9.setXi4(Res3.getXi4());
                    SliceBlankInstance.add(m,Res9);
                    SliceBlankInstance.add(m,Res8);
                    SliceBlankInstance.add(m,Res7);
                    SliceBlankInstance.add(m,Res6);
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
                Residue Res12 = (Residue)SliceBlankInstance.get(n);
                if((Res12.getXi2()-Res12.getXi1())<=0||(Res12.getYi2().isBefore(Res12.getYi1()))||(Res12.getYi2().isEqual(Res12.getYi1()))/*(Res12.getYi2()-Res12.getYi1())<=0*/||(Res12.getXi4()-Res12.getXi3())<=0)
                {
                    SliceBlankInstance2.add(Res12);
                    continue;
                }
                for(int q=0;q<SliceBlankInstance.size();q++)
                {
                    Residue Res13 = (Residue)SliceBlankInstance.get(q);//有问题部分
                    if(Res12.getXi1()>=Res13.getXi1()&&Res12.getXi2()<=Res13.getXi2()&&(Res12.getYi1().isAfter(Res13.getYi1())||Res12.getYi1().isEqual(Res13.getYi1()))/*Res12.getYi1()>=Res13.getYi1()*/&&(Res12.getYi2().isBefore(Res13.getYi2())||Res12.getYi2().isEqual(Res13.getYi2()))/*Res12.getYi2()<=Res13.getYi2()*/&&Res12.getXi3()>=Res13.getXi3()&&Res12.getXi4()<=Res13.getXi4()&&q!=n)
                    {
                        SliceBlankInstance2.add(Res12);
                        break;
                    }
                }
            }
            for(int n=0;n<SliceBlankInstance.size();n++)
            {
                Residue Res15 = (Residue)SliceBlankInstance.get(n);
                for(int q=0;q<SliceBlankInstance2.size();q++)
                {
                    Residue Res16 = (Residue)SliceBlankInstance2.get(q);
                    if(Res15.getXi1()==Res16.getXi1()&&Res15.getXi2()==Res16.getXi2()&&Res15.getYi1()==Res16.getYi1()&&Res15.getYi2()==Res16.getYi2())
                    {
                        count3++;
                        break;
                    }
                }
                if(count3==0)
                {
                    SliceBlankInstance3.add(Res15);
                }
                count3=0;
            }
            SliceBlankInstance.removeAll(SliceBlankInstance);
            for(int n=0;n<SliceBlankInstance3.size();n++)
            {
                Residue Res17 = (Residue)SliceBlankInstance3.get(n);
                SliceBlankInstance.add(Res17);
            }
            SliceBlankInstance3.removeAll(SliceBlankInstance3);
            SliceBlankInstance2.removeAll(SliceBlankInstance2);
			/*for(int z=0;z<residue.size();z++)
			{
				Residue Res14 = (Residue)residue.get(z);
				System.out.println("Xi1 "+Res14.getXi1()+" "+"Yi1 "+Res14.getYi1()+"Xi2 "+Res14.getXi2()+"Yi2 "+Res14.getYi2()+"Xi3 "+Res14.getXi3()+"Xi4"+Res14.getXi4());
			}*/
            SliceBlankTable.put(sliceId,SliceBlankInstance);
        }

        /*for(int z=0;z<SliceBlankInstance.size();z++)
			{
				Residue Res14 = (Residue)SliceBlankInstance.get(z);
				System.out.println("Xi1 "+Res14.getXi1()+" "+"Yi1 "+Res14.getYi1()+"Xi2 "+Res14.getXi2()+"Yi2 "+Res14.getYi2()+"Xi3 "+Res14.getXi3()+"Xi4"+Res14.getXi4());
			}*/

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
        /*Try to find available switches and vms.*/


        if(gi<RandomIndex2)
        {
            for(int i= 0;i<SliceBlankInstance.size();i++)
            {
                // if(i ==  0) {
                //    System.out.println(requestSwitchNum);
                //}
                Residue Res1 = (Residue)SliceBlankInstance.get(i);
                //System.out.print(Res1.getXi2());
                int Res1Tolerance = (Res1.getYi2().getYear()-Res1.getYi1().getYear())*518400+(Res1.getYi2().getMonthValue()-Res1.getYi1().getMonthValue())*43200+(Res1.getYi2().getDayOfMonth()-Res1.getYi1().getDayOfMonth())*1440+(Res1.getYi2().getHour()-Res1.getYi1().getHour())*60+(Res1.getYi2().getMinute()-Res1.getYi1().getMinute());
                if((Res1.getXi2()-Res1.getXi1())>=requestVMNum&&Res1Tolerance>=TimePeriodMinutes&&(Res1.getXi4()-Res1.getXi3())>=requestSwitchNum)
                {
                    // System.out.println(requestSwitchNum);
                    //ziyuan = Res1.getXi2()-Res1.getXi1(requestSwitchNum);
                    if(ziyuan>(Res1.getXi2()-Res1.getXi1()))
                    {
                        continue;
                    }
                    else if(ziyuan==(Res1.getXi2()-Res1.getXi1()))
                    {
                        if(ziyuan1>=(Res1.getXi4()-Res1.getXi3()))
                        {
                            //System.out.print(ziyuan1);
                            continue;
                        }
                        else
                        {

                            SliceFinish.setXi1(Res1.getXi1());
                            SliceFinish.setYi1(Res1.getYi2().minusMinutes(TimePeriodMinutes));//-TimePeriod);
                            SliceFinish.setXi2(Res1.getXi1()+requestVMNum);
                            SliceFinish.setYi2(Res1.getYi2().minusSeconds(1));
                            SliceFinish.setXi3(Res1.getXi3());
                            SliceFinish.setXi4(Res1.getXi3()+requestSwitchNum);
                            ziyuan1=(Res1.getXi4()-Res1.getXi3());
                        }
                    }
                    else if(ziyuan<(Res1.getXi2()-Res1.getXi1()))
                    {
                        //System.out.print(ziyuan1);
                        SliceFinish.setXi1(Res1.getXi1());
                        SliceFinish.setYi1(Res1.getYi2().minusMinutes(TimePeriodMinutes));//-TimePeriod);
                        SliceFinish.setXi2(Res1.getXi1()+requestVMNum);
                        SliceFinish.setYi2(Res1.getYi2().minusSeconds(1));
                        SliceFinish.setXi3(Res1.getXi3());
                        SliceFinish.setXi4(Res1.getXi3()+requestSwitchNum);
                        ziyuan=(Res1.getXi2()-Res1.getXi1());
                        //System.out.println(requestSwitchNum);
                    }
                }
            }
        }
        else
        {
            for(int k=0;k<SliceBlankInstance.size();k++)
            {
                Residue Res1 = (Residue)SliceBlankInstance.get(k);
                int Res1Tolerance = (Res1.getYi2().getYear()-Res1.getYi1().getYear())*518400+(Res1.getYi2().getMonthValue()-Res1.getYi1().getMonthValue())*43200+(Res1.getYi2().getDayOfMonth()-Res1.getYi1().getDayOfMonth())*1440+(Res1.getYi2().getHour()-Res1.getYi1().getHour())*60+(Res1.getYi2().getMinute()-Res1.getYi1().getMinute());
                if((Res1.getXi2()-Res1.getXi1())>=requestVMNum&&/*(Res1.getYi2()-Res1.getYi1())*/Res1Tolerance>=TimePeriodMinutes&&(Res1.getXi4()-Res1.getXi3())>=requestSwitchNum)
                {
                    //System.out.println("k"+k);
                    if (requestVMNum != (Res1.getXi2() - Res1.getXi1()) &&/*(TimePeriod+Res1.getYi1())*/Res1.getYi1().plusMinutes(TimePeriodMinutes) != Res1.getYi2()) {
                        Tie1 = 2;//I1.setTie(2);
                    } else if ((requestVMNum == (Res1.getXi2() - Res1.getXi1()) &&/*(TimePeriod+Res1.getYi1())*/Res1.getYi1().plusMinutes(TimePeriodMinutes) != Res1.getYi2()) || (requestVMNum != (Res1.getXi2() - Res1.getXi1()) && (Res1.getYi1().plusMinutes(TimePeriodMinutes)) == Res1.getYi2())) {
                        Tie1 = 3;//Ri1.setTie(3);
                    } else if (requestVMNum == (Res1.getXi2() - Res1.getXi1()) &&/*(TimePeriod+Res1.getYi1())*/Res1.getYi1().plusMinutes(TimePeriodMinutes) == Res1.getYi2()) {
                        Tie1 = 4;//Ri1.setTie(4);
                    }
                    if (Tie2 > Tie1)//Ri2.getTie()>Ri1.getTie())
                    {
                        continue;
                    }
                    //Output O1 = new Output();
                    //Ri2.setTie(Ri1.getTie());
                    SliceMiddle.setXi1(Res1.getXi1());
                    SliceMiddle.setYi1(Res1.getYi1());
                    SliceMiddle.setXi2(Res1.getXi1() + requestVMNum);
                    SliceMiddle.setYi2(/*TimePeriod+Res1.getYi1()*/Res1.getYi1().plusMinutes(TimePeriodMinutes));
                    SliceMiddle.setXi3(Res1.getXi3());
                    SliceMiddle.setXi4(Res1.getXi3() + requestSwitchNum);
                    if (Tie2 == Tie1)//Ri2.getTie()==Ri1.getTie())
                    {
                        for (Map.Entry<String, List<Residue>> entry : SliceTimeTable.entrySet()) {
                            //String SliceId = entry.getKey();
                            List<Residue> BeforeSlice1 = entry.getValue();
                            Residue BeforeSliceInstance1 = BeforeSlice1.get(0);
                            //Output O3 = (Output)Output1.get(e);
                            if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && ((BeforeSliceInstance1.getYi1().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi1().isBefore(SliceMiddle.getYi2())) || (BeforeSliceInstance1.getYi2().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi2().isBefore(SliceMiddle.getYi2())))) {
                                d = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2();
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && ((BeforeSliceInstance1.getYi1().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi1().isBefore(SliceMiddle.getYi2())) || (BeforeSliceInstance1.getYi2().isAfter(SliceMiddle.getYi1()) && BeforeSliceInstance1.getYi2().isBefore(SliceMiddle.getYi2())))) {
                                d = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2();
                            } else if (SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2()) && ((BeforeSliceInstance1.getXi1() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi1() < SliceMiddle.getXi2()) || (BeforeSliceInstance1.getXi2() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi2() < SliceMiddle.getXi2()))) {
                                    /*d=SliceMiddle.getYi1()-BeforeSliceInstance1.getYi1();*/
                                d = (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi1().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi1().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi1().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi1().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi1().getMinute());
                            } else if (SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1()) && ((BeforeSliceInstance1.getXi1() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi1() < SliceMiddle.getXi2()) || (BeforeSliceInstance1.getXi2() > SliceMiddle.getXi1() && BeforeSliceInstance1.getXi2() < SliceMiddle.getXi2()))) {
                                    /*d=BeforeSliceInstance1.getYi1()-SliceMiddle.getYi2();*/
                                d = (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());
                            } else if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2())) {
                                d = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2() + (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi2().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi2().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi2().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi2().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi2().getMinute());//SliceMiddle.getYi1()-BeforeSliceInstance1.getYi2();
                            } else if (SliceMiddle.getXi2() < BeforeSliceInstance1.getXi1() && SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1())) {
                                d = BeforeSliceInstance1.getXi1() - SliceMiddle.getXi2() + (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());//BeforeSliceInstance1.getYi1()-SliceMiddle.getYi2();
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && SliceMiddle.getYi1().isAfter(BeforeSliceInstance1.getYi2())) {
                                d = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2() + (SliceMiddle.getYi1().getYear() - BeforeSliceInstance1.getYi2().getYear()) * 518400 + (SliceMiddle.getYi1().getMonthValue() - BeforeSliceInstance1.getYi2().getMonthValue()) * 43200 + (SliceMiddle.getYi1().getDayOfMonth() - BeforeSliceInstance1.getYi2().getDayOfMonth()) * 1440 + (SliceMiddle.getYi1().getHour() - BeforeSliceInstance1.getYi2().getHour()) * 60 + (SliceMiddle.getYi1().getMinute() - BeforeSliceInstance1.getYi2().getMinute());//SliceMiddle.getYi1()-BeforeSliceInstance1.getYi2();
                            } else if (SliceMiddle.getXi1() > BeforeSliceInstance1.getXi2() && SliceMiddle.getYi2().isBefore(BeforeSliceInstance1.getYi1())) {
                                d = SliceMiddle.getXi1() - BeforeSliceInstance1.getXi2() + (BeforeSliceInstance1.getYi1().getYear() - SliceMiddle.getYi2().getYear()) * 518400 + (BeforeSliceInstance1.getYi1().getMonthValue() - SliceMiddle.getYi2().getMonthValue()) * 43200 + (BeforeSliceInstance1.getYi1().getDayOfMonth() - SliceMiddle.getYi2().getDayOfMonth()) * 1440 + (BeforeSliceInstance1.getYi1().getHour() - SliceMiddle.getYi2().getHour()) * 60 + (BeforeSliceInstance1.getYi1().getMinute() - SliceMiddle.getYi2().getMinute());//BeforeSliceInstance1.getYi1()-SliceMiddle.getYi2();
                            }
                            C = 1 - d2 / S3;
                            if (C <= C2) {
                                continue;
                            }
                            d2 = d;
                            C2 = C;
                            Tie2 = Tie1;//Ri2.setTie(Ri1.getTie());
                            SliceFinish.setXi1(Res1.getXi1());
                            SliceFinish.setYi1(Res1.getYi1().plusSeconds(1));
                            SliceFinish.setXi2(Res1.getXi1() + requestVMNum);
                            SliceFinish.setYi2(Res1.getYi1().plusMinutes(TimePeriodMinutes));
                            //O2.setUsername(I1.getUsername());
                            SliceFinish.setXi3(Res1.getXi3());
                            SliceFinish.setXi4(Res1.getXi3() + requestVMNum);
                        }
                    } else if (Tie2 < Tie1)//Ri2.getTie()<Ri1.getTie())
                    {
                        Tie2 = Tie1;//Ri2.setTie(Ri1.getTie());
                        SliceFinish.setXi1(Res1.getXi1());
                        SliceFinish.setYi1(Res1.getYi1().plusSeconds(1));
                        SliceFinish.setXi2(Res1.getXi1() + requestVMNum);
                        SliceFinish.setYi2(Res1.getYi1().plusMinutes(TimePeriodMinutes));
                        SliceFinish.setXi3(Res1.getXi3());
                        SliceFinish.setXi4(Res1.getXi3() + requestVMNum);
                    }

                }
                else
                {
                    continue;
                }
            }
        }
        //System.out.println(SliceFinish.getXi1()+" "+SliceFinish.getXi2()+SliceFinish.getXi3()+" "+SliceFinish.getXi4()+" "+SliceFinish.getYi1()+" "+SliceFinish.getYi2());
        SliceFinishList.add(SliceFinish);
        SliceTimeTable.put(sliceId,SliceFinishList);



        Set<Integer> availableSwitch = new HashSet<>();
        for (int switchId = SliceFinish.getXi3();switchId<SliceFinish.getXi4();switchId++) {
            //int switchId = entry.getKey();
            //TreeSet<SliceDuration> switchTimeLine = entry.getValue();
            // if (!switchTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
            availableSwitch.add(switchId);
        }
        Set<Integer> availableVM = new HashSet<>();
        for (int vmId = SliceFinish.getXi1();vmId<SliceFinish.getXi2();vmId++) {
            // int vmId = entry.getKey();
            //TreeSet<SliceDuration> vmTimeLine = entry.getValue();
            //if (!vmTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
            availableVM.add(vmId);
        }
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : switchTimeTable.entrySet()) {
            int switchId = entry.getKey();
            TreeSet<SliceDuration> switchTimeLine = entry.getValue();
            //if (!switchTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
            //  availableSwitch.add(switchId);
        }
        //Set<Integer> availableVM = new HashSet<>();
        for (Map.Entry<Integer, TreeSet<SliceDuration>> entry : vmTimeTable.entrySet()) {
            int vmId = entry.getKey();
            TreeSet<SliceDuration> vmTimeLine = entry.getValue();
            //if (!vmTimeLine.contains(new SliceDuration(sliceId, reqBegin, reqEnd)))
            //  availableVM.add(vmId);
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
            //System.out.println(S.getStart());
            boolean b= switchTimeLine.add(S);
            // System.out.println(b);
            if (b) {
                reservedSwitch.add(switchId);
                //       System.out.println(reservedSwitch);
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
        // System.out.println(reservedSwitch);
        return resourceMap;

    }

    synchronized public Map<Integer, TreeSet<SliceDuration>> getSwitchTimeTableSnapShot() {
        return new HashMap<>(switchTimeTable);
    }

    synchronized public Map<Integer, TreeSet<SliceDuration>> getVmTimeTableSnapShot() {
        return new HashMap<>(vmTimeTable);
    }

}
