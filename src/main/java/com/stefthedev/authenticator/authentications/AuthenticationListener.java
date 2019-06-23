package com.stefthedev.authenticator.authentications;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Message;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AuthenticationListener implements Listener {

    private final Authenticator authenticator;
    private final AuthenticationHandler authenticationHandler;

    public AuthenticationListener(Authenticator authenticator) {
        this.authenticator = authenticator;
        this.authenticationHandler = authenticator.getAuthenticationHandler();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Authentication authentication = authenticationHandler.load(player.getUniqueId());

        if(authentication == null) {
            event.setJoinMessage("");
            authentication = new Authentication(player.getUniqueId(), null, false);
            authenticationHandler.add(authentication);

            if(player.hasPermission("authenticator.use")) {
                AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                        AuthenticationRequest.AuthenticationRequestAction.SETUP,
                        authentication,
                        player.getUniqueId()
                );
                authenticationRequest.send();
                authenticationHandler.add(authenticationRequest);
            }
            return;
        }

        if(authentication.isEnabled()) {
            event.setJoinMessage("");

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 1));
            player.sendMessage(Chat.format(Message.AUTHENTICATE.toString()));

            authenticationHandler.add(authentication);
            authenticationHandler.add(player.getUniqueId());

            authenticator.getServer().getScheduler().runTaskLater(authenticator, ()-> {
                if(authenticationHandler.getUuids().contains(player.getUniqueId())) {
                    player.kickPlayer(Chat.color(Message.AUTHENTICATION_KICK.toString()));
                }
            }, 20L * 30L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (authenticationHandler.getAuthentication(player.getUniqueId()) != null) {
            authenticationHandler.unload(event.getPlayer().getUniqueId());
            if(authenticationHandler.getAuthentication(player.getUniqueId()).isEnabled()) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(authenticationHandler.getUuids().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(authenticationHandler.getUuids().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(authenticationHandler.getUuids().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(authenticationHandler.getUuids().contains(player.getUniqueId())) {

            event.setCancelled(true);
            String message = event.getMessage();
            Authentication authentication = authenticationHandler.getAuthentication(player.getUniqueId());
            if(!StringUtils.isNumericSpace(message)){
                player.sendMessage(Chat.format(Message.AUTHENTICATION_FAILED.toString()));
                return;
            }

            GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
            if(googleAuthenticator.authorize(authentication.getKey(), Integer.parseInt(message))) {
                player.sendMessage(Chat.format(Message.AUTHENTICATED.toString()));
                authenticator.getServer().getScheduler().runTask(authenticator, ()-> player.removePotionEffect(PotionEffectType.BLINDNESS));
                authenticationHandler.remove(player.getUniqueId());
            } else {
                player.sendMessage(Chat.format(Message.AUTHENTICATION_FAILED.toString()));
            }
        }
    }
}
