package com.xuchg.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xuchg.common.CustomerThread;
import com.xuchg.common.FileOper;
import com.xuchg.common.SendThread;
import com.xuchg.vo.KafkaConnectVO;
import com.xuchg.window.MainWindow;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 * 控制器
 * @author xuchg
 *
 */
@Component
public class KafkaController {
	
	public static Boolean start = false;
	
	public static Boolean customerStart = false;

	private Stage arg1 = null;
	
	public static JSObject jsObject = null;
	
	public void startSend(String json){
		KafkaConnectVO vo = new KafkaConnectVO();
		try{
			vo = JSON.parseObject(json,KafkaConnectVO.class);
			FileOper.writeObj(vo);
			start = true;
			
			SendThread thread = new SendThread(vo);
			thread.start();
			MainWindow.stage.setTitle("发送中");
		}catch(Exception e){
			showInfoAlert(AlertType.ERROR,"未知错误",true);
		}
	}
	
	public String getSendObj(){
		return FileOper.readObj();
	}
	
	public void stopSend(){
		start = false;
		MainWindow.stage.setTitle("kafka模拟器-已停止");
	}
	
	/**
	 * 提示窗口
	 * @param title
	 */
	private Alert showInfoAlert(AlertType alertType,String title,boolean wait){
		Alert alert = null;
		if(StringUtils.isNotBlank(title)){
			alert = new Alert(alertType,title);
			alert.setHeaderText("");
			alert.setTitle("提示");
			if(wait){
				alert.showAndWait();
			}else{
				alert.show();
			}
		}
		return alert;
	}
	
	public void closeCustomer(){
		arg1.close();
	}
	
	public void openCustomer(String json){
		double width = 700;
		double height = 600;
		arg1 = new Stage();
		StackPane layout = new StackPane();//布局
		KafkaConnectVO vo = new KafkaConnectVO();
		vo = JSON.parseObject(json,KafkaConnectVO.class);
		layout.getChildren().add(getBodyView(vo));

		Scene scene = new Scene(layout,width,height);
		arg1.setScene(scene);
		arg1.setMinHeight(width);
		arg1.setMinWidth(height);
		arg1.getIcons().add(new Image("/pages/images/app01.png"));
		arg1.centerOnScreen();
		arg1.setTitle("消费数据");
		arg1.initModality(Modality.WINDOW_MODAL);
		arg1.setMinHeight(height);
		arg1.setMinWidth(width);
		arg1.show();
		arg1.setOnCloseRequest((WINDOW_CLOSE_REQUEST) -> {
			customerStart = false;
		});
	}
	
	private VBox getBodyView(KafkaConnectVO vo) {
        WebView webView = new WebView();
        webView.setCache(true);
        webView.setContextMenuEnabled(false);
        webView.setFontSmoothingType(FontSmoothingType.GRAY);
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        String htmlSrc = "/pages/cutomer.html";
        String url = MainWindow.class.getResource(htmlSrc).toExternalForm();
        webEngine.load(url);
        ReadOnlyObjectProperty<Worker.State> woker = webEngine.getLoadWorker().stateProperty();
        woker.addListener((obs, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                jsObject = (JSObject) webEngine.executeScript("window");
                KafkaController kafkaController = MainWindow.context.getBean(KafkaController.class);
                jsObject.setMember("kafkaController", kafkaController);
                KafkaController.customerStart = true;
                CustomerThread cusThread = new CustomerThread(vo);
                cusThread.start();
            }
        });
        return new VBox(webView);
    }
	
}

