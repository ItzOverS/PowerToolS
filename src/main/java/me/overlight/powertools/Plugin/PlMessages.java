package me.overlight.powertools.Plugin;

import me.overlight.powertools.Libraries.ColorFormat;

import java.awt.*;

public enum PlMessages {

    PlayerNotFind(ColorFormat.formatColor("@color_redPlayer not find")),
    KnockBack_SimplifyApplied(ColorFormat.formatColor("@color_greenSimplify applied knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_FailedToApply(ColorFormat.formatColor("@color_redI don't see valid knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, knockback stick")),
    Freeze_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, freeze stick")),
    KnockBack_PlayerInBlock(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis in block & i can't give they knockback")),
    Freeze_TargetIsNowFrozen(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_greenis now frozen")),
    Freeze_TargetIsNoLongerFrozen(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis no longer frozen")),
    Protect_PlayerIsNowProtected(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_greenis now protected")),
    Protect_PlayerIsNoLongerProtected(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis no longer protected")),
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
