package me.tuskdev.points.controller;

import me.tuskdev.points.PooledConnection;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class UserPointsController {

    private static final ExecutorService EXECUTOR = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `points`(`uuid` VARCHAR(36) NOT NULL, `value` INT DEFAULT 0, UNIQUE(`uuid`));";
    private static final String QUERY_INSERT_MODEL = "INSERT INTO `points`(`uuid`) VALUES (?);";
    private static final String QUERY_UPDATE_MODEL = "UPDATE `points` SET `value` = ? WHERE `uuid` = ?;";
    private static final String QUERY_SELECT_MODEL = "SELECT * FROM `points` WHERE `uuid` = ?;";

    private final PooledConnection pooledConnection;

    public UserPointsController(@NotNull PooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
        this.init();
    }

    private void init() {
        EXECUTOR.submit(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
               PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CREATE_TABLE);
               preparedStatement.execute();
               preparedStatement.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
        });
    }

    public void insert(@NotNull UUID uuid) {
        EXECUTOR.submit(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_INSERT_MODEL);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void update(@NotNull UUID uuid, int points) {
        EXECUTOR.submit(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_MODEL);
                preparedStatement.setInt(1, points);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Integer select(@NotNull UUID uuid) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        EXECUTOR.submit(() -> {
            try (Connection connection = pooledConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SELECT_MODEL);
                preparedStatement.setString(1, uuid.toString());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) completableFuture.complete(resultSet.getInt("value"));
                else completableFuture.complete(-1);

                resultSet.close(); // preparedStatement auto close this resultSet
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return completableFuture.join();
    }
}
