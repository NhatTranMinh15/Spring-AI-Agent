package com.agent_java.mcp_server.service;

import com.agent_java.mcp_server.utils.Constant;
import com.agent_java.mcp_server.viewmodel.GoogleSearchResponseVm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SearchOnlineTool {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public SearchOnlineTool(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    private final Logger logger = LoggerFactory.getLogger(SearchOnlineTool.class);
    
    @Value("${google.api.key}")
    private String apiKey;
    @Value("${google.search.engine.id}")
    private String searchEngineID;
    @Value("${google.search.endpoint}")
    private String endpoint;
    @Value("${google.search.sort}")
    private String sortParam;
    
    @Tool(description = """
        Search the web for up-to-date information.
        ALWAYS use this tool when the user asks about current events, real-time data, or information like prices, weather, news, etc.
    """
    )
    public ToolResponseMessage.ToolResponse searchOnline(String query) {
        logger.debug("searchOnline tool called");
        var resultToolName = "Result of searchOnline tool";
        var results = callGoogleSearchAPI(query);
        if (results.isEmpty()) {
            return new ToolResponseMessage.ToolResponse(
                    UUID.randomUUID().toString(),
                    resultToolName,
                    "No relevant results found."
            );
        }
        StringBuilder summary = new StringBuilder();
        for (GoogleSearchResponseVm item : results) {
            String title = item.title() != null ? item.title() : "No title";
            String snippet = item.snippet() != null ? item.snippet() : "No snippet";
            String link = item.link() != null ? item.link() : "";
            summary.append("**").append(title).append("**\n").append(snippet).append("\n").append(link).append("\n\n");
        }
        String formattedAnswer = summary
                .insert(0, "Search results for \"" + query + "\":\n\n")
                .append("\n\n(Source: Google Search)")
                .toString();
        return new ToolResponseMessage.ToolResponse(
                UUID.randomUUID().toString(),
                resultToolName,
                formattedAnswer
        );
    }
    
    public List<GoogleSearchResponseVm> callGoogleSearchAPI(String query) {
        var builder = UriComponentsBuilder
                .fromUriString(endpoint)
                .queryParam("key", apiKey)
                .queryParam("cx", searchEngineID)
                .queryParam("q", query)
                .queryParam("num", Constant.NUMBER_RESULT_TAKEN);
        if (!sortParam.isBlank()) {
            builder.queryParam("sort", sortParam);
        }
        var uri = builder.build().toUri();
        try {
            var response = restTemplate.getForObject(uri, Map.class);
            if (response == null) {
                return List.of();
            }
            var items = response.get("items");
            if (items != null) {
                return objectMapper.convertValue(items, List.class);
            }
        } catch (IllegalArgumentException ex) {
            logger.error("Unexpected error while jackson converting", ex);
        } catch (HttpClientErrorException ex) {
            logger.error("HTTP error from Google API: ${ex.statusCode} - ${ex.responseBodyAsString}", ex);
        } catch (ResourceAccessException ex) {
            logger.error("Network error when calling Google API", ex);
        } catch (NullPointerException ex) {
            logger.error("Response doesn't contain key", ex);
        } catch (RestClientException ex) {
            logger.error("Error while calling Google Search API", ex);
        }
        return List.of();
    }
}
