package housebuilder;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHouseBuilder extends BlockContainer
{
    private Random rand;
    private static boolean keepInventory = false;
    @SideOnly(Side.CLIENT)
    private Icon workbenchIconTop;
    @SideOnly(Side.CLIENT)
    private Icon workbenchIconFront;

    protected BlockHouseBuilder(int i)
    {
        super(i, Material.rock);
        rand = new Random();
    }
    
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.workbenchIconTop : (par1 == 0 ? Block.planks.getBlockTextureFromSide(par1) : (par1 != 2 && par1 != 4 ? this.blockIcon : this.workbenchIconFront));
    }
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("housebuilder:sides");
		this.workbenchIconTop = par1IconRegister.registerIcon("housebuilder:top");
		this.workbenchIconFront = par1IconRegister.registerIcon("housebuilder:HouseBlock");
    }

	  @Override
	    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

	        if (player.isSneaking())
	            return false;
	        else {
	            if (!world.isRemote) {
	            	TileEntityHouseBuilder tileentityhousebuilder = (TileEntityHouseBuilder)world.getBlockTileEntity(x, y, z);
	        		
	        		if (tileentityhousebuilder != null)
	        			{
	        				player.openGui(HouseBuilder.instance, 0,  world, x, y, z);
	        				return true;
	        			}

	            }

	            return true;
	        }
	    }
	

    /**
     * Returns the TileEntity used by this block.
     */
    public TileEntity getBlockEntity()
    {
        return new TileEntityHouseBuilder();
    }
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (!keepInventory)
        {
        	TileEntityHouseBuilder var7 = (TileEntityHouseBuilder)par1World.getBlockTileEntity(par2, par3, par4);

            if (var7 != null)
            {
                for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
                {
                    ItemStack itemstack = var7.getStackInSlot(var8);

                    if (itemstack != null)
                    {
                        float f = rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0)
                        {
                            int k1 = rand.nextInt(21) + 10;

                            if (k1 > itemstack.stackSize)
                            {
                                k1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= k1;
                            EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound())
                            {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double)((float)rand.nextGaussian() * f3);
                            entityitem.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double)((float)rand.nextGaussian() * f3);
                            par1World.spawnEntityInWorld(entityitem);
                        }
                    }
                }
                par1World.func_96440_m(par2, par3, par4, par5);
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    @Override
	public TileEntity createNewTileEntity(World world) 
	{
		return new TileEntityHouseBuilder();
	}
}