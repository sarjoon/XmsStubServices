package com.uob.xms.validator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

  public void validateCommonHeader(JsonNode requestNode) {

    validate(requestNode, "FinIQRequestHeader.EntityCode");
    validate(requestNode, "FinIQRequestHeader.LoginID");
    validate(requestNode, "FinIQRequestHeader.SourceSystem");
    validate(requestNode, "FinIQRequestHeader.MachineIP");
    validate(requestNode, "FinIQRequestHeader.RequestID");
    validate(requestNode, "FinIQRequestHeader.RequestAt");
  }

  public void validate(JsonNode root, String path) {

    String[] keys = path.split("\\.");

    JsonNode current = root;

    for (String key : keys) {

      if (current == null || !current.has(key)) {
        throw new RuntimeException("Missing mandatory field : " + path);
      }

      current = current.get(key);
    }

    if (current.isNull() || (current.isTextual() && current.asText().trim().isEmpty())) {

      throw new RuntimeException("Empty mandatory field : " + path);
    }
  }
}
