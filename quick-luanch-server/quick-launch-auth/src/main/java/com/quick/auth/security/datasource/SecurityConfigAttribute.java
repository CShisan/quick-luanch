package com.quick.auth.security.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collections;
import java.util.List;

/**
 * @author yuanbai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityConfigAttribute implements ConfigAttribute {
    private String path;
    private List<String> permissions;

    @Override
    public String getAttribute() {
        return path;
    }

    public static SecurityConfigAttribute invalid() {
        return new SecurityConfigAttribute("/invalid", Collections.singletonList("invalid"));
    }
}
