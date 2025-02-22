package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Insets;
import java.io.File;
import java.io.IOException;
import Source.Input;
import Source.Solver;
import Source.Output;
import Source.OutputImage;

public class Main extends Application {
    private TextArea outputArea;
    private Button btnSaveTxt, btnSaveImg, btnSolve;
    private Solver solver;
    private VBox outputBox;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("IQ Puzzler Solver");
        Image icon = new Image("idea.png");
        primaryStage.getIcons().add(icon);
        outputBox = new VBox();
        outputBox.setSpacing(5);
        
        outputBox.setBackground(new Background(new BackgroundFill(
        	    Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
        	)));
        
        Button btnOpenFile = new Button("File...");
        btnSolve = new Button("Solve");
        btnSaveTxt = new Button("Simpan ke TXT");
        btnSaveImg = new Button("Simpan ke Gambar");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputBox.getChildren().add(outputArea);

        btnSolve.setDisable(true);
        btnSaveTxt.setDisable(true);
        btnSaveImg.setDisable(true);

        final String[] filePath = {null};

        btnOpenFile.setOnAction(e -> {
            File file = chooseFile(primaryStage);
            if (file != null) {
                filePath[0] = file.getAbsolutePath();
                try {
                    loadInput(filePath[0]);
                    btnSolve.setDisable(false);
                } catch (Exception ex) {
                    outputArea.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnSolve.setOnAction(e -> {
        	outputBox.getChildren().remove(outputArea);
        	solver.setAttempt(0);
        	solver.setFound(false);
            if (filePath[0] != null) {
                try {
                    runSolver(filePath[0]);
                } catch (Exception ex) {
                    outputArea.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnSaveTxt.setOnAction(e -> saveSolutionToTxt(primaryStage));
        btnSaveImg.setOnAction(e -> saveSolutionToImage(primaryStage));
        HBox saveButtons = new HBox(10, btnSaveTxt, btnSaveImg);
        saveButtons.setAlignment(Pos.CENTER);
        VBox root = new VBox(10, btnOpenFile, btnSolve, outputBox,saveButtons);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File Input");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(stage);
    }

    private void loadInput(String filePath) throws IOException {
        Input input = new Input(filePath);
    }

    private void runSolver(String filePath) throws IOException {
        Input input = new Input(filePath);
        solver = new Solver(input);

        long start = System.currentTimeMillis();
        solver.solve();
        long end = System.currentTimeMillis();
        long waktu = end - start;
        if (solver.isSolutionFound()) {
        	outputBox.getChildren().clear();
            TextFlow formattedText = Output.getColoredSolution(solver.getBoard());
            outputBox.getChildren().clear();
            outputBox.getChildren().add(formattedText);

            btnSaveTxt.setDisable(false);
            btnSaveImg.setDisable(false);
        } else {
        	Label noSolution = new Label("\nTidak ada solusi yang ditemukan.");
            outputBox.getChildren().clear();
            outputBox.getChildren().add(noSolution);
        }
        Label time = new Label("\nWaktu Eksekusi: " + waktu + " ms");
        Label kasus = new Label("\nPercobaan: " + Solver.getAttempt());
        outputBox.getChildren().add(time);
        outputBox.getChildren().add(kasus);
    }

    private void saveSolutionToTxt(Stage stage) {
        if (solver == null || !solver.isSolutionFound()) {
            outputArea.appendText("\nTidak ada solusi untuk disimpan.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Solusi ke TXT");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            Output.saveToFile(file.getAbsolutePath(), solver.getSolution());
            outputArea.appendText("\nSolusi disimpan ke " + file.getAbsolutePath());
        }
    }

    private void saveSolutionToImage(Stage stage) {
        if (solver == null || !solver.isSolutionFound()) {
            outputArea.appendText("\nTidak ada solusi untuk disimpan.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Solusi ke Gambar");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            OutputImage.saveSolutionAsImage(Solver.getBoard(), file.getAbsolutePath());
            outputArea.appendText("\nSolusi disimpan sebagai gambar di " + file.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
