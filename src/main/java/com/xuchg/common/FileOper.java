package com.xuchg.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.xuchg.vo.KafkaConnectVO;
import com.xuchg.window.MainWindow;

/**
 * 文件操作
 * @author xuchg
 *
 */
public class FileOper {

	public static Path file = FileSystems.getDefault().getPath("model.log");
	
	public static void writeObj(KafkaConnectVO obj) throws IOException{
		String objStr = JSON.toJSONString(obj);
		List<String> arr = new ArrayList<>();
		arr.add(objStr);
		Charset charset = Charset.forName("UTF-8");
		if(!file.toFile().exists()){
			Files.createFile(file);
		}
		Files.write(file, arr,charset,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING );
	}
	
	public static String readObj(){
		KafkaConnectVO vo = new KafkaConnectVO();
		String json = "";
		try {
			List<String> arr = Files.readAllLines(file);
			if(arr.size() > 0){
				json = arr.get(0);
				vo = JSON.parseObject(json, vo.getClass());//测试转换
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static void getValue(String value){
		MainWindow.webEngine.executeScript("appendValue("+value+")");
	}
}
