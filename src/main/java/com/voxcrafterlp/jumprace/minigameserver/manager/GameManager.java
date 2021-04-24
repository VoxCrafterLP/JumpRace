package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.api.events.PlayerReachGoalEvent;
import com.voxcrafterlp.jumprace.api.events.TeamWinEvent;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import com.voxcrafterlp.jumprace.exceptions.TeamAmountException;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.modules.objects.ModuleRow;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import com.voxcrafterlp.jumprace.objects.ChestLoot;
import com.voxcrafterlp.jumprace.objects.Countdown;
import com.voxcrafterlp.jumprace.objects.DeathChest;
import com.voxcrafterlp.jumprace.objects.Team;
import com.voxcrafterlp.jumprace.utils.ActionBarUtil;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 26.02.2021
 * Time: 16:58
 * Project: JumpRace
 */

@Getter
public class GameManager {

    private static final int MAX_MODULES = 10;

    private final List<Team> registeredTeams;
    private GameState gameState;
    private final Countdown lobbyCountdown, endingCountdown, preJumpingCountdown, jumpingCountdown, preDeathMatchCountdown, deathMatchCountdown;
    private final Map<Player, String> playerNames;
    private final Map<Player, ModuleRow> moduleRows;
    private final ChestLoot chestLoot;
    private int fireworkTaskID;
    private final List<Player> playersLeft;
    private final SpectatorManager spectatorManager;

    private final Map<Player, Integer> livesLeft;
    private final List<DeathChest> deathChests;

    /**
     * Initialize countdowns & start lobby actionbar
     */
    public GameManager() {
        this.registeredTeams = Lists.newCopyOnWriteArrayList();
        this.gameState = GameState.LOBBY;
        this.playerNames = new HashMap<>();
        this.moduleRows = new HashMap<>();
        this.chestLoot = new ChestLoot();
        this.playersLeft = Lists.newCopyOnWriteArrayList();
        this.spectatorManager = new SpectatorManager();

        this.livesLeft = new HashMap<>();
        this.deathChests = Lists.newCopyOnWriteArrayList();

        try {
            this.registerTeams();
        } catch (TeamAmountException e) {
            e.printStackTrace();
        }

        this.lobbyCountdown = new Countdown(Countdown.Type.LOBBY, this::startGame);
        this.endingCountdown = new Countdown(Countdown.Type.ENDING, () -> {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.kickPlayer(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-game-over")));
            Bukkit.getScheduler().scheduleAsyncDelayedTask(JumpRace.getInstance(), Bukkit::shutdown, 5);
        });
        this.jumpingCountdown = new Countdown(Countdown.Type.JUMPING, this::startDeathmatch);
        this.deathMatchCountdown = new Countdown(Countdown.Type.DEATHMATCH, this::calculateWinner);

        this.preJumpingCountdown = new Countdown(Countdown.Type.PRE_JUMPING, () -> {
            this.jumpingCountdown.startCountdown();
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 4));
        });
        this.preDeathMatchCountdown = new Countdown(Countdown.Type.PRE_DEATHMATCH, () -> {
            this.deathMatchCountdown.startCountdown();
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 4));
        });

        this.startActionBar();
    }

    /**
     * Create teams with the settings from the config
     * @throws TeamAmountException If the team amount is above 8.
     */
    private void registerTeams() throws TeamAmountException {
        final int teamAmount = JumpRace.getInstance().getJumpRaceConfig().getTeamAmount();

        if(teamAmount > 8)
            throw new TeamAmountException("The set team amount is above the maximum (8)");

        for(int i = 0; i<teamAmount; i++)
            this.registeredTeams.add(new Team(TeamColor.values()[i]));
    }

    /**
     * Load player names, teleport players to the map,
     * start jumping countdown
     */
    public void startGame() {
        this.gameState = GameState.JUMPING;
        this.addPlayersToRandomTeams();
        this.loadPlayerNames();

        final AtomicInteger i = new AtomicInteger(0);

        Bukkit.getOnlinePlayers().forEach(player -> {
            this.moduleRows.put(player, JumpRace.getInstance().getModuleManager().getModuleRows().get(i.get()).assignPlayer(player));
            JumpRace.getInstance().getInventoryManager().setInventory(player, InventoryManager.Type.JUMPING);
            this.playersLeft.add(player);
            i.getAndIncrement();
        });

        this.preJumpingCountdown.startCountdown();

        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> new PlayerScoreboard().setScoreboard(player));
        }, 20);

        this.registeredTeams.forEach(team -> team.setAlive(team.getMembers().size() > 0));
    }

    /**
     * Put player colors (team colors) into a {@link Map}
     */
    private void loadPlayerNames() {
        this.registeredTeams.forEach(team -> team.getMembers().forEach(player ->
                this.playerNames.put(player, team.getTeamColor().getColorCode() + player.getName())));
    }

    /**
     * Start countdown if enough players are online
     */
    public void handlePlayerJoin() {
        if(!this.lobbyCountdown.isRunning() && Bukkit.getOnlinePlayers().size() >= JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart()  &&
            JumpRace.getInstance().getLocationManager().getLoadedMaps().size() != 0)
            this.lobbyCountdown.startCountdown();
    }

    /**
     * Check if the lobby countdown should be stopped or the game should end
     * @param player Player who leaves the server
     */
    public void handlePlayerQuit(Player player) {
        if(this.gameState == GameState.LOBBY && Bukkit.getOnlinePlayers().size() < JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart())
            this.lobbyCountdown.reset(false);

        if(this.gameState == GameState.LOBBY) {
            final Team team = this.getTeamFromPlayer(player);
            if(team != null) team.getMembers().remove(player);

            JumpRace.getInstance().getInventoryManager().updateTeamSelectorInventory();
            return;
        }

        if(this.gameState == GameState.JUMPING || this.gameState == GameState.DEATHMATCH) {
            final Team team = this.getTeamFromPlayer(player);
            if(team != null) {
                team.getMembers().remove(player);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(team.getMembers().size() == 0) {
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-team-eliminated", team.getTeamColor().getColorCode(), team.getTeamColor().getDisplayName()));
                        team.setAlive(false);
                    } else
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("message-team-players-left", team.getTeamColor().getColorCode(), team.getTeamColor().getDisplayName(), String.valueOf(team.getMembers().size())));
                });
                this.checkTeams();
            }
            this.playersLeft.remove(player);
            this.playerNames.remove(player);
            this.spectatorManager.removeSpectator(player);
        }
    }

    /**
     * End the game if there are not enough players online
     */
    private void checkTeams() {
        if((int) this.registeredTeams.stream().filter(team -> team.getMembers().size() >= 1).count() < 2)
            this.endGame(this.registeredTeams.stream().filter(Team::isAlive).findAny().get());
    }

    private void endGame(Team winningTeam) {
        this.preJumpingCountdown.stop();
        this.jumpingCountdown.stop();
        this.preDeathMatchCountdown.stop();
        this.deathMatchCountdown.stop();

        this.gameState = GameState.ENDING;
        this.moduleRows.forEach((player, moduleRow) -> moduleRow.stopRespawnScheduler());

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(JumpRace.getInstance().getLocationManager().getLobbyLocation());
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-team-won", winningTeam.getTeamColor().getColorCode(), winningTeam.getTeamColor().getDisplayName()));
            JumpRace.getInstance().getInventoryManager().setInventory(player, InventoryManager.Type.ENDING);
            player.setLevel(0);
            player.setExp(0);
        });

        this.firework();
        Bukkit.getPluginManager().callEvent(new TeamWinEvent(winningTeam));
        this.endingCountdown.startCountdown();
    }

    /**
     * Spawn firework at the lobby location
     */
    private void firework() {
        final AtomicInteger spawned = new AtomicInteger(0);
        final Location fireworkLocation = JumpRace.getInstance().getLocationManager().getLobbyLocation();

        this.fireworkTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JumpRace.getInstance(), () -> {
            if(spawned.get() == 5) {
                Bukkit.getScheduler().cancelTask(this.fireworkTaskID);
                return;
            }

            final Firework firework =  fireworkLocation.getWorld().spawn(fireworkLocation, Firework.class);
            final FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withFlicker().withColor(Color.GREEN).build());
            fireworkMeta.setPower(2);

            spawned.getAndIncrement();
        }, 5, 10);
    }

    public Team getTeamFromPlayer(Player player) {
        return this.registeredTeams.stream().filter(team -> team.getMembers().contains(player)).findAny().orElse(null);
    }

    /**
     * Get every player who isn't in a team and add him to a random one
     */
    private void addPlayersToRandomTeams() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(this.getTeamFromPlayer(player) == null) {
                this.registeredTeams.stream().filter(team -> team.getMembers().size() <
                        JumpRace.getInstance().getJumpRaceConfig().getTeamSize()).limit(1).findAny().get().getMembers().add(player);
            }
        });
    }

    /**
     * Start a {@link BukkitScheduler}
     */
    public void startActionBar() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            if(this.gameState == GameState.LOBBY) {
                if(!this.lobbyCountdown.isRunning()) {
                    if(JumpRace.getInstance().getLocationManager().getLoadedMaps().size() == 0) {
                        Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-no-map")));
                        return;
                    }

                    final int playerLeft = JumpRace.getInstance().getJumpRaceConfig().getPlayersRequiredForStart() - Bukkit.getOnlinePlayers().size();
                    Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player,
                            ((playerLeft == 1) ? JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-waiting-player") : JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-waiting-players", String.valueOf(playerLeft)))));
                } else
                    Bukkit.getOnlinePlayers().forEach(player -> new ActionBarUtil().sendActionbar(player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-teaming-forbidden")));
            } else {
                Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> this.registeredTeams.forEach(team -> team.getMembers().forEach(player ->
                        new ActionBarUtil().sendActionbar(player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-team", team.getTeamColor().getColorCode(), team.getTeamColor().getDisplayName())))), 5, 200);

                this.spectatorManager.getSpectators().forEach(player -> new ActionBarUtil().sendActionbar(player, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("actionbar-spectator")));
            }
        }, 20, 20);
    }

    /**
     * Set the time left to 10 seconds
     * @param player Player who reaches the goal
     */
    public void reachGoal(Player player) {
        Bukkit.broadcastMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-goal-reached", JumpRace.getInstance().getGameManager().getPlayerNames().get(player)));
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL,1,1);
        player.getInventory().setBoots(new ItemManager(Material.DIAMOND_BOOTS).setUnbreakable(true).build());

        if(this.jumpingCountdown.getTimeLeft() > 10) {
            if(JumpRace.getInstance().getJumpRaceConfig().getTeamSize() != 1) {
                final AtomicBoolean skipJumpPhase = new AtomicBoolean(true);

                JumpRace.getInstance().getGameManager().getTeamFromPlayer(player).getMembers().forEach(member -> {
                    if(JumpRace.getInstance().getGameManager().getModuleRows().get(member).getModulesCompleted() != MAX_MODULES)
                        skipJumpPhase.set(false);
                });

                if(!skipJumpPhase.get()) {
                    player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-skip-info-1"));
                    player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-skip-info-2"));
                } else
                    this.jumpingCountdown.setTimeLeft(10);
            } else
                this.jumpingCountdown.setTimeLeft(10);
        }

        Bukkit.getPluginManager().callEvent(new PlayerReachGoalEvent(player));
    }

    /**
     * Sort the players by their progress
     * @return {@link Map} object containing the 12 best players with their progress
     * with {@link Player} as key and {@link Integer} as value
     */
    public Map<Player, Integer> getTopScoreboardPlayers() {
        final Map<Player, Integer> progress = new HashMap<>();
        final Map<Player, Integer> map = new LinkedHashMap<>();

        this.playersLeft.forEach(player -> progress.put(player, player.getLocation().getBlockX()));

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

    /**
     * Sort the players by their amount of lives
     * @return {@link Map} object containing the 12 best players with their lives
     * with {@link Player} as key and {@link Integer} as value
     */
    public Map<Player, Integer> getTopArenaPlayers() {
        final Map<Player, Integer> map = new LinkedHashMap<>();

        final LinkedHashMap<Player, Integer> sortedLivesMap = new LinkedHashMap<>();

        this.livesLeft.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(lives -> sortedLivesMap.put(lives.getKey(), lives.getValue()));

        sortedLivesMap.forEach((player, integer) -> {
            if(map.size() <= 12)
                map.put(player, integer);
        });

        return map;
    }

    /**
     * Teleports the players and spectators to the deathmatch arena and sets the {@link org.bukkit.scoreboard.Scoreboard}
     */
    private void startDeathmatch() {
        this.gameState = GameState.DEATHMATCH;
        final com.voxcrafterlp.jumprace.objects.Map map = JumpRace.getInstance().getLocationManager().getSelectedMap();

        final AtomicInteger index = new AtomicInteger(0);

        this.moduleRows.forEach((player, moduleRow) -> {
            moduleRow.stopRespawnScheduler();

            if(map.getSpawnLocations().size() == index.get())
                index.set(0);

            this.livesLeft.put(player, 3);
            player.teleport(map.getSpawnLocations().get(index.get()));
            player.setGameMode(GameMode.SURVIVAL);
            index.getAndIncrement();
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player ->
                new PlayerScoreboard().setScoreboard(player)), 20);

        this.preDeathMatchCountdown.startCountdown();

        this.getSpectatorManager().getSpectators().forEach(player -> player.teleport(JumpRace.getInstance().getLocationManager().getSelectedMap().getRandomSpawnLocation()));
    }

    /**
     * Removes the player's life in deathmatch.
     * If the player has no lives left, he'll be set into spectator mode and a {@link DeathChest} will be spawned at his death location
     * @param player Player whose life should be removed
     * @param drops List containing the player's items as {@link ItemStack} objects
     */
    public void removeLife(Player player, List<ItemStack> drops) {
        if(this.livesLeft.get(player) == 1) {
            this.spectatorManager.setSpectating(player);

            this.deathChests.add(new DeathChest(player, drops));

            final Team team = this.getTeamFromPlayer(player);
            if(team != null) {
                team.getMembers().remove(player);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(team.getMembers().size() == 0) {
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-team-eliminated", team.getTeamColor().getColorCode(), team.getTeamColor().getDisplayName()));
                        team.setAlive(false);
                    } else
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("message-team-players-left", team.getTeamColor().getColorCode(), team.getTeamColor().getDisplayName(), String.valueOf(team.getMembers().size())));
                });
                this.checkTeams();
            }
            return;
        }

        this.livesLeft.replace(player, (this.livesLeft.get(player) - 1));
    }

    /**
     * Calculate the winner based on his distance to end point
     */
    public void calculateWinner() {
        final Location endPointLocation = JumpRace.getInstance().getLocationManager().getSelectedMap().getEndPointLocation();
        final Player winner = this.playersLeft.stream().min(Comparator.comparing(player -> player.getLocation().distance(endPointLocation))).get();
        this.endGame(this.getTeamFromPlayer(winner));
    }

}
