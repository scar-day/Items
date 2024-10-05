package dev.scarday.plugin.handler.litecommands;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.scarday.plugin.config.Configuration;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static dev.scarday.plugin.util.ColorUtil.*;

@AllArgsConstructor
public class InvalidUsageHandler implements dev.rollczi.litecommands.invalidusage.InvalidUsageHandler<CommandSender> {
    Configuration configuration;
    @Override
    public void handle(Invocation<CommandSender> invocation,
                       InvalidUsage<CommandSender> result,
                       ResultHandlerChain<CommandSender> resultHandlerChain
    ) {
        val schematic = result.getSchematic();
        val messages = configuration.getMessages();

        val sender = invocation.sender();

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("only one subcommand is available from the console: 'reload'");
            return;
        }

        if (!schematic.isOnlyFirst()) {
            sender.sendMessage(colorizeList(messages.getCommands()));
        }
    }
}
