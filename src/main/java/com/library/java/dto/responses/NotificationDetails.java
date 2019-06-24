package com.library.java.dto.responses;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder(toBuilder = true)
@Value
public class NotificationDetails {
    private final Map<String,Object> templateParam;
    private final String recipient;
    private final String templateName;
    private final String subject;
}
