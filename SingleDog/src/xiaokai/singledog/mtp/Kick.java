package xiaokai.singledog.mtp;

import xiaokai.singledog.SingleDog;
import xiaokai.singledog.form.MakeForm;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

/**
 * @author Winfxk
 */
public class Kick {
	public static Kick kick;
	/**
	 * 插件主累对象
	 */
	public SingleDog mis;
	/**
	 * 插件猪被子文件 <b>config</b></br>
	 * 表单ID配置文件 <b>formIdConfig</b> </br>
	 */
	public Config config, formIdConfig;
	/**
	 * 系统配置文件的文件名
	 */
	public String ConfigName = "Config.yml";
	/**
	 * 表单ID存储类
	 */
	public FormID formID;
	/**
	 * 消息文件存储文件名
	 */
	public String MsgName = "Message.yml";
	/**
	 * 消息文件类
	 */
	public Message Message;
	/**
	 * 要初始化的表单ID键值
	 */
	public String[] FormIDName = {};
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 玩家数据库
	 */
	public LinkedHashMap<String, MyPlayer> data = new LinkedHashMap<>();
	/**
	 * 要检查数据是否匹配的配置文件
	 */
	public String[] isLoadFileName = { ConfigName, MsgName };
	/**
	 * 玩家配置文件存储路径
	 */
	public static final String PlayerConfigPath = "Players/";
	/**
	 * 结婚数据库 存储玩家结婚后的共同数据
	 */
	public static final String Family = "Family/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { PlayerConfigPath };
	/**
	 * 异步线程类
	 */
	public startThread sThread;
	public MakeForm makeForm;

	public Kick(SingleDog bemilk) {
		kick = this;
		if (!bemilk.getDataFolder().exists())
			bemilk.getDataFolder().mkdirs();
		mis = bemilk;
		formIdConfig = new Config(new File(bemilk.getDataFolder(), FormIDConfigName), Config.YAML);
		(new Belle(this)).start();
		config = new Config(new File(bemilk.getDataFolder(), ConfigName), Config.YAML);
		formID = new FormID(this);
		formID.setConfig(formIdConfig.getAll());
		Message = new Message(this);
		sThread = new startThread(this);
		sThread.start();
		config.set("2000451", TextFormat.GREEN+"");
		config.save();
	}
}
