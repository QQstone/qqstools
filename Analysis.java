/* 
 * @(#)Analysis.java
 *
 * Copyright 2019, 迪爱斯通信设备有限公司保留.
 */
package compute;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * TODO 请添加类型Analysis的描述
 *
 * <p>
 * <b>History:</b>
 * <table border="1">
 * <tr>
 * <th>Date</th>
 * <th>Operator</th>
 * <th>Memo</th>
 * </tr>
 * <tr>
 * <td>2019年1月28日</td>
 * <td>qiushi</td>
 * <td>Create</td>
 * </tr>
 * </table>
 * 
 * @author qiushi
 *  
 * @version TODO 请输入Analysis的当前版本
 * @since TODO 请输入Analysis的起始版本
 */
public class Analysis {

	/**
	 * TODO 请添加方法Analysis.main的注释
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 完成Analysis.main方法的构建或覆盖
		//
		try {
			FileInputStream fs = new FileInputStream("D:\\backup\\logs\\Server_20190121.log");
			//Reader in = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fs,"GB2312"));
			String record = "";
			ArrayList<String> summary = new ArrayList<String>();
			while(true){
				String line = reader.readLine();
				if(line==null){
					break;
				}
				//是否新记录
				String timeExp = "^(2019-[0-9]{2}-[0-9]{2} [0-9]{2}\\:[0-9]{2}\\:[0-9]{2}\\,[0-9]{3})";
				java.util.regex.Pattern timeExp_pattern = java.util.regex.Pattern.compile(timeExp);
				java.util.regex.Matcher timeExp_matcher = timeExp_pattern.matcher(line);
				if(timeExp_matcher.find()){
					//记录功能
					if(record.indexOf("接收到WebSocket消息")>0
							&&record.indexOf("branchorder")>0){
						summary.add("接收到WebSocket消息");
						
						//记录单号
						String SerialNumExp = "\"ZLDBH\": \"([0-9a-z]{28})\"";
						java.util.regex.Pattern SerialNum_pattern = java.util.regex.Pattern.compile(SerialNumExp);
						java.util.regex.Matcher SerialNum_matcher = SerialNum_pattern.matcher(record);
						if(SerialNum_matcher.find()){
							summary.add(SerialNum_matcher.group(1));
						}
					}
					if(record.indexOf("SendMessageToJs")>0
							&&record.indexOf("branchorder")>0){
						summary.add("SendMessageToJs");
						
						//记录单号
						String SerialNumExp0 = "\\"+"\\\"ZLDBH\\\\\": \\\\\"([0-9a-z]{28})\\\\\"";
						java.util.regex.Pattern SerialNum_pattern0 = java.util.regex.Pattern.compile(SerialNumExp0);
						java.util.regex.Matcher SerialNum_matcher0 = SerialNum_pattern0.matcher(record);
						if(SerialNum_matcher0.find()){
							summary.add(SerialNum_matcher0.group(1));
						}
					}
					//压入状态
					if(record.indexOf("压入branchorder")>0||record.indexOf("取出branchorder")>0){
						if(record.indexOf("压入branchorder")>0){
							summary.add("压入branchorder");
						}else{
							summary.add("取出branchorder");
						}
						String InQueueExp = "zldbh:([0-9a-z]{28})";
						java.util.regex.Pattern InQueue_pattern = java.util.regex.Pattern.compile(InQueueExp);
						java.util.regex.Matcher InQueue_matcher = InQueue_pattern.matcher(record);
						if(InQueue_matcher.find()){
							summary.add(InQueue_matcher.group(1));
						}
					}
					//主页取出
					if(record.indexOf("主页取出newCaseEvent")>0){
							summary.add("主页取出newCaseEvent");
						String InQueueExp = "zldbh:([0-9a-z]{28})";
						java.util.regex.Pattern InQueue_pattern = java.util.regex.Pattern.compile(InQueueExp);
						java.util.regex.Matcher InQueue_matcher = InQueue_pattern.matcher(record);
						if(InQueue_matcher.find()){
							summary.add(InQueue_matcher.group(1));
						}
					}
					//将summary保存
					writeData(summary);
					
					//新记录
					record=line;
					summary= new ArrayList<String>();
					//记录时间
					summary.add(timeExp_matcher.group());
				}else{
					record+=line;
				}
//				if(line!=null){
//					System.out.println(line);
//				}
				Thread.sleep(10);
			}
			reader.close();
			fs.close();
		} catch (Exception e) {
			// TODO 填写日志输出并完成异常处理逻辑
//			if(logger.isErrorEnabled()){
//				logger.error("",e);
//			}
			System.out.println("read file fail");			
			System.out.print(e);
		}
		
	}
	public static void writeData(ArrayList<String> summary){
		if(summary.size()>2){
			String out="";
			for(String s :summary){
				out+="|"+s;
			}
			System.out.println(out);
			
			try {
				FileWriter printer = new FileWriter("D:\\backup\\logs\\analysis.txt",true);
				printer.write(out);
				printer.write("\r\n");
				printer.close();
			} catch (FileNotFoundException e) {
				// TODO 填写日志输出并完成异常处理逻辑
				System.out.println("file not found");
			} catch (IOException e) {
				// TODO 填写日志输出并完成异常处理逻辑
				System.out.println("file print error");
			}
			
		}
	}
}
