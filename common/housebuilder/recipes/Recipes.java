package housebuilder.recipes;

import housebuilder.HouseBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;

public class Recipes {

	public static void init(){
		
		CraftingManager.getInstance().addRecipe(new ItemStack(HouseBuilder.houseBuilder, 1, 0),
				new Object[] {
				"000",
				"010",
				"000",
				Character.valueOf('0'), new ItemStack(Block.planks, 1, OreDictionary.WILDCARD_VALUE),
				Character.valueOf('1'), new ItemStack(Block.workbench, 1)});
		
	}
}
