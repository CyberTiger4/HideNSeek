package it.cybertiger.hidenseek.commands;

import it.cybertiger.hidenseek.game.GameManager;
import it.cybertiger.hidenseek.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;

public class StartCommand implements CommandExecutor, TabCompleter {

    private final GameManager gm;

    public StartCommand(GameManager gm) { this.gm = gm; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§cSolo gli operatori possono farlo.");
            return true;
        }

        if (gm.getState() != GameState.WAITING) {
            sender.sendMessage("§cUna partita è già in corso!");
            return true;
        }

        List<Player> online = new ArrayList<>(Bukkit.getOnlinePlayers());

        if(args.length == 0){
            gm.startPreGameCountdown(online);
            sender.sendMessage("§aCountdown pre-partita avviato!");
            return true;
        }

        List<Player> chosenSeekers = new ArrayList<>();
        for(String name: args){
            Player p = Bukkit.getPlayerExact(name);
            if(p != null) chosenSeekers.add(p);
        }

        if(chosenSeekers.isEmpty()){
            sender.sendMessage("§cNessun player valido.");
            return true;
        }

        gm.startPreGameCountdownWithSeekers(online, chosenSeekers);
        sender.sendMessage("§aCountdown pre-partita avviato con seeker scelti!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command cmd,
                                      String alias,
                                      String[] args) {
        if(!sender.isOp()) return Collections.emptyList();
        String current = args.length==0?"":args[args.length-1].toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(n->n.toLowerCase().startsWith(current))
                .collect(Collectors.toList());
    }
}
