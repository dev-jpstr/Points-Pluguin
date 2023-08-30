package me.tuskdev.points;

import me.tuskdev.points.command.executor.PointsCommand;
import me.tuskdev.points.controller.UserPointsController;
import org.bukkit.plugin.java.JavaPlugin;

public class PointsPlugin extends JavaPlugin {

    private static PointsPlugin i;
    public static PointsPlugin get() { return i; }
    public PointsPlugin() { i = this; }

    private UserPointsController userPointsController;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        userPointsController = new UserPointsController(new PooledConnection(getConfig().getConfigurationSection("database")));
    }

    @Override
    public void onEnable() {
        getCommand("points").setExecutor(new PointsCommand());
    }

    public UserPointsController getUserPointsController() {
        return userPointsController;
    }
}
