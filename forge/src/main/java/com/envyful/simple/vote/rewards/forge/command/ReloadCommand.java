package com.envyful.simple.vote.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "simplevoterewards",
        description = "Reloads the simple vote rewards config",
        aliases = {
        "svreload"
        }
)
@Permissible("simple.vote.rewards.command.reload")
@SubCommands({
        TestVoteCommand.class
})
public class ReloadCommand {

    @CommandProcessor
    public void runReload(@Sender ICommandSender sender, String[] args) {
        SimpleVoteRewardsForge.getInstance().loadConfig();
        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&', "&bReloaded config.")));
    }
}
