package com.stefthedev.authenticator.authentications;

import com.stefthedev.authenticator.Authenticator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class AuthenticationHandler {

    private final Authenticator authenticator;
    private final FileConfiguration fileConfiguration;

    private final Set<Authentication> authentications;
    private final Set<AuthenticationRequest> authenticationRequests;

    private final Set<UUID> uuids;

    public AuthenticationHandler(Authenticator authenticator) {
        this.authenticator = authenticator;
        this.fileConfiguration = authenticator.getConfig();

        this.authentications = new HashSet<>();
        this.authenticationRequests = new HashSet<>();

        this.uuids = new HashSet<>();
    }

    public Authentication load(UUID uuid) {
        if(fileConfiguration.get(uuid.toString()) == null) return null;
        String secretKey = new String(Base64.getDecoder().decode(Objects.requireNonNull(fileConfiguration.getString(uuid.toString() + ".key"))));
        boolean enabled = fileConfiguration.getBoolean(uuid.toString() + ".enabled");
        return new Authentication(uuid, secretKey, enabled);
    }

    void unload(UUID uuid) {
        Authentication authentication = getAuthentication(uuid);
        if (authentication == null) return;
        if(authentication.getKey() != null) {
            String secretKey = new String((Base64.getEncoder().encode(authentication.getKey().getBytes())));
            fileConfiguration.set(authentication.getUuid().toString() + ".key", secretKey);
        } else {
            fileConfiguration.set(authentication.getUuid().toString() + ".key", "");
        }

        fileConfiguration.set(authentication.getUuid().toString() + ".enabled", authentication.isEnabled());
        uuids.remove(uuid);
        authenticator.saveConfig();
    }

    public void unload() {
        authentications.forEach(authentication -> {
            if(authentication.getKey() != null) {
                String secretKey = new String((Base64.getEncoder().encode(authentication.getKey().getBytes())));
                fileConfiguration.set(authentication.getUuid().toString() + ".key", secretKey);
            } else {
                fileConfiguration.set(authentication.getUuid().toString() + ".key", "");
            }

            fileConfiguration.set(authentication.getUuid().toString() + ".enabled", authentication.isEnabled());
        });

        authenticator.saveConfig();
    }

    public void add(Authentication authentication) {
        authentications.add(authentication);
    }

    void add(UUID uuid) {
        uuids.add(uuid);
    }

    public void add(AuthenticationRequest authenticationRequest) {
        authenticationRequests.add(authenticationRequest);
    }

    public void remove(AuthenticationRequest authenticationRequest) {
        authenticationRequests.remove(authenticationRequest);
    }

    public void remove(Authentication authentication) {
        authentications.remove(authentication);
    }

    void remove(UUID uuid) {
        uuids.remove(uuid);
    }

    public AuthenticationRequest getRequest(UUID uuid) {
        for(AuthenticationRequest authenticationRequest : authenticationRequests) {
            if(authenticationRequest.getUuid().equals(uuid)) {
                return authenticationRequest;
            }
        }
        return null;
    }

    public Authentication getAuthentication(UUID uuid) {
        for(Authentication authentication : authentications) {
            if(authentication.getUuid().equals(uuid)) {
                return authentication;
            }
        }
        return null;
    }

    Set<UUID> getUuids() {
        return Collections.unmodifiableSet(uuids);
    }
}
