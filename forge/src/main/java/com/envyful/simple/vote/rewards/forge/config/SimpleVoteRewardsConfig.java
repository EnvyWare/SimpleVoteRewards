package com.envyful.simple.vote.rewards.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigPath("config/SimpleVoteRewards/config.yml")
@ConfigSerializable
public class SimpleVoteRewardsConfig extends AbstractYamlConfig {

    private double luckyVotePercentage = 0.15;

    private List<String> luckyVoteRewards = Lists.newArrayList(
            "give %player% diamond 10"
    );

    private List<String> rewardCommands = Lists.newArrayList(
            "give %player% diamond 1"
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
}
