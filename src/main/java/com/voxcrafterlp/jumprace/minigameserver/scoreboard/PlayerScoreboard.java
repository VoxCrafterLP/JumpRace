package com.voxcrafterlp.jumprace.minigameserver.scoreboard;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 04.03.2021
 * Time: 13:19
 * Project: JumpRace
 */

public class PlayerScoreboard {

    public void setScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§b§lJumpRace");

        //=====================================//

        scoreboard.registerNewTeam("009").setPrefix(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-player"));

        Arrays.stream(TeamColor.values()).forEach(teamColor ->
                scoreboard.registerNewTeam(teamColor.getDisplayName().toLowerCase()).setPrefix(teamColor.getTeamNameWithColorCode() + JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-team")));

        final Team spectator = scoreboard.registerNewTeam("000spectator");
        spectator.setPrefix(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-spectator"));
        spectator.setCanSeeFriendlyInvisibles(true);

        //=====================================//

        switch (JumpRace.getInstance().getGameManager().getGameState()) {
            case LOBBY:
                objective.getScore(" ").setScore(14);
                objective.getScore(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-map")).setScore(13);

                {
                    Team team = scoreboard.registerNewTeam("x12");
                    team.setPrefix(" §8➜ ");
                    team.setSuffix("§b" + JumpRace.getInstance().getLocationManager().getCurrentMapName());
                    team.addEntry("§b");
                    objective.getScore("§b").setScore(12);
                }

                objective.getScore("  ").setScore(11);
                objective.getScore(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-teams")).setScore(10);
                objective.getScore(" §8➜ §b" + JumpRace.getInstance().getJumpRaceConfig().getTeamAmount() +
                        "x" + JumpRace.getInstance().getJumpRaceConfig().getTeamSize()).setScore(9);
                objective.getScore("   ").setScore(8);

                break;
            case JUMPING:
                objective.getScore(" ").setScore(14);

                {
                    Team team = scoreboard.registerNewTeam("x13");
                    team.setPrefix(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-time"));
                    team.setSuffix("§b" + JumpRace.getInstance().getGameManager().getJumpingCountdown().getTimeLeftFormatted());
                    team.addEntry("§b");
                    objective.getScore("§b").setScore(13);
                }

                objective.getScore("  ").setScore(12);

                this.prepareJumpingPlayers(JumpRace.getInstance().getGameManager().getTopScoreboardPlayers(), player).forEach((position, list) -> {
                    Team team = scoreboard.registerNewTeam("x" + position);
                    team.setPrefix(list.get(0));
                    team.addEntry(list.get(1));
                    team.setSuffix(list.get(2));
                    objective.getScore(list.get(1)).setScore(position);
                });
                break;
            case DEATHMATCH:
                objective.getScore(" ").setScore(14);

                {
                    Team team = scoreboard.registerNewTeam("x13");
                    team.setPrefix(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("scoreboard-time"));
                    team.setSuffix("§b" + JumpRace.getInstance().getGameManager().getDeathMatchCountdown().getTimeLeftFormatted());
                    team.addEntry("§b");
                    objective.getScore("§b").setScore(13);
                }

                objective.getScore("  ").setScore(12);

                this.prepareArenaPlayers(JumpRace.getInstance().getGameManager().getTopArenaPlayers(), player).forEach((position, list) -> {
                    Team team = scoreboard.registerNewTeam("x" + position);
                    team.setPrefix(list.get(0));
                    team.addEntry(list.get(1));
                    team.setSuffix(list.get(2));
                    objective.getScore(list.get(1)).setScore(position);
                });
                break;
        }

        //=====================================//

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(players))
                spectator.addPlayer(players);
            else {
                final com.voxcrafterlp.jumprace.objects.Team team = JumpRace.getInstance().getGameManager().getTeamFromPlayer(players);
                scoreboard.getTeam(((team != null) ? team.getTeamColor().getDisplayName().toLowerCase() : "009")).addPlayer(players);
            }
        });

        //=====================================//

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player, Map<Player, Integer> map) {
        if(player.getScoreboard() == null || player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) return;
        final Scoreboard scoreboard = player.getScoreboard();

        switch (JumpRace.getInstance().getGameManager().getGameState()) {
            case LOBBY:
                scoreboard.getTeam("x12").setSuffix("§b" + JumpRace.getInstance().getLocationManager().getCurrentMapName());
                break;
            case JUMPING:
                scoreboard.getTeam("x13").setSuffix("§b" + JumpRace.getInstance().getGameManager().getJumpingCountdown().getTimeLeftFormatted());

                this.prepareJumpingPlayers(map, player).forEach((position, list) -> {
                    final Team team = scoreboard.getTeam("x" + position);

                    if(team != null) {
                        team.setPrefix(list.get(0));
                        team.getEntries().forEach(scoreboard::resetScores);
                        team.addEntry(list.get(1));
                        team.setSuffix(list.get(2));

                        scoreboard.getObjective("scoreboard").getScore(list.get(1)).setScore(position);
                    }
                });

                this.cleanupScoreboard(scoreboard, JumpRace.getInstance().getGameManager().getPlayersLeft().size());
                break;
            case DEATHMATCH:
                scoreboard.getTeam("x13").setSuffix("§b" + JumpRace.getInstance().getGameManager().getDeathMatchCountdown().getTimeLeftFormatted());

                this.prepareArenaPlayers(map, player).forEach((position, list) -> {
                    final Team team = scoreboard.getTeam("x" + position);
                    team.setPrefix(list.get(0));
                    team.getEntries().forEach(scoreboard::resetScores);
                    team.addEntry(list.get(1));
                    team.setSuffix(list.get(2));

                    scoreboard.getObjective("scoreboard").getScore(list.get(1)).setScore(position);
                });

                this.cleanupScoreboard(scoreboard, JumpRace.getInstance().getGameManager().getLivesLeft().size());
                break;

            case ENDING:

                break;
        }
    }

    private Map<Integer, List<String>> prepareJumpingPlayers(Map<Player, Integer> players, Player scoreboardPlayer) {
        final Map<Integer, List<String>> playersMap = new HashMap<>();

        final AtomicInteger score = new AtomicInteger(11);

        players.forEach((player, percentage) -> {
            final String colorCode = ChatColor.getLastColors(JumpRace.getInstance().getGameManager().getPlayerNames().get(player)) + (player.equals(scoreboardPlayer) ? "§l§n" : "");
            final String percentageString = " §7(" + percentage + "%)";
            final List<String> list = Lists.newCopyOnWriteArrayList();

            list.addAll(Arrays.asList((colorCode + player.getName()).split("(?<=\\G.{16})"))); //Splits the string into to 2 strings with max 16 characters

            if(list.size() == 2)
                list.set(1, (colorCode + list.get(1) + this.getColorCodes()[score.get()]));
            else
                list.add(this.getColorCodes()[score.get()]);

            list.add(percentageString);

            playersMap.put(score.get(), list);
            score.getAndDecrement();
        });

        return playersMap;
    }

    private Map<Integer, List<String>> prepareArenaPlayers(Map<Player, Integer> players, Player scoreboardPlayer) {
        final Map<Integer, List<String>> playersMap = new HashMap<>();

        final AtomicInteger score = new AtomicInteger(11);

        players.forEach((player, lives) -> {
            final String colorCode = ChatColor.getLastColors(JumpRace.getInstance().getGameManager().getPlayerNames().get(player)) + (player.equals(scoreboardPlayer) ? "§l§n" : "");
            final List<String> list = Lists.newCopyOnWriteArrayList();

            list.add(this.getLivesLeftFormatted(lives));
            list.addAll(Arrays.asList((colorCode + player.getName()).split("(?<=\\G.{16})"))); //Splits the string into to 2 strings with max 16 characters

            if(list.size() == 3)
                list.set(2, (colorCode + list.get(2) + this.getColorCodes()[score.get()]));
            else
                list.add(this.getColorCodes()[score.get()]);

            playersMap.put(score.get(), list);
            score.getAndDecrement();
        });

        return playersMap;
    }

    private void cleanupScoreboard(Scoreboard scoreboard, int scores) {
        for(int i = (11 - scores); i>=0; i--) {
            final Team team = scoreboard.getTeam("x" + i);

            if(team != null) {
                final String entry = team.getEntries().iterator().next();
                team.unregister();
                scoreboard.resetScores(entry);
            }
        }
    }

    private String getLivesLeftFormatted(int lives) {
        final int maxLives = JumpRace.getInstance().getJumpRaceConfig().getMaxLives();
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("§c");

        for(int i = 0; i<lives; i++)
            stringBuilder.append("❤");

        stringBuilder.append("§7");

        for(int i = 0; i<(maxLives - lives); i++)
            stringBuilder.append("❤");

        stringBuilder.append(" ");

        return stringBuilder.toString();
    }

    private String[] getColorCodes() {
        return new String[]{"§r§a", "§r§c", "§r§d", "§r§e", "§r§f", "§r§1", "§r§2", "§r§3", "§r§4", "§r§5", "§r§6", "§r§7"};
    }

}
