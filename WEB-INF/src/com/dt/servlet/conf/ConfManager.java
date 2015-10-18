package com.dt.servlet.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * ��ȡ�����ļ�valueֵ���࣬����ʹ���˵���ģʽ
 * Created by ZhangHua. 
 * @author hua
 * @date 2015��10��16��
 * @time ����9:48:35
 */
public class ConfManager {
       private static ConfManager confManager;
       private static Properties properties;
       private static String configFile;
	private ConfManager() {
		//�����ļ���
		configFile = "db.properties";
		//super();
		//ʵ����һ��properties����
		properties = new Properties();
		//��ȡ���ļ���������
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
      
    //��ȡʵ��,����ģʽ����ȡһ������
	public static ConfManager getInstance() {
		if(confManager==null)
			confManager = new ConfManager();
		return confManager;
	}
	
	//���ݼ���ȡֵ
	public String getValue(String key){
		return properties.getProperty(key);
	}
}
