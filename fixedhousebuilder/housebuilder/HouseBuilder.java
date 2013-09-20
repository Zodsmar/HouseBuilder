package housebuilder;

import housebuilder.recipes.Recipes;
import housebuilder.util.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "HouseBuilder", name = "HouseBuilder", version = "0.2.0" )
@NetworkMod(clientSideRequired = true, serverSideRequired = true,packetHandler = PacketHandler.class,channels={"HouseBuilder"})

public class HouseBuilder 
{
	@Instance("HouseBuilder")
	public static HouseBuilder instance;
	
	private GuiHandlerHouseBuilder guiHandlerHouseBuilder = new GuiHandlerHouseBuilder();
	
	public static Block houseBuilder;
	public static int houseBuilderID;
	
	@SidedProxy(clientSide = "housebuilder.util.ClientProxy", serverSide = "housebuilder.util.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		houseBuilderID = config.getBlock("builder ID", 3006, (String)null).getInt();
		
		config.save();
		
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		houseBuilder = new BlockHouseBuilder(houseBuilderID).setHardness(2.5F).setResistance(2000.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("builder").setCreativeTab(CreativeTabs.tabBlock);
		
		GameRegistry.registerBlock(houseBuilder, "houseBuilder");
		LanguageRegistry.addName(houseBuilder, "House Builder");
		
		
		Recipes.init();
		//GameRegistry.addRecipe(new ItemStack(houseBuilder, 1), new Object[] {"HPN", "PSP", " P ", Character.valueOf('P'), Block.planks, Character.valueOf('H'), this.hammer, Character.valueOf('N'), this.nail, Character.valueOf('S'), this.handSaw});
		
		//GameRegistry.registerCraftingHandler(new BetterCraftingHandler());
		
		GameRegistry.registerTileEntity(TileEntityHouseBuilder.class, "TileEntityHouseBuilder");
		NetworkRegistry.instance().registerGuiHandler(this, guiHandlerHouseBuilder);
		
		proxy.registerRenderThings();
		
	}

	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		
	}
	
	
	

}