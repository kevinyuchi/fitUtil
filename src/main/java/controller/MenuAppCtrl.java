package controller;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;


/**
 * Created by kkaiwen on 7/3/17.
 */

@RestController


public class MenuAppCtrl {

    @RequestMapping(value = "/api/get", method = RequestMethod.GET)
    String apiGet() {
        return "get method !\n";
    }


    @RequestMapping(value = "{userId}/api/post", method = RequestMethod.POST)
    String apiPost(@PathVariable String userId, @RequestBody JSONObject postBody) {
        if (postBody.containsKey("test") && userId != null) {
            return "post method success !\n";
        }
        return "post method not success !\n";
    }

//    @RequestMapping(value = "/api/post" , method = RequestMethod.POST)
//    String apiPost( @RequestBody JSONObject postBody){
//        if(postBody.containsKey("test")){
//            return "post method success !\n";
//        }
//        return "post method not success !\n";
//    }


}
