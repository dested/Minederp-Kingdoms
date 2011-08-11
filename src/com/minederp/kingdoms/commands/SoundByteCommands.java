package com.minederp.kingdoms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class SoundByteCommands {
	@Command(aliases = { "sb", "SoundByte" }, usage = "", desc = "Soundbyte commands", min = 0, max = 3)
	public static void town(CommandContext args, KingdomsPlugin plugin, CommandSender sender) throws CommandException {
		plugin.gameLogic.processCommand("sb", args, ((Player) sender));
	}
}