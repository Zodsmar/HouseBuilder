package housebuilder;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotHouseBuilder extends Slot
{
	public SlotHouseBuilder(IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        if(i < 0)
        {
        	int kvb = 0;
        }
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemstack)
    {
        int i = itemstack.getItem().itemID;
        return i <= 256 || i == Item.bucketWater.itemID || i == Item.bucketLava.itemID || i == Item.redstone.itemID || i == Item.minecartEmpty.itemID || i == Item.boat.itemID || i == Item.minecartPowered.itemID || i == Item.minecartCrate.itemID;
    }
}
