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
        RESET,
        SETUP
    }

    private  Authentication authentication;
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
            case RESET: {
                player.sendMessage(Chat.format(Message.REQUEST_RESET.toString()));
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

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        switch (authenticationRequestAction) {
            case RESET: {
                GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                authentication.setKey(googleAuthenticatorKey.getKey());
                authentication.setEnabled(true);

                player.sendMessage(Chat.format(Message.REQUEST_RESET_ACCEPT.toString().replace("{0}", googleAuthenticatorKey.getKey())));
            } break;
            case SETUP: {
                GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

                authentication.setKey(googleAuthenticatorKey.getKey());
                authentication.setEnabled(true);

                player.sendMessage(Chat.format(Message.REQUEST_SETUP_ACCEPT.toString().replace("{0}", googleAuthenticatorKey.getKey())));
            } break;
        }
    }

    public void deny() {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null)return;

        switch (authenticationRequestAction) {
            case RESET: {
                authentication.setEnabled(false);
                player.sendMessage(Chat.format(Message.REQUEST_RESET_DENY.toString()));
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

    public Authentication getAuthentication() {
        return authentication;
    }
}
