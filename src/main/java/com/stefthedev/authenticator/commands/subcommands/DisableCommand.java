package com.stefthedev.authenticator.commands.subcommands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.authentications.Authentication;
import com.stefthedev.authenticator.authentications.AuthenticationHandler;
import com.stefthedev.authenticator.authentications.AuthenticationRequest;
import com.stefthedev.authenticator.utilities.Command;
import org.bukkit.entity.Player;

public class DisableCommand extends Command {

    private final AuthenticationHandler authenticationHandler;

    public DisableCommand(Authenticator authenticator) {
        super("disable");
        this.authenticationHandler = authenticator.getAuthenticationHandler();
    }

    @Override
    public boolean run(Player player, String[] args) {
        Authentication authentication = authenticationHandler.getAuthentication(player);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                AuthenticationRequest.AuthenticationRequestAction.DISABLE,
                authentication,
                player.getUniqueId()
        );

        authenticationRequest.send();
        authenticationHandler.add(authenticationRequest);

        return false;
    }
}
