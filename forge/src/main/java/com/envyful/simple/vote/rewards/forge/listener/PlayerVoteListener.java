package com.envyful.simple.vote.rewards.forge.listener;

import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.forge.server.UtilForgeServer;
import com.envyful.simple.vote.rewards.forge.SimpleVoteRewardsForge;
import com.envyful.simple.vote.rewards.forge.config.VoteCache;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.api.event.Listener;

import java.util.List;
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
            SimpleVoteRewardsForge.addCache(event.getVote().getUsername(), event.getVote().getServiceName());
            return;
        }

        voteRewards(player, this.mod, event.getVote().getServiceName());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        List<VoteCache> cachedVotes = SimpleVoteRewardsForge.getCache((EntityPlayerMP) event.player);

        for (VoteCache cachedVote : cachedVotes) {
            voteRewards((EntityPlayerMP) event.player, this.mod, cachedVote.getService());
        }

        SimpleVoteRewardsForge.removeCachedVotes(cachedVotes);
    }

    public static void voteRewards(EntityPlayerMP player, SimpleVoteRewardsForge mod, String serviceName) {
        for (String rewardCommand : mod.getConfig().getRewardCommands(serviceName)) {
            UtilForgeServer.executeCommand(rewardCommand.replace("%player%", player.getName()).replace("%service%", serviceName));
        }

        if (mod.getConfig().getLuckyVoteChance() > 0) {
            if (ThreadLocalRandom.current().nextDouble() < mod.getConfig().getLuckyVoteChance()) {
                for (String luckyVoteReward : mod.getConfig().getLuckyVoteRewards()) {
                    UtilForgeServer.executeCommand(luckyVoteReward.replace("%player%", player.getName()));
                }
            }
        }

        SimpleVoteRewardsForge.getInstance().setVoteCounter(SimpleVoteRewardsForge.getInstance().getVoteCounter() + 1);

        if (SimpleVoteRewardsForge.getInstance().getVoteCounter() < SimpleVoteRewardsForge.getInstance().getConfig().getVotePartyRequired()) {
            return;
        }

        SimpleVoteRewardsForge.getInstance().setVoteCounter(0);

        for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            for (String playerVotePartyCommand : SimpleVoteRewardsForge.getInstance().getConfig().getPlayerVotePartyCommands()) {
                UtilForgeServer.executeCommand(playerVotePartyCommand.replace("%player%", entityPlayerMP.getName()));
            }
        }

        for (String serverVotePartyCommand : SimpleVoteRewardsForge.getInstance().getConfig().getServerVotePartyCommands()) {
            UtilForgeServer.executeCommand(serverVotePartyCommand);
        }
    }
}
