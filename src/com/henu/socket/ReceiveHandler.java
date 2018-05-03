package com.henu.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.henu.domain.SocketMessage;
import com.henu.service.Methods;
import com.henu.utils.Config;

public class ReceiveHandler implements Runnable {
	private SocketHelper sh;

	public ReceiveHandler(SocketHelper sh) {
		this.sh = sh;
	}

	@Override
	public void run() {
		boolean flag = true;
		try {
			InputStream is = sh.getSocket().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder msg = new StringBuilder();
			while(flag) {
				msg.setLength(0);
				String str = null;
				while (!"over".equals(str=in.readLine())) {					
					msg.append(str);
				}
				str = msg.toString();
				System.out.println(str);
				SocketMessage sm = SocketMessage.parseJson(str);
				
				//当接收到byebye时，关闭连接，同时结束当前进程
				if(SocketMessage.TYPE_EXIT == sm.getType()) {
					flag = false;
					sh.Close();
					Config.Print("一个客户端断开连接...");
				}else{
					Methods.queryMethod(sm, sh);
				}
			}
		} catch (IOException e) {
			flag = false;
			sh.Close();
			Config.Print("一个客户端断开连接...");
		}		
	}
}
