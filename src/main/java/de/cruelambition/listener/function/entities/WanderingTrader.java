package de.cruelambition.listener.function.entities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class WanderingTrader implements Listener {

	@EventHandler
	public void handle(VillagerAcquireTradeEvent e) {
		MerchantRecipe rec = e.getRecipe();

		if (!(e.getEntity() instanceof org.bukkit.entity.WanderingTrader))
			if (rec.getResult().getType() == Material.ROOTED_DIRT) {

				ItemStack cd = new ItemStack(Material.COARSE_DIRT);
				cd.setAmount(3);

				MerchantRecipe recipe = new MerchantRecipe(cd, 2, 6, true,
						2, 1.15f, true);
				e.setRecipe(recipe);
			}

		rec.setExperienceReward(true);
		rec.setMaxUses(rec.getMaxUses() + 2);
	}
}
