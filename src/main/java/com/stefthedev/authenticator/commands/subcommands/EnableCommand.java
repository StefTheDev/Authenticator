package com.stefthedev.authenticator.commands.subcommands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.authentications.Authentication;
import com.stefthedev.authenticator.authentications.AuthenticationHandler;
import com.stefthedev.authenticator.authentications.AuthenticationRequest;
import com.stefthedev.authenticator.utilities.Command;
import org.bukkit.entity.Player;

public class EnableCommand extends Command {

    private final AuthenticationHandler authenticationHandler;

    public EnableCommand(Authenticator authenticator) {
        super("enable");
        this.authenticationHandler = authenticator.getAuthenticationHandler();
    }

    @Override
    public boolean run(Player player, String[] args) {
        Authentication authentication = authenticationHandler.getAuthentication(player);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                AuthenticationRequest.AuthenticationRequestAction.ENABLE,
                authentication,
                player.getUniqueId()
        );

        authenticationRequest.send();
        authenticationHandler.add(authenticationRequest);
        return false;
    }
}
