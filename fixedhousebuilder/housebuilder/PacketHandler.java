package housebuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;


public class PacketHandler implements IPacketHandler
{
		@Override
		public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player) 
		{
			if (packet.channel.equals("HouseBuilder")) 
			{
	            handlePacket(packet);
			}
		}
		
		enum PacketIds
		{
			IdBuild,
			IdLayer,
			IdCopy,
			IdPaste,
			IdClear
		}
		
		static void sendPacket(PacketIds id,int x,int y,int z,int worldId,int data,String username)
		{
			Packet250CustomPayload packet = new Packet250CustomPayload();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
			DataOutputStream outputStream = new DataOutputStream(bos);
			
			try 
			{
				outputStream.writeInt(id.ordinal());
				outputStream.writeInt(worldId);
				outputStream.writeInt(x);
				outputStream.writeInt(y);
				outputStream.writeInt(z);
				outputStream.writeInt(data);
				outputStream.writeUTF(username);
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		
			packet.channel = "HouseBuilder";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			
			PacketDispatcher.sendPacketToServer(packet);
		}
		
		public static void sendPacketBuild(int x,int y,int z,int worldId)
		{
			sendPacket(PacketIds.IdBuild,x,y,z,worldId,0,"");
		}
		
		public static void sendPacketLayer(int x,int y,int z,int worldId,int layer, String username)
		{
			sendPacket(PacketIds.IdLayer,x,y,z,worldId,layer,username);
		}
		
		public static void sendPacketCopy(int x,int y,int z,int worldId)
		{
			sendPacket(PacketIds.IdCopy,x,y,z,worldId,0,"");
		}
		
		public static void sendPacketPaste(int x,int y,int z,int worldId)
		{
			sendPacket(PacketIds.IdPaste,x,y,z,worldId,0,"");
		}
		
		public static void sendPacketClear(int x,int y,int z,int worldId)
		{
			sendPacket(PacketIds.IdClear,x,y,z,worldId,0,"");
		}
		
		@SuppressWarnings("unchecked")
		public void handlePacket(Packet250CustomPayload packet)
		{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
	        
	        int packetId=0;
	        int worldId,x,y,z;
	        
	        try 
	        {
				packetId = inputStream.readInt();
				worldId = inputStream.readInt();
				x = inputStream.readInt();
				y = inputStream.readInt();
				z = inputStream.readInt();
				int data= inputStream.readInt();
				
				String name= inputStream.readUTF();
				
				EntityPlayer player=null;
				if(name.length()!=0)
				{
					List<EntityPlayer> list=DimensionManager.getWorld(worldId).playerEntities;
					for(int i=0; i<list.size(); i++)
					{
						if(list.get(i).username.equals(name))
						{
							player=list.get(i);
						}
					}
				}
	       		 
	       		TileEntityHouseBuilder tile=(TileEntityHouseBuilder)DimensionManager.getWorld(worldId).getBlockTileEntity(x, y, z);
	       		if(tile==null)
	       			return;
	       		
	            switch(PacketIds.values()[packetId])
	            {
	            	case IdBuild:
	            		tile.createHouse(null);
	            		break;
	            	case IdLayer:
	            		tile.setLayer(data);
	            		player.openGui(HouseBuilder.instance,0, player.worldObj, x,y,z);
	            		break;
					case IdClear:
						tile.clear(null);
						break;
					case IdCopy:
						tile.copy(null);
						break;
					case IdPaste:
						tile.paste(null);
						break;
	            }
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	        
	        
		}
}