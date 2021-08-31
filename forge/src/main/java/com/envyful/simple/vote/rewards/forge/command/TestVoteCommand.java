package com.envyful.simple.vote.rewards.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.concurrency.UtilForgeConcurrency;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import com.envyful.simple.vote.rewards.forge.listener.PlayerVoteListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "testvote",
        description = "Tests the configuration values",
        aliases = {
                "fakevote"
        }
)
@Permissible("simple.vote.rewards.command.test.vote")
public class TestVoteCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("Please specify a target for the fake vote."));
            return;
        }

        EntityPlayerMP player = UtilPlayer.findByName(args[0]);

        if (player == null) {
            sender.sendMessage(new TextComponentString("Please specify a target for the fake vote."));
            return;
        }

        UtilForgeConcurrency.runSync(() -> {
            PlayerVoteListener.voteRewards(player, SimpleVoteRewardsForge.getInstance());
        });

    }
}
