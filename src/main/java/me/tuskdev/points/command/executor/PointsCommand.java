package me.tuskdev.points.command.executor;

import com.google.common.collect.ImmutableMap;
import me.tuskdev.points.PointsPlugin;
import me.tuskdev.points.command.AddCommand;
import me.tuskdev.points.command.RemoveCommand;
import me.tuskdev.points.command.SetCommand;
import me.tuskdev.points.controller.UserPointsController;
import me.tuskdev.points.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

public class PointsCommand implements CommandExecutor {

    private final Map<String, SimpleCommand> commands = ImmutableMap.
            <String, SimpleCommand>builder()
            .put("adicionar", new AddCommand())
            .put("remover", new RemoveCommand())
            .put("definir", new SetCommand())
            .build();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        UserPointsController userPointsController = PointsPlugin.get().getUserPointsController();
        if (strings.length == 0) {
            if (!(commandSender instanceof Player)) return false;

            commandSender.sendMessage(ChatColor.YELLOW + "Seu saldo: " + ChatColor.WHITE + NumberUtil.format(userPointsController.select(((Player)commandSender).getUniqueId())));
            return false;
        }

        SimpleCommand simpleCommand = commands.get(strings[0].toLowerCase());
        if (simpleCommand == null || !commandSender.hasPermission("points.admin")) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[0]);
            commandSender.sendMessage(ChatColor.YELLOW + "O saldo de \"" + offlinePlayer.getName() + "\" é: " + ChatColor.WHITE + NumberUtil.format(userPointsController.select(offlinePlayer.getUniqueId())));
            return false;
        }

        simpleCommand.execute(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
        return false;
    }
}
