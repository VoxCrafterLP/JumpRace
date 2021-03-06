package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import com.voxcrafterlp.jumprace.exceptions.TeamAmountException;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.modules.objects.ModuleRow;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import com.voxcrafterlp.jumprace.objects.ChestLoot;
import com.voxcrafterlp.jumprace.objects.Countdown;
import com.voxcrafterlp.jumprace.objects.Team;
import com.voxcrafterlp.jumprace.utils.ActionBarUtil;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 26.02.2021
 * Time: 16:58
 * Project: JumpRace
 */

@Getter
public class GameManager {

    private final List<Team> registeredTeams;
    private GameState gameState;
    private final Countdown lobbyCountdown, endingCountdown, jumpingCountdown;
    private final Map<Player, String> playerNames;
    private final Map<Player, ModuleRow> moduleRows;
    private final ChestLoot chestLoot;

    public GameManager() {
        this.registeredTeams = Lists.newCopyOnWriteArrayList();
        this.gameState = GameState.LOBBY;
        this.playerNames = new HashMap<>();
        this.moduleRows = new HashMap<>();
        this.chestLoot = new ChestLoot();

        try {
            this.registerTeams();
        } catch (TeamAmountException e) {
            e.printStackTrace();
        }

        this.lobbyCountdown = new Countdown(Countdown.Type.LOBBY, this::startGame);
        this.endingCountdown = new Countdown(Countdown.Type.ENDING, () -> Bukkit.getOnlinePlayers().forEach(player ->
                player.kickPlayer(JumpRace.getInstance().getPrefix() + "§7The game is §bover§8.")));
        this.jumpingCountdown = new Countdown(Countdown.Type.JUMPING, () -> {

        });

        this.startLobbyActionBar();
    }

    private void registerTeams() throws TeamAmountException {
        final int teamAmount = JumpRace.getInstance().getJumpRaceConfig().getTeamAmount();

        if(teamAmount > 8)
            throw new TeamAmountException("The set team amount is above the maximum (8)");

        for(int i = 0; i<teamAmount; i++)
            this.registeredTeams.add(new Team(TeamColor.values()[i]));
    }

    public void startGame() {
        this.gameState = GameState.JUMPING;
        this.addPlayersToRandomTeams();
        this.loadPlayerNames();

        AtomicInteger i = new AtomicInteger(0);

        Bukkit.getOnlinePlayers().forEach(player -> {
            this.moduleRows.put(player, JumpRace.getInstance().getModuleManager().getModuleRows().get(i.get()).assignPlayer(player));
            JumpRace.getInstance().getInventoryManager().setInventory(player, InventoryManager.Type.JUMPING);
            i.getAndIncrement();
        });

        this.jumpingCountdown.startCountdown();

        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                new PlayerScoreboard().setScoreboard(player);
            });
        }, 20);
    }

    private void loadPlayerNames() {
        this.registeredTeams.forEach(team -> team.getMembers().forEach(player ->
                this.playerNames.put(player, team.getTeamColor().getColorCode() + player.getName())));
    }

    public void handlePlayerJoin() {
        if(!this.lobbyCountdown.isRunning() && Bukkit.getOnlinePlayers().size() >= JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart()  &&
            JumpRace.getInstance().getLocationManager().getLoadedMaps().size() != 0)
            this.lobbyCountdown.startCountdown();
    }

    public void handlePlayerQuit(Player player) {
        if(this.gameState == GameState.LOBBY && Bukkit.getOnlinePlayers().size() < JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart())
            this.lobbyCountdown.reset(false);

        if(this.gameState == GameState.LOBBY) {
            final Team team = this.getTeamFromPlayer(player);
            if(team != null) team.getMembers().remove(player);

            JumpRace.getInstance().getInventoryManager().updateTeamSelectorInventory();
        }

        if(this.gameState == GameState.JUMPING || this.gameState == GameState.DEATHMATCH) {
            final Team team = this.getTeamFromPlayer(player);
            if(team != null) {
                team.getMembers().remove(player);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(team.getMembers().size() == 0)
                        players.sendMessage(JumpRace.getInstance().getPrefix() + team.getTeamColor().getColorCode() + " Team " +
                                team.getTeamColor().getDisplayName() + " §7has been §4eliminated§8.");
                    else
                        players.sendMessage(JumpRace.getInstance().getPrefix() + team.getTeamColor().getColorCode() + " Team " +
                                team.getTeamColor().getDisplayName() + " §7has got §c" + team.getMembers().size() + " players §7left§8.");
                });
                checkTeams();
            }
            this.playerNames.remove(player);
        }
    }

    private void checkTeams() {
        if((int) this.registeredTeams.stream().filter(team -> team.getMembers().size() >= 1).count() < 2) {
            this.gameState = GameState.ENDING;
            this.endGame();
        }
    }

    private void endGame() {

    }

    public Team getTeamFromPlayer(Player player) {
        return this.registeredTeams.stream().filter(team -> team.getMembers().contains(player)).findAny().orElse(null);
    }

    private void addPlayersToRandomTeams() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(this.getTeamFromPlayer(player) == null) {
                this.registeredTeams.stream().filter(team -> team.getMembers().size() <
                        JumpRace.getInstance().getJumpRaceConfig().getTeamSize()).limit(1).findAny().get().getMembers().add(player);
            }
        });
    }

    public void startLobbyActionBar() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            if(this.gameState == GameState.LOBBY) {
                if(!this.lobbyCountdown.isRunning()) {
                    if(JumpRace.getInstance().getLocationManager().getLoadedMaps().size() == 0) {
                        Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player, "§cNo map has been set up!"));
                        return;
                    }

                    final int playerLeft = JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart() - Bukkit.getOnlinePlayers().size();
                    Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player,
                            ((playerLeft == 1) ? "§7Waiting for §bone §7more player§8..." : "§7Waiting for §b" + playerLeft + " §7more players§8...")));
                } else
                    Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player, "§cTeaming forbidden"));
            }
        }, 20, 20);
    }

    public void reachGoal(Player player) {
        this.jumpingCountdown.setTimeLeft(10);
        Bukkit.broadcastMessage(JumpRace.getInstance().getPrefix() + JumpRace.getInstance().getGameManager().getPlayerNames().get(player) +
                " §7reached the §bgoal§8.");
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL,1,1);
        player.getInventory().setBoots(new ItemManager(Material.DIAMOND_BOOTS).setUnbreakable(true).build());
    }

    /*
        https://howtodoinjava.com/java/sort/java-sort-map-by-values/
     */
    public Map<Player, Integer> getTopScoreboardPlayers() {
        Map<Player, Integer> progress = new HashMap<>();
        Map<Player, Integer> map = new LinkedHashMap<>();

        Bukkit.getOnlinePlayers().forEach(player -> progress.put(player, player.getLocation().getBlockX()));

        final LinkedHashMap<Player, Integer> sortedProgressMap = new LinkedHashMap<>();

        progress.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedProgressMap.put(x.getKey(), x.getValue()));

        sortedProgressMap.forEach((player, integer) -> {
            if(map.size() <= 12)
                map.put(player, new CalculatorUtil().calculatePercent(JumpRace.getInstance().getModuleManager().getModuleLength(), integer));
        });

        return map;
    }

}
