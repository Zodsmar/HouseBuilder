package housebuilder;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class GuiHouseBuilder extends GuiContainer
{
    private int level;
    private TileEntityHouseBuilder tileEntity;


    private EntityPlayer player;
    @SuppressWarnings("unused")
	private InventoryPlayer in;

    public GuiHouseBuilder(InventoryPlayer invPlayer, TileEntityHouseBuilder tileentity, int i, EntityPlayer entityplayer)
    {
        super(new ContainerHouseBuilder(invPlayer, tileentity, i));
        in = invPlayer;
        tileEntity = tileentity;
        level = i;
        player = entityplayer;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    
    private static final ResourceLocation tex1 = new ResourceLocation("housebuilder:HouseGui.png");
    private static final ResourceLocation tex2 = new ResourceLocation("housebuilder:HouseGuiInv.png");
    
    @SuppressWarnings("unchecked")
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(tex1);
        int i1 = (width - xSize) / 2 - 100;
        int j1 = (height - ySize) / 2 - 35;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, i1 + 190, j1 + 78, 100, 20, "Go Up"));
        this.buttonList.add(new GuiButton(1, i1 + 190, j1 + 118, 100, 20, "Go Down"));
        this.buttonList.add(new GuiButton(2, i1 + 190, j1 + 148, 100, 20, "Create House"));
        this.buttonList.add(new GuiButton(3, i1 + 296, j1 + 78, 40, 20, "Copy"));
        this.buttonList.add(new GuiButton(4, i1 + 296, j1 + 102, 40, 20, "Paste"));
        this.buttonList.add(new GuiButton(5, i1 + 296, j1 + 138, 40, 20, "Clear"));
        drawTexturedModalRect(i1, j1, 0, 0, 256, 256);
        this.mc.renderEngine.func_110577_a(tex2);
        drawTexturedModalRect(i1 + 180, j1, 0, 0, 256, 256);
        fontRenderer.drawString((new StringBuilder()).append("Level: ").append(level).toString(), i1 + 220, j1 + 105, 0);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0 && level < 5)
        {
            level++;

            if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
            {
            	player.closeScreen();
            	PacketHandler.sendPacketLayer(tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord,tileEntity.worldObj.provider.dimensionId,level,player.username);
            }
            
            tileEntity.setLayer(level);

            player.openGui(HouseBuilder.instance, 0, player.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
     }

        if (guibutton.id == 1 && level > 1)
        {
            level--;

            if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT)
            {
            	player.closeScreen();
            	PacketHandler.sendPacketLayer(tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord,tileEntity.worldObj.provider.dimensionId,level,player.username); 
            }
            
            tileEntity.setLayer(level);
            
            player.openGui(HouseBuilder.instance, 0, player.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        }

        if (guibutton.id == 2)
        {
            TileEntityHouseBuilder tileentityhousebuilder = tileEntity;
            tileentityhousebuilder.createHouse(player);
        }
        
        if (guibutton.id == 3)
        {
            TileEntityHouseBuilder tileentityhousebuilder1 = tileEntity;
            tileentityhousebuilder1.copy(player);
        }

        if (guibutton.id == 4)
        {
            TileEntityHouseBuilder tileentityhousebuilder2 = tileEntity;
            tileentityhousebuilder2.paste(player);
        }

        if (guibutton.id == 5)
        {
            TileEntityHouseBuilder tileentityhousebuilder3 = tileEntity;
            tileentityhousebuilder3.clear(player);
        }
    }
}
