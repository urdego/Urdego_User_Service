package io.urdego.urdego_user_service.domain.service;

public record TokenizerResponse(
        long[] tokenIds,
        String[] tokens,
        long[] attentionMask
) {
    public static TokenizerResponse fromTokens(long[] tokenIds,String[] tokens,long[] attentionMask) {
        return new TokenizerResponse(tokenIds, tokens, attentionMask);
    }
}
