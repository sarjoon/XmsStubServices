package com.uob.xms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uob.xms.config.StubProperties;
import com.uob.xms.enums.TradeSide;
import com.uob.xms.service.CommonStubService;
import com.uob.xms.validator.BondPlaceOrderRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.basePath}")
public class BondPlaceOrderController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BondPlaceOrderController.class);

    private final CommonStubService commonStubService;
    private final BondPlaceOrderRequestValidator validator;
    private final ObjectMapper objectMapper;
    private final StubProperties stubProperties;

    public BondPlaceOrderController(
            CommonStubService commonStubService,
            BondPlaceOrderRequestValidator validator,
            ObjectMapper objectMapper,
            StubProperties stubProperties) {

        this.commonStubService = commonStubService;
        this.validator = validator;
        this.objectMapper = objectMapper;
        this.stubProperties = stubProperties;
    }

    @PostMapping("${api.endpoints.bondBooking}")
    public ResponseEntity<JsonNode> placeOrder(
            @RequestBody String request) throws Exception {

        LOGGER.info("Received Bond Place Order Request");

        /*
         * STEP-1
         * Convert incoming request payload into JsonNode
         */
        JsonNode requestNode = objectMapper.readTree(request);

        /*
         * STEP-2
         * Validate mandatory request fields
         */
        validator.validate(requestNode);

        /*
         * STEP-3
         * Read UnderlyingSecurityID from request
         */
        String underlyingSecurityId = requestNode
                .path("Order_Save_Req_DTO")
                .path("UnderlyingSecurityID")
                .asText();

        LOGGER.info(
                "Received UnderlyingSecurityID={}",
                underlyingSecurityId);

        /*
         * STEP-4
         * Error Flow
         *
         * Success only for 1801
         * All other values return
         * bond-place-order-error-response.json
         */
        if (!"1801".equals(underlyingSecurityId)) {

            LOGGER.info(
                    "Unsupported UnderlyingSecurityID={} Returning Error Stub Response",
                    underlyingSecurityId);

            JsonNode response =
                    commonStubService.getStubResponse(
                            requestNode,
                            stubProperties.getFiles()
                                    .getBondPlaceOrderError());

            return ResponseEntity.ok(response);
        }

        /*
         * STEP-5
         * Determine BUY / SELL flow
         */
        String sideValue = requestNode
                .path("Order_Save_Req_DTO")
                .path("Side")
                .asText();

        TradeSide side =
                TradeSide.valueOf(sideValue.toUpperCase());

        LOGGER.info(
                "Processing Bond Place Order SecurityId={} Side={}",
                underlyingSecurityId,
                side);

        /*
         * STEP-6
         * Return appropriate success stub
         *
         * BUY  -> bond-place-order-buy-success-response.json
         * SELL -> bond-place-order-sell-success-response.json
         */
        JsonNode response = switch (side) {

            case BUY -> commonStubService.getStubResponse(
                    requestNode,
                    stubProperties.getFiles()
                            .getBondPlaceOrderBuy());

            case SELL -> commonStubService.getStubResponse(
                    requestNode,
                    stubProperties.getFiles()
                            .getBondPlaceOrderSell());
        };

        /*
         * STEP-7
         * Return final response
         */
        return ResponseEntity.ok(response);
    }
}