package com.envyful.simple.vote.rewards.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import com.envyful.simple.vote.rewards.forge.command.ReloadCommand;
import com.envyful.simple.vote.rewards.forge.command.VotePartyCommand;
import com.envyful.simple.vote.rewards.forge.config.SimpleVoteRewardsConfig;
import com.envyful.simple.vote.rewards.forge.listener.PlayerVoteListener;
import com.vexsoftware.votifier.sponge.NuVotifier;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.bstats.forge.Metrics;
import org.spongepowered.api.Sponge;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;

@Mod(
        modid = "simplevoterewards",
        name = "SimpleVoteRewards Forge",
        version = SimpleVoteRewardsForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class SimpleVoteRewardsForge {

    protected static final String VERSION = "0.3.0";

    private static SimpleVoteRewardsForge instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private SimpleVoteRewardsConfig config;
    private int voteCounter = 0;

    @Mod.EventHandler
    public void onServerStarting(FMLPreInitializationEvent event) {
        instance = this;

        this.loadConfig();

        Metrics metrics = new Metrics(
                Loader.instance().activeModContainer(),
                event.getModLog(),
                Paths.get("config/"),
                12497
        );
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(SimpleVoteRewardsConfig.class);
            this.voteCounter = this.config.getNode().node("voteparty.votes").getInt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartedEvent event) {
        this.commandFactory.registerCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), new ReloadCommand());
        this.commandFactory.registerCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),
                                            new VotePartyCommand());

        Sponge.getEventManager().registerListeners(Sponge.getPluginManager().getPlugin("NuVotifier").get(), new PlayerVoteListener(this));
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        this.config.getNode().node("voteparty.votes", this.voteCounter);
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
}
