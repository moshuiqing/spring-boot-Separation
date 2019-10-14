package com.home.lh.util;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtil {

	@SuppressWarnings("deprecation")
	public static String getCode(String client_id, String response_type, String redirect_uri,String name,String pwd) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 将用户名和密码放入header中
        String url =Global.authorizeUrl+"?client_id=" + client_id + "&"
                + "response_type=" + response_type + "&" + "redirect_uri=" + redirect_uri+"&userName="+name+"&password="+pwd;

        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).setConnectionRequestTimeout(5000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        String result = "";
        try {

            HttpResponse response = httpClient.execute(httpGet);
            // 从从定向地址中取得code
            if (response.getStatusLine().getStatusCode() == 302) {
                Header header = response.getFirstHeader("Location");
                System.out.println(response.getParams());
                String location = header.getValue();
                String code = location.substring(location.indexOf("=") + 1, location.length());
                return code;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
