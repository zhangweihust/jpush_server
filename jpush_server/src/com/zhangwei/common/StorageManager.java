package com.zhangwei.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;



import com.google.gson.Gson;



/**
 * @author zhangwei
 */
public class StorageManager {
	public static final String CHACHE_PREFIX = "storage_";

	private static final String TAG = "StorageManager";
	private static StorageManager instance;
	private HashMap<String, StorageValue> cache;
	private static Gson gson;

	private class CacheFilter implements FilenameFilter {

		public boolean isCache(String file) {
			if (file.toLowerCase(Locale.ENGLISH).startsWith(CHACHE_PREFIX)) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String fname) {
			return (isCache(fname));

		}

	}


	
	private StorageManager() {
		cache = new HashMap<String, StorageValue>();
		gson = new Gson();
		load();
	}

	private void load() {
		FilenameFilter filter = new CacheFilter();
/*		File dirFile = new File("mydir");
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}*/
		File dirFile = new File(".");
		
		File[] filelist = dirFile.listFiles(filter);
		for (File f : filelist) {
			cache.put(f.getName(),
					new StorageValue(f.getName(), (int) f.length()));
		}
	}

	public static StorageManager getInstance() {

		if (instance == null) {
			instance = new StorageManager();
		}

		return instance;
	}

	/**
	 * @return 被Cache的元数据信息
	 */
	public String getItem(String uri) {
		String key = MD5.encode(CHACHE_PREFIX, uri.getBytes());
		if (cache.containsKey(key)) {
			// Read the created file and display to the screen
			try {
				File dataFile = new File(key);
				FileInputStream mInput = new FileInputStream(dataFile);
				int len = 0;
				byte[] data = new byte[1024];
				StringBuilder sb = new StringBuilder();

				while ((len = mInput.read(data)) != -1) {
					sb.append(new String(data, 0, len));
				}

				mInput.close();
				return sb.toString();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		} else {
			return null;
		}
	}

	/**
	 * @return 被Cache的元数据信息
	 */
	public Object getItem(String uri, Class<?> cls) {
		String jsonStr = getItem(uri);
		Object object = null;
		try{
			if (jsonStr != null) {
				object = gson.fromJson(jsonStr, cls);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return object;
	}
	
	public Object getItem(String uri, Type type) {
		String jsonStr = getItem(uri);
		Object object = null;
		try{
			if (jsonStr != null) {
				object = gson.fromJson(jsonStr, type);
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		return object;
	}

	/**
	 * 
	 * @param uri 要查找的资源uri
	 * @param objStr 将json String作为内容写入内存�?overwrite)
	 */
	public StorageValue putItem(String uri, String objStr) {
		String key = MD5.encode(CHACHE_PREFIX, uri.getBytes());
		FileOutputStream mOutput = null;
		StorageValue result = null;
		try {
			// overwrite
			File dataFile = new File(key);
			mOutput = new FileOutputStream(dataFile, false);
			mOutput.write(objStr.getBytes());
			mOutput.close();
			result = cache.put(key, new StorageValue(key,
					objStr.getBytes().length));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @param uri 要插入的资源uri
	 * @param object json Object
	 * @param cls json对象类型
	 */
	public StorageValue putItem(String uri, Object object, Class<?> cls) {

		String jsonStr = null;
		jsonStr = gson.toJson(object, cls);
		if (jsonStr != null) {
			return putItem(uri, jsonStr);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param uri
	 *            要删除的资源uri
	 */
	public void deleteItem(String uri) {
		String key = MD5.encode(CHACHE_PREFIX, uri.getBytes());
		if (cache.containsKey(key)) {
			cache.remove(key);
			File dataFile = new File(key);
			dataFile.delete();

		}
	}

	public void cleanAll() {
		Iterator<Entry<String, StorageValue>> iter = cache.entrySet()
				.iterator();

		while (iter.hasNext()) {
			Map.Entry<String, StorageValue> entry = (Map.Entry<String, StorageValue>) iter
					.next();
			String key = entry.getKey();
			File dataFile = new File(key);
			dataFile.delete();
		}

		cache.clear();

	}

}