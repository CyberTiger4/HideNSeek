package it.cybertiger.hidenseek.game;

import it.cybertiger.hidenseek.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class LobbyManager {

    private Location lobbyLocation;
    private double lobbyRadius = 20.0;

    public void setLobby(Player setter, String[] args){
        World world = setter.getWorld();

        double x = setter.getLocation().getX();
        double y = setter.getLocation().getY();
        double z = setter.getLocation().getZ();
        float yaw = setter.getLocation().getYaw();
        float pitch = setter.getLocation().getPitch();

        if(args.length >= 3){
            try {
                x = Double.parseDouble(args[0]);
                y = Double.parseDouble(args[1]);
                z = Double.parseDouble(args[2]);
                if(args.length >= 5){
                    yaw = Float.parseFloat(args[3]);
                    pitch = Float.parseFloat(args[4]);
                }
            } catch (NumberFormatException e){
                setter.sendMessage("§cCoordinate non valide!");
                return;
            }
        }

        lobbyLocation = new Location(world, x, y, z, yaw, pitch);
        setter.sendMessage("§aLobby settata in: " + x + ", " + y + ", " + z);
        saveToConfig();
    }

    public Location getLobby(){ return lobbyLocation; }

    private void saveToConfig(){
        if(lobbyLocation == null) return;
        HideAndSeek.get().getConfig().set("lobby.world", lobbyLocation.getWorld().getName());
        HideAndSeek.get().getConfig().set("lobby.x", lobbyLocation.getX());
        HideAndSeek.get().getConfig().set("lobby.y", lobbyLocation.getY());
        HideAndSeek.get().getConfig().set("lobby.z", lobbyLocation.getZ());
        HideAndSeek.get().getConfig().set("lobby.yaw", lobbyLocation.getYaw());
        HideAndSeek.get().getConfig().set("lobby.pitch", lobbyLocation.getPitch());
        HideAndSeek.get().getConfig().set("lobby.radius", lobbyRadius);
        HideAndSeek.get().saveConfig();
    }

    public void loadFromConfig(){
        String worldName = HideAndSeek.get().getConfig().getString("lobby.world");
        World world = Bukkit.getWorld(worldName);
        if(world == null) return;

        double x = HideAndSeek.get().getConfig().getDouble("lobby.x");
        double y = HideAndSeek.get().getConfig().getDouble("lobby.y");
        double z = HideAndSeek.get().getConfig().getDouble("lobby.z");
        float yaw = (float) HideAndSeek.get().getConfig().getDouble("lobby.yaw");
        float pitch = (float) HideAndSeek.get().getConfig().getDouble("lobby.pitch");
        lobbyRadius = HideAndSeek.get().getConfig().getDouble("lobby.radius", 20.0);

        lobbyLocation = new Location(world, x, y, z, yaw, pitch);
    }

    public void teleportToLobby(Player p){ if(lobbyLocation != null) p.teleport(lobbyLocation); }

    // Genera spawn random fuori dal raggio della lobby
    public Location getRandomSpawn(double minDistanceFromLobby){
        if(lobbyLocation == null) return null;
        World w = lobbyLocation.getWorld();
        Random r = new Random();
        Location loc;
        int attempts = 0;
        do{
            double x = r.nextInt(1000) - 500;
            double z = r.nextInt(1000) - 500;
            double y = w.getHighestBlockYAt((int)x, (int)z) + 1;
            loc = new Location(w, x, y, z);
            attempts++;
        } while(loc.distance(lobbyLocation) < minDistanceFromLobby && attempts < 50);
        return loc;
    }

    public double getRadius(){ return lobbyRadius; }
}
