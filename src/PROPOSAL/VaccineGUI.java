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
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Scanner;

public class VaccineGUI extends Application {

    private ObservableList<Vaccines> vaccineData = FXCollections.observableArrayList();
    private TableView<Vaccines> table = new TableView<>();
    private static final String LOGIN_FILE = "stafflogin.txt";

    public static void main(String[] args) {
        ensureLoginFileExists();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vaccine Inventory System");
        primaryStage.getIcons().add(new Image("https://img.icons8.com/color/96/000000/syringe.png"));
        

        showLoginScreen(primaryStage);
    }


    private void showLoginScreen(Stage stage) {

        VBox root = new VBox(30); 
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");


        Label appTitle = new Label("Vaccine Dose Stock Tracker");
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        appTitle.setTextFill(Color.web("#2c3e50"));
        appTitle.setEffect(new DropShadow(5, Color.rgb(75, 75, 75, 0.2)));

       
        HBox loginCard = new HBox(0);
        loginCard.setMaxSize(800, 480);
        loginCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 20, 0, 0, 0);");


        VBox imageSide = new VBox();
        imageSide.setAlignment(Pos.CENTER);
        imageSide.setPadding(new Insets(20));
        imageSide.setPrefWidth(350);
        imageSide.setStyle("-fx-background-color: #dfe6e9; -fx-background-radius: 15 0 0 15;");
        

        ImageView doctorImg = new ImageView(new Image("https://img.icons8.com/clouds/250/000000/doctor-male.png"));
        doctorImg.setFitHeight(250); 
        doctorImg.setFitWidth(250);
        
        Label quote = new Label("\"Safety in every dose.\"");
        quote.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 16));
        quote.setTextFill(Color.web("#7f8c8d"));
        
        imageSide.getChildren().addAll(doctorImg, quote);


        VBox formSide = new VBox(25);
        formSide.setAlignment(Pos.CENTER);
        formSide.setPadding(new Insets(50));
        formSide.setPrefWidth(450);

        Label formTitle = new Label("Staff Access");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        formTitle.setTextFill(Color.web("#2c3e50"));

        TextField userField = styledTextField("Username");
        userField.setPrefHeight(45);

        HBox passContainer = new HBox();
        passContainer.setAlignment(Pos.CENTER_RIGHT);
        passContainer.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;");
        passContainer.setPrefHeight(45);

        PasswordField hiddenPass = new PasswordField();
        hiddenPass.setPromptText("Password");
        hiddenPass.setStyle("-fx-background-color: transparent; -fx-padding: 10; -fx-font-size: 14px;");
        hiddenPass.setPrefWidth(300); 

        TextField visiblePass = new TextField();
        visiblePass.setPromptText("Password");
        visiblePass.setStyle("-fx-background-color: transparent; -fx-padding: 10; -fx-font-size: 14px;");
        visiblePass.setPrefWidth(300);
        visiblePass.setManaged(false); visiblePass.setVisible(false);


        visiblePass.textProperty().bindBidirectional(hiddenPass.textProperty());


        ImageView eyeIcon = new ImageView(new Image("https://img.icons8.com/material-outlined/24/7f8c8d/visible.png"));
        StackPane eyeBtn = new StackPane(eyeIcon);
        eyeBtn.setPadding(new Insets(0, 10, 0, 0));
        eyeBtn.setCursor(Cursor.HAND);


        eyeBtn.setOnMouseClicked(e -> {
            if (hiddenPass.isVisible()) {

                hiddenPass.setVisible(false); hiddenPass.setManaged(false);
                visiblePass.setVisible(true); visiblePass.setManaged(true);

                eyeIcon.setImage(new Image("https://img.icons8.com/material-outlined/24/3498db/visible.png"));
            } else {
  
                visiblePass.setVisible(false); visiblePass.setManaged(false);
                hiddenPass.setVisible(true); hiddenPass.setManaged(true);

                eyeIcon.setImage(new Image("https://img.icons8.com/material-outlined/24/7f8c8d/visible.png"));
            }
        });


        StackPane fieldsStack = new StackPane(hiddenPass, visiblePass);
        fieldsStack.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(fieldsStack, Priority.ALWAYS);

        passContainer.getChildren().addAll(fieldsStack, eyeBtn);


        Button loginBtn = new Button("LOGIN");
        styleButton(loginBtn, "#27ae60", "#ffffff"); 
        loginBtn.setPrefWidth(400);
        loginBtn.setPrefHeight(45);
        loginBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        Button registerLink = new Button("Register New Staff");
        registerLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #2980b9; -fx-underline: true; -fx-cursor: hand; -fx-font-size: 13px;");


        loginBtn.setOnAction(e -> {
            String currentPassword = hiddenPass.isVisible() ? hiddenPass.getText() : visiblePass.getText();
            if (authenticate(userField.getText(), currentPassword)) {
                showSuccessPopup("Welcome, " + userField.getText());
                showMainDashboard(stage);
            } else {
                ShowUnsuccessfulPopup("Invalid Credentials");
            }
        });

        registerLink.setOnAction(e -> showRegisterScreen(stage));

        formSide.getChildren().addAll(formTitle, userField, passContainer, loginBtn, registerLink);


        loginCard.getChildren().addAll(imageSide, formSide);
        root.getChildren().addAll(appTitle, loginCard);

        Scene scene = new Scene(root, 950, 700);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }




    private void showRegisterScreen(Stage stage) {
        VBox regLayout = new VBox(20);
        regLayout.setAlignment(Pos.CENTER);
        regLayout.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 40;");

        VBox card = new VBox(20);
        card.setMaxWidth(400);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("New Staff Registration");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        TextField newUser = styledTextField("New Username");
        newUser.setPrefHeight(40);
        
        PasswordField newPass = new PasswordField();
        newPass.setPromptText("New Password");
        newPass.setStyle("-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 10;");
        newPass.setPrefHeight(40);

        Button registerBtn = new Button("Create Account");
        styleButton(registerBtn, "#2980b9", "#ffffff"); 
        registerBtn.setPrefWidth(300);
        registerBtn.setPrefHeight(40);

        Button backBtn = new Button("Cancel");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-cursor: hand;");

        registerBtn.setOnAction(e -> {
            if (newUser.getText().isEmpty() || newPass.getText().isEmpty()) {
                ShowUnsuccessfulPopup("Fields cannot be empty");
                return;
            }
            if (registerStaff(newUser.getText(), newPass.getText())) {
                showSuccessPopup("Staff Registered!");
                showLoginScreen(stage); 
            } else {
                ShowUnsuccessfulPopup("Error saving data.");
            }
        });

        backBtn.setOnAction(e -> showLoginScreen(stage));

        card.getChildren().addAll(titleLabel, newUser, newPass, registerBtn, backBtn);
        regLayout.getChildren().add(card);
        
        Scene scene = new Scene(regLayout, 950, 700);
        stage.setScene(scene);
    }


    private static void ensureLoginFileExists() {
        try {
            File f = new File(LOGIN_FILE);
            if (f.createNewFile()) {
                try (FileWriter fw = new FileWriter(f)) {
                    fw.write("admin,admin123\n");
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private boolean authenticate(String user, String pass) {
        try (Scanner sc = new Scanner(new File(LOGIN_FILE))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (parts[0].trim().equals(user) && parts[1].trim().equals(pass)) return true;
                }
            }
        } catch (FileNotFoundException e) { return false; }
        return false;
    }

    private boolean registerStaff(String user, String pass) {
        try (FileWriter fw = new FileWriter(LOGIN_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user + "," + pass);
            return true;
        } catch (IOException e) { return false; }
    }



    public void showMainDashboard(Stage primaryStage) {
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
        expCol.setCellFactory(column -> new TableCell<Vaccines, String>() {
            @Override 
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    int status = closeToExpiry(item);
                    if (status == 2) {
                        setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");
                    } else if (status == 1){
                        setStyle("-fx-text-fill: #ff6a00; -fx-font-weight: bold;");
                    } else {
                         setStyle("-fx-text-fill: #00c10a; -fx-font-weight: bold;");  
                    }
                }
            }
        });

        table.setItems(vaccineData);
        table.getColumns().clear();
        table.getColumns().add(nameCol);
        table.getColumns().add(qtyCol);
        table.getColumns().add(mfrCol);
        table.getColumns().add(expCol);
        table.setStyle("-fx-selection-bar: #3498db; -fx-selection-bar-non-focused: #add8e6; -fx-font-size: 18px;");

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

        // Logout Button
        Button logoutBtn = new Button("Exit / Logout");
        styleButton(logoutBtn, "#7f8c8d", "#ffffff");
        logoutBtn.setOnAction(e -> showLoginScreen(primaryStage));

        HBox actionBox = new HBox(15);
        actionBox.setPadding(new Insets(15));
        actionBox.setStyle("-fx-background-color: #34495e;"); 
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.getChildren().addAll(logoutBtn, loadBtn, cleanupBtn, dispenseBtn);

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(table);
        layout.setBottom(new VBox(inputBox, actionBox));

        Scene scene = new Scene(layout, 950, 700);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        
        loadDataFromFile("vaccines.txt");
    }


    private void showSuccessPopup(String messageText) {
        Stage popupStage = new Stage();

        popupStage.initOwner(table.getScene() != null ? table.getScene().getWindow() : null);
        popupStage.initStyle(StageStyle.TRANSPARENT);

        ImageView checkIcon = new ImageView(new Image("https://img.icons8.com/fluency/96/000000/checked.png"));
        checkIcon.setFitHeight(40); checkIcon.setFitWidth(40);

        Label message = new Label("Successful!\n" + messageText);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox box = new VBox(10, checkIcon, message);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: linear-gradient(to right, #2ecc71, #27ae60); -fx-background-radius: 20; -fx-padding: 20; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 20;");
        box.setEffect(new DropShadow(10, Color.BLACK));

        Scene scene = new Scene(box);
        scene.setFill(Color.TRANSPARENT); 
        popupStage.setScene(scene);
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

    private void ShowUnsuccessfulPopup(String detailMessage) {
        Stage failedPopupStage = new Stage();
        failedPopupStage.initStyle(StageStyle.TRANSPARENT);

        ImageView failIcon = new ImageView(new Image("https://img.icons8.com/fluency/96/000000/medium-risk.png"));
        failIcon.setFitHeight(40); failIcon.setFitWidth(40);

        Label message = new Label("Action Failed!\n" + detailMessage);
        message.setTextAlignment(TextAlignment.CENTER);
        message.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox box = new VBox(10, failIcon, message);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: linear-gradient(to right, #e83232, #ac1919); -fx-background-radius: 20; -fx-padding: 20; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 20;");
        box.setEffect(new DropShadow(10, Color.BLACK));

        Scene scene = new Scene(box);
        scene.setFill(Color.TRANSPARENT); 
        failedPopupStage.setScene(scene);
        failedPopupStage.show();

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(250), box);
        scaleIn.setFromX(0); scaleIn.setFromY(0);
        scaleIn.setToX(1); scaleIn.setToY(1);

        PauseTransition delay = new PauseTransition(Duration.seconds(1.2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), box);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> failedPopupStage.close()); 

        SequentialTransition sequence = new SequentialTransition(scaleIn, delay, fadeOut);
        sequence.play();
    }
    
    private void dispenseSelected() {
        Vaccines selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = selected.dispenseVial();
            if (success) {
                table.refresh();
                showSuccessPopup("Dispensed " + selected.getVaccineName());
            } else {
                ShowUnsuccessfulPopup("Batch is Empty.");
            }
        } else {
            ShowUnsuccessfulPopup("Please select a row first.");
        }
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 5; -fx-font-size: 14px;");
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
            ShowUnsuccessfulPopup("Check your inputs.");
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
        else ShowUnsuccessfulPopup("No expired items found.");
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
            ShowUnsuccessfulPopup("Could not find " + filename);
        }
    }

    private int closeToExpiry(String expiryDate) {
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate current = LocalDate.now();

            long daysFrom = ChronoUnit.DAYS.between(current, expiry);

            if (daysFrom >= 0 && daysFrom <= 30) {
                    return 1;
            } else if (daysFrom < 0) {
            return 2;
        }
        }
        catch (Exception e) {
            return 0;
        }
        return 0;
    }
}