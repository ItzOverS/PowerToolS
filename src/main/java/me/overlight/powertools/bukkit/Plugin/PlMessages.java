package me.overlight.powertools.bukkit.Plugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.overlight.powertools.bukkit.Libraries.ColorFormat;
import me.overlight.powertools.bukkit.Libraries.RepItem;
import me.overlight.powertools.bukkit.PowerTools;
import org.bukkit.entity.Player;

public enum PlMessages {

    PlayerNotFind("@color_redPlayer not find"),
    KnockBack_SimplifyApplied("@color_greenSimplify applied knockback on @color_gold%PLAYER_NAME%"),
    KnockBack_FailedToApply("@color_redI don't see valid knockback on @color_gold%PLAYER_NAME%"),
    KnockBack_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, knockback stick"),
    Freeze_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, freeze stick"),
    Rotate_StickSimplifyGiven("@color_greenSimplify given @color_gold%PLAYER_NAME%@color_green, rotate stick"),
    KnockBack_PlayerInBlock("@color_gold%PLAYER_NAME% @color_redis in block & I can't give they knockback"),
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
    Toggle_SimplifyRemovedToggle("@color_greenSimplify un-toggled"),
    OnlyPlayersCanUseCommand("@color_redOnly players can use this command"),
    NoPermission("@color_redYou don't have enough permission to do this!"),
    ReloadSuccess("@color_greenSimplify reloaded config.yml"),
    HelpCMD_CommandNotFind("@color_redYour target command not find!"),
    CommandNotFind("@color_redCommand not found... Use /pts help"),
    Settings_InvalidPath("@color_redInvalid path"),
    Settings_SuccessSetValue("@color_greenSimplify set @color_gold%PATH%@color_green to @color_gold%VALUE% @color_green(from @color_gold%FROM%@color_green)"),
    InvalidUsage("@color_redInvalid usage...@color_gold use %CORRECT%"),
    BlackList_Added("@color_redBlackListed %USERNAME%"),
    BlackList_Removed("@color_redUn BlackListed %USERNAME%"),
    WhiteList_Added("@color_greenWhiteListed %USERNAME%"),
    WhiteList_Removed("@color_greenUn Whitelisted %USERNAME%"),
    Mute_UserAlreadyMuted("@color_gold%USERNAME% @color_redhas already muted"),
    Mute_UserMuted("@color_greenSimplify muted @color_gold%USERNAME%"),
    Mute_UserNoLongerMuted("@color_gold%USERNAME%@color_red is no longer muted"),
    Mute_UserIsntMuted("@color_gold%USERNAME%@color_red isn't muted"),
    Mute_YouCantSendChatMessage("@color_redYou are muted & can't send message in public chat"),
    Mute_YouCantPlaceSign("@color_redYou are muted & can't place sign"),
    Vote_SelectYourVoteName("@color_greenEnter your vote name you wants in chat"),
    Vote_SelectYourVoteItem("@color_greenEnter your vote item #%INDEX% in chat"),
    Vote_SelectYourVotePermission("@color_greenEnter your vote permission you wants in chat"),
    Vote_SuccessfulUpdateVoteName("@color_greenSimplify updated your vote name"),
    Vote_SuccessfulUpdateVoteOption("@color_greenSimplify updated your vote item"),
    Vote_SuccessfulUpdateVotePermission("@color_greenSimplify updated your vote permission"),
    Vote_SuccessfulStarted("@color_greenStarted vote who has %PERM% permission"),
    Vote_SuccessfulVoted("@color_greenYou simplify voted for %VOTE%"),
    Vote_AlreadyVotedFor("@color_redYou already voted"),
    Vote_VoteEnded("@color_greenVote ended & was have %VOTERS% voter(s)"),
    Vote_TargetVoteHasExpired("@color_redTarget vote has expired"),
    Img2Map_TargetUrlNotFind("@color_redYour requested url not found"),
    Img2Map_SimplifyCreatedMap("@color_greenSimplify created image on map"),
    OutDatedFuture("@color_redThis future only support on %VERSION%"),
    DumpItemNotFind("@color_redTarget dump type not found!"),
    ChatManager_BadWordDetected("@color_redI flagged your message!"),
    ChatManager_DuplicateWord("@color_redI see duplicate words in message!"),
    ChatManager_MessagesDelay("@color_redPlease wait before send another message"),
    ChatManager_AntiSpam("@color_redSpam is not allowed in this server"),
    ChatManager_AntiAdventure("@color_redAdventures are not allowed in this server"),
    SimplifyCreatedDump("@color_greenDump created! URL: %URL%"),
    SimplifyCreatedConsoleLog("@color_greenConsole Log created! URL: %URL%"),
    FailedCreateConsoleLog("@color_redCould not created console log url"),
    SimplifyCreatedConfigDump("@color_greenConfig copy created! URL: %URL%"),
    FailedCreateConfigDump("@color_redCould not created config copy url"),
    FailedCreateDump("@color_redCould not created dump url"),
    Plugins_PluginAlreadyEnabled("@color_greenPlugin @color_gold%PLUGIN_NAME%@color_green already enabled"),
    Plugins_PluginNotFind("@color_redPlugin @color_gold%PLUGIN_NAME%@color_green not find in enabled or disabled plugins"),
    Plugins_PluginSimplifyEnabled("@color_greenPlugin @color_gold%PLUGIN_NAME%@color_green simplify enabled"),
    Plugins_AllPluginSimplifyEnabled("@color_greenAll plugins simplify enabled"),
    Plugins_AllPluginSimplifyDisabled("@color_greenAll plugins simplify disabled"),
    Plugins_AllPluginSimplifyRestarted("@color_greenAll plugins simplify restarted"),
    Plugins_PluginAlreadyDisabled("@color_redPlugin @color_gold%PLUGIN_NAME%@color_red already disabled"),
    Plugins_PluginSimplifyDisabled("@color_greenPlugin @color_gold%PLUGIN_NAME%@color_green simplify disabled"),
    Plugins_PluginSimplifyRestarted("@color_greenPlugin @color_gold%PLUGIN_NAME%@color_green simplify restarted"),
    Plugins_ThisCommandExecuteForOnePlugin("@color_redYou can only execute this command for one plugin not 'all'"),
    Speed_InvalidNumber("@color_redNumber entered is not validate"),
    Speed_NumberIsNegative("@color_redNumbers can only positive"),
    Speed_SimplifyAppliedFlySpeed("@color_greenSuccessful set your @color_goldfly@color_green speed to @color_gold%NUM%"),
    Speed_SimplifyAppliedMovementSpeed("@color_greenSuccessful set your @color_goldmovement@color_green speed to @color_gold%NUM%"),
    InvSee_SimplifyOpenedTargetInventory("@color_greenSimplify opened @color_gold%NAME%@color_green's inventory"),
    ItemDisabler_ThisItemHasBeenDisabled("@color_redThis item has been disabled!"),
    Optimizer_SimplifyRemovedItemsAndMobs("@color_redSimplify removed @color_green%ITEMS% item(s) @color_red&@color_green %MOBS% mob(s)@color_red from ground"),
    Optimizer_SimplifyRemovedItems("@color_redSimplify removed @color_green%ITEMS% item(s)@color_red from ground"),
    Optimizer_ItemWillRemovedIn("@color_red%OBJ% on ground will remove in @color_gold%TIME% second(s)"),
    Optimizer_SimplifyRemovedMobs("@color_redSimplify removed @color_green %MOBS% mob(s)@color_red from ground"),
    Worlds_Go_TargetWorldNotFound("@color_redCould not found world @color_gold%WORLD%"),
    Worlds_Go_SuccessFulEnteredWorld("@color_greenSimplify teleported to @color_gold%WORLD%"),
    Worlds_Create_NameAlreadyExists("@color_redThe name you selected is existing"),
    Worlds_Create_SimplifyCreated("@color_greenSimplify create a new world! Use /powertools world go %WORLD%"),
    Worlds_Delete_TargetNotFound("@color_redTarget world not found"),
    Worlds_Delete_SimplifyDeletedWorld("@color_greenSimplify delete the world called %WORLD%"),
    ;
    private final String desc;

    PlMessages(String desc) {
        this.desc = desc;
    }

    public String get(RepItem... items) {
        String item = ColorFormat.formatColor(PowerTools.messages.contains(this.name()) ? PowerTools.messages.getString(this.name()) : desc);
        for (RepItem i : items) item = item.replace(i.key(), i.value());
        return PlInfo.PREFIX + item;
    }

    public String get(Player player, RepItem... items) {
        String item = ColorFormat.formatColor(PowerTools.messages.contains(this.name()) ? PowerTools.messages.getString(this.name()) : desc);
        for (RepItem i : items) item = item.replace(i.key(), i.value());
        return PlaceholderAPI.setPlaceholders(player, PlInfo.PREFIX + item);
    }
}
