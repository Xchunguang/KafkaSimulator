package com.xuchg.window;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.javafx.webkit.WebConsoleListener;
import com.xuchg.MainApplication;
import com.xuchg.common.FileOper;
import com.xuchg.controller.KafkaController;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class MainWindow extends Application {

	//上下文对象
	public static ConfigurableApplicationContext context = null;

	private double minWidth = 850.00;
	private double minHeight = 550.00;

	public static Stage stage;

	private static KafkaController kafkaController = null;

	public static WebView webView;
	public static WebEngine webEngine;


	@Override
	public void start(Stage arg0) throws Exception {
		StackPane layout = new StackPane();//布局
		layout.setId("main-view");
		layout.getStylesheets().add("/pages/css/main-view.css");

		//启动扫描服务
		context = SpringApplication.run(MainApplication.class);
		if(null != context){
			kafkaController = context.getBean(KafkaController.class);
		}
		stage =  new Stage();
		layout.getChildren().add(getBodyView());
		Scene scene = new Scene(layout,minWidth,minHeight);
		stage.setScene(scene);
		stage.setMinHeight(minHeight);
		stage.setMinWidth(minWidth);
		stage.getIcons().add(new Image("/pages/images/app01.png"));
		stage.centerOnScreen();
		stage.setTitle("kafka模拟器");
		stage.show();
		stage.setOnCloseRequest((WINDOW_CLOSE_REQUEST)->{
			Thread.currentThread().interrupt();
		});
	}

	public WebView getBodyView() {

		webView = new WebView();
		webView.setCache(false);
		webEngine = webView.getEngine();
		webView.setContextMenuEnabled(true);
		webEngine.setJavaScriptEnabled(true);
		webView.setFontSmoothingType(FontSmoothingType.GRAY);
		webEngine.load(MainWindow.class.getResource("/pages/index.html").toExternalForm());

		//设置数据目录
		String basePath = System.getProperty("user.home");
		String dataPath = basePath + "/.KafkaSimulator/temp";
		existsFile(dataPath);
		webEngine.setUserDataDirectory(new File(dataPath));

		//监听事件
		Worker<Void> woker = webEngine.getLoadWorker();
		woker.stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == Worker.State.SUCCEEDED) {
				JSObject jsObject = (JSObject) webEngine.executeScript("window");
				jsObject.setMember("kafkaController", kafkaController);
				//初始化
				String objStr = FileOper.readObj();
                jsObject.call("getObj", objStr);
			}
		});

		//页面异常事件
		woker.exceptionProperty().addListener((ObservableValue<? extends Throwable> ov, Throwable t0, Throwable t1) -> {
			System.out.println("Received Exception: " + t1.getMessage());
		});

		//控制台监听事件
		WebConsoleListener.setDefaultListener((WebView curWebView, String message, int lineNumber, String sourceId) -> {
			if (message.contains("ReferenceError: Can't find variable")) {
				//                webEngine.reload();
			}
			System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
		});

		return webView;
	}

	public static void existsFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
	}

}
