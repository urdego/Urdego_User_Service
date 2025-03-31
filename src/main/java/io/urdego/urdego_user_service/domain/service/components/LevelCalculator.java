package io.urdego.urdego_user_service.domain.service.components;

import org.springframework.stereotype.Component;

@Component
public class LevelCalculator {
    public int calculateLevel(Long totalExp){
        if (totalExp >= 2500) {
            return 9;
        } else if (totalExp >= 2000) {
            return 8;
        } else if (totalExp >= 1600) {
            return 7;
        } else if (totalExp >= 1200) {
            return 6;
        } else if (totalExp >= 900) {
            return 5;
        } else if (totalExp >= 600) {
            return 4;
        } else if (totalExp >= 400) {
            return 3;
        } else if (totalExp >= 200) {
            return 2;
        }
        else {
            // 100 미만의 exp는 아직 레벨업이 되지 않은 것으로 처리
            return 1;
        }
    }
}
