package me.overlight.powertools.Plugin;

import me.overlight.powertools.Libraries.ColorFormat;

import java.awt.*;

public enum PlMessages {

    PlayerNotFind(ColorFormat.formatColor("@color_redPlayer not find")),
    KnockBack_SimplifyApplied(ColorFormat.formatColor("@color_greenSimplify applied knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_FailedToApply(ColorFormat.formatColor("@color_redI don't see valid knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, knockback stick")),
    KnockBack_PlayerInBlock(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis in block & i can't give they knockback")),


    NoPermission(ColorFormat.formatColor("@color_redYou don't have enough permission to do this!"))

    ;
    private final String name;
    PlMessages(String name){
        this.name = name;
    }
    public String get(){
        return PlInfo.PREFIX + this.name;
    }
}
