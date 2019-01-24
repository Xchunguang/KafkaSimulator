package com.xuchg.common;

import java.sql.Timestamp;
import java.util.Random;

/**
 * 随机工具
 * @author xuchg
 *
 */
public class RandomUtil {

	public static final String SOURCES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	
	public static Long getNum(){
		return Math.round(Math.random()*100);
	}
	
	public static String getStr(){
		return generateString(new Random(),SOURCES,10);
	}
	
	public static String generateString(Random random,String characters,int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
	
	public static String getDoubleStr(){
		double i = Math.round(Math.random()*10) + Math.random();
		return String.valueOf(i);
	}
	
	public static String getTimeStr(){
		Long time = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(time + Math.round(Math.random()*10000000));
		return timestamp.toString();
	}
	
	public static String getTimeLongStr(){
		Long time = System.currentTimeMillis();
		time = time + Math.round(Math.random()*10000000);
		return time.toString();
	}
	
}
