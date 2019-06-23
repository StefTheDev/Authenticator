package com.stefthedev.authenticator.commands.subcommands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.authentications.AuthenticationHandler;
import com.stefthedev.authenticator.authentications.AuthenticationRequest;
import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Command;
import com.stefthedev.authenticator.utilities.Message;
import org.bukkit.entity.Player;

public class AcceptCommand extends Command {

    private final AuthenticationHandler authenticationHandler;

    public AcceptCommand(Authenticator authenticator) {
        super("accept");
        this.authenticationHandler = authenticator.getAuthenticationHandler();
    }

    @Override
    public boolean run(Player player, String[] args) {
        AuthenticationRequest authenticationRequest = authenticationHandler.getRequest(player.getUniqueId());
        if(authenticationRequest == null) {
            player.sendMessage(Chat.format(Message.REQUEST_NULL.toString()));
        } else {
            authenticationRequest.accept();
            authenticationHandler.remove(authenticationRequest.getAuthentication());
            authenticationHandler.add(authenticationRequest.getAuthentication());
            authenticationHandler.remove(authenticationRequest);
        }
        return false;
    }
}
