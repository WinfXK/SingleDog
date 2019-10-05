package xiaokai.singledog.cmd;

import xiaokai.singledog.mtp.Kick;
import xiaokai.singledog.mtp.Message;
import xiaokai.singledog.mtp.MyPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class PlayerCommand extends Command {
	private Kick kick;
	private Message msg;

	public PlayerCommand(Kick kick) {
		super("结婚", "结婚主命令", "/结婚", new String[] { "singledog", "sd" });
		this.kick = kick;
		msg = kick.Message;
		commandParameters = new HashMap<>();
		commandParameters.put("打开主操作页面", new CommandParameter[] {});
		commandParameters.put("向一个玩家求婚",
				new CommandParameter[] { new CommandParameter("向一个玩家求婚", false, new String[] { "求婚" }),
						new CommandParameter("玩家名称", CommandParamType.TARGET, false) });
		commandParameters.put("接受一个玩家的求婚请求",
				new CommandParameter[] { new CommandParameter("接受一个玩家的求婚请求", false, new String[] { "接受" }),
						new CommandParameter("玩家名称", CommandParamType.TARGET, false) });
		commandParameters.put("赠送金币给一个玩家以增加友好度",
				new CommandParameter[] { new CommandParameter("赠送金币给一个玩家以增加友好度", false, new String[] { "赠送" }),
						new CommandParameter("玩家名称", CommandParamType.TARGET, false) });
		commandParameters.put("从伴侣处获取金币",
				new CommandParameter[] { new CommandParameter("从伴侣处获取金币", false, new String[] { "拿取" }),
						new CommandParameter("要拿拿取的金币数量", CommandParamType.FLOAT, false) });
		commandParameters.put("给与伴侣金币",
				new CommandParameter[] { new CommandParameter("给与伴侣金币", false, new String[] { "给与" }),
						new CommandParameter("要给与的金币数量", CommandParamType.FLOAT, false) });
		commandParameters.put("传送到伴侣附近",
				new CommandParameter[] { new CommandParameter("传送到伴侣附近", false, new String[] { "传送" }) });
		commandParameters.put("同自己的伴侣离婚",
				new CommandParameter[] { new CommandParameter("同自己的伴侣离婚", false, new String[] { "离婚" }) });
		commandParameters.put("打开小家庭面板",
				new CommandParameter[] { new CommandParameter("打开小家庭面板", false, new String[] { "家庭" }) });
		commandParameters.put("打开设置小面板",
				new CommandParameter[] { new CommandParameter("打开设置小面板", false, new String[] { "设置" }) });
		commandParameters.put("情侣签到",
				new CommandParameter[] { new CommandParameter("情侣签到", false, new String[] { "签到" }) });
		commandParameters.put("设置自己的性别",
				new CommandParameter[] { new CommandParameter("设置自己的性别", false, new String[] { "性别" }),
						new CommandParameter("要设置的性别", false, new String[] { "男", "女", "妖", "无" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.isPlayer()) {
			sender.sendMessage("§4请在游戏内执行此命令！");
			return true;
		}
		Player player = (Player) sender;
		String name = player.getName();
		File file;
		String[] k = new String[] { "{Player}", "{Money}" };
		Object[] d = new Object[] { name, EconomyAPI.getInstance().myMoney(name) };
		if (args.length < 1)
			return kick.makeForm.MakeMain(player);
		Config config = MyPlayer.getConfig(name);
		switch (args[0]) {
		case "设置":
			return kick.makeForm.MySetting(player);
		case "家庭":
			return kick.makeForm.MyFamily(player);
		case "性别":
			return sex(player, args, name, config);
		case "求婚":
			if (MyPlayer.getSex(name) == null) {
				player.sendMessage(msg.getMessage("未设置性别", k, d));
				return true;
			}
			if (args.length < 2) {
				player.sendMessage(msg.getSun("命令", "玩家命令", "请输入要求婚的玩家名", k, d));
				return true;
			}
			if (MyPlayer.isMarry(name)) {
				player.sendMessage(msg.getSun("命令", "玩家命令", "已有伴侣", k, d));
				return MyPlayer.sendMessage(MyPlayer.getMarry(name),
						msg.getSun("命令", "玩家命令", "伴侣向别人求婚",
								new String[] { "{Player}", "{Money}", "{Gay}", "{ByPlayer}" }, new Object[] { args[2],
										EconomyAPI.getInstance().myMoney(name), MyPlayer.getMarry(name), name }),
						true);
			}
			if (EconomyAPI.getInstance().myMoney(player) < kick.config.getDouble("结婚费用")) {
				player.sendMessage(msg.getSun("命令", "玩家命令", "结婚费用不足", k, d));
				return true;
			}
			Config bsConfig = MyPlayer.getConfig(args[1]);
			if (bsConfig == null) {
				player.sendMessage(msg.getMessage("玩家未加入过游戏", k, d));
				return true;
			}
			Object object = bsConfig.get("被求婚");
			List<String> list = object == null || !(object instanceof List) ? new ArrayList<>()
					: (ArrayList<String>) object;
			object = config.get("被求婚");
			List<String> list2 = object == null || !(object instanceof List) ? new ArrayList<>()
					: (ArrayList<String>) object;
			if (list.contains(name) || list2.contains(args[1])) {
				player.sendMessage(msg.getSun("命令", "玩家命令", "已向该玩家求过婚", k, d));
				return true;
			}
			list.add(name);
			list2.add(args[1]);
			config.set("被求婚", list2);
			config.save();
			bsConfig.set("被求婚", list);
			bsConfig.save();
			player.sendMessage(msg.getSun("命令", "玩家命令", "求婚成功", k, d));
			EconomyAPI.getInstance().reduceMoney(player, kick.config.getDouble("结婚费用"));
			return MyPlayer.sendMessage(args[1],
					msg.getSun("命令", "玩家命令", "被求婚", new String[] { "{Player}", "{Money}", "{ByPlayer}" },
							new Object[] { args[2], EconomyAPI.getInstance().myMoney(name), name }),
					true);
		case "接受":
			file = MyPlayer.getFamilyFile();
			Config Familyconfig = new Config(file, Config.YAML);
			Familyconfig.setAll(MyPlayer.getFamilyDConfig());
			Familyconfig.save();
			return true;
		default:
			return false;
		}
	}

	public boolean sex(Player player, String[] args, String name, Config config) {
		String[] k = new String[] { "{Player}", "{Money}" };
		Object[] d = new Object[] { name, EconomyAPI.getInstance().myMoney(name) };
		if (MyPlayer.getSex(name) != null) {
			player.sendMessage(msg.getSun("命令", "玩家命令", "已设置性别", k, d));
			return true;
		}
		if (args.length < 2) {
			player.sendMessage(msg.getSun("命令", "玩家命令", "未输入性别", k, d));
			return true;
		}
		String sex;
		switch (args[1]) {
		case "0":
		case "男":
		case "nan":
		case "♂":
		case "man":
		case "boy":
		case "lan":
			sex = "男";
			break;
		case "女":
		case "lv":
		case "nv":
		case "♀":
		case "woman":
			sex = "女";
			break;
		case "妖":
		case "yao":
		case "gay":
			sex = "妖";
			break;
		case "无":
		case "wu":
		case "null":
		case "太监":
			sex = "无";
			break;
		default:
			player.sendMessage(msg.getSun("命令", "玩家命令", "请输入对应的性别", k, d));
			return false;
		}
		config.set("性别", sex);
		config.save();
		player.sendMessage(msg.getSun("命令", "玩家命令", "性别设置成功", new String[] { "{Player}", "{Money}", "{Sex}" },
				new Object[] { name, EconomyAPI.getInstance().myMoney(name), sex }));
		return true;
	}
}
