package com.stefthedev.authenticator.authentications;

import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Message;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AuthenticationRequest {

    public enum AuthenticationRequestAction {
        DISABLE,
        ENABLE,
        SETUP
    }

    private final Authentication authentication;
    private final UUID uuid;
    private final AuthenticationRequestAction authenticationRequestAction;

    public AuthenticationRequest(AuthenticationRequestAction authenticationRequestAction, Authentication authentication, UUID uuid) {
        this.authenticationRequestAction = authenticationRequestAction;
        this.authentication = authentication;
        this.uuid = uuid;
    }

    public void send() {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null)return;

        TextComponent accept = new TextComponent(Chat.color(Message.REQUEST_ACCEPT.toString()) + " ");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/authenticator accept"));

        TextComponent deny = new TextComponent(Chat.color(Message.REQUEST_DENY.toString()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/authenticator deny"));

        switch (authenticationRequestAction) {
            case DISABLE: {
                player.sendMessage(Chat.format(Message.REQUEST_DISABLE.toString()));
                player.spigot().sendMessage(accept, deny);
            } break;
            case ENABLE: {
                player.sendMessage(Chat.format(Message.REQUEST_ENABLE.toString()));
                player.spigot().sendMessage(accept, deny);
            } break;
            case SETUP: {
                player.sendMessage(Chat.format(Message.REQUEST_SETUP.toString()));
                player.spigot().sendMessage(accept, deny);
            } break;
        }
    }

    public void accept() {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null)return;

        switch (authenticationRequestAction) {
            case DISABLE: {
                authentication.setEnabled(false);
                player.sendMessage(Chat.format(Message.REQUEST_DISABLE_ACCEPT.toString()));
            } break;
            case ENABLE: {

                GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
                GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                String key = googleAuthenticatorKey.getKey();
                authentication.setKey(key);
                authentication.setEnabled(true);

                player.sendMessage(Chat.format(Message.REQUEST_ENABLE_ACCEPT.toString().replace("{0}", key)));
            } break;
            case SETUP: {

                GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
                GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                String key = googleAuthenticatorKey.getKey();
                authentication.setKey(key);
                authentication.setEnabled(true);

                player.sendMessage(Chat.format(Message.REQUEST_SETUP_ACCEPT.toString().replace("{0}", key)));
            } break;
        }
    }

    public void deny() {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null)return;

        switch (authenticationRequestAction) {
            case DISABLE: {
                authentication.setEnabled(false);
                player.sendMessage(Chat.format(Message.REQUEST_DISABLE_DENY.toString()));
            } break;
            case ENABLE: {
                player.sendMessage(Chat.format(Message.REQUEST_ENABLE_DENY.toString()));
            } break;
            case SETUP: {
                authentication.setEnabled(false);
                player.sendMessage(Chat.format(Message.REQUEST_SETUP_DENY.toString()));
            } break;
        }
    }

    UUID getUuid() {
        return uuid;
    }
}
