package com.envyful.simple.vote.rewards.forge.listener;

import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerVoteListener {

    private final SimpleVoteRewardsForge mod;

    public PlayerVoteListener(SimpleVoteRewardsForge mod) {
        this.mod = mod;
    }

    @Listener
    public void onPlayerVote(VotifierEvent event) {
        EntityPlayerMP player = UtilPlayer.findByName(event.getVote().getUsername());

        if (player == null) {
            return;
        }

        for (String rewardCommand : this.mod.getConfig().getRewardCommands()) {
            UtilForgeServer.executeCommand(rewardCommand.replace("%player%", player.getName()));
        }

        if (this.mod.getConfig().getLuckyVoteChance() > 0) {
            if (ThreadLocalRandom.current().nextDouble() < this.mod.getConfig().getLuckyVoteChance()) {
                for (String luckyVoteReward : this.mod.getConfig().getLuckyVoteRewards()) {
                    UtilForgeServer.executeCommand(luckyVoteReward.replace("%player%", player.getName()));
                }
            }
        }
    }
}
