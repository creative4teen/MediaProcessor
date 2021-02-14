package org.creativeteen.tools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	

	public static Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT)
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.serializeNulls()
			.create();

	public static <T> T parse(String jsonstr, Class<T> clazz) {
		T obj = null;
		try {
			obj = (T) gson.fromJson(jsonstr, clazz);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	public static <T> String serialize(T obj) {
		return gson.toJson(obj);
	}
		
	
}
