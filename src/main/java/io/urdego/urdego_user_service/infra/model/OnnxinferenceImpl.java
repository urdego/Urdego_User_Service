/*
package io.urdego.urdego_user_service.infra.model;

import ai.onnxruntime.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.LongBuffer;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnnxinferenceImpl implements OnnxInference{
    private OrtEnvironment env;
    private OrtSession session;

    @Value("${onnx.model.path}")
    private String modelPath;

    @PostConstruct
    public void init() throws OrtException {
        env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions options = new OrtSession.SessionOptions();
        session = env.createSession(modelPath, options);
    }
    @Override
    public float[][] runInference(long[] inputIdsData, long[] attentionMaskData, long[] inputShape) throws OrtException{
        // long 배열을 LongBuffer로 변경.
        LongBuffer inputIdsBuffer = LongBuffer.wrap(inputIdsData);
        LongBuffer attentionMaskBuffer = LongBuffer.wrap(attentionMaskData);

        // int64 텐서 생성
        OnnxTensor inputIdsTensor = OnnxTensor.createTensor(env, inputIdsBuffer, inputShape);
        OnnxTensor attentionMaskTensor = OnnxTensor.createTensor(env, attentionMaskBuffer, inputShape);

        Map<String, OnnxTensor> inputs = new HashMap<>();

        inputs.put("input_ids", inputIdsTensor);
        inputs.put("attention_mask", attentionMaskTensor);

        OrtSession.Result results = session.run(inputs);
        float[][] logits = (float[][]) results.get(0).getValue();
        log.info("logits len = {}", logits[0].length);

        inputIdsTensor.close();
        attentionMaskTensor.close();
        return logits;
    }

    @Override
    public void close() throws OrtException {
        session.close();
        env.close();
    }
}
*/
