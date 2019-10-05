package xiaokai.singledog.event;

import xiaokai.singledog.mtp.Kick;
import xiaokai.singledog.mtp.MyPlayer;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Kick kick;

	/**
	 * 事件监听类
	 * 
	 * @param kick
	 */
	public PlayerEvent(Kick kick) {
		this.kick = kick;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		kick.data.put(player.getName(), new MyPlayer(kick, player));
		if (MyPlayer.getSex(player.getName()) == null)
			player.sendMessage(kick.Message.getMessage("未设置性别", new String[] { "{Player}", "{Money}" },
					new Object[] { player.getName(), EconomyAPI.getInstance().myMoney(player) }));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (kick.data.containsKey(player.getName())) {
			MyPlayer myPlayer = kick.data.get(player.getName());
			if (myPlayer != null && myPlayer.config != null)
				myPlayer.config.save();
			kick.data.remove(player.getName());
		}
	}
}
