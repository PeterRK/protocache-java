package com.github.peterrk.protocache.fr;

import java.util.HashMap;
import java.util.TreeMap;

public class Main {
    public int i32;
    public int u32;
    public long i64;
    public long u64;
    public boolean flag;
    public int mode;
    public String str;
    public byte[] data;
    public float f32;
    public double f64;
    public Small object;
    public int[] i32v;
    public long[] u64v;
    public String[] strv;
    public byte[][] datav;
    public float[] f32v;
    public double[] f64v;
    public boolean[] flags;
    public Small[] objectv;
    public int t_u32;
    public int t_i32;
    public int t_s32;
    public long t_u64;
    public long t_i64;
    public long t_s64;
    public TreeMap<String,Integer> index;  //FIXME: HashMap doesn't work
    public TreeMap<Integer,Small> objects;
    public float[][] matrix;
    public TreeMap<String,float[]>[] vector;
    public TreeMap<String,float[]> arrays;
}
