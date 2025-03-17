package com.PhamChien.ecommerce.service;


import com.PhamChien.ecommerce.dto.message.CheckoutMessage;
import com.PhamChien.ecommerce.dto.request.PaymentRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.util.VNPayUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${payment.vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${payment.vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${payment.vnpay.url}")
    private String vnp_PayUrl;

    @Value("${payment.vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${spring.kafka.topic.checkoutOrder}")
    private String checkoutOrderTopic;

    public String createPayment(PaymentRequest paymentRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        Random random = new Random();
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang " + paymentRequest.getOrderInfo();
        String orderType = "billpayment";
        String vnp_TxnRef = String.valueOf(10000000 + random.nextInt(9000000));
        String vnp_IpAddr = VNPayUtil.getIpAddress(request);
        String vnp_TmnCode = this.vnp_TmnCode;
        Map vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(paymentRequest.getAmount() * 100 * 20000));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:5173/checkout");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(this.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnp_PayUrl + "?" + queryUrl;
    }


    public String vnpayReturn(String responseCode, String orderInfo) {
        String orderId = orderInfo.substring("Thanh toan don hang ".length());
        CheckoutMessage checkoutMessage = new CheckoutMessage(orderId, responseCode);
        Gson gson = new Gson();
        String json = gson.toJson(checkoutMessage);
        kafkaTemplate.send(checkoutOrderTopic, json);
        return "OK";
    }
}
