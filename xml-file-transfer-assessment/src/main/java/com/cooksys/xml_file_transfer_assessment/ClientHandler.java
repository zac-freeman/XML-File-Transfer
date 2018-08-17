package com.cooksys.xml_file_transfer_assessment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ClientHandler implements Runnable {
	private Socket socket;
	private String rootName;

	public ClientHandler(Socket socket, String rootName) {
		this.socket = socket;
		this.rootName = rootName;
	}

	public void run() {
		try ( InputStream in = this.socket.getInputStream(); )
		{
			JAXBContext context = JAXBContext.newInstance(FileMessage.class);
			Unmarshaller unmarshaller = createUnmarshaller(context);
			FileMessage message = (FileMessage) unmarshaller.unmarshal(in);
			String filePath = rootName + "/" + message.getUsername() + "/" + message.getDate() + "/";
			System.out.println(filePath);
			File file = new File(filePath);
			filePath = filePath + message.getFilename();
			file.createNewFile();

			try ( OutputStream out = new FileOutputStream(filePath); )
			{
				out.write(message.getContents());
			}

		} catch (IOException e) {
			System.out.println("Error getting InputStream from Socket:");
			e.printStackTrace();
		} catch (JAXBException e) {
			System.out.println("Error creating JAXBContext, or Unmarshaller:");
			e.printStackTrace();
		}


		{
			
		}
	}

	public static Unmarshaller createUnmarshaller (JAXBContext context) {
		try {
			return context.createUnmarshaller();
		} catch (JAXBException e) {
			System.out.println("Failed to create Unmarshaller");
			e.printStackTrace();
		}
		return null;
	}
}
