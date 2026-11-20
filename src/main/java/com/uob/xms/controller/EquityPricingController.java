package com.uob.xms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uob.xms.config.StubProperties;
import com.uob.xms.enums.TradeSide;
import com.uob.xms.service.CommonStubService;
import com.uob.xms.util.ResponseHeaderUtil;
import com.uob.xms.validator.EquityRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.basePath}")
public class EquityPricingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EquityPricingController.class);

  private final CommonStubService commonStubService;
  private final EquityRequestValidator validator;
  private final ObjectMapper objectMapper;
  private final StubProperties stubProperties;

  public EquityPricingController(
      CommonStubService commonStubService,
      EquityRequestValidator validator,
      ObjectMapper objectMapper,
      StubProperties stubProperties) {

    this.commonStubService = commonStubService;
    this.validator = validator;
    this.objectMapper = objectMapper;
    this.stubProperties = stubProperties;
  }

  @PostMapping("${api.endpoints.equityPricing}")
  public ResponseEntity<JsonNode> getPricing(@RequestBody String request) throws Exception {

    LOGGER.info("Received Equity Pricing Request");

    JsonNode requestNode = objectMapper.readTree(request);

    validator.validate(requestNode);

    String underlyingSecurityId =
        requestNode.path("EquityEnquiryRequest").path("UnderlyingSecurityID").asText();

    /*
     * Success response only for AAPL.
     * Return error response for any other security.
     */
    if (!"AAPL".equalsIgnoreCase(underlyingSecurityId)) {

      LOGGER.info("Security {} not configured. Returning failure response.", underlyingSecurityId);

      ObjectNode responseNode = objectMapper.createObjectNode();

      responseNode.putObject("FinIQResponseHeader");

      ResponseHeaderUtil.populateResponseHeader(requestNode, responseNode);

      ObjectNode responseHeader = (ObjectNode) responseNode.get("FinIQResponseHeader");

      responseHeader.put("Status", "Failed");

      ObjectNode equityResponse = responseNode.putObject("EquityEnquiryResponse");

      ObjectNode responseDetails = equityResponse.putObject("ResponseDetails");

      responseDetails.put("Code", "1001");

      responseDetails.put(
          "Description", "Prices not available for security " + underlyingSecurityId);

      responseDetails.putNull("Remark");

      equityResponse.put("RFQID", "497350");
      equityResponse.putNull("AskPrice");
      equityResponse.putNull("BidPrice");
      equityResponse.putNull("LastUpdate");

      return ResponseEntity.ok(responseNode);
    }

    String sideValue = requestNode.path("EquityEnquiryRequest").path("Side").asText();

    TradeSide side = TradeSide.valueOf(sideValue.toUpperCase());

    LOGGER.info("Processing Equity Pricing for Security={} Side={}", underlyingSecurityId, side);

    JsonNode response =
        switch (side) {
          case BUY ->
              commonStubService.getStubResponse(
                  requestNode, stubProperties.getFiles().getEquityPricingBuy());

          case SELL ->
              commonStubService.getStubResponse(
                  requestNode, stubProperties.getFiles().getEquityPricingSell());
        };

    return ResponseEntity.ok(response);
  }
}
