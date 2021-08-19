package com.envyful.simple.vote.rewards.forge;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import com.envyful.simple.vote.rewards.forge.command.ReloadCommand;
import com.envyful.simple.vote.rewards.forge.config.SimpleVoteRewardsConfig;
import com.envyful.simple.vote.rewards.forge.listener.PlayerVoteListener;
import com.vexsoftware.votifier.sponge.NuVotifier;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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

    protected static final String VERSION = "0.2.0";

    private static SimpleVoteRewardsForge instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private SimpleVoteRewardsConfig config;

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

        ForgeUpdateBuilder.instance()
                .name("SimpleVoteRewards")
                .requiredPermission("simplevoterewards.update.notify")
                .owner("Pixelmon-Development")
                .repo("SimpleVoteRewards")
                .version(VERSION)
                .start();
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(SimpleVoteRewardsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartedEvent event) {
        commandFactory.registerCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), new ReloadCommand());

        Sponge.getEventManager().registerListeners(Sponge.getPluginManager().getPlugin("NuVotifier").get(), new PlayerVoteListener(this));
    }

    public static SimpleVoteRewardsForge getInstance() {
        return instance;
    }

    public SimpleVoteRewardsConfig getConfig() {
        return this.config;
    }
}
