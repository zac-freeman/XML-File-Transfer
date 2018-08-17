package com.cooksys.xml_file_transfer_assessment;

public class ServerMain {
	public static void main(String[] args) {
		new Server(8081, "output_directory");
	}
}
