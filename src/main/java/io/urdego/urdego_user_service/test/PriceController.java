package io.urdego.urdego_user_service.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user-service")
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/test/{id}")
    public String getPrice(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        String result = priceService.getPrice(id);
        long end = System.currentTimeMillis();
        long duration = end - start;

        log.info("📌 단가 조회 요청 - 상품 ID: {} | 응답 시간: {}ms", id, duration);
        return "Result: " + result + " | Response time: " + duration + "ms";
    }
}