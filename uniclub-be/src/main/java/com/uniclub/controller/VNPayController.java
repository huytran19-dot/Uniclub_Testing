package com.uniclub.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniclub.dto.request.VNPayPaymentRequest;
import com.uniclub.dto.response.VNPayPaymentResponse;
import com.uniclub.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class VNPayController {
    
    private final VNPayService vnPayService;
    
    /**
     * Create VNPay payment URL
     * POST /api/vnpay/create-payment
     */
    @PostMapping("/create-payment")
    public ResponseEntity<VNPayPaymentResponse> createPayment(
            @RequestBody VNPayPaymentRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            String ipAddr = getClientIp(httpRequest);
            String paymentUrl = vnPayService.createPaymentUrl(
                    request.getOrderId(),
                    request.getOrderInfo(),
                    ipAddr,
                    request.getLocale()
            );
            
            return ResponseEntity.ok(new VNPayPaymentResponse(paymentUrl, "Payment URL created successfully"));
        } catch (Exception e) {
            log.error("Error creating payment URL", e);
            return ResponseEntity.badRequest()
                    .body(new VNPayPaymentResponse(null, "Error: " + e.getMessage()));
        }
    }
    
    /**
     * VNPay IPN (Instant Payment Notification)
     * GET /api/vnpay/ipn
     * This is called by VNPay server
     */
    @GetMapping("/ipn")
    public ResponseEntity<Map<String, String>> handleIPN(@RequestParam Map<String, String> params) {
        log.info("Received VNPay IPN: {}", params);
        
        Map<String, String> response = vnPayService.handleIPN(params);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * VNPay Return URL
     * GET /api/vnpay/return
     * This is where user is redirected after payment
     */
    @GetMapping("/return")
    public ResponseEntity<Map<String, Object>> handleReturn(@RequestParam Map<String, String> params) {
        log.info("Received VNPay return: {}", params);
        
        Map<String, Object> result = vnPayService.handleReturn(params);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Get client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        
        // If multiple IPs, take the first one
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        
        return ipAddress != null ? ipAddress : "127.0.0.1";
    }
}
