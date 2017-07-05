package com.cisco.controller;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cisco.service.FileExporter;


/**
 * Created by kkaiwen on 7/3/17.
 */

@RestController


public class MenuAppCtrl {

private static Logger logger = LoggerFactory.getLogger(MenuAppCtrl.class);


@Autowired
FileExporter fileExporter;

@RequestMapping(value = "/api/get", method = RequestMethod.GET)
String apiGet() {
	return "get method !\n";
}


//    @RequestMapping(value = "{userId}/api/post", method = RequestMethod.POST)
//    String apiPost(@PathVariable String userId, @RequestBody JSONObject postBody) {
//        if (postBody.containsKey("test") && userId != null) {
//            return "post method success !\n";
//        }
//        return "post method not success !\n";
//    }

@RequestMapping(value = "/api/post", method = RequestMethod.POST)
String apiPost(@RequestBody JSONObject postBody) {
	if (postBody.containsKey("CampaignId")) {
		fileExporter.metaDataReader(postBody);
		return "File has been exported !\n";
	}
	return "Missing Campaign Id !\n";
}


}
