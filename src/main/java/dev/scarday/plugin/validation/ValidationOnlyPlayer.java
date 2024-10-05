package dev.scarday.plugin.validation;

import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorResult;
import dev.scarday.plugin.annotation.OnlyPlayer;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValidationOnlyPlayer implements AnnotatedValidator<CommandSender, CommandSender, OnlyPlayer> {
    @Override
    public ValidatorResult validate(Invocation<CommandSender> invocation,
                                    CommandExecutor<CommandSender> commandExecutor,
                                    Requirement<CommandSender> requirement, CommandSender commandSender,
                                    OnlyPlayer onlyPlayer) {
        val sender = invocation.sender();

        if (!(sender instanceof Player)) {
            return ValidatorResult.invalid("This subcommand/command works only for the player!");
        }

        return ValidatorResult.valid();
    }
}
