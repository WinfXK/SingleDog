package xiaokai.singledog.mtp;

import xiaokai.singledog.SingleDog;
import xiaokai.singledog.tool.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Message {
	private SingleDog mis;
	private String[] Key = { "{n}", "{RandColor}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };
	private String[] Data = {};
	private Config Message;

	public Message(Kick mis) {
		this.mis = mis.mis;
		Message = new Config(new File(mis.mis.getDataFolder(), mis.MsgName), 2);
	}

	private void load() {
		Data = new String[] { "\n", Tool.getRandColor(), mis.getServer().getMotd(), mis.getName(), mis.getMoneyName(),
				Tool.getTime(), Tool.getDate() };
	}

	public String getSun(String t, String Son, String Sun) {
		return getSun(t, Son, Sun, new String[] {}, new String[] {});
	}

	public String getSun(String t, String Son, String Sun, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son) && (map.get(Son) instanceof Map)) {
				map = (HashMap<String, Object>) map.get(Son);
				if (map.containsKey(Sun))
					return getText(map.get(Sun).toString(), k, d);
			}
		}
		return null;
	}

	public String getSon(String t, String Son) {
		return getSon(t, Son, new String[] {}, new String[] {});
	}

	public String getSon(String t, String Son, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString(), k, d);
		}
		return null;
	}

	public String getMessage(String t) {
		return getMessage(t, new String[] {}, new String[] {});
	}

	public String getMessage(String t, String[] k, Object[] d) {
		if (Message.exists(t))
			return getText(Message.getString(t), k, d);
		return null;
	}

	public String getText(String text) {
		return getText(text, new String[] {}, new String[] {});
	}

	public String getText(Object tex, String[] k, Object[] d) {
		load();
		String text = String.valueOf(tex);
		for (int i = 0; i < Key.length; i++)
			if (text.contains(Key[i]))
				text = text.replace(Key[i], Data[i]);
		for (int i = 0; (i < k.length && i < d.length); i++)
			if (text.contains(k[i]))
				text = text.replace(k[i], String.valueOf(d[i]));
		return text;
	}
}
