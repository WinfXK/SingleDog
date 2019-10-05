package xiaokai.singledog;

import xiaokai.singledog.cmd.AdminCommand;
import xiaokai.singledog.cmd.PlayerCommand;
import xiaokai.singledog.event.PlayerEvent;
import xiaokai.singledog.mtp.Kick;
import xiaokai.singledog.tool.Tool;

import java.time.Duration;
import java.time.Instant;

import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;

/**
 * @author Winfxk
 */
public class SingleDog extends PluginBase {
	private Instant loadTime = Instant.now();
	/**
	 * 插件缓存数据集合
	 */
	protected static Kick kick;

	/**
	 * 明人不说暗话！这就是插件启动事件
	 */
	@Override
	public void onEnable() {
		Instant EnableTime = Instant.now();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerEvent(kick), this);
		float entime = ((Duration.between(loadTime, Instant.now()).toMillis()));
		String onEnableString = (entime > 1000 ? ((entime / 1000) + "§6s!(碉堡了) ") : entime + "§6ms");
		SimpleCommandMap asCmd = this.getServer().getCommandMap();
		asCmd.register(getName(), new AdminCommand(kick));
		asCmd.register(getName(), new PlayerCommand(kick));
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "启动！") + "§6总耗时:§9" + onEnableString
				+ " 启动耗时:§9" + ((float) (Duration.between(EnableTime, Instant.now()).toMillis())) + "§6ms");
	}

	/**
	 * 返回货币的名称，如“金币”
	 * 
	 * @return
	 */
	public String getMoneyName() {
		return kick.config.getString("货币单位");
	}

	/**
	 * ????这都看不懂？？这是插件关闭事件
	 */
	@Override
	public void onDisable() {
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "关闭！") + TextFormat.GREEN + "本次运行时长" + TextFormat.BLUE
						+ Tool.getTimeBy(((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000)));
	}

	/**
	 * PY已准备好！插件加载事件
	 */
	@Override
	public void onLoad() {
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载..."));
		kick = new Kick(this);
		if (Tool.getRand(1, 5) == 1)
			getLogger().info(Tool.getColorFont("本插件完全免费，如果你是给钱了的，那你就可能被坑啦~"));
	}

	public static Kick getKick() {
		return kick;
	}

	/**
	 * 快来和本插件PY交易吧~
	 * 
	 * @return 插件主类对象
	 */
	public static SingleDog getPY() {
		return kick.mis;
	}
}
