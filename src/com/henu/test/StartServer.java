package com.henu.test;

import java.io.IOException;

import com.henu.socket.SocketServer;

public class StartServer{

	public static void main(String[] args) {
		SocketServer server = new SocketServer();
		try {
			server.service();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务出错，请重启服务...");
		}
	}

}
