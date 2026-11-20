package com.uob.xms.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.uob.xms.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class BondRequestValidator {

  public void validate(JsonNode requestNode) {

    // Header Validation
    validateField(requestNode, "FinIQRequestHeader.EntityCode");

    validateField(requestNode, "FinIQRequestHeader.LoginID");

    validateField(requestNode, "FinIQRequestHeader.SourceSystem");

    validateField(requestNode, "FinIQRequestHeader.MachineIP");

    validateField(requestNode, "FinIQRequestHeader.RequestID");

    validateField(requestNode, "FinIQRequestHeader.RequestAt");

    // Bond Validation
    validateField(requestNode, "BondEnquiryRequest.UnderlyingSecurityID");

    validateField(requestNode, "BondEnquiryRequest.UnderlyingSecurityIDSource");

    validateField(requestNode, "BondEnquiryRequest.Side");

    validateField(requestNode, "BondEnquiryRequest.ExpireTime");
  }

  private void validateField(JsonNode root, String path) {

    String[] fields = path.split("\\.");

    JsonNode current = root;

    for (String field : fields) {

      if (current == null || !current.has(field)) {

        throw new ValidationException("Missing mandatory field : " + path);
      }

      current = current.get(field);
    }

    if (current.isNull()) {

      throw new ValidationException("Null value for field : " + path);
    }

    if (current.isTextual() && current.asText().trim().isEmpty()) {

      throw new ValidationException("Empty value for field : " + path);
    }
  }
}
