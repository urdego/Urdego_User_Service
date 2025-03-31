package io.urdego.urdego_user_service.domain.service.components;

import ai.onnxruntime.OrtException;
import io.urdego.urdego_user_service.api.user.dto.request.BadWordResponse;
import io.urdego.urdego_user_service.common.enums.PlatformType;
import io.urdego.urdego_user_service.common.exception.user.InvalidActiveCharacterException;
import io.urdego.urdego_user_service.common.exception.user.InvalidNicknameUserException;
import io.urdego.urdego_user_service.common.exception.userCharacter.DuplicatedCharacterUserException;
import io.urdego.urdego_user_service.domain.entity.User;
import io.urdego.urdego_user_service.infra.model.OnnxInference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {
    //학습 임계값
    private static final float THRESHOLD = 0.5f;

    private final UserReader userReader;
    private final OnnxInference onnxInference;
    private final Tokenizer tokenizer;

    //이미 회원가입 된 유저인지 확인
    public boolean checkSignUpUser(String email, PlatformType platformType) {
        if(userReader.existsByEmailAndPlatformType(email, platformType)){
            return true;
        }
        return false;
    }

    //삭제된 회원인지 확인
    public boolean checkDeletedUser(User existingUser){
        if(existingUser.getIsDeleted().equals(Boolean.TRUE)){
            return true;
        }
        return false;
    }

    public boolean validateNickname(String nickname) throws OrtException {
        if(userReader.existsByNicknameAndIsDeletedFalse(nickname)){
            throw DuplicatedCharacterUserException.EXCEPTION;
        }
        if(isProfane(nickname)){
            throw InvalidNicknameUserException.EXCEPTION;
        }
        return true;
    }

    public boolean isProfane(String plainText) throws OrtException {
        BadWordResponse response = tokenizer.getTokenizer(plainText);
        if(response == null || response.tokenIds() == null){
            log.error("Tokenizer response is null for text : {}", plainText);
            return true;
        }

        long[] tokenIds = response.tokenIds();
        long[] attentionMask = response.attentionMask();
        long[] inputShape = new long[]{1, tokenIds.length};
        float[][] logits = onnxInference.runInference(tokenIds, attentionMask, inputShape);

        // 모든 로짓에 대해 시그모이드 적용하여 확률 배열 생성
        int numClasses = logits[0].length;
        float[] probabilities = new float[numClasses];
        for (int i = 0; i < numClasses; i++) {
            probabilities[i] = (float)(1 / (1 + Math.exp(-logits[0][i])));
        }

        log.info("Logits: {}", Arrays.toString(logits[0]));
        log.info("Probabilities: {}", Arrays.toString(probabilities));

        boolean[] predictions = new boolean[numClasses];
        for (int i = 0; i < numClasses; i++) {
            predictions[i] = probabilities[i] >= THRESHOLD;
        }
        log.info("Predictions: {}", Arrays.toString(predictions));

        // 예시: "clean"을 제외한 나머지 중 하나라도 true면 욕설이 포함된 것으로 간주
        String[] labelNames = {"여성/가족", "남성", "성소수자", "인종/국적", "연령",
                "지역", "종교", "기타 혐오", "악플/욕설", "clean"};
        for (int i = 0; i < predictions.length; i++) {
            if (predictions[i] && !labelNames[i].equals("clean")) {
                log.info("Predicted Label: {}", labelNames[i]);
                return true;
            }
        }
        return false;
    }
}
