package org.interonet.ldm.PowerManager;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class PowerManager {
    public String powerSystemIpAddress = "10.0.0.2";
    public String sIP = "10.0.0.2";
    public int sPort = 3000;
    byte address = 0x01;
    private Socket mSocketClient = null;
    static BufferedInputStream mBufferedReaderClient = null;
    static PrintStream mPrintStreamClient = null;
    int wayState[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0};

    private byte buf[] = {(byte) 0x55, (byte) 0x01, (byte) 0x13, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x55,
            (byte) 0x02, (byte) 0x13, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00};
    public byte recv[] = {(byte) 0x55, (byte) 0x01, (byte) 0x13, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x55,
            (byte) 0x02, (byte) 0x13, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00};
    public int recvState = 0;
    private int ways;
    private Thread mThreadClient = null;
    public int Socketstate = 0;
    String recvMessageClient = "";
    public int relayState = 0;
    public int state = 0;
    private Logger logger = Logger.getLogger(PowerManager.class.getCanonicalName());

    public PowerManager() {
        try {
            powerOnSwitchById(1);
            Thread.sleep(1000);
            powerOffSwitchById(1);
            Thread.sleep(1000);

            powerOnSwitchById(2);
            Thread.sleep(1000);
            powerOffSwitchById(2);
            Thread.sleep(1000);

            logger.info("Init: power off all the switch.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int CreateSendCommand(byte buf[], byte openWay, boolean open) {
        int offset = 0;
        if (openWay < 1 || openWay > 32)
            return 0;
        buf[1 + offset] = address;
        if (openWay > 16) {
            openWay = (byte) (openWay - 16);
            buf[1 + offset] += 1;
        }
        buf[0 + offset] = 0x55;

        if (open)
            buf[2 + offset] = 0x12;
        else
            buf[2 + offset] = 0x11;
        buf[3 + offset] = 0;
        buf[4 + offset] = 0;
        buf[5 + offset] = 0;
        buf[6 + offset] = openWay;
        return 1;
    }

    public int CheckRelayState() {
        return relayState;
    }

    public int storeRelayState() {
        if (ways <= 16) {
            if (recv[0] == 0x22 && recv[1] == address) {
                int crc = recv[7];
                int datacrc = (recv[0] + recv[1] + recv[2] + recv[3] + recv[4]
                        + recv[5] + recv[6]) % 256;
                if (crc == datacrc || crc + 256 == datacrc) {
                    relayState = (int) recv[6];
                    if (recv[6] < 0)
                        relayState += 256;
                    if (recv[5] < 0)
                        relayState += (((int) recv[5] + 256) << 8);
                    else
                        relayState += (((int) recv[5]) << 8);
                    for (int i = 0; i < 16; i++) {
                        if ((relayState & (1 << i)) != 0)
                            wayState[i] = 1;
                        else
                            wayState[i] = 0;
                    }
                }
            }
        }
        return relayState;
    }

    public int senBufWithSocket(String sIPs, int sPorts, byte bufs[], int wayss) {
        int temp = 8;
        sIP = sIPs;
        sPort = sPorts;
        ways = wayss;
        if (wayss > 16)
            temp = 16;
        for (int i = 0; i < temp; i++)
            buf[i] = bufs[i];
        Socketstate = -1;
        recvMessageClient = "Board connect failed!";
        mThreadClient = new Thread(mRunnable);
        mThreadClient.start();
        recvState = 0;
        state = 0;
        for (int i = 0; i < 20; i++) {
            if (Socketstate != -1)
                break;
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        if (Socketstate == -1) {
            Socketstate = 0;
            if (state >= 3 && mSocketClient != null) // waiting for data, kill
            // the client
            {
                try {
                    mSocketClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mThreadClient.interrupt();
        }
        return Socketstate;
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (sIP.equals("") || sPort == 0)
                return;
            buf[7] = 0;
            for (int i = 0; i < 7; i++) {
                buf[7] += buf[i];
            }
            if (ways > 16) {
                buf[15] = 0;
                for (int i = 8; i < 15; i++) {
                    buf[15] += buf[i];
                }
            }
            int ret = 0;
            try {
                InetAddress x = java.net.InetAddress.getByName(sIP);
                String ipaddress = x.getHostAddress();
                mSocketClient = new Socket(ipaddress, sPort); // portnum
                state = 1;
                mBufferedReaderClient = new BufferedInputStream(
                        new DataInputStream(mSocketClient.getInputStream()));
                mPrintStreamClient = new PrintStream(
                        mSocketClient.getOutputStream(), true);
                state = 2;
                if (ways > 16)
                    mPrintStreamClient.write(buf, 0, 16);
                else
                    mPrintStreamClient.write(buf, 0, 8);
                mPrintStreamClient.flush();
                state = 3;
                if (ways > 16)
                    ret = mBufferedReaderClient.read(recv, 0, 16);
                else
                    ret = mBufferedReaderClient.read(recv, 0, 8);
                if (ret >= 8) {
                    recvState = ret;
                    storeRelayState();
                }
                mBufferedReaderClient.close();
                mPrintStreamClient.close();
                mSocketClient.close();
                Socketstate = 1;
            } catch (Exception e) {
//					recvMessageClient = "Connection Error:" + e.toString() + ":"
//							+ e.getMessage() + "\n";
                e.printStackTrace();
                try {
                    mSocketClient.close();
                } catch (Exception f) {
                    f.printStackTrace();
                    Socketstate = 0;
                    return;
                }
                Socketstate = 0;
            }

        }
    };

    public void powerOnSwitchById(Integer switchID) throws Exception {
        // Initiate the Magic Number.
        byte buff[] = {(byte) 0x55, (byte) 0x01, (byte) 0x12, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        int ID = switchID;
        byte openWayy = (byte) ID;

        CreateSendCommand(buff, openWayy, true);
        senBufWithSocket(powerSystemIpAddress, 3000, buff, 8);
//        int sendStatement = senBufWithSocket("10.0.0.2", 3000, buff, 8);
//
//        if (sendStatement != 0) {
//            throw new Exception("Failed to sendBufWithSocket when power on switchId=" + switchID);
//        }
    }

    public void powerOffSwitchById(Integer switchID) throws Exception {
        byte buff[] = {(byte) 0x55, (byte) 0x01, (byte) 0x12, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        int ID = switchID;
        byte closeWayy = (byte) ID;
        CreateSendCommand(buff, closeWayy, false);
        senBufWithSocket(powerSystemIpAddress, 3000, buff, 8);

//        int sendStatement = senBufWithSocket("10.0.0.2", 3000, buff, 8);
//
//        if (sendStatement != 0) {
//            throw new Exception("Failed to sendBufWithSocket when power off switchId=" + switchID);
//        }
    }
}
