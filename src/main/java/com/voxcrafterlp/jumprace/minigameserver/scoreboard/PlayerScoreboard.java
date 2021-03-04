package com.voxcrafterlp.jumprace.minigameserver.scoreboard;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import org.bukkit.Bukkit;
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

            objective.getScore(" ").setScore(12);



        }



        //=====================================//


        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player, Map<Player, Integer> players) {
        if(player.getScoreboard() == null || player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) return;

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING) {
            player.getScoreboard().getTeam("x13").setSuffix("§b" + JumpRace.getInstance().getGameManager().getJumpingCountdown().getTimeLeftFormatted());
        }
    }

    private Map<Integer, String[]> preparePlayers(Map<Player, Integer> players, Player scoreboardPlayer) {
        final Map<Integer, String[]> playersMap = new HashMap<>();

        AtomicInteger i = new AtomicInteger(11);

        players.forEach((player, percentage) -> {
            final String string = player.equals(scoreboardPlayer) ? "§n§b" + JumpRace.getInstance().getGameManager().getPlayerNames().get(player) + " §7(" + percentage + "%)" :
                    JumpRace.getInstance().getGameManager().getPlayerNames().get(player)  + " §7(" + percentage + "%)";

            playersMap.put(i.get(), string.split("(?<=\\G.{16})")); //Splits the string into to 2 strings with max 16 characters
            i.getAndDecrement();
        });

        return playersMap;
    }

}
