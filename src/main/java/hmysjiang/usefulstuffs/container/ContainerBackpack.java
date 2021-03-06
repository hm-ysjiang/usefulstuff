package hmysjiang.usefulstuffs.container;

import hmysjiang.usefulstuffs.items.baubles.ItemBackpack;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class ContainerBackpack extends ContainerItem {
	
	public ContainerBackpack(EntityPlayer player, IInventory inv, ItemStack stack, int size, boolean onKey) {
		super(player, inv, stack, size);
		
		if (onKey)
			blocked = -1;
		
		int xH = 8, yH = 18;

		for (int y = 0 ; y < 6 ; y++) {
			for (int x = 0 ; x < 9 ; x++) {
				this.addSlotToContainer(new SlotNotBag(handler, x + (y * 9), xH + x * 18, yH + y * 18));
			}
		}

		
		int xPos = 8;
		int yPos = 140;
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}
		
		for (int x = 0; x < 9; ++x) {
			this.addSlotToContainer(new Slot(inv, x, xPos + x * 18, yPos + 58));
		}
	}
	
	public class SlotNotBag extends SlotItemHandler {

		public SlotNotBag(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stackIn) {
			return super.isItemValid(stackIn) && checkHasBackpack(stackIn);
		}
		
		// Return true if the stack should be able to insert to handler
		private boolean checkHasBackpack(ItemStack stack) {
			if (!(stack.getItem() instanceof ItemBackpack))
				return true;
			ItemStackHandler handler = getDeserializedHandler(stack, size);
			for (int i = 0 ; i<handler.getSlots() ; i++) {
				if (handler.getStackInSlot(i).getItem() instanceof ItemBackpack)
					return false;
			}
			return true;
		}
		
	}

}
