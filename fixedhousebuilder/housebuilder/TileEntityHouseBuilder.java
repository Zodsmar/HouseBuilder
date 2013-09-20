package housebuilder;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHouseBuilder extends TileEntity implements IInventory
{
    private ItemStack items[];
    private Random rand;
    public boolean built;
    public static ItemStack copiedItems[];

    public TileEntityHouseBuilder()
    {
        built = false;
        rand = new Random();
        items = new ItemStack[2000];
    }
    
    int layer=1;
    public void setLayer(int layer)
    {
    		this.layer=layer;
    }
    
    public int getLayer()
    {
    	return layer;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return items.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return items[i];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (items[i] != null)
        {
            if (items[i].stackSize <= j)
            {
                ItemStack itemstack = items[i];
                items[i] = null;
                return itemstack;
            }

            ItemStack itemstack1 = items[i].splitStack(j);

            if (items[i].stackSize == 0)
            {
                items[i] = null;
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (items[i] != null)
        {
            ItemStack itemstack = items[i];
            items[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (items[i] == itemstack)
        {
            decrStackSize(i, 1);
        }

        items[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }
    public void copy(EntityPlayer entityplayer)
    {
        if (entityplayer !=null)
        {
        	PacketHandler.sendPacketCopy(xCoord,yCoord,zCoord,entityplayer.worldObj.provider.dimensionId);
        }

        if (copiedItems == null)
        {
            copiedItems = new ItemStack[2000];
        }

        for (int i = 0; i < items.length; i++)
        {
            copiedItems[i] = items[i];
        }
    }

    public void paste(EntityPlayer entityplayer)
    {
    	if (entityplayer !=null)
        {
    		if(entityplayer.capabilities.isCreativeMode)
    			PacketHandler.sendPacketPaste(xCoord,yCoord,zCoord,entityplayer.worldObj.provider.dimensionId);
            return;
        }

        if (copiedItems != null)
        {
            for (int i = 0; i < items.length; i++)
            {
                items[i] = copiedItems[i];
            }
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "House Builder";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        items = new ItemStack[getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getInteger("Slot");

            if (j < 0 || j >= items.length)
            {
                continue;
            }

            if (ItemStack.loadItemStackFromNBT(nbttagcompound1).getItem().itemID == Item.stick.itemID)
            {
                items[j] = null;
            }
            else
            {
                items[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < items.length; i++)
        {
            boolean flag = false;

            if (items[i] == null)
            {
                flag = true;
                items[i] = new ItemStack(Item.stick, 1);
            }

            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setInteger("Slot", i);
            items[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);

            if (flag)
            {
                items[i] = null;
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        else
        {
            return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
        }
    }

    public void openChest()
    {
    }

    public void clear(EntityPlayer entityplayer)
 {
		if (entityplayer != null) {
			PacketHandler.sendPacketClear(xCoord, yCoord, zCoord,entityplayer.worldObj.provider.dimensionId);
			return;
		}

		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack itemstack = getStackInSlot(i);

			if (itemstack == null) {
				continue;
			}

			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;

			if (itemstack.stackSize <= 0) {
				continue;
			}

			int k = rand.nextInt(21) + 10;

			if (k > itemstack.stackSize) {
				k = itemstack.stackSize;
			}

			itemstack.stackSize -= k;
			EntityItem entityitem = new EntityItem(worldObj,
					(float) xCoord + f, (float) yCoord + f1, (float) zCoord
							+ f2, new ItemStack(itemstack.itemID, k,
							itemstack.getItemDamage()));

			if (itemstack.hasTagCompound()) {
				entityitem.getEntityItem().stackTagCompound = (NBTTagCompound) itemstack
						.getTagCompound().copy();
			}

			float f3 = 0.05F;
			entityitem.motionX = (float) rand.nextGaussian() * f3;
			entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float) rand.nextGaussian() * f3;
			worldObj.spawnEntityInWorld(entityitem);
		}

        for (int j = 0; j < items.length; j++)
        {
            items[j] = null;
        }
    }

    public void closeChest()
    {
    }

    public void createHouse(EntityPlayer entityplayer)
    {
        if (entityplayer != null)
        {
        	entityplayer.closeScreen();
            PacketHandler.sendPacketBuild(xCoord,yCoord,zCoord,entityplayer.worldObj.provider.dimensionId);
            return;
        }

        built = true;

        for (int i = 0; i < 5; i++)
        {
            for (int k = 0; k < 10; k++)
            {
                for (int i1 = 0; i1 < 10; i1++)
                {
                    worldObj.destroyBlock(xCoord + k + 1, yCoord + i, zCoord + i1 + 1, true);
                }
            }
        }

        this.invalidate();
        worldObj.setBlock(xCoord, yCoord, zCoord, 0);

        for (int j = 0; j < 5; j++)
        {
            for (int l = 0; l < 10; l++)
            {
                for (int j1 = 0; j1 < 10; j1++)
                {
                    if (items[j * 100 + l + j1 * 10] == null)
                    {
                        continue;
                    }

                    int k1 = items[j * 100 + j1 * 10 + l].getItem().itemID;

                    if (k1 == Item.bucketWater.itemID)
                    {
                        EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, new ItemStack(Item.bucketEmpty, 1));
                        entityitem.delayBeforeCanPickup = 10;
                        worldObj.spawnEntityInWorld(entityitem);
                        k1 = Block.waterStill.blockID;
                    }

                    if (k1 == Item.bucketLava.itemID)
                    {
                        EntityItem entityitem1 = new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, new ItemStack(Item.bucketEmpty, 1));
                        entityitem1.delayBeforeCanPickup = 10;
                        worldObj.spawnEntityInWorld(entityitem1);
                        k1 = Block.lavaMoving.blockID;
                    }

                    if (k1 == Item.redstone.itemID)
                    {
                        k1 = Block.redstoneWire.blockID;
                    }

                    if (k1 == Item.minecartEmpty.itemID)
                    {
                        EntityMinecart entityminecart = new EntityMinecartEmpty(worldObj, xCoord + l + 1, yCoord + j, zCoord + j1 + 1);
                        worldObj.spawnEntityInWorld(entityminecart);
                        continue;
                    }

                    if (k1 == Item.minecartPowered.itemID)
                    {
                        EntityMinecart entityminecart1 = new EntityMinecartFurnace(worldObj, xCoord + l + 1, yCoord + j, zCoord + j1 + 1);
                        worldObj.spawnEntityInWorld(entityminecart1);
                        continue;
                    }

                    if (k1 == Item.minecartCrate.itemID)
                    {
                        EntityMinecart entityminecart2 = new EntityMinecartEmpty(worldObj, xCoord + l + 1, yCoord + j, zCoord + j1 + 1);
                        worldObj.spawnEntityInWorld(entityminecart2);
                        continue;
                    }

                    if (k1 == Item.boat.itemID)
                    {
                        EntityBoat entityboat = new EntityBoat(worldObj, xCoord + l + 1, yCoord + j, zCoord + j1 + 1);
                        worldObj.spawnEntityInWorld(entityboat);
                    }
                    else
                    {
                        int l1 = items[j * 100 + j1 * 10 + l].getItemDamage();
                        worldObj.setBlock(xCoord + 1 + l, yCoord + j, zCoord + j1 + 1, k1);
                        worldObj.setBlockMetadataWithNotify(xCoord + 1 + l, yCoord + j, zCoord + j1 + 1, l1, 3);
                        worldObj.notifyBlockOfNeighborChange(xCoord + 1 + l, yCoord + j, zCoord + j1 + 1, k1);
                    }
                }
            }
        }
        
    }

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

}
