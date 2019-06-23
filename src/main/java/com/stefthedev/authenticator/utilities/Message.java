package com.stefthedev.authenticator.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public enum Message {
    AUTHENTICATE("authenticate", "Welcome to the server. Please enter 2FA code to authenticate!"),
    AUTHENTICATED("authenticated", "You have successfully been authenticated. Enjoy your stay."),
    AUTHENTICATION_FAILED("authentication-failed", "You failed to authenticate! Please enter a valid 2FA code."),
    AUTHENTICATION_KICK("authentication-kick", "&a&lAuthenticator\n\n&7You failed to enter the correct code in time.\n&7Please reconnect and try again."),
    FREFIX("prefix", "&a&lAuthenticator: &7"),
    HELP("help", "&a&lAuthenticator: &7Version {0}"),
    HELP_ITEM("help-item", "  &7- {0}"),
    REQUEST_ACCEPT("request-accept", "&a[Accept]"),
    REQUEST_DENY("request-deny", "&a[Deny]"),
    REQUEST_DISABLE("request-disable", "Are you sure you want to disable 2FA?"),
    REQUEST_DISABLE_ACCEPT("request-disable-accept", "You have decided to disable 2FA."),
    REQUEST_DISABLE_DENY("request-disable-deny", "You have decided to deny disabling 2FA."),
    REQUEST_ENABLE("request-enable", "Are you sure you want to enable 2FA? Make sure to keep your secret key safe!"),
    REQUEST_ENABLE_ACCEPT("request-enable-accept", "You have enabled 2FA. Please store your secret key safely: &b{0}"),
    REQUEST_ENABLE_DENY("request-enable-deny", "You have decided to deny enabling 2FA."),
    REQUEST_NULL("request-null", "&7You do not have any pending requests."),
    REQUEST_SETUP("request-setup", "Welcome to the server. To make your account more secure would you like to enable 2FA?"),
    REQUEST_SETUP_ACCEPT("request-setup-accept", "You have enabled 2FA. Do not log out! Please store your secret key safely: &b{0}"),
    REQUEST_SETUP_DENY("request-setup-deny", "You have denied enabling 2FA. We hope you enjoy your experience.");

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
