package com.minederp.kingdoms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class MapsCommands {
	@Command(aliases = { "map", "m" }, usage = "", desc = "Map based commands", min = 0, max = 2)
	public static void town(CommandContext args, KingdomsPlugin plugin, CommandSender sender) throws CommandException {
		if (args.argsLength() == 0 || args.getString(0) == "?"|| args.getString(0).toLowerCase() == "help") {
			sender.sendMessage(ChatColor.YELLOW + "Map Commands:");
			sender.sendMessage(ChatColor.YELLOW + "  /setid {id}");
			return;
		}

		if (args.argsLength() > 0) {
			if (sender instanceof Player && args.getString(0).equals("setid")) {
				int f = args.getInteger(1);

				Player player = ((Player) sender);
				ItemStack itm = player.getItemInHand();

				sender.sendMessage(ChatColor.YELLOW + " Map set to " + args.getInteger(1));
				if (itm.getTypeId() == 358) {
					itm.setDurability((short) f);
					return;
				}
			}

		}
	}

}