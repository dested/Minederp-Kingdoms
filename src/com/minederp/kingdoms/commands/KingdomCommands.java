package com.minederp.kingdoms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class KingdomCommands {
	@Command(aliases = { "kingdom","k" }, usage = "", desc = "Kingdom commands", min = 0, max = 0)
	public static void kingdom(CommandContext args, KingdomsPlugin plugin,
			CommandSender sender) throws CommandException {
		if (args.argsLength() > 0) {
			sender.sendMessage(ChatColor.YELLOW + args.getString(0));

			if (args.getString(0).toLowerCase().equals("join")) {
				//do
				sender.sendMessage(ChatColor.YELLOW + " You have joined a kingdom.");
			}
		}
	}

}