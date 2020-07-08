package com.jsh.erp.mall.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：stephen
 * @date ：Created in 2020/7/7 17:23
 * @description：TODO
 */
@Service
public class ShopOutClient {
    @Autowired
    private RestTemplate restTemplate;
    protected static HttpHeaders headers;
    private static final String url = "http://localhost:8080/api/merchandise/done";

    static {
        headers = new HttpHeaders();
        headers.set("lumiere_erp_123", "lumiere_erp_123");
    }

    public void outDone(String number){
        try {
            HttpEntity httpEntity = new HttpEntity<>(null, this.headers);
            String doneUrl = url + "/" + number;
            restTemplate.postForEntity(doneUrl, httpEntity, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发货失败");
        }
    }
}
