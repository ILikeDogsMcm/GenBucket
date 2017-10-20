package net.oreocraft.play;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Methods {
	GenBuckets plugin;
	public Methods(GenBuckets plugin){
		this.plugin = plugin;
	}
	public ItemStack MakeItemStack(Material Type, int Amt, String DName, List<String> Lore, boolean UseLore){
	    ItemStack iss = new ItemStack(Type, Amt);
	    ItemMeta ism = iss.getItemMeta();
	    ism.setDisplayName(ChatColor.translateAlternateColorCodes('&', DName));
	    if (UseLore){
	      List<String> ColorCodedLore = new ArrayList<String>();
	      for (String CurLore : Lore) {
	        ColorCodedLore.add(ChatColor.translateAlternateColorCodes('&', CurLore));
	      }
	      ism.setLore(ColorCodedLore);
	    }
	    iss.setItemMeta(ism);
	    return iss;
	  }
	
	public void sendMessage(Player player, String message){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	 
	public void sellInv(Inventory inv, short glasscolor, ItemStack obsdian, ItemStack cobble){
		ItemStack gPane = new ItemStack(Material.STAINED_GLASS, 1, glasscolor);
		for(int x = 0; x < inv.getSize(); x++){
			inv.setItem(x, gPane);
		}
		inv.setItem(plugin.getConfig().getInt("genbucket.cobble.slot"), cobble);
		inv.setItem(plugin.getConfig().getInt("genbucket.obsdian.slot"), obsdian);
	}
	public boolean hasAvaliableSlot(Player player){
	    Inventory inv = player.getInventory();
	    for (ItemStack item: inv.getContents()) {
	         if(item == null) {
	                 return true;
	         }
	     }
	return false;
	}
}
