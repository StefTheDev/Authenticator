package com.stefthedev.authenticator.authentications;

import java.util.UUID;

public class Authentication {

    private final UUID uuid;
    private String key;
    private boolean enabled;

    Authentication(UUID uuid, String key, boolean enabled) {
        this.uuid = uuid;
        this.key = key;
        this.enabled = enabled;
    }

    void setKey(String key) {
        this.key = key;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    UUID getUuid() {
        return uuid;
    }

    String getKey() {
        return key;
    }

    boolean isEnabled() {
        return enabled;
    }
}
