package housebuilder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerHouseBuilder implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntityHouseBuilder tile_entity = (TileEntityHouseBuilder)world.getBlockTileEntity(x, y, z);
		switch(id)
		{	
			case 0: 
				return new ContainerHouseBuilder(player.inventory, (TileEntityHouseBuilder) tile_entity, tile_entity.getLayer());
		}
		return null;
	}
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntityHouseBuilder tile_entity = (TileEntityHouseBuilder)world.getBlockTileEntity(x, y, z);
		switch(id)
		{		
			case 0: 
				return new GuiHouseBuilder(player.inventory, (TileEntityHouseBuilder) tile_entity, tile_entity.getLayer(), player);
		}
		return null;
	}	
}