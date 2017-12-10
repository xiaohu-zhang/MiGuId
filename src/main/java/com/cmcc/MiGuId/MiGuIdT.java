package com.cmcc.MiGuId;

import java.io.IOException;

import com.cmcc.MiGuId.Gen.Generator;
import com.cmcc.MiGuId.Gen.SuffixThread;

public class MiGuIdT {

    
    public static void main(String[] args) throws IOException, InterruptedException {
        Generator g = new Generator();
        SuffixThread s = new SuffixThread(g);
        Thread t = new Thread(s);
        t.setDaemon(true);
        t.start();
        String r = "";
        System.out.println(g.generate());
        long t1 = System.currentTimeMillis();
            for(int i = 0;i < 5000_0000;++i){
                 r = g.generate();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("time: " + (t2 - t1));
        System.out.println(r);
        
    }

}
