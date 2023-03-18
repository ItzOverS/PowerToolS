package me.overlight.powertools.bukkit.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    private static JDA client;

    public static void loginClient(String token){
        client = JDABuilder.createDefault(token, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES).build();
    }

    public static JDA getClient(){
        return client;
    }

    public static void setClient(JDA client){
        Bot.client = client;
    }
}
