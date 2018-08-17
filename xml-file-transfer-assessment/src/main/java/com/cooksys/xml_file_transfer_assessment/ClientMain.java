package com.cooksys.xml_file_transfer_assessment;

public class ClientMain {
	public static void main(String[] args) {
		new Client("localhost", 8081, "test_user", "test_folder/image.jpg");
	}
}