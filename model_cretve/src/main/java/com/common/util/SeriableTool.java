package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SeriableTool {

	public static void saveBean(String filePath, Object obj) throws Exception {
		if(null==obj){
			return;
		}
		File f = new File(filePath);
		if (f.exists()) {
			LogUtil.i("SeriableTool", "delete saveBean...");
			f.delete();
		}
		f.createNewFile();
		ObjectOutputStream obos = new ObjectOutputStream(new FileOutputStream(f));
		obos.writeObject(obj);
		obos.flush();
		obos.close();
	}

	public static Object getBean(String filePath) throws Exception {
		File f = new File(filePath);
		ObjectInputStream oins = new ObjectInputStream(new FileInputStream(f));
		Object obj = oins.readObject();
		return obj;
	}

}
