package PROPOSAL;

import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Iterator;

public class VaccineGUI extends Application {

    private ObservableList<Vaccines> vaccineData = FXCollections.observableArrayList();
    private TableView<Vaccines> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vaccine Inventory System");
        primaryStage.getIcons().add(new Image("https://img.icons8.com/color/96/000000/syringe.png"));

        String iconUrl = "https://img.icons8.com/color/96/000000/syringe.png"; 
        ImageView logoView = new ImageView(new Image(iconUrl));
        logoView.setFitHeight(50); logoView.setFitWidth(50);

        Label titleLabel = new Label("VACCINE CONTROL CENTER");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 2 0;");
        headerBox.getChildren().addAll(logoView, titleLabel);

        TableColumn<Vaccines, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("vaccineName"));
        nameCol.setMinWidth(150);

        TableColumn<Vaccines, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("currentQuantity"));
        qtyCol.setMinWidth(80);
        qtyCol.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");

        TableColumn<Vaccines, String> mfrCol = new TableColumn<>("Manufacturer");
        mfrCol.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        mfrCol.setMinWidth(150);

        TableColumn<Vaccines, String> expCol = new TableColumn<>("Expiry Date");
        expCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        expCol.setMinWidth(100);

        table.setItems(vaccineData);
        table.getColumns().addAll(nameCol, qtyCol, mfrCol, expCol);
        table.setStyle("-fx-selection-bar: #3498db; -fx-selection-bar-non-focused: #add8e6; -fx-font-size: 14px;");

        TextField nameInput = styledTextField("Vaccine Name");
        TextField qtyInput = styledTextField("Qty"); qtyInput.setPrefWidth(70);
        TextField mfrInput = styledTextField("Manufacturer");
        TextField dateInput = styledTextField("YYYY-MM-DD");

        Button addButton = new Button("✚ Add Batch");
        styleButton(addButton, "#27ae60", "#ffffff"); 
        addButton.setOnAction(e -> addBatch(nameInput, qtyInput, mfrInput, dateInput));

        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(15));
        inputBox.setStyle("-fx-background-color: #ffffff;");
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().addAll(nameInput, qtyInput, mfrInput, dateInput, addButton);

        Button loadBtn = new Button("📂 Load File");
        styleButton(loadBtn, "#2980b9", "#ffffff"); 
        loadBtn.setOnAction(e -> loadDataFromFile("vaccines.txt"));

        Button dispenseBtn = new Button("💉 Dispense Dose");
        styleButton(dispenseBtn, "#c0392b", "#ffffff"); 
        dispenseBtn.setOnAction(e -> dispenseSelected());

        Button cleanupBtn = new Button("🗑 Remove Expired");
        styleButton(cleanupBtn, "#e67e22", "#ffffff"); 
        cleanupBtn.setOnAction(e -> cleanupInventory());

        HBox actionBox = new HBox(15);
        actionBox.setPadding(new Insets(15));
        actionBox.setStyle("-fx-background-color: #34495e;"); 
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.getChildren().addAll(loadBtn, cleanupBtn, dispenseBtn);

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(table);
        layout.setBottom(new VBox(inputBox, actionBox));

        Scene scene = new Scene(layout, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        loadDataFromFile("vaccines.txt");
    }

    private void showSuccessPopup(String vaccineName) {
        Stage popupStage = new Stage();
        popupStage.initOwner(table.getScene().getWindow());
        popupStage.initStyle(StageStyle.TRANSPARENT);

        ImageView checkIcon = new ImageView(new Image("https://img.icons8.com/fluency/96/000000/checked.png"));
        checkIcon.setFitHeight(40); checkIcon.setFitWidth(40);

        Label message = new Label("Dispensed Successfully!\n" + vaccineName);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox box = new VBox(10, checkIcon, message);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: linear-gradient(to right, #2ecc71, #27ae60); -fx-background-radius: 20; -fx-padding: 20; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 20;");
        box.setEffect(new DropShadow(10, Color.BLACK));

        Scene scene = new Scene(box);
        scene.setFill(Color.TRANSPARENT); 
        popupStage.setScene(scene);

        double centerX = table.getScene().getWindow().getX() + (table.getScene().getWindow().getWidth() / 2) - 125;
        double centerY = table.getScene().getWindow().getY() + (table.getScene().getWindow().getHeight() / 2) - 75;
        popupStage.setX(centerX);
        popupStage.setY(centerY);

        popupStage.show();

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(250), box);
        scaleIn.setFromX(0); scaleIn.setFromY(0);
        scaleIn.setToX(1); scaleIn.setToY(1);

        PauseTransition delay = new PauseTransition(Duration.seconds(1.2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), box);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> popupStage.close()); 

        SequentialTransition sequence = new SequentialTransition(scaleIn, delay, fadeOut);
        sequence.play();
    }

    private void dispenseSelected() {
        Vaccines selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.dispenseVial();
            if (success) {
                table.refresh();
                showSuccessPopup(selected.getVaccineName());
            } else {
                showAlert("Warning", "Batch is empty!");
            }
        } else {
            showAlert("Selection Error", "Please select a row first.");
        }
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 5;");
        return tf;
    }

    private void styleButton(Button btn, String bgColor, String textColor) {
        String normalStyle = "-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15 8 15;";
        String hoverStyle = "-fx-background-color: derive(" + bgColor + ", 20%); -fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 15 8 15;";
        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
    }

    private void addBatch(TextField nameInput, TextField qtyInput, TextField mfrInput, TextField dateInput) {
        try {
            String name = nameInput.getText();
            int qty = Integer.parseInt(qtyInput.getText());
            String mfr = mfrInput.getText();
            String date = dateInput.getText();
            if(name.isEmpty() || mfr.isEmpty() || date.isEmpty()) throw new Exception();

            vaccineData.add(new Vaccines(name, qty, mfr, date));
            nameInput.clear(); qtyInput.clear(); mfrInput.clear(); dateInput.clear();
            showSuccessPopup("New Batch Added"); 
        } catch (Exception e) {
            showAlert("Error", "Check your inputs.");
        }
    }

    private void cleanupInventory() {
        Iterator<Vaccines> iterator = vaccineData.iterator();
        String currentDate = LocalDate.now().toString();
        int removedCount = 0;
        while (iterator.hasNext()) {
            Vaccines batch = iterator.next();
            if (batch.getCurrentQuantity() == 0 || batch.getExpiryDate().compareTo(currentDate) < 0) {
                iterator.remove();
                removedCount++;
            }
        }
        table.refresh();
        if(removedCount > 0) showSuccessPopup("Removed " + removedCount + " Items");
        else showAlert("Cleanup", "No expired items found.");
    }

    public void loadDataFromFile(String filename) {
        vaccineData.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    vaccineData.add(new Vaccines(data[0].trim(), Integer.parseInt(data[1].trim()), data[2].trim(), data[3].trim()));
                }
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not find vaccines.txt");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}