package com.xuchg.common;

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
	
}
