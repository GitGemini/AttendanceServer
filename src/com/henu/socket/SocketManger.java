package com.henu.socket;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketManger {
	//保存在线用户的socket;
	private static Map<Integer, Socket> sockets = new HashMap<Integer, Socket>();
	
	public static Map<Integer, Socket> getSockets() {
		return sockets;
	}

	public static void addSocket(int id, Socket socket) {
		Socket s = sockets.get(id);
		if(s==null) {
			sockets.put(id, socket);
		}else {
			sockets.remove(id, s);
			sockets.put(id, socket);
		}
	}
	
	public static void removeSocket(int id) {
		Socket socket = sockets.get(id);
		if(socket!=null) {
			sockets.remove(id, socket);
		}
	}
	
	//向所有客户端推送消息
	public static void pushMessage(String msg) {
		
	}
	
	//向指定客户端推送消息
	public static void pushToClient(Integer id, String msg) { 
		Socket socket = sockets.get(id);
		if(socket != null) {
			System.out.println(id + msg);
			Thread t = new Thread(new SendMsgHandler(socket, msg));
			t.start();
		}else {
			System.out.println(id + "不在线");
		}
		
	}
}
