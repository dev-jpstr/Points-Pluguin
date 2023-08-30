package me.tuskdev.points.command;

import me.tuskdev.points.command.executor.SimpleCommand;
import me.tuskdev.points.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand extends SimpleCommand {

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Utilize /pontos remover <usuário> <quantia>.");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        int points = userPointsController.select(offlinePlayer.getUniqueId());
        if (points == -1) userPointsController.insert(offlinePlayer.getUniqueId());

        int amount = NumberUtil.parseInt(args[1]);
        if (amount == -1) {
            sender.sendMessage(ChatColor.RED + "A quantia \"-1\" é inválida para essa operação.");
            return;
        }

        userPointsController.update(offlinePlayer.getUniqueId(), points-amount);
        sender.sendMessage(ChatColor.YELLOW + "Foram removidos " + NumberUtil.format(amount) + " pontos de " + offlinePlayer.getName() + ".");
    }
}
