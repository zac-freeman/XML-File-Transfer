package com.cooksys.xml_file_transfer_assessment;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class FileMessage {

	@XmlElement
	private String username;

	@XmlElement
	private String date;

	@XmlElement
	private String filename;

	@XmlElement
	private byte[] contents;

	public FileMessage() {}

	public FileMessage(String username, String date, String filename, byte[] contents) {
		this.username = username;
		this.date = date;
		this.filename = filename;
		this.contents = contents;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	public byte[] getContents() {
		return this.contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "username: " + this.username + "\ndate: " + this.date + "\nfilename: " + this.filename + "\n\n" + new String(this.contents);
	}
}
