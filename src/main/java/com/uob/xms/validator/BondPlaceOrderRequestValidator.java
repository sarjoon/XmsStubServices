package com.uob.xms.validator;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class BondPlaceOrderRequestValidator {

    public void validate(JsonNode requestNode) {

        if (!requestNode.has("Order_Save_Req_DTO")) {
            throw new IllegalArgumentException(
                    "Order_Save_Req_DTO is mandatory");
        }
    }
}
