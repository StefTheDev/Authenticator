package com.stefthedev.authenticator.commands;

import com.stefthedev.authenticator.Authenticator;
import com.stefthedev.authenticator.utilities.Chat;
import com.stefthedev.authenticator.utilities.Command;
import com.stefthedev.authenticator.utilities.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class AuthenticatorCommand extends Command implements TabCompleter {

    private final Authenticator authenticator;
    private Set<Command> commands;

    public AuthenticatorCommand(Authenticator authenticator) {
        super("authenticator");
        this.authenticator = authenticator;
        this.commands = new HashSet<>();
    }

    public boolean run(Player player, String[] args) {
        if(args.length > 0) {
            commands.forEach(command -> {
                if(args[0].equalsIgnoreCase(command.toString())) {
                    if(player.hasPermission(toString() + "." + command.toString()) || player.hasPermission(toString() + ".*")) {
                        command.run(player, args);
                    }
                }
            });
        } else {

            player.sendMessage(Chat.color(Message.HELP.toString().replace("{0}", authenticator.getDescription().getVersion())));
            commands.forEach(command -> player.sendMessage(
                    Message.HELP_ITEM.toString().replace("{0}", command.toString())
            ));

        }
        return false;
    }

    public void initialise(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if(strings.length == 1) {
            commands.forEach(subCommand -> list.add(subCommand.toString()));
        }
        return list;
    }
}
