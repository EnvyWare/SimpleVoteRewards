package com.envyful.simple.vote.rewards.forge.listener;

import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
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

        voteRewards(player, this.mod);

        this.mod.setVoteCounter(this.mod.getVoteCounter() + 1);

        if (this.mod.getVoteCounter() < this.mod.getConfig().getVotePartyRequired()) {
            return;
        }

        this.mod.setVoteCounter(0);

        for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            for (String playerVotePartyCommand : this.mod.getConfig().getPlayerVotePartyCommands()) {
                UtilForgeServer.executeCommand(playerVotePartyCommand.replace("%player%", entityPlayerMP.getName()));
            }
        }

        for (String serverVotePartyCommand : this.mod.getConfig().getServerVotePartyCommands()) {
            UtilForgeServer.executeCommand(serverVotePartyCommand);
        }
    }

    public static void voteRewards(EntityPlayerMP player, SimpleVoteRewardsForge mod) {
        for (String rewardCommand : mod.getConfig().getRewardCommands()) {
            UtilForgeServer.executeCommand(rewardCommand.replace("%player%", player.getName()));
        }

        if (mod.getConfig().getLuckyVoteChance() > 0) {
            if (ThreadLocalRandom.current().nextDouble() < mod.getConfig().getLuckyVoteChance()) {
                for (String luckyVoteReward : mod.getConfig().getLuckyVoteRewards()) {
                    UtilForgeServer.executeCommand(luckyVoteReward.replace("%player%", player.getName()));
                }
            }
        }
    }
}
