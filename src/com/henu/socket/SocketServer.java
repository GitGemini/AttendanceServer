package com.henu.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer{
	private static final int PORT = 52068;
	
	public static int count = 0;
	
	public void service() throws IOException {
		try(ServerSocket server = new ServerSocket(PORT)){
			System.out.println("waiting for client connection...");
			//waiting for client connection
			while(true) {
				Socket s = server.accept();
				count++;
				System.out.println("第"+ count +"个客户端连接");
				SocketHelper socket = new SocketHelper(s);
				socket.readFromClient();
			}
		}
	}
}
