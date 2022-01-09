package com.envyful.simple.vote.rewards.forge.config;

public class VoteCache {

    private final String username;
    private final String service;

    public VoteCache(String username, String service) {
        this.username = username;
        this.service = service;
    }

    public String getUsername() {
        return this.username;
    }

    public String getService() {
        return this.service;
    }

    public String serialize() {
        return this.username + "###" + this.service;
    }

    public static VoteCache deserialize(String data) {
        String[] args = data.split("###");
        return new VoteCache(args[0], args[1]);
    }
}
