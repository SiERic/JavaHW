package me.sieric.cannon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Graphics for "Scorched Earth"-like game.
 * @see <a href="https://en.wikipedia.org/wiki/Scorched_Earth_(video_game)"></a>
 * Usage:
 *   LEFT/RIGHT to move cannon
 *   UP/DOWN to change ange of cannon's barrel
 *   ENTER to fire
 *   [0-4] to change bomb type
 * Game last 30 seconds + 5 more seconds for every target achieved
 * Player cannot shot while prevoius bullet didn't land
 */
public class Main extends Application {

    /** Screen pane */
    private final Pane pane = new Pane();

    /** Game scene */
    private final Scene scene = new Scene(pane, Landscape.WIDTH, Landscape.HEIGHT);

    /** Landscape for current game "level" */
    private Landscape landscape;

    /** Current cannon image */
    private Cannon cannon;

    /** Current target image */
    private Circle target;

    /** Score text */
    private Text scoreText = new Text();

    /** Remaining game time text */
    private Text timeText = new Text();

    /** Game duration (in seconds) */
    private final int GAME_TIME = 30;

    /** Current target radius */
    private int targetRadius;

    /** Current bullet type [0-4] (1 by default) */
    private int bulletType = 1;

    /** Current number of achieved targets */
    private int score;

    /** Game remaining time (in seconds) */
    private int timeLeft;

    /** True if now cannon is shooting */
    private boolean isShooting;

    /** Lock to change remaining time in parallel */
    private Lock timeLock = new ReentrantLock();

    /** Lock to change game status in parallel */
    private Lock statusLock = new ReentrantLock();

    /** Lock to change shooting status in parallel */
    private Lock shootingLock = new ReentrantLock();

    /** Enum for game state */
    private enum GameStatus {
        IN_PROGRESS,
        FINISHED
    }

    /** Current game state */
    private GameStatus status = GameStatus.IN_PROGRESS;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(scene);

        scoreText.setLayoutX(Landscape.WIDTH / 16);
        scoreText.setLayoutY(Landscape.HEIGHT / 16);
        setScoreText();

        timeText.setLayoutX(Landscape.WIDTH * 14 /  16);
        timeText.setLayoutY(Landscape.HEIGHT / 16);
        setTimeLeft();

        pane.getChildren().add(scoreText);
        pane.getChildren().add(timeText);

        timeLeft = GAME_TIME;

        startNewGame();

        Thread timerThread = new Thread(() -> {
            timeLock.lock();
            while (timeLeft > 0) {
                setTimeLeft();
                try {
                    timeLock.unlock();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                timeLock.lock();
                timeLeft--;
            }
            timeLock.unlock();
            statusLock.lock();
            status = GameStatus.FINISHED;
            statusLock.unlock();
            Platform.runLater(this::endGame);
        });
        timerThread.start();

        primaryStage.getScene().setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            statusLock.lock();
            if (status.equals(GameStatus.FINISHED)) {
                statusLock.unlock();
                return;
            }
            statusLock.unlock();
            if (code == KeyCode.LEFT) {
                cannon.moveLeft();
            } else if (code == KeyCode.RIGHT) {
                cannon.moveRight();
            } else if (code == KeyCode.UP) {
                cannon.increaseAngle();
            } else if (code == KeyCode.DOWN) {
                cannon.decreaseAngle();
            } else if (code == KeyCode.ENTER) {
                if (isTooClose()) {
                    return;
                }
                shootingLock.lock();
                if (!isShooting) {
                    isShooting = true;
                    shootingLock.unlock();
                    fire();
                } else {
                    shootingLock.unlock();
                }

            } else if (code.isDigitKey() && Integer.parseInt(code.getChar()) < Bullet.getTypesNumber()) {
                bulletType = Integer.parseInt(code.getChar());
            }
        });

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /** Checks if target and cannon are too close to shot */
    private boolean isTooClose() {
        return (Math.hypot(cannon.getBody().getCenterX() - target.getCenterX(),
                cannon.getBody().getCenterY() - target.getCenterY()) < targetRadius * 4);
    }

    /** Set's current score to score text */
    private void setScoreText() {
        scoreText.setText("Score: " + score);
    }

    /** Sets current left time to time text */
    private void setTimeLeft() {
        timeText.setText(String.format("%01d:%02d", timeLeft / 60, timeLeft % 60));
    }

    /** Creates new bullet (of current bullet type) and models it flight */
    private void fire() {
        int x0 = cannon.getBarrelEndX();
        int y0 = cannon.getBarrelEndY();
        Runnable task = () -> {
            int time = 0;
            Bullet bullet = new Bullet(x0, y0, bulletType, cannon.getAngle());
            Platform.runLater(() -> pane.getChildren().add(bullet.getBody()));
            int newX = bullet.getXByTime(time);
            int newY = bullet.getYByTime(time);
            while (true) {

                if (newX <= 0 || newX >= Landscape.WIDTH
                              || newY - bullet.getRadius() / 2 > landscape.getYByX(newX)
                              || isCloseToTarget(newX, newY)) {
                    break;
                }

                bullet.setX(newX);
                bullet.setY(newY);

                time++;
                newX = bullet.getXByTime(time);
                newY = bullet.getYByTime(time);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }
            if (isCloseToTarget(newX, newY)) {
                target.setRadius(targetRadius * 3);
                score++;
                timeLock.lock();
                timeLeft += 5;
                setScoreText();
                timeLock.unlock();
                Platform.runLater(this::clear);
                Platform.runLater(this::startNewGame);
            }
            Platform.runLater(() -> pane.getChildren().remove(bullet.getBody()));
            shootingLock.lock();
            isShooting = false;
            shootingLock.unlock();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /** Prepares landscape and other graphics for a new game "level" */
    private void startNewGame() {
        Random rnd = new Random(System.currentTimeMillis());
        statusLock.lock();
        if (status.equals(GameStatus.FINISHED)) {
            statusLock.unlock();
            return;
        }
        statusLock.unlock();

        landscape = new Landscape();
        int x = rnd.nextInt(Landscape.WIDTH * 3 / 4) + Landscape.WIDTH / 8;
        cannon = new Cannon(x, landscape.getYByX(x), landscape);
        x = rnd.nextInt(Landscape.WIDTH * 3 / 4) + Landscape.WIDTH / 8;
        targetRadius = rnd.nextInt(Landscape.WIDTH / 128) + Landscape.WIDTH / 64;
        target = new Circle(x, landscape.getYByX(x), targetRadius, Color.RED);

        pane.getChildren().addAll(landscape.getMounts());
        pane.getChildren().addAll(cannon.getBody(), cannon.getBarrel());
        pane.getChildren().add(target);
    }

    /** Removes landscape and shows final score */
    private void endGame() {
        pane.getChildren().clear();
        Text endText = new Text(String.format("Game is over. You scored %d points", score));
        endText.setLayoutX(Landscape.WIDTH / 8);
        endText.setLayoutY(Landscape.HEIGHT / 8);
        pane.getChildren().add(endText);
    }

    /** Clears pane of old objects */
    private void clear() {
        pane.getChildren().removeAll(landscape.getMounts());
        pane.getChildren().removeAll(cannon.getBody(), cannon.getBarrel());
        pane.getChildren().remove(target);
    }

    /**
     * Checks if bullet lands close to target, depending on current bullet type
     * @param x bullet's x coordinate
     * @param y bullet's y coordinate
     * @return true if bullet lands close to target, depending on current bullet type, false otherwise
     */
    private boolean isCloseToTarget(int x, int y) {
        return Math.hypot(x - target.getCenterX(), y - target.getCenterY()) < targetRadius
                + Bullet.getTypeDist(bulletType);
    }
}
