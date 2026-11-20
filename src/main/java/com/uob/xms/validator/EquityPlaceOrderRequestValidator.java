package com.uob.xms.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.uob.xms.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class EquityPlaceOrderRequestValidator {

  public void validate(JsonNode requestNode) {

    // FinIQ Header
    validateField(requestNode, "FinIQRequestHeader.EntityCode");

    validateField(requestNode, "FinIQRequestHeader.LoginID");

    validateField(requestNode, "FinIQRequestHeader.SourceSystem");

    validateField(requestNode, "FinIQRequestHeader.MachineIP");

    validateField(requestNode, "FinIQRequestHeader.RequestID");

    validateField(requestNode, "FinIQRequestHeader.RequestAt");

    // Order Request
    validateField(requestNode, "Order_Save_Req_DTO.TapOrderID");

    validateField(requestNode, "Order_Save_Req_DTO.TradeDate");

    validateField(requestNode, "Order_Save_Req_DTO.UnderlyingSecurityID");

    validateField(requestNode, "Order_Save_Req_DTO.UnderlyingSecurityIDSource");

    validateField(requestNode, "Order_Save_Req_DTO.ExDestination");

    validateField(requestNode, "Order_Save_Req_DTO.Side");

    validateField(requestNode, "Order_Save_Req_DTO.OrderQty");

    validateField(requestNode, "Order_Save_Req_DTO.OrdType");

    validateField(requestNode, "Order_Save_Req_DTO.Currency");

    validateField(requestNode, "Order_Save_Req_DTO.Price");

    validateField(requestNode, "Order_Save_Req_DTO.SettlCurrency");

    validateField(requestNode, "Order_Save_Req_DTO.TimeInForce");

    validateField(requestNode, "Order_Save_Req_DTO.UserGroup");

    validateField(requestNode, "Order_Save_Req_DTO.RMID");

    validateField(requestNode, "Order_Save_Req_DTO.UUID");

    validateField(requestNode, "Order_Save_Req_DTO.ROID");

    validateField(requestNode, "Order_Save_Req_DTO.TAPPortfolioID");

    validateField(requestNode, "Order_Save_Req_DTO.StrategyYN");

    String side = requestNode.path("Order_Save_Req_DTO").path("Side").asText();

    if (!"BUY".equalsIgnoreCase(side) && !"SELL".equalsIgnoreCase(side)) {

      throw new ValidationException("Side must be BUY or SELL");
    }
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
