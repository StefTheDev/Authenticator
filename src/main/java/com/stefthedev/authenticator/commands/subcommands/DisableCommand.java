package com.stefthedev.authenticator.commands.subcommands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.authentications.Authentication;
import com.stefthedev.authenticator.authentications.AuthenticationHandler;
import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Command;
import com.stefthedev.authenticator.utilities.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DisableCommand extends Command {

    private final Authenticator authenticator;
    private final AuthenticationHandler authenticationHandler;

    public DisableCommand(Authenticator authenticator) {
        super("disable");
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
                authentication.setEnabled(false);

                player.sendMessage(Chat.format(Message.AUTHENTICATION_DISABLE_OTHER.toString().replace("{0}", Objects.requireNonNull(offlinePlayer.getName()))));
            }
        } else {
            Authentication authentication = authenticationHandler.getAuthentication(player.getUniqueId());
            if(authentication.isEnabled()) {
                authentication.setEnabled(false);
                player.sendMessage(Chat.format(Message.AUTHENTICATION_DISABLE.toString()));
            } else {
                player.sendMessage(Chat.format(Message.AUTHENTICATION_DISABLED.toString()));
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
