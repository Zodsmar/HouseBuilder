package housebuilder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerHouseBuilder extends Container
{
    private TileEntityHouseBuilder houseBuilder;
    @SuppressWarnings("unused")
	private int level;

    public ContainerHouseBuilder(InventoryPlayer invPlayer, TileEntityHouseBuilder tileentity, int i)
    {
        level = i;
        houseBuilder = tileentity;

        for (int j = 0; j < 10; j++)
        {
            for (int i1 = 0; i1 < 10; i1++)
            {
                this.addSlotToContainer(new SlotHouseBuilder(tileentity, (i - 1) * 100 + j * 10 + i1, i1 * 18 - 100, (j * 18 + 4) - 38));
            }
        }

        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
            	this.addSlotToContainer(new Slot(invPlayer, j1 + k * 9 + 9, 8 + j1 * 18 + 72 + 1, k * 18 - 33));
            }
        }

        for (int l = 0; l < 9; l++)
        {
        	this.addSlotToContainer(new Slot(invPlayer, l, 8 + l * 18 + 72 + 1, 21));
        }
    }

    protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return houseBuilder.isUseableByPlayer(entityplayer);
    }
}
