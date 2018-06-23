package hmysjiang.usefulstuffs.miscs.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryHelper {
	
	public static ItemStack findStackInPlayerInventory(EntityPlayer player, ItemStack template) {
		InventoryPlayer playerInv = player.inventory;
		for (ItemStack stack:playerInv.mainInventory) {
			if (stack == null)
				continue;
			if (stack.isItemEqual(template)) {
				return stack;
			}
		}
		for (ItemStack stack:playerInv.offHandInventory) {
			if (stack == null)
				continue;
			if (stack.isItemEqual(template)) {
				return stack;
			}
		}
		return null;
	}
	
}