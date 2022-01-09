package com.envyful.simple.vote.rewards.forge;

import com.envyful.api.config.util.UtilConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.simple.vote.rewards.forge.command.ReloadCommand;
import com.envyful.simple.vote.rewards.forge.command.VotePartyCommand;
import com.envyful.simple.vote.rewards.forge.config.SimpleVoteRewardsConfig;
import com.envyful.simple.vote.rewards.forge.config.VoteCache;
import com.envyful.simple.vote.rewards.forge.listener.PlayerVoteListener;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Mod(
        modid = "simplevoterewards",
        name = "SimpleVoteRewards Forge",
        version = SimpleVoteRewardsForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class SimpleVoteRewardsForge {

    protected static final String VERSION = "0.7.0";

    private static SimpleVoteRewardsForge instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private SimpleVoteRewardsConfig config;
    private int voteCounter = 0;
    private List<VoteCache> cachedVotes = Lists.newArrayList();

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        instance = this;

        this.loadConfig();
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(SimpleVoteRewardsConfig.class);
            this.voteCounter = this.config.getNode().node("voteparty", "votes").getInt(0);
            List<VoteCache> voteCache = Lists.newArrayList();

            for (String s : UtilConfig.getList(this.config.getNode(), String.class, "vote", "cache")) {
                voteCache.add(VoteCache.deserialize(s));
            }

            this.cachedVotes = voteCache;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartedEvent event) {
        this.commandFactory.registerCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), new ReloadCommand());
        this.commandFactory.registerCommand(
                FMLCommonHandler.instance().getMinecraftServerInstance(),
                new VotePartyCommand()
        );

        Sponge.getEventManager().registerListeners(Sponge.getPluginManager().getPlugin("NuVotifier").get(), new PlayerVoteListener(this));
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        try {
            this.config.getNode().node("voteparty", "votes").set(this.voteCounter);
            List<String> cachedVotes = Lists.newArrayList();
            for (VoteCache cachedVote : this.cachedVotes) {
                cachedVotes.add(cachedVote.serialize());
            }
            this.config.getNode().node("vote", "cache").set(cachedVotes);
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        this.config.save();
    }

    public static SimpleVoteRewardsForge getInstance() {
        return instance;
    }

    public SimpleVoteRewardsConfig getConfig() {
        return this.config;
    }

    public int getVoteCounter() {
        return this.voteCounter;
    }

    public void setVoteCounter(int voteCounter) {
        this.voteCounter = voteCounter;
    }

    public List<VoteCache> getCachedVotes(EntityPlayerMP player) {
        List<VoteCache> voteCache = Lists.newArrayList();

        for (VoteCache cachedVote : this.cachedVotes) {
            if (Objects.equals(cachedVote.getUsername(), player.getName())) {
                voteCache.add(cachedVote);
            }
        }

        return voteCache;
    }

    public static List<VoteCache> getCache(EntityPlayerMP player) {
        return SimpleVoteRewardsForge.getInstance().getCachedVotes(player);
    }

    public static void addCache(String name, String service) {
        SimpleVoteRewardsForge.getInstance().cachedVotes.add(new VoteCache(name, service));
    }

    public static void removeCachedVotes(List<VoteCache> voteCaches) {
        SimpleVoteRewardsForge.getInstance().cachedVotes.removeAll(voteCaches);
    }
}