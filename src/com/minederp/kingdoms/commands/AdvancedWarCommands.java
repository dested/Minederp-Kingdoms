package com.minederp.kingdoms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class AdvancedWarCommands {
	@Command(aliases = { "advancedwar","aw" }, usage = "", desc = "Advanced War commands", min = 0, max = 4)
	public static void kingdom(CommandContext args, KingdomsPlugin plugin,
			CommandSender sender) throws CommandException {
		plugin.gameLogic.processCommand("aw", args, ((Player) sender));
		
		
		 
	}

}