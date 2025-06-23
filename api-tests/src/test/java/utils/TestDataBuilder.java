package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class TestDataBuilder {
    private Map<String, Object> data;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TestDataBuilder() {
        data = new HashMap<>();
        data.put("completed", false);
    }

    public static TestDataBuilder createTask() {
        return new TestDataBuilder();
    }

    public TestDataBuilder withTitle(String title) {
        data.put("title", title);
        return this;
    }

    public TestDataBuilder withDescription(String description) {
        data.put("description", description);
        return this;
    }

    public TestDataBuilder withCompleted(boolean completed) {
        data.put("completed", completed);
        return this;
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JSON", e);
        }
    }
}