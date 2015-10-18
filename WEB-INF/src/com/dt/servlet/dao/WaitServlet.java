package com.dt.servlet.dao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dt.servlet.conf.ConfManager;

 /**
 
  * 登录模块校验Servlet

  * @author Administrator

  *

  */

  public class WaitServlet extends HttpServlet {


     private static final long serialVersionUID = 1L;
    
 	
 	
     public WaitServlet()

     {

         super();

     }

    
     protected void doGet(HttpServletRequest req, HttpServletResponse res)

             throws ServletException, IOException {

         res.setContentType("text/html;charset=utf-8");

         req.setCharacterEncoding("utf-8");

         res.setCharacterEncoding("utf-8");
         List<List> tempList = new ArrayList<List>();
         SimpleDateFormat    formatterIn    =   new    SimpleDateFormat    ("yyyy-MM-dd hh:mm:ss");       
		   Date    curDateIn    =   new    Date(System.currentTimeMillis());//获取当前时间       
	    //inTime= new SimpleFormat().format(inTime,"yyyy-MM-dd");
	    // Date    curDateIn    =   new    Date(curDateIn);//获取当前时间       
		String  inTime    =    formatterIn.format(curDateIn);
		Dao dao = new Dao();
		ConfManager confManager = ConfManager.getInstance();
		String driver = confManager.getValue("driver");
		String url    = confManager.getValue("url");
		String userName = confManager.getValue("userName");
		String password = confManager.getValue("password");
	//	tempList=dao.searchCustomer("czk123123");
      //   String billno=dao.getMainNo("Bill");
      //   String checkCard=dao.checkCard("ck123123");
       //  boolean inCome=dao.inCome();
    //     String callCardOut=dao.callCardOut("ck123123");
         
         
         List<List> list1 = new ArrayList<List>();
          list1 =dao.getHistory("2015-04-01",inTime,"1=1");
        
         PrintWriter out = res.getWriter();
         
       //  out.println("Hello11, Brave new World!");
        // out.println("mm1"+dao.dbClassName);
         out.println("Hello World!1212 ");
       //  out.println(billno);
         for(List<String> lt:list1)
         {  
        	 ArrayList<String> alt = (ArrayList<String>)lt;
        	 for(String str:alt)
        	 out.println(" "+str);
        	 out.println("<br/>");
         }
       
         out.close();

     }

     

     @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse res)

             throws ServletException, IOException {

         if (null == req)

         {

             return;

         }

         res.setContentType("text/html;charset=utf-8");

         req.setCharacterEncoding("utf-8");

         res.setCharacterEncoding("utf-8");

         

         PrintWriter out = res.getWriter();
         
         
         
         String operatetype = req.getParameter("operate_type");
        if(operatetype.equals("identityverification"))
        {
        	Dao dao = new Dao();   //每种情况申请一个实例
        	
        	String username = req.getParameter("user_name");

            String password = req.getParameter("password");
            
         if(username.toString()!=null || password.toString()!=null)
         {
         try {
			if (dao.checkLogin(username, password))
			 {			 			    	
			         out.print("正确");        ///< 正确
			     }
			     else
			     {
			         out.print("错误");        ///< 密码错误
			     }
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

         }

         else

         {

             out.println("用户名或密码为空！");            ///< 用户名错误

          }
        }
        else if(operatetype.equals("ConsumeHistory"))
        {
        	
        	String startTime = req.getParameter("start_time");

            String endTime = req.getParameter("end_time");
            
            String searchData = req.getParameter("search_data");
            
            Dao dao = new Dao(); 
            
            
            String searchAdd="";
             if(searchData.isEmpty())
            	 searchAdd="1=1";
             else
             {
            	 String[] arr=searchData.split("&");
            	 for(int i=1;i<arr.length;i++)
            	 {
            		 searchAdd=searchAdd+arr[i];
            		 if(i<arr.length-1)
            		 searchAdd=searchAdd+" and ";
            			 
            	 }
             }
        	 List<List> list1 = new ArrayList<List>();
	         list1 =dao.getHistory(startTime,endTime,searchAdd);
	         for(List<String> lt:list1)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("&"+str);
	        	// out.print("&");
	        	// out.print("\n");
	        	// out.println("<br/>");
	         }
        }
        else if(operatetype.equals("searchCustomer"))
        {
        	
        	//String startTime = req.getParameter("start_time");

           String sCardNo = req.getParameter("card_no");
            
        	 List<List> customerList = new ArrayList<List>();
        	// List<List> posList = new ArrayList<List>();
        	 Dao dao = new Dao();
        	 customerList =dao.searchCustomer(sCardNo);
        	 
	         for(List<String> lt:customerList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("&"+str);
	        	 
	         }
        }
        else if(operatetype.equals("SearchPosAndConsumeType"))
        {
        	
        	//String startTime = req.getParameter("start_time");

         //   String endTime = req.getParameter("end_time");
            
        	 List<List> consumeTypeList = new ArrayList<List>();
        	 List<List> posList = new ArrayList<List>();
        	 Dao dao = new Dao();
        	 consumeTypeList =dao.getConsumeType();
        	 posList =dao.getPos();
	         for(List<String> lt:consumeTypeList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("&"+str);
	        	 
	         }
	         out.print("&");
	         for(List<String> lt:posList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("#"+str);
	         }
        }
        
        else if(operatetype.equals("SearchPosAndOperator"))
        {
        	
        	//String startTime = req.getParameter("start_time");

         //   String endTime = req.getParameter("end_time");
            
        	 List<List> consumeTypeList = new ArrayList<List>();
        	 List<List> posList = new ArrayList<List>();
        	 Dao dao = new Dao();
        	 consumeTypeList =dao.getOperator();
        	 posList =dao.getPos();
	         for(List<String> lt:consumeTypeList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("&"+str);
	        	 
	         }
	         out.print("&");
	         for(List<String> lt:posList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	 for(String str:alt)
	        	 out.print("#"+str);
	         }
        }
        else if(operatetype.equals("SearchDayStatic"))
        {
        	//查询开始日期
        	String startTime = req.getParameter("start_time");
            //查询截止日期

        	String endTime = req.getParameter("end_time");
            
            String searchData = req.getParameter("search_data");
            
            String searchType = req.getParameter("search_type");
            
            Double skSum=0.0,getCardSum=0.0,numCardSum=0.0,timeCardSum=0.0,goodsSum=0.0;
            
        	 List<List> skList = new ArrayList<List>();
        	 List<List> getCardList = new ArrayList<List>();
        	 List<List> numCardList = new ArrayList<List>();
        	 List<List> timeCardList = new ArrayList<List>();
        	 List<List> goodsConsumeList = new ArrayList<List>();
        	 
        	 String searchAdd="";
             if(searchData.isEmpty())
            	 searchAdd="1=1";
             else
             {
            	 String[] arr=searchData.split("&");
            	 for(int i=1;i<arr.length;i++)
            	 {
            		 searchAdd=searchAdd+arr[i];
            		 if(i<arr.length-1)
            		 searchAdd=searchAdd+" and ";
            			 
            	 }
             }
             
        	 Dao dao = new Dao();
        	 skList =dao.searchSKIncome(startTime, endTime, searchAdd);
        	 getCardList =dao.searchGetCardIncome(startTime, endTime, searchAdd);
        	 numCardList = dao.searchNumCardIncome(startTime, endTime, searchAdd);
        	 timeCardList=dao.searchTimeCardIncome(startTime, endTime, searchAdd);
        	 goodsConsumeList=dao.searchGoodsIncome(startTime, endTime, searchAdd);
        	 
        	 if(searchType.equals("BillAmount"))
        	 {
	         for(List<String> lt:skList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
	        	// for(String str:alt)
	        	//  sum=0.0;
	        	  skSum+=Double.parseDouble(alt.get(19).toString());
	 
	         }
	         
	         out.print("&"+skSum);
	        // out.print("&");
	         for(List<String> lt:getCardList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
		        	// for(String str:alt)
		       
	        	 getCardSum+=Double.parseDouble(alt.get(3).toString());
	          }
	         out.print("&"+getCardSum);
	         for(List<String> lt:numCardList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
		        	// for(String str:alt)
		       
	        	 numCardSum+=Double.parseDouble(alt.get(4).toString());
	          }
	         out.print("&"+numCardSum);
	         for(List<String> lt:timeCardList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
		        	// for(String str:alt)
		       
	        	 timeCardSum+=Double.parseDouble(alt.get(4).toString());
	          }
	         out.print("&"+timeCardSum);
	         for(List<String> lt:goodsConsumeList)
	         {  
	        	 ArrayList<String> alt = (ArrayList<String>)lt;
		        	// for(String str:alt)
		       
	        	 goodsSum+=Double.parseDouble(alt.get(15).toString());
	          }
	         out.print("&"+goodsSum);
        	 }
        	 
        	 out.print("&"+(goodsSum+timeCardSum+numCardSum+getCardSum+skSum));
        }
        
        
        else if(operatetype.equals("getkeyinstatus"))
        {
        	
        	String keyInsideNo = req.getParameter("key_insideno");
        	String cardNo;
       	    String cardType;
       	    
       	 Dao dao = new Dao();
        	 List<List> keyList = new ArrayList<List>();
        	 boolean ifPasswordCard = false;
        	
				try {
				 ifPasswordCard=dao.checkIfPasswordCard(keyInsideNo);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(ifPasswordCard==true)
				out.print("passwordCard&"); //管理卡
			else
			{
				keyList =dao.getKeySatus(keyInsideNo,"ifKeyExist");//钥匙是否存在
				if(keyList.isEmpty())
				out.print("keyNotExist&");
				else
				{
					List lsKeyNo=keyList.get(0);
				    String keyNo=lsKeyNo.get(0).toString();
			 keyList =dao.getKeySatus(keyInsideNo,"ifKeyActive"); //钥匙是否被激活。
			 if(keyList.isEmpty())
				out.print("keyNotActive&"+keyNo);
			 else
			 {
			 List lsKey=keyList.get(0);	 
			 cardNo=lsKey.get(3).toString();
        	 cardType=lsKey.get(4).toString();
			 keyList =dao.getKeySatus(keyInsideNo,"ifKeyBeUsed");//有效钥匙是否已被使用。
			 
        	// String bathItemNo=lsKey.get(2).toString();
        	 //String keyNo=ls.get(2).toString();
        	
			 if(!keyList.isEmpty())
					out.print("keyBeUsed&"+keyNo+"&"+cardNo+"&"+cardType);
			 else
			 {
        	 keyList =dao.getKeySatus(keyInsideNo,"ifKeyInValid"); //允许通过闸机
        	 List ls=keyList.get(0);
        	 String bathItemNo=ls.get(2).toString();
        	 //String keyNo=ls.get(2).toString();
        	  cardNo=ls.get(3).toString();
        	  cardType=ls.get(4).toString();
        	 int exeKeyInUpdateKey=dao.exeKeyInUpdateKey(keyInsideNo);
        	 int exeKeyInUpdateBathItem=dao.exeKeyInUpdateBathItem(bathItemNo);
	      //   for(List<String> lt:keyList)
	         //{  
	        //	 ArrayList<String> alt = (ArrayList<String>)lt;
	        //	 for(String str:alt)
	        	 out.print("pleaseComeIn&"+keyNo+"&"+cardNo+"&"+cardType);
	        	// out.print("&");
	        	// out.print("\n");
	        	// out.println("<br/>");
	            //   }
			  }
				}
			  }
			}
        }  
        else if(operatetype.equals("getkeyoutstatus"))  //出场的时候
        {
        	String keyInsideNo = req.getParameter("key_insideno");
        	String csIfOut = req.getParameter("csIfOut");
        	String outIfChecked=req.getParameter("outIfChecked");
           	
        	Dao dao = new Dao();
        	
        	long timeDiff=0;  
        	long tempTimeDiff = 0;
        	long LS=0,CS=0;
        	double CSB;
       	    List<List> keyList = new ArrayList<List>();
       	    boolean ifPasswordCard = false;
       	
				try {
				 ifPasswordCard=dao.checkIfPasswordCard(keyInsideNo);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(ifPasswordCard==true)
				out.print("passwordCard&");  //管理卡
			else
			{
				keyList =dao.getKeySatus(keyInsideNo,"ifKeyExist");
				if(keyList.isEmpty())
				out.print("keyNotExist&");  //钥匙不存在
				else
			{
			 List lsKeyNo=keyList.get(0);
			 String keyNo=lsKeyNo.get(0).toString();
			 keyList =dao.getKeySatus(keyInsideNo,"ifKeyOutValid");
			 if(keyList.isEmpty())
				out.print("keyNotActive&"+keyNo);   //钥匙没有激活
			 else
			 {
				 String bathItemNo="";
				 String inTime="";
				 String limiteLong="";
				 String chargeType="";
				 String timeType="";
				 String consumeName="";
				 String cardNo="";
				 String cardType="";
				 String checked="";
			List ls=keyList.get(0);
			if(ls.get(5)!=null)
		    checked=ls.get(5).toString();
		    if(ls.get(2)!=null)
		     bathItemNo=ls.get(2).toString();
		    String openDoor=ls.get(1).toString();
		    if(ls.get(6)!=null)
		    inTime=ls.get(6).toString();
		    if(ls.get(7)!=null)
		     limiteLong=ls.get(7).toString();
		    if(ls.get(8)!=null)
		     chargeType=ls.get(8).toString();
		    if(ls.get(9)!=null)
		     timeType=ls.get(9).toString();
		    if(ls.get(10)!=null)
		     consumeName=ls.get(10).toString();
		    if(ls.get(3)!=null)
		     cardNo=ls.get(3).toString();
		    if(ls.get(11)!=null)
       	     cardType=ls.get(11).toString();
       	    // if(dao.getSysSet("OutOfTime").isEmpty()) 其实应该判断一下不存在的情况。
		    String outOfTime =dao.getSysSet("OutOfTime").substring(1, 2);
 	        String inDiff =dao.getSysSet("InDiff").substring(1,dao.getSysSet("InDiff").length()-1);
 	        String outDiff =dao.getSysSet("OutDiff").substring(1,dao.getSysSet("OutDiff").length()-1);
 	      
 	    	   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	
 	    	       java.util.Date    curDate =   new    Date(System.currentTimeMillis());//获取当前时间 
	    	    
					long inDate = 0;
					
					if(!inTime.equals(""))
					{
					try {
						
						inDate = dfs.parse(inTime).getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
					}
					long diff = curDate.getTime() - inDate;//这样得到的差值是微秒级别
				
	    	     
	    	     long minutes = diff / (1000 * 60);  //得到相隔的分钟数
	    	     tempTimeDiff = minutes;
	    	     LS           =	minutes-Integer.parseInt(inDiff)-Integer.parseInt(outDiff);
	    	   //  if(tempTimeDiff<0)
	    	    //	tempTimeDiff=0;
	    	     SimpleDateFormat    formatterIn    =   new    SimpleDateFormat    ("HH:mm:ss");       
				 //  Date    curDateIn    =   new    Date(System.currentTimeMillis());//获取当前时间       
	    	    //inTime= new SimpleFormat().format(inTime,"yyyy-MM-dd");
	    	     Date    curDateIn    =   new    Date(inDate);//获取当前时间       
				  inTime    =    formatterIn.format(curDateIn);
						//	  inTime = inTime.format("HH:mm:ss");
	    
		    if(checked.equals("1"))  //已结账，但未出场，可以出场
		    {
		    	tempTimeDiff=0;
		    	inTime="";
		    int exeKeyOutUpdateChecked=dao.exeKeyOutUpdateChecked(keyInsideNo);
		    int exeKeyOutUpdateBathItemChecked=dao.exeKeyOutUpdateBathItemChecked(bathItemNo);
		    out.print("keyIsChecked&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+exeKeyOutUpdateChecked+"&"+exeKeyOutUpdateBathItemChecked);
		    }
		    else
		    {
		    if(!openDoor.equals("1"))    //有效钥匙但是未入场即出场刷闸机，提示请先刷进再刷出，不开闸。
		    out.print("validKeyButNotIn&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime);
		    
		    else
		    {
  	    	 if(!outOfTime.equals("0"))
  	    		timeDiff=LS;
				 else
				timeDiff=-100;
  	    	 if(timeDiff>Long.parseLong(limiteLong) && csIfOut.equals("No"))
  	    	 {
  	    		//记录超时出场时间，作为结账时间依据
  	    		 int exeCsNotOutUpdateKey=dao.exeCsNotOutUpdateKey(keyInsideNo);
  	    		 String Charge= dao.getSysSet("Charge").substring(1,dao.getSysSet("Charge").length()-1);
  	    		 List<List> chargeFormulaList = new ArrayList<List>();  
  	    		  chargeFormulaList=dao.getChargeFormula(Charge, consumeName, chargeType, timeType);
  	    		 if(chargeFormulaList.isEmpty())
  	    		 {
  	    	     out.print("csNotOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+"csNotChargeFormula"+"&"+exeCsNotOutUpdateKey); //提示“该钥匙已超时,不容许出场!”	 
  	    		 } else
  	    		 {		 
  	    		 
  	    		// List<List> cFL = new ArrayList<List>(); 
  	    		  String BC="";
  	    		  String timeUnit        =chargeFormulaList.get(0).get(0).toString();
  	    		  String formulaLimitLong=chargeFormulaList.get(0).get(1).toString();
  	    		  String bookPrice       =chargeFormulaList.get(0).get(2).toString();
 	    		  String price           =chargeFormulaList.get(0).get(3).toString();
 	    		  String leastPrice      =chargeFormulaList.get(0).get(4).toString();
 	    		  CS=LS-Long.parseLong(limiteLong);
 	    		  int CSP=(int) ((CS+Integer.parseInt(timeUnit)-1)/Integer.parseInt(timeUnit));
 	    		  if(LS>Long.parseLong(formulaLimitLong))
 	    			 BC="bc";
 	    		  else
 	    			 BC="js";
 	    		  if(BC.equals("bc"))
 	    			 CSB=Double.parseDouble(bookPrice)-Double.parseDouble(leastPrice);
 	    		  else
 	    			  CSB=CSP*Double.parseDouble(price);
 	    		 String CSF= new DecimalFormat("#.00").format(CSB);
  	    		  out.print("csNotOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+CSF+"&"+exeCsNotOutUpdateKey); //提示“该钥匙已超时,不容许出场!”
  	    		 }
  	    	 }  else
  	    	 {
  	    		 
  	    		out.print("CanOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime);
  	    	     if(outIfChecked.equals("Yes"))
  	    	     {	
  	    	     out.print("&outAndChecked&"); 
  	    	     //可以出场，并且出场即结账
  	    		 int exeCsButCanOutUpdateKey=dao.exeCsButCanOutUpdateKey(keyInsideNo);
  	    		 int UpdateBathItemChecked  =dao.exeKeyOutUpdateBathItemChecked(bathItemNo);
  	    	      out.print(exeCsButCanOutUpdateKey+"&"+UpdateBathItemChecked);
  	    	     } else
  	    	     {
  	    	     //可以出场，并且出场不结账
  	    	     out.print("&outNotChecked&");
  	    	     int exeOutNotCheckedUpdateKey=dao.exeOutNotCheckedUpdateKey(keyInsideNo);
  	    	      out.print(exeOutNotCheckedUpdateKey+"");	 
  	    	         }
  	    	        }
		           }
			     }
			    }
			   }
			 }
         } else if(operatetype.equals("getinstatus"))
         {
         	String insideNo = req.getParameter("insideno");
         	List<List> keyList = new ArrayList<List>();
         	
         	Dao dao = new Dao();
         	
         	 SimpleDateFormat    formatterIn    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
			   Date    curDateIn    =   new    Date(System.currentTimeMillis());//获取当前时间       
    	    //inTime= new SimpleFormat().format(inTime,"yyyy-MM-dd");
			   String dt= formatterIn.format(curDateIn);
    	    // Date    curDateIn    =   new    Date(inDate);//获取当前时间   
         	 
         	boolean ifPasswordCard = false;
        	
			try {
			 ifPasswordCard=dao.checkIfPasswordCard(insideNo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(ifPasswordCard==true)
			out.print("passwordCard&"); //管理卡
		else
		{ 
			keyList =dao.getKeySatus(insideNo,"ifKeyExist");
			if(keyList.isEmpty())
			{
				
				//钥匙不存在
			String checkCard=dao.checkCard(insideNo);
			//String[] str=checkCard.split("&");
			if(checkCard.equals("success"))
			{	
				if(dao.inCome())
				out.print("success&"+dao.cardNo);	
				else
				out.print("fail&");
			}
			else
			out.print(checkCard+"&"+dao.cardNo);  //钥匙不存在
			//卡号是否存在	
			}
			else
			{
				String keyInsideNo = req.getParameter("insideno");
				String cardNo;
	       	    String cardType;
	       	    
	           
	        //	 List<List> keyList = new ArrayList<List>();
	        	
				
						List lsKeyNo=keyList.get(0);
					    String keyNo=lsKeyNo.get(0).toString();
				 keyList =dao.getKeySatus(keyInsideNo,"ifKeyActive"); //钥匙是否被激活。
				 if(keyList.isEmpty())
					out.print("keyNotActive&"+keyNo);
				 else
				 {
				 List lsKey=keyList.get(0);	 
				 cardNo=lsKey.get(3).toString();
	        	 cardType=lsKey.get(4).toString();
				 keyList =dao.getKeySatus(keyInsideNo,"ifKeyBeUsed");//有效钥匙是否已被使用。
				 
	        	// String bathItemNo=lsKey.get(2).toString();
	        	 //String keyNo=ls.get(2).toString();
	        	
				 if(!keyList.isEmpty())
						out.print("keyBeUsed&"+keyNo+"&"+cardNo+"&"+cardType);
				 else
				 {
	        	 keyList =dao.getKeySatus(keyInsideNo,"ifKeyInValid"); //允许通过闸机
	        	 List ls=keyList.get(0);
	        	 String bathItemNo=ls.get(2).toString();
	        	 //String keyNo=ls.get(2).toString();
	        	  cardNo=ls.get(3).toString();
	        	  cardType=ls.get(4).toString();
	        	 int exeKeyInUpdateKey=dao.exeKeyInUpdateKey(keyInsideNo);
	        	 int exeKeyInUpdateBathItem=dao.exeKeyInUpdateBathItem(bathItemNo);
		      //   for(List<String> lt:keyList)
		         //{  
		        //	 ArrayList<String> alt = (ArrayList<String>)lt;
		        //	 for(String str:alt)
		        	 out.print("pleaseComeIn&"+keyNo+"&"+cardNo+"&"+cardType);
		        	// out.print("&");
		        	// out.print("\n");
		        	// out.println("<br/>");
		            //   }
				  
					}
				  }
				}
			}
        	 
         } else if(operatetype.equals("getoutstatus"))
         {
         	String insideNo = req.getParameter("insideno");
         	String csIfOut = req.getParameter("csIfOut");
         	String outIfChecked=req.getParameter("outIfChecked"); 
         	
         	List<List> keyList = new ArrayList<List>();
         	
         	Dao dao = new Dao();
         	
         	 SimpleDateFormat    formatterdt    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");       
			   Date    curDatedt    =   new    Date(System.currentTimeMillis());//获取当前时间       
    	    //inTime= new SimpleFormat().format(inTime,"yyyy-MM-dd");
			   String dt= formatterdt.format(curDatedt);
    	    // Date    curDateIn    =   new    Date(inDate);//获取当前时间   
         	 
         	boolean ifPasswordCard = false;
        	
			try {
			 ifPasswordCard=dao.checkIfPasswordCard(insideNo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(ifPasswordCard==true)
			out.print("passwordCard&"); //管理卡
		else
		{ 
			keyList =dao.getKeySatus(insideNo,"ifKeyExist");
			if(keyList.isEmpty())
			{
			//out.print("insideNoNotExist&");  //钥匙不存在
				if(dao.callCardOut(insideNo).equals("success"))
				out.print("success&"+dao.cardNo+"&"+dao.tempTimeDiff);
				else
				out.print(dao.callCardOut(insideNo)+"&"+dao.cardNo+"&"+dao.tempTimeDiff);
			//卡号是否存在			
			}
			else
			{
				String keyInsideNo = req.getParameter("insideno");
				long timeDiff=0;  
	        	long tempTimeDiff = 0;
	        	long LS=0,CS=0;
	        	double CSB;
	       	   					
				 List lsKeyNo=keyList.get(0);
				 String keyNo=lsKeyNo.get(0).toString();
				 keyList =dao.getKeySatus(keyInsideNo,"ifKeyOutValid");
				 if(keyList.isEmpty())
					out.print("keyNotActive&"+keyNo);   //钥匙没有激活
				 else
				 {
				List ls=keyList.get(0);
			    String checked=ls.get(5).toString();
			    String bathItemNo=ls.get(2).toString();
			    String openDoor=ls.get(1).toString();
			    String inTime=ls.get(6).toString();
			    String limiteLong=ls.get(7).toString();
			    String chargeType=ls.get(8).toString();
			    String timeType=ls.get(9).toString();
			    String consumeName=ls.get(10).toString();
			    String cardNo=ls.get(3).toString();
	       	    String cardType=ls.get(11).toString();
			    String outOfTime =dao.getSysSet("OutOfTime").substring(1, 2);
	 	        String inDiff =dao.getSysSet("InDiff").substring(1,dao.getSysSet("InDiff").length()-1);
	 	        String outDiff =dao.getSysSet("OutDiff").substring(1,dao.getSysSet("OutDiff").length()-1);
	 	      
	 	    	   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	
	 	    	       java.util.Date    curDate =   new    Date(System.currentTimeMillis());//获取当前时间 
		    	    
						long inDate = 0;
						try {
							inDate = dfs.parse(inTime).getTime();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						long diff = curDate.getTime() - inDate;//这样得到的差值是微秒级别
					
		    	     
		    	     long minutes = diff / (1000 * 60);  //得到相隔的分钟数
		    	     tempTimeDiff = minutes;
		    	     LS           =	minutes-Integer.parseInt(inDiff)-Integer.parseInt(outDiff);
		    	   //  if(tempTimeDiff<0)
		    	    //	tempTimeDiff=0;
		    	     SimpleDateFormat    formatterIn    =   new    SimpleDateFormat    ("HH:mm:ss");       
					 //  Date    curDateIn    =   new    Date(System.currentTimeMillis());//获取当前时间       
		    	    //inTime= new SimpleFormat().format(inTime,"yyyy-MM-dd");
		    	     Date    curDateIn    =   new    Date(inDate);//获取当前时间       
					  inTime    =    formatterIn.format(curDateIn);
							//	  inTime = inTime.format("HH:mm:ss");
		    
			    if(checked.equals("1"))  //已结账，但未出场，可以出场
			    {
			    int exeKeyOutUpdateChecked=dao.exeKeyOutUpdateChecked(keyInsideNo);
			    int exeKeyOutUpdateBathItemChecked=dao.exeKeyOutUpdateBathItemChecked(bathItemNo);
			    out.print("keyIsChecked&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+exeKeyOutUpdateChecked+"&"+exeKeyOutUpdateBathItemChecked);
			    }
			    
			    if(!openDoor.equals("1"))    //有效钥匙但是未入场即出场刷闸机，提示请先刷进再刷出，不开闸。
			    out.print("validKeyButNotIn&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime);
			    
			    else
			    {
	  	    	 if(!outOfTime.equals("0"))
	  	    		timeDiff=LS;
					 else
					timeDiff=-100;
	  	    	 if(timeDiff>Long.parseLong(limiteLong) && csIfOut.equals("No"))
	  	    	 {
	  	    		//记录超时出场时间，作为结账时间依据
	  	    		 int exeCsNotOutUpdateKey=dao.exeCsNotOutUpdateKey(keyInsideNo);
	  	    		 String Charge= dao.getSysSet("Charge").substring(1,dao.getSysSet("Charge").length()-1);
	  	    		 List<List> chargeFormulaList = new ArrayList<List>();  
	  	    		  chargeFormulaList=dao.getChargeFormula(Charge, consumeName, chargeType, timeType);
	  	    		 if(chargeFormulaList.isEmpty())
	  	    		 {
	  	    	     out.print("csNotOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+"csNotChargeFormula"+"&"+exeCsNotOutUpdateKey); //提示“该钥匙已超时,不容许出场!”	 
	  	    		 } else
	  	    		 {		 
	  	    		 
	  	    		// List<List> cFL = new ArrayList<List>(); 
	  	    		  String BC="";
	  	    		  String timeUnit        =chargeFormulaList.get(0).get(0).toString();
	  	    		  String formulaLimitLong=chargeFormulaList.get(0).get(1).toString();
	  	    		  String bookPrice       =chargeFormulaList.get(0).get(2).toString();
	 	    		  String price           =chargeFormulaList.get(0).get(3).toString();
	 	    		  String leastPrice      =chargeFormulaList.get(0).get(4).toString();
	 	    		  CS=LS-Long.parseLong(limiteLong);
	 	    		  int CSP=(int) ((CS+Integer.parseInt(timeUnit)-1)/Integer.parseInt(timeUnit));
	 	    		  if(LS>Long.parseLong(formulaLimitLong))
	 	    			 BC="bc";
	 	    		  else
	 	    			 BC="js";
	 	    		  if(BC.equals("bc"))
	 	    			 CSB=Double.parseDouble(bookPrice)-Double.parseDouble(leastPrice);
	 	    		  else
	 	    			  CSB=CSP*Double.parseDouble(price);
	 	    		 String CSF= new DecimalFormat("#.00").format(CSB);
	  	    		  out.print("csNotOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime+"&"+CSF+"&"+exeCsNotOutUpdateKey); //提示“该钥匙已超时,不容许出场!”
	  	    		 }
	  	    	 }  else
	  	    	 {
	  	    		 
	  	    		out.print("CanOut&"+keyNo+"&"+cardNo+"&"+cardType+"&"+tempTimeDiff+"&"+inTime);
	  	    	     if(outIfChecked.equals("Yes"))
	  	    	     {	
	  	    	     out.print("&outAndChecked&"); 
	  	    	     //可以出场，并且出场即结账
	  	    		 int exeCsButCanOutUpdateKey=dao.exeCsButCanOutUpdateKey(keyInsideNo);
	  	    		 int UpdateBathItemChecked  =dao.exeKeyOutUpdateBathItemChecked(bathItemNo);
	  	    	      out.print(exeCsButCanOutUpdateKey+"&"+UpdateBathItemChecked);
	  	    	     } else
	  	    	     {
	  	    	     //可以出场，并且出场不结账
	  	    	     out.print("&outNotChecked&");
	  	    	     int exeOutNotCheckedUpdateKey=dao.exeOutNotCheckedUpdateKey(keyInsideNo);
	  	    	      out.print(exeOutNotCheckedUpdateKey+"");	 
	  	    	        }	    	      
				    }
				   }
				 }
			  }
		   } 
         }
        
        
         out.flush();

         out.close();

     }

 } 

