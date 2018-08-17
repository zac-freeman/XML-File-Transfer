package com.cooksys.xml_file_transfer_assessment;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Client {

	private String ip;
	private int port;
	private String targetName;	//name of target file or directory to read from
	private String username = "test_user";	//TODO: best practices way to get username

	public Client(String ip, int port, String targetName) {
		this.ip = ip;
		this.port = port;
		this.targetName = targetName;

		File target = new File(this.targetName);
		File[] files;
		if (target.isDirectory()) {
			files = target.listFiles();
		} else {
			files = new File[] {target};
		}

		for (File file : files) {
			try (
				Socket socket = new Socket(this.ip, this.port);
				InputStream in = new FileInputStream(file.getPath());	//TODO: appropriate getPath()
				OutputStream out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
			) {
				JAXBContext context = JAXBContext.newInstance(FileMessage.class);
				Marshaller marshaller = createMarshaller(context);
				StringBuffer buffer = new StringBuffer();

				String date = "2018-08-17";	//TODO: find date
				byte[] contents = new byte[in.available()];
				in.read(contents);

				FileMessage message = new FileMessage(username, date, file.getName(), contents);
				marshaller.marshal(message, out);

			} catch (UnknownHostException e) {
				System.out.println("Error connecting to server:");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error interacting with I/O streams:");
				e.printStackTrace();
			} catch (JAXBException e) {
				System.out.println("Error creating JAXB, or Marshaller:");
					e.printStackTrace();
				}
		}
	}

	public static Marshaller createMarshaller (JAXBContext context) {
		try {
			return context.createMarshaller();
		} catch (JAXBException e) {
			System.out.println("Failed to create Marshaller");
			e.printStackTrace();
		}
		return null;
	}
}
