package com.uob.xms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uob.xms.util.ResponseHeaderUtil;
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CommonStubService {

  private final ObjectMapper objectMapper;

  public CommonStubService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JsonNode getStubResponse(JsonNode requestNode, String filePath) throws Exception {

    InputStream inputStream = new ClassPathResource(filePath).getInputStream();

    ObjectNode responseNode = (ObjectNode) objectMapper.readTree(inputStream);

    ResponseHeaderUtil.populateResponseHeader(requestNode, responseNode);

    return responseNode;
  }
}