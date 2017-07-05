package com.cisco.handler;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by kkaiwen on 7/4/17.
 */

@Component
public class ApiQueryHandler {

private Set<String> emails;
private Set<Integer> contactIds;
	
	void queryActivitiesByCompaignId(String CompaignId){
		//contactIds = queryContactWithId();
		emails = queryContactWithEmail();
	}

	Set<String> queryContactWithEmail(){
		return null;
	}
	
}
