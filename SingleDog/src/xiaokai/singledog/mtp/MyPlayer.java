package xiaokai.singledog.mtp;

import xiaokai.singledog.tool.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;

@SuppressWarnings("unchecked")
public class MyPlayer {
	public Player player;
	public Config config;

	/**
	 * 玩家数据集
	 * 
	 * @param kick
	 * @param player
	 */
	public MyPlayer(Kick kick, Player player) {
		this.player = player;
		File file = getFile(player.getName());
		if (!file.exists()) {
			config = new Config(file, Config.YAML);
			config.setAll(getDConfig());
			config.save();
		} else
			config = new Config(file, Config.YAML);
	}

	/**
	 * 获取一个玩家的性别
	 * 
	 * @param player
	 * @return
	 */
	public static String getSex(String player) {
		Config config = getConfig(player);
		if (config == null)
			return null;
		Object obj = config.get("性别");
		String sex;
		try {
			sex = (String) obj;
		} catch (Exception e) {
			sex = null;
		}
		return obj == null || sex == null || sex.isEmpty() ? null : sex;
	}

	/**
	 * 获取一个玩家的伴侣
	 * 
	 * @param player
	 * @return
	 */
	public static String getMarry(String player) {
		if (isMarry(player) || isEngag(player)) {
			Config config = getConfig(player);
			return config.getString("伴侣");
		}
		return null;
	}

	/**
	 * 判断玩家是否结婚
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isMarry(String player) {
		Config config = getConfig(player);
		if (config == null)
			return false;
		return config.get("伴侣") == null;
	}

	/**
	 * 获取一个玩家的配置文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static Config getConfig(String player) {
		File file = getFile(player);
		return file.exists() ? new Config(file, Config.YAML) : null;
	}

	/**
	 * 获取一个玩家的配置文件的文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static File getFile(String player) {
		return new File(new File(Kick.kick.mis.getDataFolder(), Kick.PlayerConfigPath), player);
	}

	public static boolean sendMessage(String name, String msg, boolean back) {
		Player player = Server.getInstance().getPlayer(MyPlayer.getMarry(name));
		if (player == null) {
			Config config = getConfig(name);
			Object obj = config.getList("未读消息");
			List<Object> list = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<Object>) obj;
			list.add(msg);
			config.set("未读消息", list);
			config.save();
		} else
			player.sendMessage(msg);
		return back;
	}

	/**
	 * 获取个人配置默认数据
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, Object> getDConfig() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("伴侣", null);
		map.put("家庭", null);
		map.put("婚姻次数", 0);
		map.put("性别", null);
		map.put("允许伴侣传送", true);
		map.put("允许伴侣拿钱", false);
		map.put("订婚", false);
		map.put("未读消息", new ArrayList<>());
		map.put("友好度", new HashMap<>());
		map.put("闺蜜", new HashMap<>());
		map.put("基友", new HashMap<>());
		map.put("被求婚", new ArrayList<>());
		map.put("求婚", new ArrayList<>());
		return map;
	}

	/**
	 * 判断玩家是否订婚
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isEngag(String player) {
		return Tool.ObjToBool(getConfig(player).get("订婚"), false);
	}

	/**
	 * 获取小家庭默认数据
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, Object> getFamilyDConfig() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("亲密度", 0);
		map.put("小金库", 0);
		map.put("关系", null);
		map.put("纪念日", null);
		map.put("Players", new ArrayList<>());
		map.put("签到", new HashMap<>());
		map.put("小仓库", new HashMap<>());
		return map;
	}

	/**
	 * 获取婚姻关系
	 * 
	 * @param name
	 * @param player
	 * @return
	 */
	public static String getRelation(String name, String player) {
		String sex1 = getSex(name), sex2 = getSex(player);
		if (getSexBool(sex1, sex2, "男", "妖"))
			return Kick.kick.Message.getSon("关系", "男妖");
		String[] sexs = { "男", "女", "无", "妖" };
		String string = Kick.kick.Message.getSon("关系", "默认");
		for (int i = 0; i < sexs.length; i++)
			for (int j = i; j < sexs.length; j++)
				if (getSexBool(sex1, sex2, sexs[i], sexs[j])) {
					string = Kick.kick.Message.getSon("关系", sexs[i] + sexs[j]) == null
							? Kick.kick.Message.getSon("关系", sexs[j] + sexs[i])
							: Kick.kick.Message.getSon("关系", sexs[i] + sexs[j]);
					break;
				}
		return string;
	}

	public static boolean getSexBool(String sex1, String sex2, String sex3, String sex4) {
		return (sex1.equals(sex3) && sex2.equals(sex4)) || (sex1.equals(sex4) && sex2.equals(sex1));
	}

	/**
	 * 获取一个不重复的家庭配置文件的文件对象
	 * 
	 * @return
	 */
	public static File getFamilyFile() {
		File file = new File(new File(Kick.kick.mis.getDataFolder(), Kick.Family), "0");
		for (int i = 0; file.exists(); i++)
			file = new File(new File(Kick.kick.mis.getDataFolder(), Kick.Family), String.valueOf(i));
		return file;
	}
}
