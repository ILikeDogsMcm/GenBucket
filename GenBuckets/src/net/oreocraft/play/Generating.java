package net.oreocraft.play;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class Generating implements Listener {
	GenBuckets plugin;

	public Generating(GenBuckets plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(PlayerBucketEmptyEvent e) {

		if (e.isCancelled()) {
			return;
		}
		if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(
				ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("genbucket.cobble.name")))) {
			if (e.getPlayer().getItemInHand().getAmount() == 1) {
				e.getPlayer().getItemInHand().setType(Material.BUCKET);
			} else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
			}
			e.getBlockClicked().getRelative(e.getBlockFace()).getLocation().getBlock().setType(Material.COBBLESTONE);

			new BukkitRunnable() {

				int x = 0;

				@Override
				public void run() {
					x--;
					Location newblock = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation().add(0, x, 0);
					if (newblock.getBlock().getType() != Material.AIR) {
						cancel();
						return;
					}

					if(newblock.add(0, 3, 0).getBlock().getType() == Material.SPONGE){
						cancel();
						return;
					}
					if(newblock.add(0, 0, -3).getBlock().getType() == Material.SPONGE){
						cancel();
						return;
					}
					if (newblock.add(3, 0, 0).getBlock().getType() == Material.SPONGE) {
						cancel();
						
						return;
					}
					if(newblock.add(-3, 0, 0).getBlock().getType() == Material.SPONGE){
						cancel();
						
						return;
					}
					if(newblock.add(0, -3, 0).getBlock().getType() == Material.SPONGE){
						cancel();
						
						return;
					}
					if(newblock.add(0, 0, 3).getBlock().getType() == Material.SPONGE){
						cancel();
						return;
					}
					newblock.getBlock().setType(Material.COBBLESTONE);
				}
			}.runTaskTimer(plugin, 20L, 20L);

		}
		if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(
				ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("genbucket.obsdian.name")))) {
			if (e.getPlayer().getItemInHand().getAmount() == 1) {
				e.getPlayer().getItemInHand().setType(Material.BUCKET);
			} else {
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
				e.getPlayer().getInventory().addItem(new ItemStack(Material.BUCKET));
			}
			e.getBlockClicked().getRelative(e.getBlockFace()).getLocation().getBlock().setType(Material.OBSIDIAN);

			new BukkitRunnable() {

				int x = 0;

				@Override
				public void run() {
					x--;
					Location newblock = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation().add(0, x, 0);
					if (newblock.getBlock().getType() != Material.AIR) {
						cancel();
						return;
					}

					if (newblock.add(3, 0, 0).getBlock().getType() == Material.SPONGE
							|| newblock.add(-3, 0, 0).getBlock().getType() == Material.SPONGE
							|| newblock.add(0, -3, 0).getBlock().getType() == Material.SPONGE
							|| newblock.add(0, 3, 0).getBlock().getType() == Material.SPONGE
							|| newblock.add(0, 0, 3).getBlock().getType() == Material.SPONGE
							|| newblock.add(0, 0, -3).getBlock().getType() == Material.SPONGE) {
						cancel();
						;
						return;
					}
					newblock.getBlock().setType(Material.OBSIDIAN);
				}
			}.runTaskTimer(plugin, 20L, 20L);
		}
	}
}
