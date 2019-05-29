package me.sieric.findpairs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Application for playing "Find pairs" game
 * Accepts one integer parameter as command line argument
 * Given integer should be even number between 2 and 8 and
 * it defines number of rows and columns on the field.
 */
public class Main extends Application {

    /**
     * Initializes the window and passes size of the filed to {@link Controller}
     * @param args command line arguments should contain one even number between 2 and 8
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application
     * @param primaryStage application window
     */
    @Override
    public void start(Stage primaryStage) {
        Parameters args = getParameters();
        int size = Integer.parseInt(args.getUnnamed().get(0));
        if (size < 2 || size > 8 || size % 2 == 1) {
            System.out.println("Sorry, this application works only with even number between 2 and 8");
            return;
        }

        primaryStage.setTitle("Find pairs");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);

        Button[][] buttons = new Button[size][size];

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        for (int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        for (int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setMaxHeight(Double.MAX_VALUE);
                buttons[i][j].setMaxWidth(Double.MAX_VALUE);
                grid.add(buttons[i][j], j, i);
            }
        }
        grid.setMaxHeight(Double.MAX_VALUE);
        grid.setMaxWidth(Double.MAX_VALUE);
        primaryStage.setScene(new Scene(grid));
        primaryStage.show();

        Controller controller = new Controller(buttons);
        controller.setUp();
    }
}