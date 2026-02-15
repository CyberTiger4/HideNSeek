package it.cybertiger.hidenseek.listeners;

import it.cybertiger.hidenseek.game.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameListener implements Listener {

    private final GameManager gm;

    public GameListener(GameManager gm){ this.gm = gm; }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)) return;
        if(!(e.getEntity() instanceof Player)) return;

        Player damager = (Player)e.getDamager();
        Player victim = (Player)e.getEntity();

        if(gm.getState() != GameState.INGAME) return;
        e.setCancelled(true);

        if(!gm.isSeeker(damager)) return;
        if(!gm.isSeekerStick(damager.getInventory().getItemInMainHand())) return;

        if(gm.isHider(victim)) gm.killHider(victim);
    }
}
