package mx.com.rodel.modbanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

public class PlayerModsAPI {
	public static List<ModData> getPlayerMods(Player player) throws Exception{
		EntityPlayerMP pmp = ((EntityPlayerMP) player);
		NetHandlerPlayServer connection = (NetHandlerPlayServer) pmp.getClass().getField("field_71135_a").get(pmp);
		NetworkManager nm = (NetworkManager) connection.getClass().getField("field_147371_a").get(connection);
		
		List<ModData> data = new ArrayList<>();
		
		for(Entry<String, String> mod : NetworkDispatcher.get(nm).getModList().entrySet()){
			data.add(new ModData() {
				@Override
				public String getVersion() {
					return mod.getValue();
				}
				
				@Override
				public String getName() {
					return mod.getKey();
				}
			});
		}
		setData(player.getName(), data);
		return data;
	}
	
	public static void setData(String player, List<ModData> mods){
		List<String> r = new ArrayList<>();
		
		for(ModData mod : mods){
			r.add(mod.getName()+":"+mod.getVersion());
		}
		
		HashMap<String, String> dd = DataFile.read(Main.mod_dataF);
		dd.put(player, String.join(",", r));
		DataFile.write(Main.mod_dataF, dd);
	}
	
	public static List<ModData> getLastPlayerData(String player){
		HashMap<String, String> data = DataFile.read(Main.mod_dataF);
		if(!data.containsKey(player)){
			return new ArrayList<>();
		}
		List<String> playerData = Arrays.asList(data.get(player).split(","));
		List<ModData> rs = new ArrayList<>();
		for(String s : playerData){
			rs.add(new ModData() {
				@Override
				public String getVersion() {
					return s.split(":")[1];
				}
				
				@Override
				public String getName() {
					return s.split(":")[0];
				}
			});
		}
		return rs;
	}
	
	public static List<String> getPlayersWhoUseMod(String mod){
		List<String> players = new ArrayList<>();
		for(String player : DataFile.read(Main.mod_dataF).keySet()){
			for(ModData data : getLastPlayerData(player)){
				if(data.getName().toLowerCase().contains(mod)){
					players.add(player);
				}
			}
		}
		return players;
	}
//	
//	public static boolean useMod(List<ModData> mods, String mod){
//		for(ModData modData : mods){
//			if(modData.getName().toLowerCase().contains(mod)){
//				return true;
//			}
//		}
//	}

//	public static Optional<HashMap<String, String>> getLastData(String name){
//		if(Main.modsData.containsKey(name)){
//			return Optional.of(Main.modsData.get(name));
//		}else{
//			return Optional.empty();
//		}
//	}
//	
//	public static Optional<Map<String, String>> getLastModData(String mod){
//		List<String> playersWhoUse = new ArrayList<>();
//		for(Entry<String, HashMap<String, String>> player : Main.modsData.entrySet()){
//			for(Entry<String, V>player.getValue()){
//				
//			}
//		}
//	}
}
