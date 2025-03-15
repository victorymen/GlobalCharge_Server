package org.appserver.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.appserver.utils.JsapiServiceExample;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wxApi")
public class WxApi extends ApiController {



    /**
     * JSAPI支付下单，并返回JSAPI调起支付数据
     */
    @PostMapping("prepayWithRequestPayment")
    public PrepayWithRequestPaymentResponse prepayWithRequestPayment(@RequestBody PrepayRequest request)  {
        return JsapiServiceExample.prepayWithRequestPayment(request);
    }


    /**
     * 微信支付订单号查询订单
     */
    @PostMapping("queryOrderById")
    public  Transaction queryOrderById(@RequestBody QueryOrderByIdRequest request){
        return JsapiServiceExample.queryOrderById(request);
    }

    /**
     * 商户订单号查询订单
     */
    @PostMapping("queryOrderByOutTradeNo")
    public  Transaction queryOrderByOutTradeNo(@RequestBody QueryOrderByOutTradeNoRequest request) {
        return JsapiServiceExample.queryOrderByOutTradeNo(request);
    }

    /**
     * 关闭订单
     */
    @PostMapping("closeOrder")
    public  void closeOrder( @RequestBody CloseOrderRequest request) {
        JsapiServiceExample.closeOrder(request);
    }


    /**
     * 回调接口触发  用于支付是相应
     * @param
     * @return
     */
    @PostMapping("closeOrderTradeNo")
    public void closeOrderTradeNo()  {
        //todo 回调接口触发  用于支付是相应处理逻辑   还没想好支付成功后这里写什么
        System.out.println("回调接口触发  用于支付是相应处理逻辑   还没想好支付成功后这里写什么 ");
    }

}
