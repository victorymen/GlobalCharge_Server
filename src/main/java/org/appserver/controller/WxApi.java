package org.appserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.appserver.entity.ProductsUser;
import org.appserver.service.ProductsUserService;
import org.appserver.utils.JsapiServiceExample;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
     * 服务对象
     */
    @Resource
    private ProductsUserService productsUserService;
    /**
     * 回调接口触发  执行充值命令  自动防止重复请求
     * @param
     * @return
     */
    @PostMapping("closeOrderTradeNo")
    public void closeOrderTradeNo(@RequestParam String outTradeNo)  {   //支付成功后回调这里接口     其它接口访问存在断点时 会重复请求
        ProductsUser productsUser = productsUserService.getOne(new QueryWrapper<ProductsUser>().eq("out_trade_no", outTradeNo));
        productsUser.setSernoberstate(null);
        productsUserService.saveOrder(productsUser);
    }

}
