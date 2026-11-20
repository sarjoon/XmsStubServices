package com.uob.xms.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.uob.xms.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class EquityRequestValidator {

  public void validate(JsonNode requestNode) {

    // FinIQ Header
    validateField(requestNode, "FinIQRequestHeader.EntityCode");

    validateField(requestNode, "FinIQRequestHeader.LoginID");

    validateField(requestNode, "FinIQRequestHeader.SourceSystem");

    validateField(requestNode, "FinIQRequestHeader.MachineIP");

    validateField(requestNode, "FinIQRequestHeader.RequestID");

    validateField(requestNode, "FinIQRequestHeader.RequestAt");

    // Equity Request
    validateField(requestNode, "EquityEnquiryRequest.UnderlyingSecurityID");

    validateField(requestNode, "EquityEnquiryRequest.UnderlyingSecurityIDSource");

    validateField(requestNode, "EquityEnquiryRequest.ExDestination");

    validateField(requestNode, "EquityEnquiryRequest.Side");

    validateField(requestNode, "EquityEnquiryRequest.Currency");

    String side = requestNode.path("EquityEnquiryRequest").path("Side").asText();

    if (!"BUY".equalsIgnoreCase(side) && !"SELL".equalsIgnoreCase(side)) {

      throw new ValidationException("Invalid Side value. Allowed values are BUY or SELL");
    }
  }

  private void validateField(JsonNode root, String path) {

    String[] keys = path.split("\\.");

    JsonNode current = root;

    for (String key : keys) {

      if (current == null || !current.has(key)) {

        throw new ValidationException("Missing mandatory field : " + path);
      }

      current = current.get(key);
    }

    if (current.isNull()) {

      throw new ValidationException("Null value for : " + path);
    }

    if (current.isTextual() && current.asText().trim().isEmpty()) {

      throw new ValidationException("Empty value for : " + path);
    }
  }
}
