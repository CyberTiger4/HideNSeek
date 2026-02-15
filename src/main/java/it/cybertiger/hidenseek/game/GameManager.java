package it.cybertiger.hidenseek.game;

import it.cybertiger.hidenseek.HideAndSeek;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private final LobbyManager lobby;
    private GameState state = GameState.WAITING;
    private final Set<Player> seekers = new HashSet<>();
    private final Set<Player> hiders = new HashSet<>();
    private int taskId = -1;

    public GameManager(LobbyManager lobbyManager){ this.lobby = lobbyManager; }

    public GameState getState() { return state; }
    public boolean isSeeker(Player p){ return seekers.contains(p); }
    public boolean isHider(Player p){ return hiders.contains(p); }

    public void start(List<Player> players){
        if(players.isEmpty()) return;
        Player randomSeeker = players.get(new Random().nextInt(players.size()));
        startPreGameCountdownWithSeekers(players, Collections.singletonList(randomSeeker));
    }

    public void startWithSeekers(List<Player> players, List<Player> chosenSeekers){
        state = GameState.STARTING;
        seekers.clear();
        hiders.clear();

        seekers.addAll(chosenSeekers);

        double lobbyRadius = lobby.getRadius();
        int blindTime = HideAndSeek.get().getConfig().getInt("seeker-blind-time", 60);

        for(Player p: players){
            p.getInventory().clear();
            if(seekers.contains(p)){
                lobby.teleportToLobby(p);
                // Effetto cecità massima + immobilità
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindTime*20, 255, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, blindTime*20, 255, false, false));
            } else {
                Location loc = lobby.getRandomSpawn(lobbyRadius);
                if(loc != null){
                    p.teleport(loc);
                    hiders.add(p);
                }
            }
        }

        for(Player s: seekers)
            s.getInventory().addItem(getSeekerStick());

        Bukkit.broadcastMessage("§aPartita iniziata!");
        state = GameState.INGAME;
        startTimer();
    }

    private void startTimer(){
        int seconds = HideAndSeek.get().getConfig().getInt("seeker-time", 120);
        if(taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
        taskId = Bukkit.getScheduler().runTaskLater(HideAndSeek.get(), () -> endGame(false), seconds*20L).getTaskId();
    }

    // Countdown hype
    public void startPreGameCountdown(List<Player> players){
        if(players.size() < HideAndSeek.get().getConfig().getInt("min-players", 4)){
            Bukkit.broadcastMessage(ChatColor.RED + "Non ci sono abbastanza giocatori per avviare la partita!");
            return;
        }
        startPreGameCountdownWithSeekers(players, null);
    }

    public void startPreGameCountdownWithSeekers(List<Player> players, List<Player> chosenSeekers){
        int minPlayers = HideAndSeek.get().getConfig().getInt("min-players", 4);
        int countdown = HideAndSeek.get().getConfig().getInt("pre-start-time", 15);
        boolean chatEnabled = HideAndSeek.get().getConfig().getBoolean("pre-start-chat", true);

        new BukkitRunnable() {
            int timeLeft = countdown;

            @Override
            public void run() {
                if(players.size() < minPlayers){
                    Bukkit.broadcastMessage(ChatColor.RED + "Giocatori insufficienti, countdown annullato!");
                    cancel();
                    return;
                }

                if(chatEnabled && timeLeft > 10)
                    Bukkit.broadcastMessage(ChatColor.AQUA + "La partita inizierà tra " + timeLeft + " secondi!");

                if(timeLeft <= 10 && timeLeft > 0){
                    ChatColor color = (timeLeft >=6) ? ChatColor.GREEN : (timeLeft >=4) ? ChatColor.GOLD : ChatColor.RED;
                    for(Player p : players){
                        p.sendTitle(color + "" + timeLeft, "", 0, 20, 0);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
                    }
                }

                if(timeLeft == 0){
                    cancel();
                    if(chosenSeekers == null || chosenSeekers.isEmpty())
                        start(players);
                    else
                        startWithSeekers(players, chosenSeekers);
                }

                timeLeft--;
            }
        }.runTaskTimer(HideAndSeek.get(),0L,20L);
    }

    public void killHider(Player hider){
        if(!hiders.contains(hider)) return;
        hiders.remove(hider);
        hider.setGameMode(GameMode.SPECTATOR);
        hider.sendMessage("§cSei morto!");
        if(hiders.isEmpty()) endGame(true);
    }

    private void endGame(boolean seekersWin){
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
        state = GameState.END;

        for(Player p : Bukkit.getOnlinePlayers()){
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
        }

        if(seekersWin){
            for(Player s: seekers) s.sendMessage("§aHai vinto!");
            Bukkit.broadcastMessage("§6I SEEKERS hanno vinto!");
        } else {
            for(Player h: hiders) h.sendMessage("§aHai vinto!");
            Bukkit.broadcastMessage("§6Gli HIDER hanno vinto!");
        }

        seekers.clear();
        hiders.clear();
        state = GameState.WAITING;
    }

    public ItemStack getSeekerStick(){
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta m = stick.getItemMeta();
        m.setDisplayName("§6Seeker Stick");
        m.getPersistentDataContainer().set(
                new NamespacedKey(HideAndSeek.get(),"seeker_stick"),
                org.bukkit.persistence.PersistentDataType.INTEGER,
                1
        );
        stick.setItemMeta(m);
        return stick;
    }

    public boolean isSeekerStick(ItemStack item){
        if(item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(
                new NamespacedKey(HideAndSeek.get(),"seeker_stick"),
                org.bukkit.persistence.PersistentDataType.INTEGER
        );
    }
}
