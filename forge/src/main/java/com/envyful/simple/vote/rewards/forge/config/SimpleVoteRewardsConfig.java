package com.envyful.simple.vote.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigPath("config/SimpleVoteRewards/config.yml")
@ConfigSerializable
public class SimpleVoteRewardsConfig extends AbstractYamlConfig {

    private int votePartyRequired = 100;
    private double luckyVotePercentage = 0.15;

    private List<String> votePartyMessage = Lists.newArrayList(
            "&e&l(!) &e%votes%&7/&c%voteparty_total% for the next vote party!"
    );

    private List<String> luckyVoteRewards = Lists.newArrayList(
            "give %player% diamond 10"
    );

    private List<String> rewardCommands = Lists.newArrayList(
            "give %player% diamond 1"
    );

    private List<String> serverVotePartyCommands = Lists.newArrayList(
            "broadcast HEY"
    );

    private List<String> playerVotePartyCommands = Lists.newArrayList(
            "give %player% minecraft:diamond 1"
    );

    private Map<String, List<String>> serviceSpecificRewards = ImmutableMap.of(
            "minecraftserverlist", Lists.newArrayList("broadcast hello %service%")
    );

    public SimpleVoteRewardsConfig() {
        super();
    }

    public List<String> getVotePartyMessage() {
        return this.votePartyMessage;
    }

    public List<String> getRewardCommands(String serviceName) {
        if (this.serviceSpecificRewards.containsKey(serviceName.toLowerCase())) {
            return this.serviceSpecificRewards.get(serviceName.toLowerCase());
        }

        return this.rewardCommands;
    }

    public double getLuckyVoteChance() {
        return this.luckyVotePercentage;
    }

    public List<String> getLuckyVoteRewards() {
        return this.luckyVoteRewards;
    }

    public List<String> getServerVotePartyCommands() {
        return this.serverVotePartyCommands;
    }

    public List<String> getPlayerVotePartyCommands() {
        return this.playerVotePartyCommands;
    }

    public int getVotePartyRequired() {
        return this.votePartyRequired;
    }
}
