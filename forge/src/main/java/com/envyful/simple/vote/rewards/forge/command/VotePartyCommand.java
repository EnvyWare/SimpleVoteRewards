package com.envyful.simple.vote.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "voteparty",
        description = "Vote party command",
        aliases = {
                "vp"
        }
)
@Permissible("simple.vote.rewards.command.vote.party")
public class VotePartyCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, String[] args) {
        for (String s : SimpleVoteRewardsForge.getInstance().getConfig().getVotePartyMessage()) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                    '&', s
                            .replace("%votes%", SimpleVoteRewardsForge.getInstance().getVoteCounter() + "")
                            .replace("%voteparty_total%",
                                     SimpleVoteRewardsForge.getInstance().getConfig().getVotePartyRequired() + ""))));
        }
    }
}
