package com.jsxnh.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List ByteIndexof(byte[] search, byte[] find){
        int length = find.length;
        ArrayList l = new ArrayList();
        for(int i=0;i<search.length-length;i++){
            if(Arrays.equals(subBytes(search,i,length),find)){
                l.add(i);
            }
        }
        return l;
    }
}
