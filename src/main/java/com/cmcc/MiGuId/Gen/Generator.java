package com.cmcc.MiGuId.Gen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

import com.cmcc.MiGuId.util.IntToCharUtil;

/**
 * 全局时序唯一id生成器
 * 
 * @author silver
 *
 */
public class Generator {

    private ReentrantLock lock = new ReentrantLock();

    private int num = 0;
    
    private static char[][] result = new char[3][];
    
    private char[] id = new char[22];
    
    private int count = 0;
    
    private static char[][] zerofills = new char[9][];
    
    public Generator(){
        result[0] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")).toCharArray();
        result[2] = new char[10];
        for(int i = 0;i < 9;++i){
            zerofills[i] = new char[9-i];
            for(int j  = 0 ;j <9 - i;++j){
                zerofills[i][j] = '0';
            }
        }
    }

    
    public String generate() {
        // spin when need to change the suffix by another thread
        lock.lock();
        try {
            num++;
            count =IntToCharUtil.stringSize(num);
            result[1] = zerofills[count-1];
            IntToCharUtil.getChars(num, count, result[2]);
            
            for(int i = 0, j = 0,k = 0;i < 22;++i){
                if(i < 12){
                    id[i] = result[0][i];
                }else if(i < 22 - count){
                    id[i] = result[1][j++];
                }else{
                    id[i] = result[2][k++];
                }
            }
            return new String(id);
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String...lock ){
        StringBuilder b = new StringBuilder(1);
        b.append(105).toString();
    }

    void setNum(int num) {
        this.num = num;
    }

    int getNum() {
        return num;
    }

    ReentrantLock getLock() {
        return lock;
    }
    
    public char[] getResult0(){
        return result[0];
    }
    
    public void setResult0(char[] dateSuffix){
        result[0] = dateSuffix;
    }

}
