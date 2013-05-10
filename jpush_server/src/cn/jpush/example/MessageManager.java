package cn.jpush.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.zhangwei.common.StorageManager;


public class MessageManager {

	public static transient MessageManager ins = null;
	private static transient final String MessageManager_ID = "MessageManager";
	private static transient final int list_size = 20;

	
	/**********************************************************/
	public NotifyInfo notify;
	public NotifyCondition condition;
	public ArrayList<PushMessage> MsgList;
	/**********************************************************/
	
	
	public static MessageManager getInstance(){
		if(ins==null){
			MessageManager a =  (MessageManager) StorageManager.getInstance().getItem(
					MessageManager_ID, MessageManager.class);
			if (a != null) {
				ins = a;
			} else {
				ins = new MessageManager();
			}
		}
		
		return ins;
	}
	
	private MessageManager(){
		MsgList = new ArrayList<PushMessage>();
		notify = new NotifyInfo();
		condition = new NotifyCondition();
	};
	
	public MessageManager addMessage(PushMessage pMsg){
		MsgList.add(pMsg);
		autoClean();
		return ins;
	}
	
	public MessageManager addMessageList(MessageManager pMsgList){
		MsgList.addAll(pMsgList.MsgList);
		notify = pMsgList.notify;
		autoClean();
		return ins;
	}
	
	private void autoClean(){
		ComparatorMessage comparator=new ComparatorMessage();
		Collections.sort(MsgList, comparator);
		int index = MsgList.size();
		while(index>list_size){
			MsgList.remove(index-1);
			index--;
		}
	}
	
	public MessageManager persist(){
		StorageManager.getInstance().putItem(MessageManager_ID, ins, MessageManager.class);
		return ins;
	}
	
	public class ComparatorMessage implements Comparator<PushMessage>{

		 public int compare(PushMessage arg0, PushMessage arg1) {

		  return (int)(arg0.time - arg1.time);

		 }
		 
	}
}
