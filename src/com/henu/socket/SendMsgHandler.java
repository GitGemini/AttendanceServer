package com.henu.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMsgHandler implements Runnable{
	
	private Socket socket;
	private String msg;
	
	public SendMsgHandler(Socket socket,String msg) {
		this.socket = socket;
		this.msg = msg;
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(msg);
			out.println("over");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
