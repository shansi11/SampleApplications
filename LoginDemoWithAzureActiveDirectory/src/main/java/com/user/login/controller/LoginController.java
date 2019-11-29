package com.user.login.controller;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.microsoft.aad.adal4j.AuthenticationResult;



@Controller
@RequestMapping("/logindemowithazureactivedirectory")
public class LoginController {

	 @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String index(ModelMap model, HttpServletRequest httpRequest) {
		 
		 HttpSession session = httpRequest.getSession();
	        AuthenticationResult result = (AuthenticationResult) session.getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME);
	        if (result == null) {
	            model.addAttribute("error", new Exception("AuthenticationResult not found in session."));
	            return "/error";
	        } else {
	            String data;
	            try {
	                String tenant = session.getServletContext().getInitParameter("tenant");
	                data = getUsernamesFromGraph(result.getAccessToken(), tenant);
	                model.addAttribute("tenant", tenant);
	                model.addAttribute("users", data);
	                model.addAttribute("userInfo", result.getUserInfo());
	            } catch (Exception e) {
	                model.addAttribute("error", e);
	                return "/error";
	            }
	        }
	        return "/logindemowithazureactivedirectory";
		//return "hello.html";
	}
	 
	 private String getUsernamesFromGraph(String accessToken, String tenant) throws Exception {


	        URL url = new URL("https://graph.microsoft.com/v1.0/users");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
	        conn.setRequestProperty("Accept","application/json");
	        int httpResponseCode = conn.getResponseCode();

	        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
	        // logger.info("goodRespStr ->" + goodRespStr);
	        int responseCode = conn.getResponseCode();
	        JSONObject response = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);
	        JSONArray users;

	        users = JSONHelper.fetchDirectoryObjectJSONArray(response);

	        StringBuilder builder = new StringBuilder();
	        User user;
	        for (int i = 0; i < users.length(); i++) {
	            JSONObject thisUserJSONObject = users.optJSONObject(i);
	            user = new User();
	            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
	            builder.append(user.getUserPrincipalName() + "<br/>");
	        }
	        return builder.toString();
	    }
}