package com.minederp.kingdoms.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minederp.kingdoms.KingdomsPlugin;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class CTFCommands {
	@Command(aliases = { "ctf" ,"capture"}, usage = "", desc = "Capture the flag commands", min = 1, max = 2,flags="")
	public static void ctf(CommandContext args, KingdomsPlugin plugin,
			CommandSender sender) throws CommandException {
		
		sender.sendMessage(args.argsLength()+" SDS");
		if (args.argsLength() > 0) {

			plugin.ctfGame.processCommand(args, ((Player) sender));
		}
	}

}