package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
public class JsonUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 将JSON数据转换为具体的对象
	 * @author weisihua
	 *
	 */
	public static <T> T json2Object(String jsonStr, Class<T> cla) {
		if("".equals(jsonStr)||jsonStr.length()==0||null==jsonStr){
			return null;
		}
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		if (jsonObject == null){
			return null;
		}
		Field[] fb = cla.getDeclaredFields();
		T t;
		try {
			t = cla.newInstance();
			for (int j = 0; j < fb.length; j++) {
				String fieldName = fb[j].getName();
				String fieldNameU = fieldName.substring(0, 1).toUpperCase()	+ fieldName.substring(1);
				Method method = null;
				try {
					method = cla.getMethod("set" + fieldNameU,fb[j].getType());
				} catch (NoSuchMethodException noSuchMethodException) {
					continue;
				}
				//解析时间
				if(fb[j].getType().getName().indexOf("Date")>0){
					method.invoke(t,sdf.parse((String) jsonObject.get(fieldName)));
				}
				else{
					method.invoke(t, jsonObject.get(fieldName));
				}
				
			}
			return t;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将json字符串转成jsonObject
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject json2JsonObject(String jsonStr){
		if("".equals(jsonStr)||jsonStr.length()==0||null==jsonStr){
			return null;
		}
        JSONObject jsonObject;
		try {
			jsonObject = JSONObject.fromObject(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (jsonObject == null){
			return null;
		}
		return jsonObject;
	}
	
	/**
	 * 从jsonObject中获取相应的key值
	 * @param key
	 * @param targetObject
	 * @return
	 */
	public static String getStringValue(String key,JSONObject jsonObject){
		
		if(jsonObject == null){
			return "";
		}
		if(!jsonObject.containsKey(key)){
			return "";
		}
		String result = jsonObject.getString(key);
		return result;
	}
	
	/**
	 * 对象转json
	 * @param obj
	 * @return
	 */
	public static String object2Json(Object obj){
		if(null == obj){
			return "";
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		String result = JSONObject.fromObject(obj,config).toString();
		logger.debug("result = {}",result);
		return result;
	}
	/**
	 * json数组转成list
	 * @param cls
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> json2List(Class<T> cls,String jsonStr){
		JSONArray array = JSONArray.fromObject(jsonStr);
		@SuppressWarnings("deprecation")
		List<T> list = JSONArray.toList(array, cls);
		return list;
	}
	/**
	 * list转成json
	 * @param list
	 * @return
	 */
	public static String list2Json(List<?> list){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		JSONArray array = JSONArray.fromObject(list,config);
		return array.toString();
	}
	
	public static <T> List<T> json2List(Class<T> cls,String jsonStr,Map<String,Object> map){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		JSONArray array = JSONArray.fromObject(jsonStr,config);
//		JSONArray array = JSONArray.fromObject(jsonStr);
		@SuppressWarnings({ "deprecation", "unchecked" })
		List<T> list = JSONArray.toList(array, cls,map);
		return list;
	}
	
	private static Gson gson= new Gson();
	
	public static <T> List<T> toList(Class<T> clz,String json){  
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();    
        List<T> list  = new ArrayList<>();  
        for(final JsonElement elem : array){    
             list.add(gson.fromJson(elem, clz));  
        }  
        return list;  
    } 
	
	public static <T> T jsonToObj(Class<T> cls,String jsonStr){
		T t = gson.fromJson(jsonStr, cls);
		return t;
	}
	
	
}