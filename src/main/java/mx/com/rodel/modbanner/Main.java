package mx.com.rodel.modbanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import mx.com.rodel.modbanner.commands.ModBannerCommand;
import mx.com.rodel.modbanner.commands.ModsCommand;

@Plugin(id=ModInfo.ID, name=ModInfo.NAME, version=ModInfo.VERSION, description="Ban Mods")
public class Main {
	
	public static Main instance;
//	public static HashMap<String, List<ModData>> lastData = new HashMap<>();
	public static File mod_dataF;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private Path configPath;

	@Inject
	public Logger log;
	
	public Path getConfigPath(){
		return configPath;
	}
	
	public ConfigurationManager cfgManager;
	
	@Listener
	public void onGamePreInitializationEvent(GamePreInitializationEvent e){
		instance = this;
		
		Sponge.getCommandManager().register(this, new ModBannerCommand(), "modbanner", "modblacklist");
		Sponge.getCommandManager().register(this, new ModsCommand(), "mods", "modinfo");
		File d = Sponge.getGame().getSavesDirectory().resolve("modbanner").toFile();
		if(!d.exists()){
			d.mkdirs();
		}
		File f = new File(d, "mod_data.txt");
		mod_dataF = f;
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		reloadConfiguration();
	}
	
	@Listener
	public void on(ClientConnectionEvent.Join e) {
		Sponge.getScheduler().createTaskBuilder().execute(() -> {
			try {
				boolean kicked = false;
				List<ModData> mods = PlayerModsAPI.getPlayerMods(e.getTargetEntity());
				if(mods==null){
					log.warn("[ModBanner] Can not get "+e.getTargetEntity().getName()+" mods cause is not using forge!");
					return;
				}
				List<String> bannedMods = new ArrayList<>();
				for(String blacklist : cfgManager.blackList){
					for(ModData mod : mods){
						if(mod.getName().contains(blacklist)){
							bannedMods.add(mod.getName());
							kicked = true;
						}
					}
				}
				List<String> lo = new ArrayList<>();
				for(ModData mod : mods){
					if(bannedMods.contains(mod.getName())){
						lo.add(mod.getCompleteData()+" (Banned)");
					}else{
						lo.add(mod.getCompleteData());
					}
				}
				log.info(("[ModBanner] "+e.getTargetEntity().getName()+" is trying to join with there mods: "+String.join(", ", lo)+" "+(kicked ? "(Getting kicked)" : "")));
				if(kicked){
					e.getTargetEntity().kick(Helper.format(cfgManager.kickMsg.replace("%mods%", String.join(", ", bannedMods))));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}).delay(1000, TimeUnit.MILLISECONDS).submit(Main.instance);
	}
	
	public void reloadConfiguration(){
		try {
			cfgManager = new ConfigurationManager(this);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
