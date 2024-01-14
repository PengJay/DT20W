package com.polarbear.dt20w.globaldata;

public class Command {
    public byte[] PlusRVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA0,
                (byte) (step+(byte)1),
                0
        };
    }
    public byte[] MinusRVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA0,
                (byte) (step-(byte)1),
                0
        };
    }
    public byte[] PlusGVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA1,
                (byte) (step+(byte)1),
                0
        };
    }
    public byte[] MinusGVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA0,
                (byte) (step-(byte)1),
                0
        };
    }
    public byte[] PlusBVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA2,
                (byte) (step+(byte)1),
                0
        };
    }
    public byte[] MinusBVol(byte step){
        return new byte[]{
                (byte) 0xFB,
                (byte) 0xA0,
                (byte) (step-(byte)1),
                0
        };
    }
}
