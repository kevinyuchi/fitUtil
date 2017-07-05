package com.cisco.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by kkaiwen on 7/4/17.
 */

@Service
public class FileExporter {

private static Logger logger = LoggerFactory.getLogger(FileExporter.class);

public void metaDataReader(JSONObject metaData){
		
		JSONArray data = queryData(metaData);
		exportFileToPath(data);
		logger.debug("Test!");
	}
	
	JSONArray queryData(JSONObject metaData){
		return null;
	}
	
	void exportFileToPath(JSONArray data){
	
	}
}
