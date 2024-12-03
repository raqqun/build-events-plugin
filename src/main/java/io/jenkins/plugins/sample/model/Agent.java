package io.jenkins.plugins.sample.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Agent {
    private String name;
    private Map<String, String> systemEnvVars;
}
