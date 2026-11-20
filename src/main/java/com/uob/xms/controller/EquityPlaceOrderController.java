package com.uob.xms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uob.xms.config.StubProperties;
import com.uob.xms.enums.TradeSide;
import com.uob.xms.service.CommonStubService;
import com.uob.xms.validator.EquityPlaceOrderRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.basePath}")
public class EquityPlaceOrderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EquityPlaceOrderController.class);

  private final CommonStubService commonStubService;
  private final EquityPlaceOrderRequestValidator validator;
  private final ObjectMapper objectMapper;
  private final StubProperties stubProperties;

  public EquityPlaceOrderController(
      CommonStubService commonStubService,
      EquityPlaceOrderRequestValidator validator,
      ObjectMapper objectMapper,
      StubProperties stubProperties) {

    this.commonStubService = commonStubService;
    this.validator = validator;
    this.objectMapper = objectMapper;
    this.stubProperties = stubProperties;
  }

  @PostMapping("${api.endpoints.equityBooking}")
  public ResponseEntity<JsonNode> placeOrder(@RequestBody String request) throws Exception {

    LOGGER.info("Received Equity Place Order Request");

    JsonNode requestNode = objectMapper.readTree(request);

    validator.validate(requestNode);

    String underlyingSecurityId =
        requestNode.path("Order_Save_Req_DTO").path("UnderlyingSecurityID").asText();

    /*
     * Success only for 1801
     */
    if (!"1801".equals(underlyingSecurityId)) {

      LOGGER.info(
          "Unsupported UnderlyingSecurityID : {}. Returning error response.", underlyingSecurityId);

      JsonNode response =
          commonStubService.getStubResponse(
              requestNode, stubProperties.getFiles().getEquityPlaceOrderError());

      return ResponseEntity.ok(response);
    }

    String sideValue = requestNode.path("Order_Save_Req_DTO").path("Side").asText();

    TradeSide side = TradeSide.valueOf(sideValue.toUpperCase());

    LOGGER.info(
        "Processing Equity Place Order for Security={} Side={}", underlyingSecurityId, side);

    JsonNode response =
        switch (side) {
          case BUY ->
              commonStubService.getStubResponse(
                  requestNode, stubProperties.getFiles().getEquityPlaceOrderBuy());

          case SELL ->
              commonStubService.getStubResponse(
                  requestNode, stubProperties.getFiles().getEquityPlaceOrderSell());
        };

    return ResponseEntity.ok(response);
  }
}
