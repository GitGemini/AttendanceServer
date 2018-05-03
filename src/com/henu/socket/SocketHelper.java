package com.henu.socket;

import java.io.IOException;
import java.net.Socket;

public class SocketHelper{	
	private Socket socket = null;
	
	public SocketHelper(Socket socket) {
		//初始化属性
		this.socket = socket;
		
		//加入Socket管理列表
		//SocketManger.addSocket(id, this.socket);
	}
	
	public Socket getSocket() {
		return socket;
	}

	
	public void readFromClient() {
		Thread t = new Thread(new ReceiveHandler(this));
		t.start();
	}
	
	public void write2Client(String msg) {
		Thread t = new Thread(new SendMsgHandler(this.socket, msg));
		t.start();		
	}
	
	public void Close() {		
		//关闭连接
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
