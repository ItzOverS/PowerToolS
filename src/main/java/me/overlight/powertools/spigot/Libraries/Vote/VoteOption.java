package me.overlight.powertools.spigot.Libraries.Vote;

import me.overlight.powertools.spigot.Libraries.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class VoteOption {
    private String name;
    private int id;
    private String text;
    private int range;
    private List<String> voters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void addVoter(String name) {
        this.voters.add(name);
    }

    public List<String> getVoters() {
        return this.voters;
    }

    public VoteOption(String name, String text, int range) {
        this.name = name;
        this.id = RandomUtil.randomInteger();
        this.text = text;
        this.range = range;
    }
}
