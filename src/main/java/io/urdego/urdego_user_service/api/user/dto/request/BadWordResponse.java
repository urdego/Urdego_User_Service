package io.urdego.urdego_user_service.api.user.dto.request;

public record BadWordResponse(
        long[] tokenIds,
        String[] tokens,
        long[] attentionMask
) {
    public static BadWordResponse fromTokens(long[] tokenIds, String[] tokens, long[] attentionMask) {
        return new BadWordResponse(tokenIds, tokens, attentionMask);
    }
}
