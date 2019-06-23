package com.stefthedev.authenticator.authentications;

import com.stefthedev.authenticator.Authenticator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    Authentication load(Player player) {
        ConfigurationSection section = fileConfiguration.getConfigurationSection("");
        if(section == null) return null;
        for(String string : section.getKeys(false)) {
            if (string.equalsIgnoreCase(player.getUniqueId().toString())) {
                String key = fileConfiguration.getString(string + ".key");
                boolean enabled = fileConfiguration.getBoolean(string + ".enabled");
                return new Authentication(player.getUniqueId(), key, enabled);
            }
        }
        return null;
    }

    void unload(Player player) {
        Authentication authentication = getAuthentication(player);
        if (authentication == null) return;
        fileConfiguration.set(authentication.getUuid().toString() + ".key", authentication.getKey());
        fileConfiguration.set(authentication.getUuid().toString() + ".enabled", authentication.isEnabled());
        uuids.remove(player.getUniqueId());
        authenticator.saveConfig();
    }

    public void unload() {
        authentications.forEach(authentication -> {
            fileConfiguration.set(authentication.getUuid().toString() + ".key", authentication.getKey());
            fileConfiguration.set(authentication.getUuid().toString() + ".enabled", authentication.isEnabled());
        });

        authenticator.saveConfig();
    }

    void add(Authentication authentication) {
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

    public AuthenticationRequest getRequest(Player player) {
        for(AuthenticationRequest authenticationRequest : authenticationRequests) {
            if(authenticationRequest.getUuid().equals(player.getUniqueId())) {
                return authenticationRequest;
            }
        }
        return null;
    }

    public Authentication getAuthentication(Player player) {
        for(Authentication authentication : authentications) {
            if(authentication.getUuid().equals(player.getUniqueId())) {
                return authentication;
            }
        }
        return null;
    }

    Set<UUID> getUuids() {
        return Collections.unmodifiableSet(uuids);
    }
}
