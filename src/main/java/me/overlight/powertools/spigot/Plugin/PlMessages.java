package me.overlight.powertools.spigot.Plugin;

import me.overlight.powertools.spigot.Libraries.ColorFormat;
import me.overlight.powertools.spigot.Libraries.RepItem;

public enum PlMessages {

    PlayerNotFind("@color_redPlayer not find"),
    KnockBack_SimplifyApplied("@color_greenSimplify applied knockback on @color_gold%PLAYER_NAME%"),
    KnockBack_FailedToApply("@color_redI don't see valid knockback on @color_gold%PLAYER_NAME%"),
    KnockBack_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, knockback stick"),
    Freeze_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, freeze stick"),
    Rotate_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, rotate stick"),
    KnockBack_PlayerInBlock("@color_gold%PLAYER_NAME% @color_redis in block & i can't give they knockback"),
    Freeze_TargetIsNowFrozen("@color_gold%PLAYER_NAME% @color_greenis now frozen"),
    Freeze_TargetIsNoLongerFrozen("@color_gold%PLAYER_NAME% @color_redis no longer frozen"),
    Protect_PlayerIsNowProtected("@color_gold%PLAYER_NAME% @color_greenis now protected"),
    Protect_PlayerIsNoLongerProtected("@color_gold%PLAYER_NAME% @color_redis no longer protected"),
    Protect_YouAreNowProtected("@color_greenYou are now protected"),
    Protect_YouAreNoLongerProtected("@color_redYou are no longer protected"),
    Rotate_FailedToRotate("@color_redFailed to rotate @color_gold%PLAYER_NAME%"),
    Rotate_SimplifyRotated("@color_greenSimplify rotated @color_gold%PLAYER_NAME%"),
    Vanish_PlayerIsNowVanish("@color_gold%PLAYER_NAME% @color_green is now vanish"),
    Vanish_PlayerIsNoLongerVanish("@color_gold%PLAYER_NAME% @color_red is no longer vanish"),
    Vanish_YouAreNowVanish("@color_greenYou're now vanish"),
    Vanish_YouAreNoLongerVanish("@color_redYou're no longer vanish"),
    ClDetector_PlayerJoinedUsing("@color_gold%PLAYER_NAME%@color_green joined using @color_red%CLIENT% %VERSION%"),
    ClDetector_FailedToDetectClient("@color_redFailed to detect @color_gold%PLAYER_NAME%@color_red's client"),
    CpsCheck_KickedForMaxCps("@color_gold%PLAYER_NAME% @color_red has kicked for max %TYPE% cps. cps: %CPS%"),
    CpsCheck_UsingAutoClicker("@color_gold%PLAYER_NAME% @color_red using auto clicker"),
    CpsCheck_KickedForAutoClicker("@color_gold%PLAYER_NAME% @color_red kicked for using auto clicker"),
    CpsCheck_PlayersCpsGet("@color_gold%PLAYER_NAME% @color_green's @color_gold%CPS_TYPE%@color_green cps is @color_gold%CPS%"),
    NetworkChecker_PlayerJoinedUsing("@color_gold%PLAYER_NAME% @color_green's IP is %IP%"),
    NetworkChecker_PlayerTempBannedForInvalidCountry("@color_gold%PLAYER_NAME%@color_green has temp banned for invalid country"),
    Toggle_TargetItemNotFind("@color_redTarget type not find!"),
    Toggle_SimplifySet("@color_greenSimplify toggled @color_gold%TARGET_PLAYER%@color_green's @color_gold%TARGET_ITEM%@color_green for You"),
    OnlyPlayersCanUseCommand("@color_redOnly players can use this command"),
    NoPermission("@color_redYou don't have enough permission to do this!"),
    ReloadSuccess("@color_greenSimplify reloaded config.yml"),
    HelpCMD_CommandNotFind("@color_redYour target command not find!"),
    CommandNotFind("@color_redCommand not found... Use /pts help"),
    Settings_InvalidPath("@color_redInvalid path"),
    Settings_SuccessSetValue("@color_greenSimplify set @color_gold%PATH%@color_red to @color_gold%VALUE%"),

    ;
    private final String desc;

    PlMessages(String desc) {
        this.desc = desc;
    }

    public String get(RepItem... items) {
        String item = ColorFormat.formatColor(this.desc);
        for (RepItem i : items) item = item.replace(i.key(), i.value());
        return PlInfo.PREFIX + item;
    }
}
