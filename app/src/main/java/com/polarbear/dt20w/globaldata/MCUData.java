package com.polarbear.dt20w.globaldata;


public class MCUData {
    public String laser;
    public String driver;
    public Color_Data rData;
    public Color_Data gData;
    public Color_Data bData;
    public byte tecStep;
    public byte tecLim;
    public String targetTep;  //=set
    public String fw;
    public short power;
    public class Color_Data {
        //public int type; //表示颜色类型 分别有 r g b三种
        public String vol;
        public byte offset;
        public short gain;
        public short in;
        public byte th;
        public byte vol_step;
        public byte gain_step;

    }


}

