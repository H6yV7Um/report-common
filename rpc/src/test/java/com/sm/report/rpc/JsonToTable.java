package com.sm.report.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @author likangning
 * @since 2018/5/25 下午8:20
 */
public class JsonToTable {
	public static void main(String[] args) {
		String json = "{\"blank\":[\"xxx\",\"yy\"],\"id\":\"001\",\"account\":[{\"name\":\"name1\",\"phones\":[{\"phone\":\"000\"},{\"phone\":\"111\"}],\"account\":\"account1\",\"card\":[{\"cname\":\"cname1\",\"cid\":\"cid1\"},{\"cname\":\"cname2\",\"cid\":\"cid2\"}]},{\"name\":\"name2\",\"account\":\"account2\"}]}";
		System.out.println(json);

		JSONObject jsonObject = JSON.parseObject(json);

		printItem(jsonObject);

		System.out.println(jsonObject);
		printX(jsonObject,new HashMap<String, Object>());


	}

	public static void toArray(String json) {

		JSONObject jsonObject = JSON.parseObject(json);

		printItem(jsonObject);

		System.out.println(jsonObject);
		printX(jsonObject,new HashMap<String, Object>());


	}

	private static List<String> printX(JSONObject jsonObject, Map<String,Object> reMap){
		Set<String> keys = jsonObject.keySet();
		List<String> currKeys = Arrays.asList(keys.toArray(new String[]{}));
		boolean flag = false;
		for(String key : keys){
			Object value = jsonObject.get(key);
			if(value == null)continue;
			if(value instanceof JSONArray){
				flag = true;
			}else{
				reMap.put(key,value);
			}
		}
		for(String key : keys){
			Object value = jsonObject.get(key);
			if(value instanceof JSONArray){
				for(Object o1 : (JSONArray)value){
					reMap.keySet().removeAll(printX((JSONObject) o1,reMap));
				}
			}
		}

		if(!flag) System.out.println(reMap);
		return currKeys;
	}

	private static void printItem(JSONObject jsonObject){
		Iterator<String> iterator = jsonObject.keySet().iterator();
		String preArrayKey = null;
		while (iterator.hasNext()){
			String key = iterator.next();
			Object value = jsonObject.get(key);
			if(value == null)continue;
			if(value instanceof JSONArray){
				JSONArray currArray = (JSONArray) value;
				if(currArray.size() == 0){
					jsonObject.put(key,null);
					continue;
				}

				//集合下的子集非JSON对象转JSON对象 eg:[1,2,3]
				if(!(currArray.get(0) instanceof JSONArray) && !(currArray.get(0) instanceof JSONObject)){
					JSONArray newArray = new JSONArray();
					for(Object o1 : currArray){
						JSONObject jsonObjectNew = new JSONObject();
						jsonObjectNew.put(key,o1);
						newArray.add(jsonObjectNew);
					}
					jsonObject.put(key,newArray);
				}

				if(preArrayKey == null){
					preArrayKey = key;
				}else{
					for(Object o1 : currArray){
						((JSONObject)o1).put(preArrayKey,jsonObject.get(preArrayKey));
					}
					jsonObject.put(preArrayKey,null);
					preArrayKey = key;
				}
			}


		}

		if (preArrayKey != null)
			for(Object o1 : (JSONArray)jsonObject.get(preArrayKey)){
				printItem((JSONObject) o1);
			}
	}
}
