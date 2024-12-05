package io.jenkins.plugins.buildevents.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stage {
    private String name;
    private String status;
}
