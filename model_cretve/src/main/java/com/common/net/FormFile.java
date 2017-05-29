package com.common.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 上传文件专用
 * @author:wangzhengyun
 * 2012-9-11
 */
public class FormFile {
	private byte[] data;
	private InputStream inStream;
	private File file;
	private String filname;
	private String parameterName;
	public static String contentType = "application/octet-stream";
	
	public FormFile(String parameterName, byte[] data, String filname, String contentType) {
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if(contentType!=null) this.contentType = contentType;
	}


	
	public FormFile(File file, String parameterName, String contentType) {
		this.filname = file.getName();
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(contentType!=null) this.contentType = contentType;
	}
	
	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return inStream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilname() {
		return filname;
	}

	public void setFilname(String filname) {
		this.filname = filname;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}