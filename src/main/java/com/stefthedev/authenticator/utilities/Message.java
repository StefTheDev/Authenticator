package com.stefthedev.authenticator.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public enum Message {
    AUTHENTICATE("authenticate", "Welcome to the server. Please enter &a2FA&7 code to authenticate!"),
    AUTHENTICATED("authenticated", "You have successfully been authenticated. Enjoy your stay."),
    AUTHENTICATION_DISABLE("authentication-disable", "You have disabled &a2FA&7."),
    AUTHENTICATION_DISABLED("authentication-disabled", "You already have &a2FA&7 disabled."),
    AUTHENTICATION_DISABLE_OTHER("authentication-disable-other", "You have disabled &a2FA&7 for &f{0}&7."),
    AUTHENTICATION_ENABLE("authentication-enable", "You have enabled &a2FA&7."),
    AUTHENTICATION_ENABLED("authentication-enabled", "You already have &a2FA&7 enabled."),
    AUTHENTICATION_ENABLED_OTHER("authentication-enable-other", "You have enabled &a2FA&7 for &f{0}&7."),
    AUTHENTICATION_FAILED("authentication-failed", "You failed to authenticate! Please enter a valid &a2FA&7 code."),
    AUTHENTICATION_KEY("authentication-key", "You can't enable &a2FA &7because you do not have a secret key assigned. Type &b/auth reset&7 to retrieve a secret key."),
    AUTHENTICATION_KEY_OTHER("authentication-key-other", "You can't enable &a2FA for &b{0}&7 because they do not have a secret key assigned."),
    AUTHENTICATION_KICK("authentication-kick", "&b&lAuthenticator\n\n&7You failed to enter the correct code in time.\n&7Please reconnect and try again."),
    FREFIX("prefix", "&b&lAuthenticator: &7"),
    HELP("help", "&b&lAuthenticator: &7Version {0}"),
    HELP_ITEM("help-item", "  &7- {0}"),
    OFFLINE_PLAYER_NULL("offline-player-null", "The player &f{0} &7has never played before."),
    REQUEST_ACCEPT("request-accept", "&a[Accept]"),
    REQUEST_DENY("request-deny", "&c[Deny]"),
    REQUEST_NULL("request-null", "&7You do not have any pending requests."),
    REQUEST_RESET("request-reset", "Are you sure you want to reset your key? Your old key will be overridden."),
    REQUEST_RESET_ACCEPT("request-reset-accept", "You have reset your &a2FA&y key. Please store your secret key safely: &b{0}"),
    REQUEST_RESET_DENY("request-reset-deny", "You have denied requesting to reset your &a2FA&7 key."),
    REQUEST_SETUP("request-setup", "Welcome to the server. To make your account more secure would you like to enable &a2FA&7?"),
    REQUEST_SETUP_ACCEPT("request-setup-accept", "You have enabled &a2FA&7. Do not log out! Please store your secret key safely: &b{0}"),
    REQUEST_SETUP_DENY("request-setup-deny", "You have denied enabling &a2FA&7. We hope you enjoy your experience.");

    private String path, def;
    private List<String> list;
    private static FileConfiguration configuration;

    Message(String path, String def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public String toString() {
        return Chat.color(configuration.getString(path, def));
    }

    public List<String> toList() {
        return configuration.getStringList(path);
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Message.configuration = configuration;
    }

    public String getPath() {
        return path;
    }

    public String getDef() {
        return def;
    }

    public List<String> getList() {
        return list;
    }
}
