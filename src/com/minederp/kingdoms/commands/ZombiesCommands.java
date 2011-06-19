package com.minederp.kingdoms.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class ZombiesCommands {
	@Command(aliases = { "z", "zombies" }, usage = "", desc = "Zombies commands", min = 0, max = 3, flags = "")
	public static void ctf(CommandContext args, KingdomsPlugin plugin,
			CommandSender sender) throws CommandException {

		plugin.gameLogic.processCommand("z",args, ((Player) sender));

	}

}