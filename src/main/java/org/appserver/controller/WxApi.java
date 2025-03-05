package org.appserver.controller;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.extension.api.ApiController;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/api/wxApi")
public class WxApi extends ApiController {

    @Resource
    private RestTemplate restTemplate;

    @PostMapping("jsapi")
    public String jsapi(@RequestBody JSONObject json) {
        String url = "https://api.mch.weixin.qq.com/v3/pay/partner/transactions/jsapi";

        // 创建 HttpHeaders 对象并设置头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer YOUR_ACCESS_TOKEN_HERE"); // 这里设置你的 Authorization 信息
        // 根据需要添加其他头部信息

        // 创建 HttpEntity 对象，将头部信息和请求体封装在一起
        HttpEntity<String> requestEntity = new HttpEntity<>(json.toJSONString(), headers);

        // 使用 RestTemplate 发送 POST 请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return jsonObject.toJSONString();
    }

}
