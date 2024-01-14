package com.polarbear.dt20w.parser;

import com.polarbear.dt20w.globaldata.MCUData;

public class MCUFrameDecoder {

    public MCUData decoder(byte[] frame){
        MCUData mcuData = new MCUData();
        mcuData.laser = byteToInt(frame[2]) + "." + byteToInt(frame[3]);
        if(frame[1] == 1){
            mcuData.laser = "——" + mcuData.laser;
        }
        mcuData.driver = byteToInt(frame[5]) + "." + byteToInt(frame[6]);
        if(frame[4] == 1){
            mcuData.driver = "——" + mcuData.driver;
        }
        mcuData.rData.vol = byteToInt(frame[7]) + "." + byteToInt(frame[8]);
        mcuData.rData.vol_step = frame[9];
        mcuData.rData.offset = frame[16];
        mcuData.rData.gain = byteToShort(frame[19], frame[20]);
        mcuData.rData.gain_step = frame[21];
        mcuData.rData.th = frame[28];
        mcuData.rData.in = byteToShort(frame[39], frame[40]);

        mcuData.gData.vol = byteToInt(frame[10]) + "." + byteToInt(frame[11]);
        mcuData.gData.vol_step = frame[12];
        mcuData.gData.offset = frame[17];
        mcuData.gData.gain = byteToShort(frame[22], frame[23]);
        mcuData.gData.gain_step = frame[24];
        mcuData.gData.th = frame[29];
        mcuData.gData.in = byteToShort(frame[41], frame[42]);

        mcuData.bData.vol = byteToInt(frame[13]) + "." + byteToInt(frame[14]);
        mcuData.bData.vol_step = frame[15];
        mcuData.bData.offset = frame[18];
        mcuData.bData.gain = byteToShort(frame[25], frame[26]);
        mcuData.bData.gain_step = frame[27];
        mcuData.bData.th = frame[30];
        mcuData.bData.in = byteToShort(frame[43], frame[44]);

        mcuData.tecStep = frame[31];
        mcuData.tecLim = frame[32];
        mcuData.targetTep = byteToInt(frame[33]) + "." + byteToInt(frame[34]);
        mcuData.fw = byteToInt(frame[35]) + "." + byteToInt(frame[36]);
        mcuData.power = byteToShort(frame[37], frame[38]);

        return mcuData;
    }


    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
    public static short[] byteArrayToShortArray(byte[] data) {
        short[] shortValue = new short[data.length / 2];
        for (int i = 0; i < shortValue.length; i++) {
            shortValue[i] = (short) ((data[i * 2] & 0xff) | ((data[i * 2 + 1] & 0xff) << 8));
        }
        return shortValue;
    }
    public static short byteToShort(byte high,byte low) {
        return (short) ((low & 0xff) | ((high & 0xff) << 8));

    }
}
