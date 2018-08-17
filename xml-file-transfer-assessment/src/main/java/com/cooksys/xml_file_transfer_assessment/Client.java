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
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Client {

	private String ip;
	private int port;
	private String targetName;	//name of target file or directory to read from
	private String username;

	public Client(String ip, int port, String username, String targetName) {
		this.ip = ip;
		this.port = port;
		this.targetName = targetName;
		this.username = username;

		// creates a list of files to iterate over
		File target = new File(this.targetName);
		File[] files = new File[0];
		if (target.isDirectory()) {
			files = target.listFiles();
		} else if (target.isFile()) {
			files = new File[] {target};
		} else {
			System.out.println("File or directory '" + this.targetName + "' not found.");
		}

		// creates a FileMessage from each file and marshals it to ClientHandler over socket
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.println("Nested directory '" + file.getName() + "' ignored.");
				continue;
			}

			try (
				Socket socket = new Socket(this.ip, this.port);
				InputStream in = new FileInputStream(file.getPath());
				OutputStream out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
			) {
				JAXBContext context = JAXBContext.newInstance(FileMessage.class);
				Marshaller marshaller = createMarshaller(context);

				// creates formatted date string
				Calendar date = Calendar.getInstance();
				String dateText = date.get(1) + "-" + String.format("%02d", date.get(2)) + "-" + String.format("%02d", date.get(5));

				byte[] contents = new byte[in.available()];
				in.read(contents);

				FileMessage message = new FileMessage(this.username, dateText, file.getName(), contents);
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

	// creates Marshaller object from JAXBContext
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