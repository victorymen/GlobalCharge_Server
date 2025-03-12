package org.appserver.utils;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * JsapiService使用示例
 */
public class JsapiServiceExample {
    /**
     * AppId
     */
    public static final String AppId = "wx316be3df54cf177d";
    /**
     * 商户号
     */
    public static final String merchantId = "1708773892";

    /**
     * 商户API私钥路径
     */
    public static final String privateKeyPath = "E:\\workYHC\\GlobalCharge_Server\\src\\main\\resources\\apiclient\\apiclient_key.pem";
    public static final String privateKeyPath1 = "/home/JAVA_APP/apiclient_key.pem";

    /**
     * 商户证书序列号
     */
    public static final String merchantSerialNumber = "69FEA7BE7F4CE1A8A984B0B2BA919BAC5272FEAA";

    /**
     * 商户APIV3密钥
     */
    public static final String apiV3Key = "69FEA7BE7F4CE1A8A984B0B2BA919BAC";

    public static JsapiServiceExtension service;

    static {
        String os = System.getProperty("os.name");
        String path=privateKeyPath;
        if (os != null && os.toLowerCase().startsWith("linux")) {
            path=privateKeyPath1;
            System.out.println("当前系统是 Linux");
        } else {
            System.out.println("当前系统不是 Linux");
        }
        // 初始化商户配置
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .privateKeyFromPath(path)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key)
                .build();
        // 初始化服务
        service = new JsapiServiceExtension.Builder()
                .config(config)
                .signType("RSA") // 不填默认为RSA
                .build();
    }


    /**
     * 关闭订单
     */
    public static void closeOrder(CloseOrderRequest request) {
        try {
            // 调用接口
            request.setMchid(merchantId);
            service.closeOrder(request);
        } catch (HttpException e) {
            // 发送HTTP请求失败
            System.err.println("关闭订单时HTTP请求失败: " + e.getMessage());
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            System.err.println("关闭订单时服务异常: " + e.getResponseBody());
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            System.err.println("关闭订单时消息格式错误: " + e.getMessage());
        }
    }

    /**
     * JSAPI支付下单，并返回JSAPI调起支付数据
     */
    public static PrepayWithRequestPaymentResponse prepayWithRequestPayment(PrepayRequest request) {
        try {
            request.setAppid(AppId);
            request.setMchid(merchantId);
            return service.prepayWithRequestPayment(request);
        } catch (HttpException e) {
            // 发送HTTP请求失败
            System.err.println("HTTP请求失败: " + e.getMessage());
            // 可以添加日志记录或监控上报代码
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            System.err.println("服务异常: " + e.getResponseBody());
            // 可以添加日志记录或监控上报代码
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            System.err.println("消息格式错误: " + e.getMessage());
            // 可以添加日志记录或监控上报代码
        }
        return null; // 在异常情况下返回null
    }

    /**
     * 微信支付订单号查询订单
     */
    public static Transaction queryOrderById(QueryOrderByIdRequest request) {
        try {
            // 调用接口
            request.setMchid(merchantId);
            return service.queryOrderById(request);
        } catch (HttpException e) {
            // 发送HTTP请求失败
            System.err.println("查询订单时HTTP请求失败: " + e.getMessage());
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            System.err.println("查询订单时服务异常: " + e.getResponseBody());
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            System.err.println("查询订单时消息格式错误: " + e.getMessage());
        }
        return null; // 在异常情况下返回null
    }

    /**
     * 商户订单号查询订单
     */
    public static Transaction queryOrderByOutTradeNo(QueryOrderByOutTradeNoRequest request) {
        try {
            // 调用接口
            request.setMchid(merchantId);
            return service.queryOrderByOutTradeNo(request);
        } catch (HttpException e) {
            // 发送HTTP请求失败
            System.err.println("查询订单时HTTP请求失败: " + e.getMessage());
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            System.err.println("查询订单时服务异常: " + e.getResponseBody());
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            System.err.println("查询订单时消息格式错误: " + e.getMessage());
        }
        return null; // 在异常情况下返回null
    }
}
