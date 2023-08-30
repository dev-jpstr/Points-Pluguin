package me.tuskdev.points.command.executor;

import me.tuskdev.points.PointsPlugin;
import me.tuskdev.points.controller.UserPointsController;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleCommand {

    protected final UserPointsController userPointsController = PointsPlugin.get().getUserPointsController();

    public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args);

}
