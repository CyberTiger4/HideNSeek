package it.cybertiger.hidenseek.commands;

import it.cybertiger.hidenseek.game.LobbyManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {

    private final LobbyManager lobbyManager;

    public SetLobbyCommand(LobbyManager lobbyManager){
        this.lobbyManager = lobbyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Solo i giocatori possono usare questo comando.");
            return true;
        }

        Player p = (Player)sender;

        if(!p.isOp()){
            p.sendMessage("§cSolo gli operatori possono usare questo comando.");
            return true;
        }

        lobbyManager.setLobby(p, args);
        return true;
    }
}
