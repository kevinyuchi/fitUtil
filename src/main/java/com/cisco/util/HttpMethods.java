package com.cisco.util;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpMethods {
private static Logger LOGGER = LoggerFactory.getLogger(HttpMethods.class);

public HttpMethods(){
	this.client = new DefaultHttpClient();
}

@Autowired HttpClient client;
public JSONObject getMethod(String endPoint, String authToken) throws IOException {
	LOGGER.debug("Token = {} and endpont = {}", authToken, endPoint);
	HttpGet request = new HttpGet(endPoint);
	request.setHeader("Authorization", authToken);
	HttpResponse response = client.execute(request);
	JSONObject responseJson = null;
	StatusLine status = response.getStatusLine();
	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
		JSONObject nonAuthResponse = new JSONObject();
		LOGGER.error("Request is not authorized , and reason phrase is : {}",
				EntityUtils.toString(response.getEntity()));
		nonAuthResponse.put("Not_Auth", true);
		return nonAuthResponse;
	} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT) {
		JSONObject conflict = new JSONObject();
		LOGGER.error("Request is conflict , and reason phrase is : {}", EntityUtils.toString(response.getEntity()));
		conflict.put("conflict", true);
		return conflict;
	} else if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
		String json = EntityUtils.toString(response.getEntity());
		LOGGER.error("IOException : {}", json);
		throw new IOException("StatusCode =" + status.getStatusCode() + " and reason phrase is" + json);
	} else {
		responseJson = responseReader(response);
	}
	return responseJson;
}

public JSONObject patchMethod(String endPoint, String authToken, String patchJson){
	LOGGER.debug("Token = {} and endpont = {}", authToken, endPoint);
	HttpPatch patch = new HttpPatch(endPoint);
	patch.setHeader("Authorization", authToken);
	StringEntity entity = new StringEntity(patchJson, ContentType.create("application/json", "UTF-8"));
	entity.setChunked(true);
	patch.setEntity(entity);
	HttpResponse response = null;
	try{
		response = client.execute(patch);
	}catch(Exception e){
		LOGGER.error("Http Client exe fail.", e);
	}
	int code = 0;
	if(response != null){
		code = response.getStatusLine().getStatusCode();
	}
	
	LOGGER.debug("Response Code = {}", code);
	JSONObject json = new JSONObject();
	json.put("code", code);
	if (code >= 300) {
		json.put("endpoint", endPoint);
		json.put("token", authToken);
		json.put("record", patchJson);
		LOGGER.error("Patch Call Fails : {}", json);
		return json;
	}
	return json;
}


public static JSONObject postJSON(HttpClient client, String endPoint, String authToken, String postJson)
		throws ClientProtocolException, IOException {
	LOGGER.info("Token = {} and endpont = {}", authToken, endPoint);
	HttpPost post = new HttpPost(endPoint);
	post.setHeader("Authorization", authToken);
	StringEntity entity = new StringEntity(postJson, ContentType.create("application/json", "UTF-8"));
	entity.setChunked(true);
	post.setEntity(entity);
	HttpResponse response = client.execute(post);
	int code = response.getStatusLine().getStatusCode();
	StatusLine status = response.getStatusLine();
	
	LOGGER.info("Response Code = {}", code);
	
	if (code == HttpStatus.SC_UNAUTHORIZED) {
		JSONObject nonAuthResponse = new JSONObject();
		nonAuthResponse.put("Not_Auth", true);
		return nonAuthResponse;
	} else if (code == HttpStatus.SC_NO_CONTENT) {
		return null;
	} else if (code == HttpStatus.SC_CREATED) {
		return responseReader(response);
	} else if (code == HttpStatus.SC_OK) {
		JSONObject jsonObj = responseReader(response);
		LOGGER.debug("JSONObject = {}", jsonObj.toString());
		return jsonObj;
	} else if (code == HttpStatus.SC_CONFLICT) {
		JSONObject conflict = new JSONObject();
		conflict.put("conflict", true);
		return conflict;
	} else {
		LOGGER.info("StatusCode =" + status.getStatusCode() + " and reason phrase is " + status.getReasonPhrase());
		throw new IOException(
				                     "StatusCode =" + status.getStatusCode() + " and reason phrase is " + status.getReasonPhrase());
	}
}


static JSONObject responseReader(HttpResponse response) throws IOException {
	JSONObject responseJson = null;
	try {
		return new JSONObject(EntityUtils.toString(response.getEntity()));
	} catch (JSONException e) {
		LOGGER.error("JSONException = {}", e);
	}
	if (response.getStatusLine().getStatusCode() == 200) {
		LOGGER.debug("NOT AUTH Request!");
		JSONObject json = new JSONObject();
		json.put("Not_Auth", true);
		return json;
	}
	return responseJson;
}

public void setClient(HttpClient client) {
	this.client = client;
}
}
