package it.cybertiger.hidenseek;

import it.cybertiger.hidenseek.commands.SetLobbyCommand;
import it.cybertiger.hidenseek.commands.StartCommand;
import it.cybertiger.hidenseek.game.GameManager;
import it.cybertiger.hidenseek.game.LobbyManager;
import it.cybertiger.hidenseek.listeners.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

public class HideAndSeek extends JavaPlugin {

    private static HideAndSeek instance;
    private GameManager gameManager;
    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        lobbyManager = new LobbyManager();
        lobbyManager.loadFromConfig();

        gameManager = new GameManager(lobbyManager);

        getServer().getPluginManager().registerEvents(new GameListener(gameManager), this);

        getCommand("hsstart").setExecutor(new StartCommand(gameManager));
        getCommand("hsstart").setTabCompleter(new StartCommand(gameManager));

        getCommand("hssetlobby").setExecutor(new SetLobbyCommand(lobbyManager));
    }

    public static HideAndSeek get() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}
