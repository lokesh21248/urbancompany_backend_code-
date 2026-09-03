package com.urbanservices.backend.dto;

public class AppConfigDTO {
    private Long id;
    private String configKey;
    private String configValue;
    private String valueType;
    private String description;

    public AppConfigDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getValueType() { return valueType; }
    public void setValueType(String valueType) { this.valueType = valueType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
