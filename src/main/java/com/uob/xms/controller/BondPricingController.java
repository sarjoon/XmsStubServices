package com.uob.xms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uob.xms.config.StubProperties;
import com.uob.xms.enums.TradeSide;
import com.uob.xms.service.CommonStubService;
import com.uob.xms.validator.BondRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.basePath}")
public class BondPricingController {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(BondPricingController.class);

  private final CommonStubService commonStubService;
  private final BondRequestValidator validator;
  private final ObjectMapper objectMapper;
  private final StubProperties stubProperties;

  public BondPricingController(
          CommonStubService commonStubService,
          BondRequestValidator validator,
          ObjectMapper objectMapper,
          StubProperties stubProperties) {

    this.commonStubService = commonStubService;
    this.validator = validator;
    this.objectMapper = objectMapper;
    this.stubProperties = stubProperties;
  }

  @PostMapping("${api.endpoints.bondPricing}")
  public ResponseEntity<JsonNode> getPricing(
          @RequestBody String request) throws Exception {

    LOGGER.info("Received Bond Pricing Request");

    JsonNode requestNode = objectMapper.readTree(request);

    validator.validate(requestNode);

    String underlyingSecurityId = requestNode
            .path("BondEnquiryRequest")
            .path("UnderlyingSecurityID")
            .asText();

    if (!"AAPL".equalsIgnoreCase(underlyingSecurityId)) {

      LOGGER.info(
              "Unsupported Bond UnderlyingSecurityID={} returning error response",
              underlyingSecurityId);

      JsonNode response =
              commonStubService.getStubResponse(
                      requestNode,
                      stubProperties.getFiles()
                              .getBondPricingError());

      return ResponseEntity.ok(response);
    }

    String sideValue = requestNode
            .path("BondEnquiryRequest")
            .path("Side")
            .asText();

    TradeSide side =
            TradeSide.valueOf(sideValue.toUpperCase());

    JsonNode response = switch (side) {

      case BUY -> commonStubService.getStubResponse(
              requestNode,
              stubProperties.getFiles()
                      .getBondPricingBuy());

      case SELL -> commonStubService.getStubResponse(
              requestNode,
              stubProperties.getFiles()
                      .getBondPricingSell());
    };

    return ResponseEntity.ok(response);
  }
}