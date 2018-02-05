package com.jsxnh.util;

public class ByteUtil {
    public static byte[] merge(byte[]...array2){
        int size = 0;
        for(int i=0;i<array2.length;i++){
            size+=array2[i].length;
        }
        byte[] r = new byte[size];
        int pos=0;
        for(int i=0;i<array2.length;i++){
            System.arraycopy(array2[i], 0, r, pos, array2[i].length);
            pos += array2[i].length;
        }
        return r;
    }


    public static byte[] subBytes(byte[] b, int start, int length) {
        byte bytes[] = new byte[length];
        System.arraycopy(b, start, bytes, 0, length);
        return bytes;
    }
}
