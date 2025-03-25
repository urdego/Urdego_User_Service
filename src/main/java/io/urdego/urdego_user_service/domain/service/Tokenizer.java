package io.urdego.urdego_user_service.domain.service;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import io.urdego.urdego_user_service.api.user.dto.request.BadWordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;

@Component
@Slf4j
public class Tokenizer {
    @Value("${tokenizer.file.path}")
    private String tokenizerFilePath;

    public BadWordResponse getTokenizer(String text){
        try{
            // 파일 경로 주입 값 로그 출력
            log.info("Tokenizer file path: {}", tokenizerFilePath);

            HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(
                   Paths.get(tokenizerFilePath)
           );

            Encoding encoding = tokenizer.encode(text);
            long[] tokenIds = encoding.getIds();
            long[] attentionMask = createAttentionMask(tokenIds);

            // 토큰화 결과 로그 출력
            log.info("Tokenization result - Token IDs: {}", Arrays.toString(tokenIds));
            log.info("Tokenization result - Tokens: {}", Arrays.toString(encoding.getTokens()));
            log.info("Tokenization result - Attention Mask: {}", Arrays.toString(attentionMask));


            return BadWordResponse.fromTokens(tokenIds, encoding.getTokens(), attentionMask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private long[] createAttentionMask(long[] tokenIds){
        long[] mask = new long[tokenIds.length];
        for (int i = 0; i < tokenIds.length; i++){
            mask[i] = tokenIds[i] == 0 ? 0 : 1;
        }
        return mask;
    }
}
