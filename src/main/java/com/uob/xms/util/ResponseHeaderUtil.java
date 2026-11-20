package com.uob.xms.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class ResponseHeaderUtil {

  private ResponseHeaderUtil() {}

  public static void populateResponseHeader(JsonNode requestNode, ObjectNode responseNode) {

    JsonNode requestHeader = requestNode.get("FinIQRequestHeader");

    ObjectNode responseHeader = (ObjectNode) responseNode.get("FinIQResponseHeader");

    responseHeader.put("RequestID", requestHeader.path("RequestID").asText());

    responseHeader.put("MachineIP", requestHeader.path("MachineIP").asText());

    responseHeader.put(
        "ResponseAt",
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")));
  }
}
