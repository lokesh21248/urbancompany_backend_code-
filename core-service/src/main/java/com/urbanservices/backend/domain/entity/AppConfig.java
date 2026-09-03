package com.urbanservices.backend.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_config")
public class AppConfig extends BaseEntity {

    @Column(name = "config_key", nullable = false, unique = true, length = 200)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "value_type", nullable = false, length = 30)
    private String valueType = "STRING";

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_sensitive", nullable = false)
    private Boolean isSensitive = false;

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getValueType() { return valueType; }
    public void setValueType(String valueType) { this.valueType = valueType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getSensitive() { return isSensitive; }
    public void setSensitive(Boolean sensitive) { isSensitive = sensitive; }
}
