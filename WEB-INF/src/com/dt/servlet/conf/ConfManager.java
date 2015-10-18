package com.dt.servlet.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 获取配置文件value值得类，该类使用了单例模式
 * Created by ZhangHua. 
 * @author hua
 * @date 2015年10月16日
 * @time 下午9:48:35
 */
public class ConfManager {
       private static ConfManager confManager;
       private static Properties properties;
       private static String configFile;
	private ConfManager() {
		//定义文件名
		configFile = "db.properties";
		//super();
		//实例化一个properties对象
		properties = new Properties();
		//获取该文件的输入流
		InputStream in = ConfManager.class.getClassLoader().getResourceAsStream(configFile);
		// TODO Auto-generated constructor stub
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
      
    //获取实例,单例模式，获取一个对象。
	public static ConfManager getInstance() {
		if(confManager==null)
			confManager = new ConfManager();
		return confManager;
	}
	
	//根据键获取值
	public String getValue(String key){
		return properties.getProperty(key);
	}
}
