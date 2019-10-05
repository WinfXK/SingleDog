package xiaokai.singledog.mtp;

import xiaokai.singledog.tool.Update;

/**
*@author Winfxk
*/
/**
 * 异步类
 * 
 * @author Winfxk
 */
public class startThread extends Thread {
	/**
	 * 
	 */
	private final Kick kick;
	public int 检测更新间隔;

	public startThread(Kick kick) {
		this.kick = kick;
		检测更新间隔 = this.kick.config.getInt("检测更新间隔");
	}

	public void load() {
		检测更新间隔 = this.kick.config.getInt("检测更新间隔");
	}

	@Override
	public void run() {
		while (true)
			try {
				sleep(1000);
				if (this.kick.config.getInt("检测更新间隔") > 0)
					if (this.kick.config.getBoolean("检测更新")) {
						if (检测更新间隔 < 0) {
							检测更新间隔 = this.kick.config.getInt("检测更新间隔");
							new Update(this.kick.mis).start();
						} else
							检测更新间隔--;
					}
			} catch (InterruptedException e) {
				this.kick.mis.getLogger().error("§4异步线程类出现问题！" + e.getMessage());
			}
	}
}