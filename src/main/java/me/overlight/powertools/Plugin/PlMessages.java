package me.overlight.powertools.Plugin;

import me.overlight.powertools.Libraries.ColorFormat;
import me.overlight.powertools.Libraries.RepItem;

public enum PlMessages {

    PlayerNotFind(ColorFormat.formatColor("@color_redPlayer not find")),
    KnockBack_SimplifyApplied(ColorFormat.formatColor("@color_greenSimplify applied knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_FailedToApply(ColorFormat.formatColor("@color_redI don't see valid knockback on @color_gold%PLAYER_NAME%")),
    KnockBack_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, knockback stick")),
    Freeze_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, freeze stick")),
    Rotate_StickSimplifyGiven(ColorFormat.formatColor("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, rotate stick")),
    KnockBack_PlayerInBlock(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis in block & i can't give they knockback")),
    Freeze_TargetIsNowFrozen(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_greenis now frozen")),
    Freeze_TargetIsNoLongerFrozen(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis no longer frozen")),
    Protect_PlayerIsNowProtected(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_greenis now protected")),
    Protect_PlayerIsNoLongerProtected(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_redis no longer protected")),
    Protect_YouAreNowProtected(ColorFormat.formatColor("@color_greenYou are now protected")),
    Protect_YouAreNoLongerProtected(ColorFormat.formatColor("@color_redYou are no longer protected")),
    Rotate_FailedToRotate(ColorFormat.formatColor("@color_redFailed to rotate @color_gold%PLAYER_NAME%")),
    Rotate_SimplifyRotated(ColorFormat.formatColor("@color_greenSimplify rotated @color_gold%PLAYER_NAME%")),
    Vanish_PlayerIsNowVanish(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_green is now vanish")),
    Vanish_PlayerIsNoLongerVanish(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_red is no longer vanish")),
    Vanish_YouAreNowVanish(ColorFormat.formatColor("@color_greenYou're now vanish")),
    Vanish_YouAreNoLongerVanish(ColorFormat.formatColor("@color_redYou're no longer vanish")),
    ClDetector_PlayerJoinedUsing(ColorFormat.formatColor("@color_gold%PLAYER_NAME%@color_green joined using @color_red%CLIENT% %VERSION%")),
    ClDetector_FailedToDetectClient(ColorFormat.formatColor("@color_redFailed to detect @color_gold%PLAYER_NAME%@color_red's client")),
    CpsCheck_KickedForMaxCps(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_red has kicked for max %TYPE% cps. cps: %CPS%")),
    CpsCheck_UsingAutoClicker(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_red using auto clicker")),
    CpsCheck_KickedForAutoClicker(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_red kicked for using auto clicker")),
    NetworkChecker_PlayerJoinedUsing(ColorFormat.formatColor("@color_gold%PLAYER_NAME% @color_green's IP is %IP%")),
    NetworkChecker_PlayerTempBannedForInvalidCountry(ColorFormat.formatColor("@color_gold%PLAYER_NAME%@color_green has temp banned for invalid country")),
    NoPermission(ColorFormat.formatColor("@color_redYou don't have enough permission to do this!"))

    ;
    private final String name;
    PlMessages(String name){
        this.name = name;
    }
    public String get(RepItem ... items){
        String item = this.name;
        for(RepItem i : items) item = item.replace(i.key(), i.value());
        return PlInfo.PREFIX + item;
    }
}
