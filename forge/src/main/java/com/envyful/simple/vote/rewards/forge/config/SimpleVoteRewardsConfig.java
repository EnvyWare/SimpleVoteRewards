package com.envyful.simple.vote.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigPath("config/SimpleVoteRewards/config.yml")
@ConfigSerializable
public class SimpleVoteRewardsConfig extends AbstractYamlConfig {

    private int votePartyRequired = 100;
    private double luckyVotePercentage = 0.15;

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

    public SimpleVoteRewardsConfig() {
        super();
    }

    public List<String> getRewardCommands() {
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
