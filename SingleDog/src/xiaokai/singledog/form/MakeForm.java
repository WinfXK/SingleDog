package xiaokai.singledog.form;

import xiaokai.singledog.mtp.Kick;

import cn.nukkit.Player;

/**
 * @author Winfxk
 */
public class MakeForm {
	private Kick kick;

	public MakeForm(Kick kick) {
		this.kick = kick;
	}

	/**
	 * 个人设置小面板
	 * 
	 * @param player
	 * @return
	 */
	public boolean MySetting(Player player) {
		return true;
	}

	/**
	 * 构建家庭主页面
	 * 
	 * @param player
	 * @return
	 */
	public boolean MyFamily(Player player) {
		return true;
	}

	/**
	 * 构建主页面
	 * 
	 * @param player
	 * @return
	 */
	public boolean MakeMain(Player player) {

		return true;
	}
}
