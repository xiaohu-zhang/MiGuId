package com.cmcc.MiGuId.Gen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import com.cmcc.MiGuId.util.IntToCharUtil;

public class SuffixThread implements Runnable {

    Generator g;
    char[] suffix;
    long t1;
    long t2;

    public SuffixThread(Generator g) {
        this.g = g;
    }

    // run every 1 sencond
    // prevent the reorder
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000-t1+t2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException(e);
            }
            t1 = System.currentTimeMillis();
            // 和原值做对比,必须比g的suffix大
            do {
                suffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")).toCharArray();
            } while (IntToCharUtil.charArrayEquals(suffix, g.getResult0()));
            g.getLock().lock();
            try {
                g.setResult0(suffix);
                g.setNum(0);
            } finally {
                g.getLock().unlock();
            }
            t2 = System.currentTimeMillis();
        }
    }

}
