package com.cmcc.MiGuId;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cmcc.MiGuId.Gen.Generator;
import com.cmcc.MiGuId.Gen.SuffixThread;

public class MiGuId {

    public static List<String> list = new ArrayList<>();
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Generator g = new Generator();
        SuffixThread s = new SuffixThread(g);
        Thread t = new Thread(s);
        t.setDaemon(true);
        t.start();
        long t1 = System.currentTimeMillis();
        for(int j = 0;j < 10;++j){
            for(int i = 0;i < 2000;++i){
                String r = g.generate();
                list.add(r);
            }
            TimeUnit.SECONDS.sleep(1);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("time: " + (t2 - t1));
        
        for(int k = 1;k < list.size();++k){
            BigDecimal b1 =new BigDecimal(list.get(k));
            BigDecimal b2 =new BigDecimal(list.get(k-1));
            if(b1.compareTo(b2) <= 0){
                System.out.println("erro:" + k);
            }
        }
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("c:/1.txt")));
        for(String r:list){
            bw.write(r);
            bw.newLine();
        }
        bw.flush();
        
    }

}
