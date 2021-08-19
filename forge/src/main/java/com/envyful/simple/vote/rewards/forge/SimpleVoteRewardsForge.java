package com.envyful.simple.vote.rewards.forge;

import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.concurrency.ForgeUpdateBuilder;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.bstats.forge.Metrics;

import java.nio.file.Paths;

@Mod(
        modid = "simplevoterewards",
        name = "SimpleVoteRewards Forge",
        version = SimpleVoteRewardsForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class SimpleVoteRewardsForge {

    protected static final String VERSION = "0.1.0";

    private static SimpleVoteRewardsForge instance;

    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

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

    private void loadConfig() {

    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    public static SimpleVoteRewardsForge getInstance() {
        return instance;
    }
}
