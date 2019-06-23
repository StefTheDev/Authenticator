package com.stefthedev.authenticator.commands.subcommands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.authentications.Authentication;
import com.stefthedev.authenticator.authentications.AuthenticationHandler;
import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Command;
import com.stefthedev.authenticator.utilities.Message;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class EnableCommand extends Command {

    private final Authenticator authenticator;
    private final AuthenticationHandler authenticationHandler;

    public EnableCommand(Authenticator authenticator) {
        super("enable");
        this.authenticator = authenticator;
        this.authenticationHandler = authenticator.getAuthenticationHandler();
    }

    @Override
    public boolean run(Player player, String[] args) {
        if(args.length == 2 && player.hasPermission("authenticator.admin.disable")) {
            String string = args[1];
            OfflinePlayer offlinePlayer = getOfflinePlayer(string);

            if(offlinePlayer == null) {
                player.sendMessage(Chat.format(Message.OFFLINE_PLAYER_NULL.toString().replace("{0}", string)));
            } else {
                Authentication authentication = authenticationHandler.load(offlinePlayer.getUniqueId());
                if(authentication.getKey() == null) {
                    player.sendMessage(Chat.format(Message.AUTHENTICATION_KEY_OTHER.toString().replace("{0}", Objects.requireNonNull(offlinePlayer.getName()))));
                } else {
                    authentication.setEnabled(true);
                    player.sendMessage(Chat.format(Message.AUTHENTICATION_ENABLED_OTHER.toString().replace("{0}", Objects.requireNonNull(offlinePlayer.getName()))));
                }
            }
        } else {
            Authentication authentication = authenticationHandler.getAuthentication(player.getUniqueId());
            if(!authentication.isEnabled()) {
                if(authentication.getKey() == null) {
                    player.sendMessage(Chat.format(Message.AUTHENTICATION_KEY.toString()));
                } else {
                    authentication.setEnabled(true);
                    player.sendMessage(Chat.format(Message.AUTHENTICATION_ENABLE.toString()));
                }
            } else {
                player.sendMessage(Chat.format(Message.AUTHENTICATION_ENABLED.toString()));
            }
        }
        return false;
    }

    private OfflinePlayer getOfflinePlayer(String name) {
        for(OfflinePlayer offlinePlayer : authenticator.getServer().getOfflinePlayers()) {
            if(Objects.requireNonNull(offlinePlayer.getName()).equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }
}
