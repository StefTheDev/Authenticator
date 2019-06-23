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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    UUID getUuid() {
        return uuid;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
