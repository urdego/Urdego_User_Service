package io.urdego.urdego_user_service.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    @Cacheable(value = "price", key = "#productId")
    public String getPrice(Long productId) {
        log.info("캐시 미적중 - 실제 단가 조회 수행 (productId: {})", productId);
        simulateSlowQuery();
        return "Price for product " + productId;
    }

    private void simulateSlowQuery() {
        try {
            Thread.sleep(500); // 0.5초 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}