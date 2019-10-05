package xiaokai.singledog.cmd;

import xiaokai.singledog.mtp.Kick;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @author Winfxk
 */
public class AdminCommand extends Command {
	public AdminCommand(Kick kick) {
		super("shop", "商店主命令", "/shop", new String[] { "shop", "商店" });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return false;
	}
}
