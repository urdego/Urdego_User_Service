package io.urdego.urdego_user_service.infra.model;

import ai.onnxruntime.OrtException;

public interface OnnxInference {
    //추론 메서드
    float[][] runInference(long[] inputIdsData, long[] attentionMaskData, long[] inputShape )throws OrtException;

    void close() throws OrtException;
}
