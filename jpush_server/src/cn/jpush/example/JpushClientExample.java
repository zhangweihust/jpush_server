package cn.jpush.example;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import cn.jpush.api.DeviceEnum;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.IOSExtra;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JpushClientExample {

	private static final String appKey ="d4641ee57986be0fe32bb5fc";	//必填，例如466f7032ac604e02fb7bda89

	private static final String masterSecret =	"5d3766d5bdc9bc2c2f731dfc";//必填，每个应用都对应一个masterSecret（1f0e3dad99908345f7439f8ffabdffc4)

	private static JPushClient jpush = null;

	/*
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
	 * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒）。	
	 */
	private static int timeToLive =  60 * 60 * 24;  

	public static void main(String[] args) {
		/*
		 * Example1: 初始化,默认发送给android和ios，同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey,timeToLive);
		 * 
		 * Example2: 只发送给android
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 * 
		 * Example3: 只发送给IOS
		 * jpush = new JPushClient(masterSecret, appKey,  DeviceEnum.IOS);
		 * 
		 * Example4: 只发送给android,同时设置离线消息存活时间
		 * jpush = new JPushClient(masterSecret, appKey, DeviceEnum.Android);
		 */

		jpush = new JPushClient(masterSecret,appKey, DeviceEnum.Android);

		/* 
		 * 是否启用ssl安全连接, 可选
		 * 参数：启用true， 禁用false，默认为非ssl连接
		 * 
		 * Example:
		 * jpush.setEnableSSL(true);
		 */


		//测试发送消息或者通知
		//testSend();
		//sendMsg();
		sendMsg4();
		//testGson();
	}

/*	private static void testGson(){
		PushMessage pMsg = new PushMessage();
		pMsg.bitmap_url = "http://img1.gtimg.com/ent/pics/hv1/211/200/1321/85949236.jpg";
		pMsg.type = 0;
		pMsg.info = "本周来\'了'一批\\新的\"电影，哈哈";
		pMsg.title = "自定义消息";
		pMsg.time = System.currentTimeMillis();
		
		Gson gson = new Gson();
		System.out.print(gson.toJson(pMsg));
	}*/
	
	private static void sendMsg4(){
		MessageManager.getInstance().notify.sendnotify = true;
		MessageManager.getInstance().notify.info = "预置的影片1将于某日某时下线";
		MessageManager.getInstance().notify.status_info = "预置的影片1将于某日某时下线，请尽快观看";
		MessageManager.getInstance().notify.title = "预置的影片1";
		
		MessageManager.getInstance().condition.type = 1;
		MessageManager.getInstance().condition.assetID = "123111";
		MessageManager.getInstance().condition.videoname = "预置的影片1";
		
		PushMessage pMsg = new PushMessage();
		pMsg.bitmap_url = "http://img1.gtimg.com/sports/pics/hv1/125/235/1323/86088125.jpg";
		pMsg.type = 0;
		pMsg.info = "预置的影片1将于某日某时下线，请您尽快观赏";
		pMsg.title = "222陈晓做客";
		pMsg.time = System.currentTimeMillis();
		
		MessageManager.getInstance().addMessage(pMsg);
		
		Gson gson = new Gson();
		String msg_context = gson.toJson(MessageManager.getInstance()).replace("\"", "##");
		System.out.println("客户端发送数据: " + msg_context);
		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(104, "custorm msg2", msg_context);//


		
		if (null != msgResult) {
			System.out.println("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("无法获取数据，检查网络是否超时");
		}
	}
	
	private static void sendMsg(){
		MessageManager.getInstance().notify.sendnotify = true;
		MessageManager.getInstance().notify.info = "222畅聊《陆贞传奇》虐心的两段情";
		MessageManager.getInstance().notify.status_info = "222畅聊《陆贞传奇》";
		MessageManager.getInstance().notify.title = "222陈晓做客";
		
		MessageManager.getInstance().condition.type = 1;
		MessageManager.getInstance().condition.assetID = "123456";
		MessageManager.getInstance().condition.videoname = "video_name";
		
		PushMessage pMsg = new PushMessage();
		pMsg.bitmap_url = "http://img1.gtimg.com/sports/pics/hv1/124/235/1323/86088124.jpg";
		pMsg.type = 0;
		pMsg.info = "222畅聊《陆贞传奇》虐心的两段情，自曝想整容...[详细]";
		pMsg.title = "222陈晓做客";
		pMsg.time = System.currentTimeMillis();
		
		MessageManager.getInstance().addMessage(pMsg);
		
		Gson gson = new Gson();
		String msg_context = gson.toJson(MessageManager.getInstance()).replace("\"", "##");
		System.out.println("客户端发送数据: " + msg_context);
		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(104, "custorm msg2", msg_context);//


		
		if (null != msgResult) {
			System.out.println("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("无法获取数据，检查网络是否超时");
		}
	}
	
	private static void testSend() {

		int sendNo = 103;
		String msgTitle = "IOS标题";
		String msgContent = "+/通#知?内&容%<可>;=";

		/*
	       String alias = "alias";
	    
		 */
		String tag = "tag";
		//IOS 扩展参数，Badge，sound
		IOSExtra iosExtra = new IOSExtra(2,"message.wav");
		//自定义消息 android/ios
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hey", "test");
		map.put("you", "test");
		
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo,msgTitle,msgContent,0,map,iosExtra);

		//  jpush.sendNotificationWithAlias(sendNo, alias, msgTitle, msgContent,1,null,new IOSExtra(3));
			//	jpush.sendNotificationWithTag(sendNo, tag, msgTitle, msgContent);
		//		jpush.sendNotificationWithAlias(sendNo, tag, msgTitle, msgContent);

		
		if (null != msgResult) {
			System.out.println("服务器返回数据: " + msgResult.toString());
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println("发送成功， sendNo=" + msgResult.getSendno());
			} else {
				System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
			}
		} else {
			System.out.println("无法获取数据，检查网络是否超时");
		}
	}
}
