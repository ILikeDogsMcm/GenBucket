package net.oreocraft.play;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class GenBuckets extends JavaPlugin implements Listener {
	public static Economy econ = null;
	private static final Logger log = Logger.getLogger("Minecraft");
	Methods meth;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new Generating(this), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		File configFile = new File(getDataFolder() + "/config.yml");
		if (!configFile.exists())
			saveResource("config.yml", false);
		meth = new Methods(this);
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEcononomy() {
		return econ;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle()
				.equals(ChatColor.translateAlternateColorCodes('&', getConfig().getString("gui.name")))) {
			e.setCancelled(true);
			if(e.getSlotType() != SlotType.CONTAINER) return;
			List<String> obsdianlores = getConfig().getStringList("genbucket.obsdian.lores");
			ItemStack obsdianitem = meth.MakeItemStack(Material.LAVA_BUCKET,
					getConfig().getInt("genbucket.obsdian.amount"), getConfig().getString("genbucket.obsdian.name"),
					obsdianlores, true);
			List<String> cobblelores = getConfig().getStringList("genbucket.cobble.lores");
			ItemStack cobbleitem = meth.MakeItemStack(Material.LAVA_BUCKET,
					getConfig().getInt("genbucket.cobble.amount"), getConfig().getString("genbucket.cobble.name"),
					cobblelores, true);
			int cobble = getConfig().getInt("genbucket.cobble.slot");
			int obsdian = getConfig().getInt("genbucket.obsdian.slot");
			int cobbleprice = getConfig().getInt("genbucket.cobble.price");
			int obsdianprice = getConfig().getInt("genbucket.obsdian.price");
			Player player = (Player) e.getWhoClicked();
			if (e.getSlot() == cobble) {
				if (econ.getBalance(e.getWhoClicked().getName()) >= cobbleprice) {
					if(!meth.hasAvaliableSlot(player)){
						meth.sendMessage(player, getConfig().getString("gui.inv_full"));
						return ;
					}
					econ.withdrawPlayer(e.getWhoClicked().getName(), cobble);
					e.getWhoClicked().getInventory().addItem(cobbleitem);
					meth.sendMessage(player, getConfig().getString("gui.buy_message"));
				} else {
					meth.sendMessage(player, getConfig().getString("gui.not_enough_money"));
				}
			} else if (e.getSlot() == obsdian) {
				if (econ.getBalance(e.getWhoClicked().getName()) >= obsdianprice) {
					if(!meth.hasAvaliableSlot(player)){
						meth.sendMessage(player, getConfig().getString("gui.inv_full"));
						return ;
					}
					econ.withdrawPlayer(e.getWhoClicked().getName(), obsdianprice);
					e.getWhoClicked().getInventory().addItem(obsdianitem);
					meth.sendMessage(player, getConfig().getString("gui.buy_message"));
				} else {
					meth.sendMessage(player, getConfig().getString("gui.not_enough_money"));
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("genbucket")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage("test");
				List<String> cobblelores = getConfig().getStringList("genbucket.cobble.lores");
				cobblelores.add(getConfig().getInt("genbucket.cobble.price") + "$");
				ItemStack cobble = meth.MakeItemStack(Material.LAVA_BUCKET,
						getConfig().getInt("genbucket.cobble.amount"), getConfig().getString("genbucket.cobble.name"),
						cobblelores, true);
				List<String> obsdianlores = getConfig().getStringList("genbucket.obsdian.lores");
				obsdianlores.add(getConfig().getInt("genbucket.obsdian.price") + "$");
				ItemStack obsdian = meth.MakeItemStack(Material.LAVA_BUCKET,
						getConfig().getInt("genbucket.obsdian.amount"), getConfig().getString("genbucket.obsdian.name"),
						obsdianlores, true);
				Inventory inv = Bukkit.createInventory(player, 18,
						ChatColor.translateAlternateColorCodes('&', getConfig().getString("gui.name")));
				meth.sellInv(inv, (short) 15, obsdian, cobble);
				player.openInventory(inv);
			} else {
				System.out.println("This is a player only command");
			}
		}
		return false;
	}
}
