package com.voxcrafterlp.jumprace.minigameserver.scoreboard;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Array;
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

        scoreboard.registerNewTeam("000").setPrefix("§aPlayer §8▌ §7");

        Arrays.stream(TeamColor.values()).forEach(teamColor ->
                scoreboard.registerNewTeam(teamColor.getDisplayName().toLowerCase()).setPrefix(teamColor.getTeamNameWithColorCode() + " §8▌ §7"));

        //=====================================//

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.LOBBY) {
            objective.getScore(" ").setScore(14);
        }

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING) {
            objective.getScore(" ").setScore(14);

            {
                Team team = scoreboard.registerNewTeam("x13");
                team.setPrefix("§7Time§8: ");
                team.setSuffix("§b" + JumpRace.getInstance().getGameManager().getJumpingCountdown().getTimeLeftFormatted());
                team.addEntry("§b");
                objective.getScore("§b").setScore(13);
            }

            objective.getScore("  ").setScore(12);

            this.preparePlayers(JumpRace.getInstance().getGameManager().getTopScoreboardPlayers(), player).forEach((position, list) -> {
                Team team = scoreboard.registerNewTeam("x" + position);
                team.setPrefix(list.get(0));
                team.addEntry(list.get(1));
                team.setSuffix(list.get(2));
                objective.getScore(list.get(1)).setScore(position);
            });
        }

        //=====================================//


        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player, Map<Player, Integer> map) {
        if(player.getScoreboard() == null || player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) return;

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING) {
            player.getScoreboard().getTeam("x13").setSuffix("§b" + JumpRace.getInstance().getGameManager().getJumpingCountdown().getTimeLeftFormatted());

            this.preparePlayers(map, player).forEach((position, list) -> {
                final Team team = player.getScoreboard().getTeam("x" + position);
                team.setPrefix(list.get(0));
                team.getEntries().forEach(entry -> player.getScoreboard().resetScores(entry));
                team.addEntry(list.get(1));
                team.setSuffix(list.get(2));

                player.getScoreboard().getObjective("scoreboard").getScore(list.get(1)).setScore(position);
            });
        }
    }

    private Map<Integer, List<String>> preparePlayers(Map<Player, Integer> players, Player scoreboardPlayer) {
        final Map<Integer, List<String>> playersMap = new HashMap<>();

        AtomicInteger i = new AtomicInteger(11);

        players.forEach((player, percentage) -> {
            final String colorCode = ChatColor.getLastColors(JumpRace.getInstance().getGameManager().getPlayerNames().get(player)) + (player.equals(scoreboardPlayer) ? "§l§n" : "");
            final String percentageString = " §7(" + percentage + "%)";
            final List<String> list = Lists.newCopyOnWriteArrayList();

            list.addAll(Arrays.asList((colorCode + player.getName()).split("(?<=\\G.{16})"))); //Splits the string into to 2 strings with max 16 characters

            if(list.size() == 2)
                list.set(1, (colorCode + list.get(1) + this.getColorCodes()[i.get()]));
            else
                list.add(this.getColorCodes()[i.get()]);

            list.add(percentageString);

            playersMap.put(i.get(), list);
            i.getAndDecrement();
        });

        return playersMap;
    }

    private String[] getColorCodes() {
        return new String[]{"§r§a", "§r§c", "§r§d", "§r§e", "§r§f", "§r§1", "§r§2", "§r§3", "§r§4", "§r§5", "§r§6", "§r§7"};
    }

}
