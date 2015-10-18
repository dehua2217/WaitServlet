package com.dt.servlet.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
//import com.sun.java.util.jar.pack.Package.File;
/**
 * 
 * Created by ZhangHua. 
 * @author hua
 * @date 2015年8月30日
 * @time 下午9:46:00
 */
public class Dao {
	    protected static String dbClassName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	   //2008驱动包名有变 
	  //  protected static String dbClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	 	protected static String dbUrl = "jdbc:jtds:sqlserver://localhost:1433/"
	 			+ "db_database28_Data;SelectMethod=Cursor";
	 	protected static String dbUser = "";
	 	protected static String dbPwd = "";
	 	protected static String second = null;
	 	public static Connection conn = null;
	 	protected static String ip="";
	 	protected static String databaseName="";
	 	protected boolean IsTrain;
	 	
	 	
	 	protected static String get=System.getProperty("boot.ini");
	 	protected  URL getPath=getClass().getResource("/lib/boot.ini");
	 	 String  timeTypeNo="",cardType="",cardNo="",insideNo="";
	 	 Double dtbPay,cashPay,discount = 0.0,dtbLeft = 0.0,cashLeft = 0.0;
	 	 String limitHours="",subCardType="",pubDate="",timeLimit="",validateDate="";
	 	 String limitUse="",opened="",startDate = "",endDate = "";
	 	 String consumeType="游泳",chargeTypeNo="成人票";
	 	 int leastLong=0,leftNum=0,deNum=0;
	 	 Double shouldPay,pay;
	 	 static LinkedList linkedList;
	 	long tempTimeDiff=0,timeDiff=0;
	 	String numConsumeType="";
	    String	numTimeTypeNo="";
	 	static {      //加载驱动
	 		try {
	 			
	 				Class.forName(dbClassName).newInstance();
	 				//conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://192.168.1.188:1433;User=sa;Password=wmldml;DatabaseName=dtyl");
	 			
	 		} catch (ClassNotFoundException e) {
	 			e.printStackTrace();
	 			JOptionPane.showMessageDialog(null,
	 					"请将SQL Server 2000的JDBC驱动包复制到Java\\jdk1.8.0_05\\jre\\lib\\ext。");
	 			System.exit(-1);
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 		}
	 	}
	 	
	 	static {
	 		try {
	 			if (conn == null) {
	 			//	Class.forName(dbClassName).newInstance();
	 				//conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
	 				conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;User=sa;Password=wmldml;DatabaseName=dtyl;SelectMethod=Cursor");
	 			//	2008获得连接的方法 ：
	 			//	conn = DriverManager.getConnection("jdbc:sqlserver://121.40.182.240:1433;User=sa;Password=wmldml;DatabaseName=cs;SelectMethod=Cursor");  //SelectMethod=Cursor适应游标，加快查询速度。
	 			}
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			JOptionPane.showMessageDialog(null,
	 					"数据库2"+databaseName+"连接失败，请检查网络和配置文件！"+ip);
	 			System.exit(-1);
	 		}
	     
	 	} 
	 	
	 	/*
	 	static {
		 	try{ 

		 		Context initContext = new InitialContext(); 

		 		Context envContext  = (Context)initContext.lookup("java:/comp/env"); 

		 		DataSource ds = (DataSource)envContext.lookup("jdbc/sqlserver"); 

		 		 conn = ds.getConnection(); 

		 		}catch(Exception e){ 

		 		System.out.println("出错了。。。。。。。。。。。。。。。。。。"); 

		 		 }
		 	

		 	  }*/
	 	
	 	//链表数据结构
	 	public static class TableInfo {
	 		
	 		String TableName;
	 		String PrimaryKey;
	 		String Head;
	 		String  ObjNo;
	 		String  ObjCaption;
	 		String  ObjParentNo;
	 		TableInfo next;
			public TableInfo(String tableName, String primaryKey, String head,
					String objNo, String objCaption, String objParentNo) {
				super();
				TableName = tableName;
				PrimaryKey = primaryKey;
				Head = head;
				ObjNo = objNo;
				ObjCaption = objCaption;
				ObjParentNo = objParentNo;
		
			}
			public String getTableName() {
				return TableName;
			}
			public void setTableName(String tableName) {
				TableName = tableName;
			}
			public String getPrimaryKey() {
				return PrimaryKey;
			}
			public void setPrimaryKey(String primaryKey) {
				PrimaryKey = primaryKey;
			}
			public String getHead() {
				return Head;
			}
			public void setHead(String head) {
				Head = head;
			}
			public String getObjNo() {
				return ObjNo;
			}
			public void setObjNo(String objNo) {
				ObjNo = objNo;
			}
			public String getObjCaption() {
				return ObjCaption;
			}
			public void setObjCaption(String objCaption) {
				ObjCaption = objCaption;
			}
			public String getObjParentNo() {
				return ObjParentNo;
			}
			public void setObjParentNo(String objParentNo) {
				ObjParentNo = objParentNo;
			}
			public TableInfo getNext() {
				return next;
			}
			public void setNext(TableInfo next) {
				this.next = next;
			}
			
	 		
	 	}
	 	/**
	 	 * 
	 	 * Created by ZhangHua. 
	 	 * @author hua
	 	 * @date 2015年8月30日
	 	 * @time 下午9:46:59
	 	 */
	 	public static class LinkedList {
	 		
	 		protected TableInfo[] tableInfos;
	 		 /** 指针，指向链表末端待添加的位置，默认指向0的位置 */  
	 	    private int endPos = 0;  
	 	      
	 	   /** 当前链表容量 */  
	 	    private int capacity = 0; 

	 		
           
	 		
	 		/* public LinkedList() {
	 			tableInfos = new TableInfo[this.getSize()];
	 			capacity  = this.getSize();
	 		 }*/
	 		 
	 		 public LinkedList(int capacity) {  
	 			tableInfos = new TableInfo[capacity];  
	 		   this.capacity = capacity;  
	 			    }  
	 		 
	 		/** 
	 		     * 链表大小 
	 		     *  
	 		     * @return 链表大小 
	 		     */  
	 		   public int size() {  
	 		        return endPos;  
	 		    }  
	 		     
	 		    public int capacity() {  
	 		        return this.tableInfos.length;  
	 		    }  


	 		 /** 
	 		     * 向链表中加入一个节点 
	 		     *  
	 		     * @param node 新加入的节点 
	 		     */  
	 		   public void add(TableInfo node) {  

	 		        if (endPos == 0) {  
	 		           // node.setIndex(0);  
	 		           tableInfos[endPos] = node;  
	 		        } else if (endPos > 0 && endPos <= this.capacity() - 1) {  
	 		         //   node.setIndex(endPos);  
	 		        	tableInfos[endPos] = node;  
	 		        	tableInfos[endPos-1].setNext(tableInfos[endPos]);  
	 		        }  
	 		        endPos++;  
	 		   }  

	 	}
	 	
	 	public static LinkedList getTableInfos()
	 	{
           List<List> sqlList=new ArrayList<List>();
	 		
 	 		sqlList = findForList("Select * From TableInfo  ");
	 		Iterator iterator=sqlList.iterator();
	 		linkedList=new LinkedList(sqlList.size());
	         while(iterator.hasNext())
	         {
	        	 List info = (List) iterator.next();
	        	
	        	 TableInfo tableInfo=new TableInfo((String)info.get(0), (String)info.get(1), (String)info.get(2), (String)info.get(3), (String)info.get(5), (String)info.get(4));
	        	 linkedList.add(tableInfo);
	         }
	         
	         return linkedList;
	 	}
	 	// 获得账单号
	 	public  String getMainNo(String tableName)
	 	{
	 	    String column = null,head = null,str,sql,djbm;
	 	   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	
  	     java.util.Date    curDate =   new    Date(System.currentTimeMillis());//获取当前时间 
  	     
  	     String rightBillNo=sdf.format(curDate);
	 	   // while(dao.getTableInfos().tableInfos[0].next!=null)
	         for(int i=0;i<Dao.getTableInfos().capacity;i++)
	         {
	        	 if(Dao.getTableInfos().tableInfos[i].TableName.toString().equalsIgnoreCase(tableName))
	        	 {
	        		 column=Dao.getTableInfos().tableInfos[i].PrimaryKey;
	        		 head  =Dao.getTableInfos().tableInfos[i].Head;
	        		 break;
	        	 }
	         }
	         
	         head="ZJ"+head;
	         List<List> sqlList=new ArrayList<List>();
		 		
	 	 		sqlList = findForList("select max("+column+")  as djbm  from "+tableName+" where Left("+column+",Len('"+head+"')+8) = '"+head+rightBillNo+"'  ");
	 	 		
	 	 		if((String)sqlList.get(0).get(0)==null)
	 	 			djbm=head+rightBillNo+"0001";
	 	 		else
	 	 		{
	 	 		  str = (sqlList.get(0).get(0).toString()).substring(sqlList.get(0).get(0).toString().length()-4,sqlList.get(0).get(0).toString().length());
	 	 	      str = String.valueOf(Integer.parseInt(str)+1);
	 	 	      str = ("0000").substring(0,4 - str.length() ) + str;
	 	 	      djbm= head + rightBillNo+str;
	 	 		}
	 	 		
	 		return djbm;	 		
	 	}
	 	//检查卡的有效性
	 	
		public  String checkCard(String insideNo) {
	 		 String dt;
	 		 this.insideNo=insideNo;
	 		//Dao dao=new Dao();
	 		 List<List> sqlList=new ArrayList<List>();
	 		 SimpleDateFormat    formatterdt    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
			   Date    curDatedt    =   new    Date(System.currentTimeMillis());//获取当前时间        	    
			    dt= formatterdt.format(curDatedt);  
			    
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    	
	    	     java.util.Date    curDate =   new    Date(System.currentTimeMillis());//获取当前时间 
	 		   //初始化，培训卡不需钥匙
	 	        IsTrain=false;
	 	       SimpleDateFormat    formatter    =   new    SimpleDateFormat ("HH:mm:ss");       
			  // Date    curDatedt    =   new    Date(System.currentTimeMillis());//获取当前时间        	    
			  //  dt= formatterdt.format(curDatedt);    
	 	      
	 	      sqlList = findForList("Select TimeTypeNo,Name From TimeType  "
                    +"Where  [Start]<='"+formatter.format(new Date(System.currentTimeMillis()- Integer.parseInt(getSysSet("InDiff").substring(1,getSysSet("InDiff").length()-1))*60*1000))+"' And [End] >'"+formatter.format(new Date(System.currentTimeMillis()- Integer.parseInt(getSysSet("InDiff").substring(1,getSysSet("InDiff").length()-1))*60*1000))+"'  ");
	 	     if(sqlList.isEmpty())
	 	     {
	 	    	 return "nonExistTimeType";    //不存在改时间场次
	 	     } else
	 	     {
	 	    	timeTypeNo=sqlList.get(0).get(0).toString(); 
	 	     }
	 	     
	 	     if(Boolean.parseBoolean(getSysSet("GetKeyWithCardNo").substring(1,2)))
	 	     {
	 	    	sqlList = findForList("Select   InsideNo,CustomerNo,Name,State,CardType,MasterCardNo from Customer  "
                 +"  Where   ( InsideNo = '"+insideNo+"'  Or  CustomerNo = '"+insideNo+"' )      ");
	 	     } else
	 	     {
	 	    	sqlList = findForList("Select   InsideNo,CustomerNo,Name,State,CardType,MasterCardNo from Customer  "
	 	                 +"  Where   ( InsideNo = '"+insideNo+"' )      ");
	 	     }
	 	     
	 	     if(sqlList.isEmpty())
	 	     {
	 	    	 return "nonExistCustomerNo";  // 不存在客户卡号或钥匙号
	 	     } else
	 	     {
	 	    	 if(!sqlList.get(0).get(3).toString().equals("正常"))
	 	    	 {
	 	    		 String tempStr;
	 	    		if(sqlList.get(0).get(3).toString().equals("补卡"))
	 	    			tempStr="isBK";          //该卡未补卡状态
	 	    		else
	 	    			tempStr="cardException";   //卡异常
	 	    		return tempStr;
	 	    		
	 	    	 }
	 	    	cardType=sqlList.get(0).get(4).toString();
	 	    	cardNo  =sqlList.get(0).get(1).toString();
	 	    	//insideNo=sqlList.get(0).get(0).toString();
	 	    	if(cardType.equals("副卡"))
	 	    	{
	 	    		
	 	    		if(sqlList.get(0).get(5)==null || sqlList.get(0).get(5).toString().isEmpty() || sqlList.get(0).get(5).toString().equals(""))
	 	    			return "masterCardNoIsNull";  //主卡未空
	 	    		
	 	    		String masterCardNo=sqlList.get(0).get(5).toString();
	 	    		//判断主卡号
	 	    		sqlList = findForList("Select   CustomerNo,State,CardType,Code  from Customer  "
          +"  Where   ( InsideNo = '"+masterCardNo+"'  Or  CustomerNo = '"+masterCardNo+"' )    ");
	 	    		if(sqlList.isEmpty())
	 	    			return "nonExistMasterCardNo"; //主卡号不存在
	 	    		else
	 	    		{
	 	    			 if(!sqlList.get(0).get(1).toString().equals("正常"))
	 		 	    	 {
	 		 	    		 String tempStr;
	 		 	    		if(sqlList.get(0).get(1).toString().equals("补卡"))
	 		 	    			tempStr="isBK";          //该卡未补卡状态
	 		 	    		else
	 		 	    			tempStr="cardException";   //卡异常
	 		 	    		return tempStr;
	 		 	    	 }
	 	    			cardNo=sqlList.get(0).get(0).toString(); 
	 	    			cardType=sqlList.get(0).get(2).toString();
	 	    			 
	 	    		}
	 	    		
	 	    	}
	 	    	//判断时间卡是否存在
	 	    	if(cardType.equals("时间卡"))
	 	    	{
	 	    	
	 	    	sqlList = findForList("Select Tc.PubDate,TC.CustomerNo,TC.TimeCardType,VIP,TC.TimeCardNo As CardNo,TC.InsideNo,TC.SDDay,Tc.LimitTimes,Tc.LimitHours,Tc.TimeLimit,Tc.WeekLimit,Tc.MouthLimit,TC.ConsumeType,TC.TimeTypeNo,TC.Opened,TC.AddDays,TC.AutoOpenDate,TC.[Start],TC.[End] "
                   +"   From TimeCard TC  Left Join TimeCardPrice TCPR  On TC.TimeCardType=TCPR.TimeCardType "
                   +"  Where  TC.TimeCardNo='"+cardNo+"'       ");
	 	    	
	 	    	if(sqlList.isEmpty())
	 	    		return "nonExistTimeCard"; //时间卡资料不存在
	 	    	
	 	    	limitHours = sqlList.get(0).get(8).toString();
	 	    	subCardType=sqlList.get(0).get(2).toString();
	 	    	pubDate     =sqlList.get(0).get(0).toString();
	 	    	
	 	    	}
	 	    	
	 	    	
	 	    	
	 	     }
	 	     
	 	     if(cardType.equals("次卡"))
	 	     {
	 	    	sqlList = findForList("Select Tc.PubDate,TC.CustomerNo,TC.TechOrder,TC.NumCardType,TC.NumCardNo As CardNo,TC.InsideNo,TC.SDDay,TC.LimitTimes,Tc.LimitHours,TC.TimeLimit,Tc.WeekLimit,Tc.MouthLimit,TC.ConsumeType,TC.TimeTypeNo,TC.LeftNum,TC.Opened,TC.AddDays,TC.AutoOpenDate,TC.[Start],TC.[End] "
                +"  from NumCard TC Left Join  NumCardPrice TCPR  On TC.NumCardType=TCPR.NumCardType   "
                +"  Where   TC.NumCardNo='"+cardNo+"'   ");
	 	    	
	 	    	if(sqlList.isEmpty())
	 	    		return "nonExistNumCard";  //次卡资料不存在
	 	    	//培训卡的判断
	 	    	
	 	      if(sqlList.get(0).get(2).toString().equals(""))
	 	      {
	 	    	 IsTrain=false;
	 	      } else
	 	    	 IsTrain=true;
	 	        limitHours = sqlList.get(0).get(8).toString();
	 	    	subCardType=sqlList.get(0).get(3).toString();
	 	    	pubDate     =sqlList.get(0).get(0).toString();
	 	    	numConsumeType=sqlList.get(0).get(12).toString();
	 	    	numTimeTypeNo=sqlList.get(0).get(13).toString();
	 	    	limitUse=sqlList.get(0).get(7).toString();
	 	      
	 	     }
	 	     
	 	     if(cardType.equals("充值卡"))
	 	     {
	 	    	sqlList = findForList("Select LimitTimes,TimeLimit,WeekLimit,MouthLimit, Opened,JoinDate,OpenDate,AutoOpenDate,AddDays,ValidateDate,CashAmount,DTBAmount,CashLeft,Discount,DTBLeft,Teacher,StuType,Mender  from Customer  "
                   +"    Where  CustomerNo='"+cardNo+"'   And  State = '正常'    ");
	 	    	
	 	    	if(sqlList.isEmpty())
	 	    		return "nonExistGetCard";  //充值卡资料不存在
	 	    	
	 	    	dtbLeft      = Double.parseDouble(sqlList.get(0).get(14).toString());
	 	    	cashLeft     = Double.parseDouble(sqlList.get(0).get(12).toString());
	 	    	discount     = Double.parseDouble(sqlList.get(0).get(13).toString());
	 	    	limitHours    = sqlList.get(0).get(1).toString();
		 		validateDate = sqlList.get(0).get(9).toString();
	 	    	
	 	     }
	 	    if(cardType.equals("充值卡"))
	 	    {
	 	    	
	 	    	
	 	    	if(sqlList.get(0).get(4).toString().equals("未开卡"))
	 	    	{
	 	    		opened="未开卡";
	 	    		try {
						if(sdf.parse(sqlList.get(0).get(7).toString()).getTime()<sdf.parse(sdf.format(curDate)).getTime())
						{
							startDate=sqlList.get(0).get(7).toString();
							endDate  = sdf.format(new Date((sdf.parse(sqlList.get(0).get(7).toString()).getTime()+Long.parseLong(sqlList.get(0).get(8).toString())*24*60*60*1000)));
							
						} else
						{
							startDate=sdf.format(curDate);
							endDate  =sdf.format(new Date((sdf.parse(sdf.format(curDate)).getTime()+Long.parseLong(sqlList.get(0).get(8).toString())*24*60*60*1000)));
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 	    	} else
	 	    	{
	 	    		  startDate=sqlList.get(0).get(6).toString();
	 	    		  endDate  =sqlList.get(0).get(9).toString();
	 	    	}
	 	    	
	 	    	try {
					if(sdf.parse(startDate).getTime()>sdf.parse(sdf.format(curDate)).getTime() || sdf.parse(endDate).getTime()<sdf.parse(sdf.format(curDate)).getTime())
					{
						return "cardOverDue";  //卡不在有效期内。
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    	
	 	    	limitUse=sqlList.get(0).get(0).toString();
	 	    	
	 	    	if(Integer.parseInt(getSysSet("DayLimit").substring(1,2))>=1)
	 	    	{
	 	    		
	 	    		sqlList = findForList(" Select Count(*) As Num From BathItem "
                      +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  ");
	 	    		if(Integer.parseInt(limitUse)!=0)
	 	    		{
	 	    			if(Integer.parseInt(sqlList.get(0).get(0).toString())>=Integer.parseInt(limitUse))
	 	    			{
	 	    				
	 	    				return "overDayLimit";  //超过本日限制次数
	 	    			}
	 	    		}
	 	    	}
	 	    	
	 	    	 //限制卡场内人数仅1人(未结账的)
	 	    	if(1==1)
	 	    	{
	 	    		
	 	      sqlList = findForList(" Select Count(*) As Num From BathItem "
	 	     +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  And Checked=0 ");	
	 	    	if(Integer.parseInt(sqlList.get(0).get(0).toString())>=1)
	 	    		return "hadInBill";   //场内已存在记录
	 	    	}
	 	    	sqlList=getChargeFormula(getSysSet("Charge").substring(1,getSysSet("Charge").length()-1), consumeType, chargeTypeNo, timeTypeNo);
	 	    	if(sqlList.isEmpty())
	 	    	{
	 	    		return "nonExistChargeFormula";  //不存在改计费公式
	 	    	}
	 	    	leastLong=Integer.parseInt(sqlList.get(0).get(5).toString());
	 	    	pay      =Double.parseDouble(sqlList.get(0).get(4).toString());
	 	    	shouldPay=Double.parseDouble(sqlList.get(0).get(4).toString())*discount;
	 	    	
	 	        if(pay<0)
	 	        {
	 	        	return "consumeShouldGreteZero";  //消费价格应大于零
	 	        }
	 	        
	 	        if(dtbLeft+cashLeft<shouldPay)
	 	        	return "notSufficientFunds"; //余额不足
	     	 	        
	 	    }
	 	    
	 	    if(cardType.equals("时间卡"))
	 	    {
	 	    	if(sqlList.get(0).get(14).toString().equals("未开卡"))
	 	    	{
	 	    		opened="未开卡";
	 	    		
	 	    		try {
						if(sdf.parse(sqlList.get(0).get(16).toString()).getTime()<sdf.parse(sdf.format(curDate)).getTime())
						{
							startDate=sqlList.get(0).get(16).toString();
							endDate  = sdf.format(new Date((sdf.parse(sqlList.get(0).get(16).toString()).getTime()+Long.parseLong(sqlList.get(0).get(15).toString())*24*60*60*1000)));
							
						} else
						{
							startDate=sdf.format(curDate);
							endDate  =sdf.format(new Date((sdf.parse(sdf.format(curDate)).getTime()+Long.parseLong(sqlList.get(0).get(15).toString())*24*60*60*1000)));
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 	    		
	 	    	}  else
	 	    	{
	 	    		startDate=sqlList.get(0).get(17).toString();
	 	    		endDate  =sqlList.get(0).get(18).toString();
	 	    		
	 	    	}
	 	    	
	 	    	try {
					if(sdf.parse(startDate).getTime()>sdf.parse(sdf.format(curDate)).getTime() || sdf.parse(endDate).getTime()<sdf.parse(sdf.format(curDate)).getTime())
					{
						return "cardOverDue";  //卡不在有效期内。
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    	
	 	    if(!sqlList.get(0).get(12).toString().contains(consumeType))
	 	    	return "nonExistConsumeType";  //不存在消费项目
	 	    
	 	   if(!sqlList.get(0).get(13).toString().contains(timeTypeNo))
	 	    	return "nonExistTimeType";  //不存在该时间场次
	 	   
	 	  limitUse=sqlList.get(0).get(7).toString();
	 	String mm= getSysSet("DayLimit").substring(1,2);
	 	boolean n=Boolean.parseBoolean(getSysSet("DayLimit").substring(1,2));
	 	  
	 	  if(Integer.parseInt(getSysSet("DayLimit").substring(1,2))>=1)
	    	{
	    		
	    		sqlList = findForList(" Select Count(*) As Num From BathItem "
               +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  ");
	    		if(Integer.parseInt(limitUse)!=0)
	    		{
	    			if(Integer.parseInt(sqlList.get(0).get(0).toString())>=Integer.parseInt(limitUse))
	    			{
	    				
	    				return "overDayLimit";  //超过本日限制次数
	    			}
	    		}
	    	}
	 	    
	 	  //限制卡场内人数仅1人(未结账的)
	 	          if(1==1)    
	    	 {
	    		
	      sqlList = findForList(" Select Count(*) As Num From BathItem "
	     +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  And Checked=0 ");	
	    	if(Integer.parseInt(sqlList.get(0).get(0).toString())>=1)
	    		return "hadInBill";   //场内已存在记录
	    	}
	 	    	
	 	    }
	 	  if(cardType.equals("次卡")) 
	 	  {
	 		  
	 		  if(sqlList.get(0).get(18)==null)
	 			 startDate="1900-01-01"; 
	 		  else
	 		  startDate=sqlList.get(0).get(18).toString();
	 		  
	 		  if(sqlList.get(0).get(19)==null)
	 			 endDate="1900-01-01"; 
	 		  else
	    	  endDate  =sqlList.get(0).get(19).toString();
	 		  leftNum = Integer.parseInt(sqlList.get(0).get(14).toString());
	 		 
	 		  try {
				if(sdf.parse(startDate).getTime()>sdf.parse(sdf.format(curDate)).getTime() || sdf.parse(endDate).getTime()<sdf.parse(sdf.format(curDate)).getTime())
				{
				     if(sqlList.get(0).get(15).toString().equals("未开卡"))
					    {
					 
					     if(Integer.parseInt(sqlList.get(0).get(16).toString())>0)
					      {
						opened="未开卡";
						
						try {
							if(sdf.parse(sqlList.get(0).get(17).toString()).getTime()<sdf.parse(sdf.format(curDate)).getTime())
							{
								startDate=sqlList.get(0).get(17).toString();
								endDate  = sdf.format(new Date((sdf.parse(sqlList.get(0).get(17).toString()).getTime()+Long.parseLong(sqlList.get(0).get(16).toString())*24*60*60*1000)));
								
							} else
							{
								startDate=sdf.format(curDate);
								endDate  =sdf.format(new Date((sdf.parse(sdf.format(curDate)).getTime()+Long.parseLong(sqlList.get(0).get(16).toString())*24*60*60*1000)));
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}  else
					{
						return "cardOverDue";  //卡不在有效期内。
						
					  }
					} else
						return "cardOverDue";  //卡不在有效期内。
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	 	    	
	 	    //判断是否启用次卡扣次公式
	 		  if(1==1)
	 		  {
	 		 sqlList=getChargeFormula(getSysSet("Charge").substring(1,getSysSet("Charge").length()-1), consumeType, chargeTypeNo, timeTypeNo);
		 	  	if(sqlList.isEmpty())
		 	    {
		 	    		return "nonExistChargeFormula";  //不存在改计费公式
		 	    	}  
		 	  	if(sqlList.get(0).get(6)==null || Integer.parseInt(sqlList.get(0).get(6).toString())==0)
		 	  		deNum=1;
		 	  	else
		 	  	deNum=Integer.parseInt(sqlList.get(0).get(6).toString());


		 	  	
	 			  
	 		  } else
	 			  deNum=1;
	 		  if(leftNum<deNum)
	 			  return "leftNumNotEnough";   //次卡余次不足
	 	    	
	 	    if(!numConsumeType.contains(consumeType))
	 	    	return "nonExistConsumeType";  //不存在消费项目
	 	    
	 	   if(!numTimeTypeNo.contains(timeTypeNo))
	 	    	return "nonExistTimeType";  //不存在该时间场次
	 	   
	 	 
	 	  
	 	  if(Integer.parseInt(getSysSet("DayLimit").substring(1,2))>=1)
	    	{
	    		
	    		sqlList = findForList(" Select Count(*) As Num From BathItem "
            +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  ");
	    		if(Integer.parseInt(limitUse)!=0)
	    		{
	    			if(Integer.parseInt(sqlList.get(0).get(0).toString())>=Integer.parseInt(limitUse))
	    			{
	    				
	    				return "overDayLimit";  //超过本日限制次数
	    			}
	    		}
	    	}
	 	    
	 	  //限制卡场内人数仅1人(未结账的)
	 	          if(1==1)    
	    	 {
	    		
	      sqlList = findForList(" Select Count(*) As Num From BathItem "
	     +"  Where  InTime >='"+sdf.format(curDate)+"'  And  (   CardInsideNo ='"+insideNo+"' )  And [USE]='有效'  And Checked=0 ");	
	    	if(Integer.parseInt(sqlList.get(0).get(0).toString())>=1)
	    		return "hadInBill";   //场内已存在记录
	    	}
	 	    	
	 	  }
	 	  
	 	 sqlList = findForList(" Select  count(*) As Num From HoldCard  "
              +"  Where  ( InsideNo = '"+insideNo+"' ) And  (StartDate<='"+sdf.format(curDate)+"' And  EndDate>='"+sdf.format(curDate)+"' ) ");	
	 	 if(Integer.parseInt(sqlList.get(0).get(0).toString())>=1)
	 		 return "todayHoldCard"; //今日已封卡
	 	 
	 	 
	 		return "success";     //成功
			
		}
	 	
	 	public  boolean inCome()
	 	{
	 		
	 		String dtAll,dtDate,dtTime;
	 		String shouldLeaveTime,bathItemNo,billNo;
	 		Double dtbPay,cashPay;
	 		//Dao dao = new Dao();
	 		List<List> sqlList=new ArrayList<List>();
	 		 SimpleDateFormat    sdfAll    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
			   Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间        	    
		 
			    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");		    	
	    	  //   java.util.Date    curDate =   new    Date(System.currentTimeMillis());//获取当前时间 
	 	       SimpleDateFormat    sdfTime    =   new    SimpleDateFormat ("HH:mm:ss");  
	 	      dtAll = sdfAll.format(curDate);  
	 	      dtDate = sdfDate.format(curDate);  
	 	      dtTime = sdfTime.format(curDate);
	 	      
	 	    shouldLeaveTime= sdfAll.format(new Date((System.currentTimeMillis()+(Integer.parseInt(getSysSet("InDiff").substring(1,getSysSet("InDiff").length()-1))+Integer.parseInt(getSysSet("InDiff").substring(1,getSysSet("InDiff").length()-1))+Integer.parseInt(limitHours))*60*1000)));
	 	    
	 	   try {
				boolean autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				
			    billNo=getMainNo("Bill");
			    insert("Insert  into   Bill(KeyNo,BillNo,CheckTime,Operator,[Use],ConsumeName,Charge,[Pos])Values  "
                               +"   ('zj0000','"+billNo+"','"+dtAll+"','ZJ001','有效','"+cardType+"','"+chargeTypeNo+"','闸机通道（刷卡）')");
			    bathItemNo=getMainNo("BathItem");
			    
			    insert(" Insert  into  BathItem(Description,BillNO,BathItemNo,KeyNo,                       GetKeyTime,InTime,ShouldLeaveTime,CheckTime,TimeTypeNo,ChargeTypeNo,ConsumeType,PayType,CountTime,CardNo,CardInsideNo,CardType,SubNum,Operator,Checked,[USE],PreGet,CustomerNo,Limitelong,[Pos],KeyUsed)Values  "
                              +" ('','"+billNo+"','"+bathItemNo+"','zj0000','"+dtAll+"','"+dtAll+"','"+shouldLeaveTime+"','"+dtAll+"','"+timeTypeNo+"','"+chargeTypeNo+"','"+consumeType+"','"+cardType+"',0,'"+cardNo+"','"+insideNo+"','"+cardType+"',1,'ZJ001',0,'有效',0,'"+cardNo+"',"+limitHours+",'闸机通道（刷卡）',1)");
			    
				if(cardType.equals("充值卡"))
				{
					sqlList = findForList(" select DTBLeft from Customer where CustomerNo='"+cardNo+"'   ");
					
					 //如果不限制赠币使用，则自动使用赠币计算。
					if(Double.parseDouble(sqlList.get(0).get(0).toString())>=pay)
					{
						dtbPay=pay;
						cashPay=0.0;
					} else
					{
						dtbPay=Double.parseDouble(sqlList.get(0).get(0).toString());
						cashPay=pay-Double.parseDouble(sqlList.get(0).get(0).toString());
						
					}
					
					insert("Insert  into  CheckCash(BillNo,TicketNum,TimeType,ConsumeType,ChargeType,BathItemNo,OutOfTime,CustomerNo,RefPrice,TicketPrice,   Discount,CheckTime,                                                  ShouldGet,    BankGet, CardGet,    [Get], RealGet,       OtherCashGet,   [Return],        [Use],PreGet,SunCard,OtherCard)Values  "
                              +"      ('"+billNo+"',1,'"+timeTypeNo+"','"+consumeType+"','"+chargeTypeNo+"','"+bathItemNo+"','入场','"+cardNo+"',"+shouldPay+","+shouldPay+","+discount+",'"+dtAll+"',"+pay+",  0,  "+cashPay+",     0,     0, "+dtbPay+",0,'有效', 0,0,0)");
					//更新客户资料
				    update("Update  Customer  Set CashPay  = CashPay  + "+cashPay+" ,  "
                                    +"  CashLeft = CashLeft - "+cashPay+" ,  "
                                    +"  DTBPay   = DTBPay   + "+dtbPay+" ,  "
                                    +"  DTBLeft  = DTBLeft  - "+dtbPay+"  "
                                +"  Where CustomerNo='"+cardNo+"'");
				    if(opened.equals("未开卡"))
				    {
				    	update("Update Customer  "
                              +" Set   Opened  ='已使用', "
                              +" OpenDate='"+startDate+"', "
                              +" ValidateDate ='"+endDate+"' "
                +"  Where  CustomerNo = '"+cardNo+"'");
				    	
				    }
				}
				 //3.0时间卡结帐
				if(cardType.equals("时间卡"))
				{
				
					insert("Insert  into  CheckTimeCard( BillNo,      BathItemNo,OutOfTime,             CheckTime,                            TimeCardNo,           InsideNo,   Num,[Use])Values "
                                 +"      ('"+billNo+"','"+bathItemNo+"','入场','"+dtAll+"','"+cardNo+"','"+insideNo+"',1,'有效') ");
					
					//更新时间卡使用情况
					update("Update  TimeCard  Set UseTimes = UseTimes+1  "
                            +"    Where TimeCardNo='"+cardNo+"'");
					
					  if(opened.equals("未开卡"))
					    {
					    	update("Update TimeCard  "
	                              +" Set   Opened  ='已使用', "
	                              +" [Start]='"+startDate+"', "
	                              +"[End] ='"+endDate+"' "
	                +"  Where  TimeCardNo = '"+cardNo+"'");
					    	
					    }
					
				}
				
				//次卡结账
				if(cardType.equals("次卡"))
				{
					insert(" Insert  into  CheckNumCard(BillNo,      BathItemNo,OutOfTime,CheckTime,NumCardNo,InsideNo,Num,[Use])Values  "
                              +" ('"+billNo+"','"+bathItemNo+"','入场','"+dtAll+"','"+cardNo+"','"+insideNo+"',1,'有效') ");
				
				//更新次卡使用情况
				update(" Update  NumCard  Set UseNum = UseNum  + "+deNum+", "
                                 +"  LeftNum= LeftNum - "+deNum+"  "
                               +" Where NumCardNo='"+cardNo+"'");
				
				  if(opened.equals("未开卡"))
				    {
					  if(opened.equals("未开卡"))
					    {
					    	update("Update NumCard  "
	                              +" Set   Opened  ='已使用', "
	                              +" [Start]='"+startDate+"', "
	                              +"[End] ='"+endDate+"' "
	                +"  Where  NumCardNo = '"+cardNo+"'");
					    	
					    }
				    	
				    }
				}
				conn.commit();
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				
				billNo="";
				bathItemNo="";
				e.printStackTrace();
				return false;
			}
	 	    
	 	   return true;
	 	}
	 	//钥匙出场判断
	 	@SuppressWarnings("unchecked")
		public String callCardOut(String insideNo)
	 	{
	 		
	 	List<List> sqlList=new ArrayList<List>();
	 	String dtAll,dtDate,dtTime;
	 	String inTime,bathItemNo,billNo;
	 	int limiteLong,ls;
	 	long LS=0,CS=0;
    	double CSB = 0;
    	String BC;
    	String chargeTypeNo, timeTypeNo,consumeType;
	 	Double dtbLeft,cashLeft,sumLeft;
	 	String bookPrice,price,leastPrice;
	 	
	 	
	 	 SimpleDateFormat    sdfAll    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
		   Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间        	    
	 
		    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");		    	
  	
	       SimpleDateFormat    sdfTime    =   new    SimpleDateFormat ("HH:mm:ss");  
	      dtAll = sdfAll.format(curDate);  
	      dtDate = sdfDate.format(curDate);  
	      dtTime = sdfTime.format(curDate);
	      
	 	
	 	sqlList = findForList("Select C.CustomerNo, BillNo,BathItemNo,CardNo,InTime,ChargeTypeNo,ConsumeType,TimeTypeNo,LimiteLong,BI.CardType,KeyNo,Operator,C.DTBLeft,C.CashLeft,Description "
        +" from  BathItem BI join Customer C on BI.CustomerNo=C.CustomerNo "
        +" Where  BI.[USE]='有效'  And ( BI.CardInsideNo = '"+insideNo+"' or CardNo='"+insideNo+"' ) And InTime > '"+sdfDate.format(new Date(System.currentTimeMillis()-2*24*60*60*1000))+"' And BI.Checked=0     ");
	 	
	 	if(sqlList.isEmpty())
	 	{
	    sqlList = findForList("Select A.ENo As CustomerNo,BI.BillNo,BathItemNo,BI.CardNo,InTime,ChargeTypeNo,ConsumeType,TimeTypeNo,BI.LimiteLong,BI.CardType,KeyNo,Operator,0 as DTBLeft,0 as CashLeft "
                 +" from  BathItem BI join AutoENO A on BI.BillNo=A.BillNo "
         +"   Where  BI.[USE]='有效'  And ( BI.CardInsideNo = '"+insideNo+"' or BI.CardNo='"+insideNo+"' ) And InTime > '"+sdfDate.format(new Date(System.currentTimeMillis()-2*24*60*60*1000))+"' And BI.Checked=0   ");
	 	
	    if(sqlList.isEmpty())
	    {
	    	cardType="无卡";
	    	return "nonExistCustomerNo"; //会员卡号不存在！
	    }
	 		
	 	}
	 	  cardType=sqlList.get(0).get(9).toString();
	 	  cardNo  =sqlList.get(0).get(0).toString();
	 	  inTime  =sqlList.get(0).get(4).toString();
	 	  if(sqlList.get(0).get(8)==null)
	 		 limiteLong=0;
	 	  else
	 	  limiteLong=Integer.parseInt((String)sqlList.get(0).get(8));
	 	  bathItemNo=sqlList.get(0).get(2).toString();
	 	  chargeTypeNo=sqlList.get(0).get(5).toString();
	 	  timeTypeNo=sqlList.get(0).get(7).toString();
	 	  consumeType=sqlList.get(0).get(6).toString();
	 	  dtbLeft=Double.parseDouble(sqlList.get(0).get(12).toString());
	 	  cashLeft=Double.parseDouble(sqlList.get(0).get(13).toString());
	 	  sumLeft=dtbLeft+cashLeft;
	 	  
	 	 long inDate = 0;
			try {
				inDate = sdfAll.parse(inTime).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long diff = curDate.getTime() - inDate;//这样得到的差值是微秒级别
		
	     
	     long minutes = diff / (1000 * 60);  //得到相隔的分钟数
	     tempTimeDiff = minutes;
	     LS           =	minutes-Integer.parseInt(getSysSet("InDiff").substring(1,getSysSet("InDiff").length()-1))-Integer.parseInt(getSysSet("OutDiff").substring(1,getSysSet("OutDiff").length()-1));
	 	  if(!getSysSet("OutOfTime").substring(1, 2).equals("0"))     //注意这里要和客户端的超时是否开闸配合使用。
	 	  {
	 		 if(LS>limiteLong)
	 		 {
	 			 sqlList=getChargeFormula(getSysSet("Charge").substring(1,getSysSet("Charge").length()-1), consumeType, chargeTypeNo, timeTypeNo);
	 			 if(sqlList.isEmpty())
	 			 {
	 				 return "nonExistChargeFormula"; //未设置计费公式
	 			 }
	 			 
	 			 CS=LS-limiteLong;
	 			 String timeUnit=sqlList.get(0).get(0).toString();
	 			bookPrice=sqlList.get(0).get(2).toString();
	 			price=sqlList.get(0).get(3).toString();
	 			leastPrice=sqlList.get(0).get(4).toString();
 			 
	 			int CSP=(int) ((CS+Integer.parseInt(timeUnit)-1)/Integer.parseInt(timeUnit));
	 		if(LS>Long.parseLong(sqlList.get(0).get(1).toString()))
	 		  BC="bc";
	 		else
	 			BC="js";
	 		
	 		 if(BC.equals("bc"))
	    			 CSB=Double.parseDouble(bookPrice)-Double.parseDouble(leastPrice);
	    		  else
	    			  CSB=CSP*Double.parseDouble(price);
	    	 String CSF= new DecimalFormat("#.00").format(CSB);
	 		
	 		  if(sumLeft<CSB)
	 		  {
	 			  
	 			  return "cardOutOfTime"; //卡已超时，余额不足
	 		  }
	 		 
	 		 }
	 	  }
	 	  
	 	 try {
				boolean autoCommit = conn.getAutoCommit();
				conn.setAutoCommit(false);
				  if(!getSysSet("OutOfTime").substring(1, 2).equals("0"))     //注意这里要和客户端的超时是否开闸配合使用。
			 	  {
					  if(CSB>0)
					  {
					billNo=getMainNo("Bill");
					
					insert("Insert  into   Bill(BillNo,CheckTime,Operator,[Use],ConsumeName,Pos)Values  "
                                 +"     ('"+billNo+"','"+dtAll+"','ZJ001','有效','出场','闸机通道（刷卡）')");
					  
					insert(" Insert  into  CheckCash(BillNo,CustomerNo,CheckTime,                                                 RefPrice,         ShouldGet,      BankGet,         CardGet,           [Get],     RealGet,       OtherCashGet,   [Return],        [Use],PreGet,OutOfTime,ChargeType,TimeType,ConsumeType,SunCard,OtherCard)Values  "
                               +"             ('"+billNo+"','"+cardNo+"','"+dtAll+"',"+CSB+","+CSB+",0,     "+CSB+",   0,0,0,0,'有效',0,'入场','"+chargeTypeNo+"','"+timeTypeNo+"','"+consumeType+"',0,0)");  
					 
					 //如果不限制赠币使用，则自动使用赠币计算。
					if(dtbLeft>=CSB)
					{
						dtbPay=CSB;
						cashPay=0.0;
					} else
					{
						dtbPay=dtbLeft;
						cashPay=CSB-dtbLeft;
						
					}
				
					//更新客户资料
				    update("Update  Customer  Set CashPay  = CashPay  + "+cashPay+" ,  "
                                    +"  CashLeft = CashLeft - "+cashPay+" ,  "
                                    +"  DTBPay   = DTBPay   + "+dtbPay+" ,  "
                                    +"  DTBLeft  = DTBLeft  - "+dtbPay+"  "
                                +"  Where CustomerNo='"+cardNo+"'");
					  
					  }
			 	  }
			   
				  update("Update  BathItem  Set Checked = 1,CheckTime='"+dtAll+"' "
                         +"      Where   BathItemNo ='"+bathItemNo+"'");
				conn.commit();
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				
				bathItemNo="";
				e.printStackTrace();
				return "fail";
			}
	 	  
	 		return "success";
	 	}
	 	//消费历史
	 	 public  List getHistory(String startTime,String endTime,String searchData) {
	  		@SuppressWarnings("rawtypes")
			List list = findForList("select  B.BillNo,BI.InTime,BI.ConsumeType,BI.CardType,BI.CardNo,B.Pos from Bill B Left Join BathItem BI On B.BillNo=BI.BillNo "
	  				+ " Where InTime>'"+startTime+"'  and InTime<'"+endTime+"' and "+searchData+"  Order By BI.InTime       ");
	  		return list;
	  	}
	 	//消费历史,通过客户卡号
	 	 public  List getHistoryByCustomerNo(String startTime,String endTime,String customerNo) {
	  		List list = findForList("select  B.BillNo,BI.InTime,BI.ConsumeType,BI.CardType,BI.CardNo,B.Pos from Bill B Left Join BathItem BI On B.BillNo=BI.BillNo "
	  				+ " Where InTime>'"+startTime+"'  and InTime<'"+endTime+"' and CardNo='"+customerNo+"'   Order By BI.InTime       ");
	  		return list;
	  	}
	 	//消费历史，通过消费项目
	 	 public  List getHistoryByConsumeType(String startTime,String endTime,String consumeType) {
	  		List list = findForList("select  B.BillNo,BI.InTime,BI.ConsumeType,BI.CardType,BI.CardNo,B.Pos from Bill B Left Join BathItem BI On B.BillNo=BI.BillNo "
	  				+ " Where InTime>'"+startTime+"'  and InTime<'"+endTime+"' and BI.ConsumeType='"+consumeType+"'  Order By BI.InTime       ");
	  		return list;
	  	}
	 	//消费历史，通过站点
	 	 public  List getHistoryByPos(String startTime,String endTime,String pos) {
	  		List list = findForList("select  B.BillNo,BI.InTime,BI.ConsumeType,BI.CardType,BI.CardNo,B.Pos from Bill B Left Join BathItem BI On B.BillNo=BI.BillNo "
	  				+ " Where InTime>'"+startTime+"'  and InTime<'"+endTime+"' B.Pos='"+pos+"'  Order By BI.InTime       ");
	  		return list;
	  	}
	 	//消费历史，通过客户卡号、消费项目、站点
	 	 public List getHistoryByCnCtPos(String startTime,String endTime,String customerNo,String consumeType,String pos) {
	  		List list = findForList("select  B.BillNo,BI.InTime,BI.ConsumeType,BI.CardType,BI.CardNo,B.Pos from Bill B Left Join BathItem BI On B.BillNo=BI.BillNo "
	  				+ " Where InTime>'"+startTime+"'  and InTime<'"+endTime+"' and CardNo='"+customerNo+"' and BI.ConsumeType='"+consumeType+"' and B.Pos='"+pos+"' Order By BI.InTime       ");
	  		return list;
	  	}
	 	 
	 	//获取消费项目
	 	 public List getConsumeType() {
	  		List list = findForList("select Name from ConsumeType "
	  				+ "   Group By  Name   ");
	  		return list;
	  	}
	 	//获取站点信息
	 	 public List getPos() {
	  		List list = findForList("select Position from Register"
	  				+ " Group By  Position      ");
	  		return list;
	  	}
	 	 
	 	//获取消费项目
	 	 public List getOperator() {
	  		List list = findForList("select EmployeeNo from Employee "
	  				+ "   order By  EmployeeNo   ");
	  		return list;
	  	}
	 	//获取配置信息
	 	 public String getSysSet(String sysNo) {
	  		List list = findForList("select Value from SysSet "
	  				+ " Where  SysNo='"+sysNo+"'");
	  		String value=list.get(0).toString();
	  		return value;
	  	}
	 	//获取计费公式
	 	 
	 	 public List getChargeFormula(String Charge,String ConsumeType,String ChargeType,String TimeType )
	 	 {
	 		List list = findForList("Select TimeUnit,LimitLong,BookPrice,Price,LeastPrice,LeastLong,DeNum From  ChargeFormula "
               +" Where  ConsumeType = '"+ConsumeType+"' And  ChargeTypeNo = '"+ChargeType+"' And TimeTypeNo ='"+TimeType+"' And Charge ='"+Charge+"' ");
	  		return list;
	 		 
	 	 }
    //钥匙状态
	 	 public List getKeySatus(String insideNo,String searchType) {
	 		List list=null;
	 		 if(searchType.equals("ifKeyInValid")) //入场是否有效
	 		 {		 
		  		list = findForList(" Select K.KeyNo,K.Door,BathItemNo,CardNo,CardType,ChargeType   From  [Key] K "  
             +" Where  K.InsideNo ='"+insideNo+"' And K.OpenDoor = 0  And  K.State = 1    ");
	 		 }
	 		 
	 		 if(searchType.equals("ifKeyOutValid"))//出场是否有效
	 		 {		 
		  		list = findForList(" Select K.KeyNo,K.OpenDoor,BathItemNo,CardNo,ChargeType,Checked,InTime,LimiteLong,ChargeType,TimeType,ConsumeName,CardType   From  [Key] K "  
             +" Where  K.InsideNo ='"+insideNo+"' And (State  = 1 or Checked=1)    ");
	 		 }
	 		 
	 		 if(searchType.equals("ifKeyExist"))//是否存在
	 		 {		 
			  		list = findForList(" Select K.KeyNo,K.Door,BathItemNo,CardNo,ChargeType   From  [Key] K "  
	             +" Where  K.InsideNo ='"+insideNo+"'  ");
		 		 }
	 		if(searchType.equals("ifKeyActive"))//是否激活
	 		{
	 			list = findForList(" Select K.KeyNo,K.Door,BathItemNo,CardNo,CardType   From  [Key] K "  
	 		             +" Where  K.InsideNo ='"+insideNo+"'  And  (K.State = 1  and K.OpenDoor != 2)  ");
	 		}
	 		if(searchType.equals("ifKeyBeUsed"))//钥匙是否被使用
	 		{
	 			list = findForList(" Select K.KeyNo,K.Door,BathItemNo,CardNo,CardType,ChargeType   From  [Key] K "  
	 		             +" Where  K.InsideNo ='"+insideNo+"' And K.OpenDoor = 1 And  K.State = 1    ");
	 		}
		  		return list;
		  	}
	     
	    
	      public boolean checkIfPasswordCard(String insideNo)
		  			throws SQLException {
		  		
		  		ResultSet rs = findForResultSet("select EmployeeNo from Employee where "
		  			+ " InsideNo='" + insideNo + "'");
		  		if (rs == null)
		  			return false;
		  		return rs.next();
		  	}
	      public boolean checkLogin(String userStr, String passStr)
		  			throws SQLException {
		  		
		  		ResultSet rs = findForResultSet("select * from Employee where EmployeeNo='"
		  			+ userStr.toUpperCase() + "' and PassWord='" + passStr + "'");
		  		if (rs == null)
		  			return false;
		  		return rs.next();
		  	}
	      
	   // 修改库存的方法
	  	public int exeKeyInUpdateKey(String insideNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt       =    formatter.format(curDate);       

	  		return update(" Update  [Key] Set InTime='"+dt+"',OpenDoor = 1"
	               +" Where   InsideNo ='"+insideNo+"'");
	  	}
	  	//如果钥匙已经结账。则可以通过。
		public int exeKeyOutUpdateChecked(String insideNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  [Key] Set checked = 0 ,OpenDoor = 2 ,State = 0"
	               +" Where   InsideNo ='"+insideNo+"'");
	  	}
	  	
	  	
	  	public int exeKeyInUpdateBathItem(String bathItemNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  BathItem  Set  InTime='"+dt+"' "
                 +"   Where       BathItemNo ='"+bathItemNo+"' ");
	  	}
	  	
	  	public int exeKeyOutUpdateBathItemChecked(String bathItemNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  BathItem  Set  Checked = 1,CheckTime='"+dt+"' "
                 +"   Where       BathItemNo ='"+bathItemNo+"' ");
	  	}
	  	//超时但是不许出场。
	  	public int exeCsNotOutUpdateKey(String keyInsideNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  [Key]  Set  OutTime='"+dt+"' "
                 +"   Where       InsideNo ='"+keyInsideNo+"' and State=1 ");
	  	}
		//超时但是不许出场。
	  	public int exeOutNotCheckedUpdateKey(String keyInsideNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  [Key]  Set  OpenDoor = 2,OutTime='"+dt+"' "
                 +"   Where       InsideNo ='"+keyInsideNo+"'");
	  	}
	  	
	  //超时但是允许出场。
	  	public int exeCsButCanOutUpdateKey(String keyInsideNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		return update(" Update  [Key]  Set  State             = 0, OpenDoor = 2 , "
                           +"   CountTime         = 1,   "
                           +" TimeType          = NULL,ConsumeName=NULL,ChargeType=NULL,PreGet=0,Description=NULL,WithKey=NULL,TeamKey=NULL,Intime = NULL," 
                           +"        FirstNum          = 0,FirstAmount = 0,  "
                           +"    SecNum            = 0,SecAmount = 0,  "
                           +"      LimiteLong        = 0,Longs = 0,  "
                           +"         BathItemNo        = NULL,  " 
                           +"          CardNo            = NULL,   "
                           +"           CardInsideNo      = NULL,   "
                           +"           CardType          = NULL   "
                 +"   Where       InsideNo ='"+keyInsideNo+"' and State=1 ");
	  	}
	  	
	  //
	  	public List searchSKIncome(String startTime,String endTime,String searchPar) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		List list = findForList(" SELECT B.Pos,B.BillNo,B.Operator,B.KeyNo,B.CheckTime,CC.CustomerNo,C.Name,cc.ENO,CC.PreGet,CC.BankGet,CC.CardGet,CC.OtherCashGet,CC.OtherCard,CC.Get,CC.RealGet,CC.SunCard,CC.[Return],CC.RefPrice,CC.discount,CC.ShouldGet,CC.TicketPrice,CC.TicketNum ,CC.ChargeType,CC.ConsumeType,CC.TimeType "
                          +" From  ( Bill B Left Join CheckCash CC On B.BillNo = CC.BillNo ) "                                                                                                                                                                                                                       
                          +"       Left Join Customer C On CC.CustomerNo = C.CustomerNo "                                                                                                                                                                                                                                  
                          +"   Where  B.[USE]= '有效'  And ConsumeName='散客' and B.CheckTime>'"+startTime+"' and B.CheckTime<'"+endTime+"' and "+ searchPar+" Order By B.CheckTime");
	  		return list;
	  	}
	  	
	  	public List searchGetCardIncome(String startTime,String endTime,String searchPar) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		List list = findForList(" select B.Pos,B.GetCardAddNo,B.Description,B.RefPrice,B.DisCount,B.CustomerNo,C.Name,B.UnPay,B.[GetCash],B.Cash,B.OtherCash,B.Bank,B.Operator,B.OperateTime,B.OtherCard,B.SunCard  "
               +"  from   (GetCardAdd  B Left Join Customer C On B.CustomerNo = C.CustomerNo)        "
               +"  Where  [USE]= '有效' and OperateTime>'"+startTime+"' and OperateTime<'"+endTime+"'  and "+ searchPar+" Order By OperateTime");
	  		return list;
	  	}
	  	
	  	public List searchNumCardIncome(String startTime,String endTime,String searchPar) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		List list = findForList("  SELECT   B.NumCardType,B.Description,B.RefPrice,B.Discount,B.ShouldGet,B.Pos,NumCardAddNo,B.CustomerNo,C.Name,B.UnPay,B.SignCustomer,Cash,Bank,CardGet, Operator, OperateTime,B.CardNewOld,B.OtherCard,B.SunCard  "
                +" FROM  NumCardAdd AS B  LEFT JOIN Customer AS C ON B.CustomerNo=C.CustomerNo  "
                +" Where  [USE]= '有效' and OperateTime>'"+startTime+"' and OperateTime<'"+endTime+"' and "+ searchPar+" Order By OperateTime");
	  		return list;
	  	}
	  	
	  	public List searchTimeCardIncome(String startTime,String endTime,String searchPar) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		List list = findForList(" SELECT  B.TimeCardType,B.Description,B.RefPrice,B.Discount,B.ShouldGet,B.Pos,[TimeCardPubNo], B.CustomerNo, C.Name,B.UnPay,B.SignCustomer, [Cash], [Bank],CardGet, [Start],[End],[Operator], [OperateTime],B.CardNewOld,B.OtherCard,B.SunCard   "
               +"  FROM    TimeCardPub AS B LEFT JOIN Customer AS C ON B.CustomerNo=C.CustomerNo       "
               +"  Where  [USE]= '有效' and OperateTime>'"+startTime+"' and OperateTime<'"+endTime+"' and "+ searchPar+" Order By OperateTime");
	  		return list;
	  	}
	  	public List searchGoodsIncome(String startTime,String endTime,String searchPar) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       

	  		List list = findForList(" SELECT B.Pos,B.BillNo,B.Operator,B.CheckTime,CC.CustomerNo,C.Name,CC.PreGet,CC.BankGet,CC.CardGet,CC.OtherCashGet,CC.OtherCard,CC.SunCard,CC.Get,CC.RealGet,CC.[Return],CC.ShouldGet,CC.RefPrice,CC.Discount  "
                 +" From  ( Bill B Left Join CheckCash CC On B.BillNo = CC.BillNo )   "                                                                                                                                                                                                                     
                 +"      Left Join Customer C On CC.CustomerNo = C.CustomerNo    "                                                                                                                                                                                                                               
                +"  Where  B.[USE]= '有效'  And ConsumeName='专卖店'  and B.CheckTime>'"+startTime+"' and B.CheckTime<'"+endTime+"' and "+ searchPar+" Order By B.CheckTime");
	  		return list;
	  	}
	  	
	  	public List searchCustomer(String customerNo) {
	  		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
	  		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
	  		String    dt    =    formatter.format(curDate);       
	  		List<List> sqlList=new ArrayList<List>();
	  		String masterCardNo="";
	  		String cardNo="",cardType="";
	  		
	  		 sqlList = findForList(" select CustomerNo,InsideNo,Id,Name,CardType,MasterCardNo"
                      +" from Customer where CustomerNo = '"+customerNo+"' or insideNo='"+customerNo+"'");
	  		
	 	    	if(sqlList.isEmpty())
	 	    		return null;
	 	    	else
	 	    	{
	 	    	cardType=sqlList.get(0).get(4).toString();
	 	    	cardNo  =sqlList.get(0).get(0).toString();
	 	    	}
	 	    	//insideNo=sqlList.get(0).get(0).toString();
	 	    	if(cardType.equals("副卡"))
	 	    	{
	 	    		
	 	    		
	 	    		if(sqlList.get(0).get(5)==null)
	 	    			return null;
	 	    		else
	 	    		 masterCardNo=sqlList.get(0).get(5).toString();
	 	    		//判断主卡号
	 	    		sqlList = findForList("Select   CustomerNo,State,CardType  from Customer  "
                     +"  Where   ( InsideNo = '"+masterCardNo+"'  Or  CustomerNo = '"+masterCardNo+"' )    ");
	 	    		if(sqlList.isEmpty())
	 	    			return null; //主卡号不存在
	 	    		else
	 	    		{
	 	    			cardType=sqlList.get(0).get(2).toString();
	 		 	    	cardNo  =sqlList.get(0).get(0).toString();
	 	    			 
	 	    		}
	 	    		
	 	    	}
	  		 	
	 	    	if(cardType.equals("充值卡"))
	 	    	{
	  			sqlList = findForList(" select CustomerNo,InsideNo,Id,Name,CardType,CashAmount,CashPay,CashLeft,DTBAmount,DTBPay,DTBLeft,Mobile,[memo],State,Photo,OpenDate,ValidateDate "
	                      +" from Customer  where CustomerNo = '"+customerNo+"' or insideNo='"+customerNo+"'");
	 	    	}
	 	    	
	 	    	if(cardType.equals("次卡"))
	 	    	{
	 	    		sqlList = findForList(" select C.CustomerNo,InsideNo,Id,Name,CardType,CashAmount,CashPay,CashLeft,DTBAmount,DTBPay,DTBLeft,Phone,Mobile,[memo],State,Photo,NC.Start,[End] "
		                      +" from NumCard NC Left Join Customer C On NC.NumCardNo=C.CustomerNo where CustomerNo = '"+customerNo+"'");	
	 	    	}
	 	    	if(cardType.equals("时间卡"))
	 	    	{
	 	    		sqlList = findForList(" select C.CustomerNo,C.InsideNo,Id,Name,CardType,CashAmount,CashPay,CashLeft,DTBAmount,DTBPay,DTBLeft,Mobile,[memo],State,Photo,TC.Start,[End] "
		                      +" from TimeCard TC Left Join Customer C On TC.TimeCardNo=C.CustomerNo where TC.TimeCardNo = '"+customerNo+"'");  //注意线下软件在做补卡处理的时候，numcardadd和timecardpub表中的字段Customerno没有做相应的更新。
	 	    	}
	  		return sqlList;
	  	}
	  	
	  	 public static List findForList(String sql) {
		  		List<List> list = new ArrayList<List>();
		  		ResultSet rs = findForResultSet(sql);
		  		try {
		  			ResultSetMetaData metaData = rs.getMetaData();
		  			int colCount = metaData.getColumnCount();
		  			while (rs.next()) {
		  				List<String> row = new ArrayList<String>();
		  				for (int i = 1; i <= colCount; i++) {
		  					String str = rs.getString(i);
		  					if (str != null && !str.isEmpty())
		  						str = str.trim();
		  					row.add(str);
		  				}
		  				list.add(row);
		  			}
		  		} catch (Exception e) {
		  			e.printStackTrace();
		  		}
		  		return list;
		  	}
	  	//查询
	    public static  ResultSet findForResultSet(String sql) {
	  		if (conn==null)
	  			return null;
	  		long time = System.currentTimeMillis();
	  		Statement stmt = null;
	  		ResultSet rs = null;
	  		try {
	  			
	  			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	  					ResultSet.CONCUR_READ_ONLY);
	  			rs = stmt.executeQuery(sql);
	  			second = ((System.currentTimeMillis() - time) / 1000d) + "";
	  			
	  		//	stmt.close();
  			//	conn.close();   
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		} 
	  		return rs;
	  	}
	    
	    //修改更新、
	  	public static int update(String sql) {
			int result = 0;
			Statement stmt = null;
			try {
				 stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				
				//stmt.close();
  				//conn.close();   
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return result;
		}
	  	//插入
	  public static boolean insert(String sql) {
			boolean result = false;
			Statement stmt = null;
			try {
				 stmt = conn.createStatement();
				result = stmt.execute(sql);
				
			//	stmt.close();
  			//	conn.close();   
			} catch (SQLException e) {
				e.printStackTrace();
			}  
			return result;
		}

}