package com.cooksys.xml_file_transfer_assessment;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private int port;
	private String rootName;	//name of root directory of folders to write into

	//TODO: comments
	public Server(int port, String rootName) {
		this.port = port;
		this.rootName = rootName;

		try ( ServerSocket server = new ServerSocket(this.port); )
		{
			while (true) {
				ClientHandler client = new ClientHandler(server.accept(), this.rootName);
				new Thread(client).start();
			}
		} catch (IOException e) {
			System.out.println("Error creating ServerSocket, or accepting a Socket:");
			e.printStackTrace();
		}
	}
}
