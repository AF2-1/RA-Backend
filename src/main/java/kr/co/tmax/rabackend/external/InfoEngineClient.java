package kr.co.tmax.rabackend.external;

import kr.co.tmax.rabackend.interfaces.info.InfoDto;
import kr.co.tmax.rabackend.interfaces.info.InfoDto.KeywordRequest;
import kr.co.tmax.rabackend.interfaces.info.InfoDto.SentenceRequest;
import org.springframework.http.ResponseEntity;

public interface InfoEngineClient {

    public ResponseEntity<Object> getKeywords(KeywordRequest keywordRequest);

    public ResponseEntity<Object> getSentences(SentenceRequest sentenceRequest);
}
