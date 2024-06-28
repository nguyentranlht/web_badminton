package com.example.webBadminton.controller;

import com.example.webBadminton.model.BookingCourt;
import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.service.BadmintonService;
import com.example.webBadminton.service.BookingService;
import com.example.webBadminton.utils.PaymentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private BadmintonService badmintonService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/create_payment")
    public RedirectView createPayment(@RequestParam BookingCourt bookingCourt) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

//        BigDecimal totalPrice = orderService.calculateTotalPrice(orderId);
//        long amount = totalPrice.multiply(new BigDecimal(100)).longValue();
        Long badmintonId = bookingCourt.getCourt().getBadmintonId();
        Badminton badminton = badmintonService.getBadmintonById(badmintonId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + badmintonId));
        long amount = (long) (badminton.getRentalPrice() * 100);

        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl + "?bookingId=" + bookingCourt.getId());
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
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        return new RedirectView(paymentUrl);

    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("bookingId") Long bookingId, Model model) {
        // Thêm bất kỳ thông tin nào cần thiết để hiển thị trên view
        BookingCourt bookingCourt = bookingService.getBookingById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid badminton Id:" + bookingId));
        bookingCourt.setStatus("true");
        bookingService.updateBooking(bookingCourt);
        model.addAttribute("message", "Thanh toán của bạn đã được xử lý thành công!");
        return "redirect:/"; // Trả về tên của view (thymeleaf or JSP file)
    }
}

