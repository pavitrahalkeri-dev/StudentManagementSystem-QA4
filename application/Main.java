package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Button addBtn = new Button("Add Student");
        Button viewBtn = new Button("View Students");
        Button exitBtn = new Button("Exit");

        addBtn.setPrefWidth(200);
        viewBtn.setPrefWidth(200);
        exitBtn.setPrefWidth(200);

        addBtn.setOnAction(e -> openAddStudentScreen(stage));
        viewBtn.setOnAction(e -> openViewStudentsScreen(stage));
        exitBtn.setOnAction(e -> stage.close());

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(addBtn, viewBtn, exitBtn);

        stage.setTitle("Student Management System");
        stage.setScene(new Scene(layout, 400, 300));
        stage.show();
    }

    // ---------------- ADD STUDENT (CREATE) ----------------
    private void openAddStudentScreen(Stage stage) {

        Label title = new Label("Add Student");

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField deptField = new TextField();
        deptField.setPromptText("Department");

        Button saveBtn = new Button("Save");
        Button backBtn = new Button("Back");

        saveBtn.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO students (name, email, department) VALUES (?, ?, ?)"
                );
                ps.setString(1, nameField.getText());
                ps.setString(2, emailField.getText());
                ps.setString(3, deptField.getText());
                ps.executeUpdate();
                conn.close();

                nameField.clear();
                emailField.clear();
                deptField.clear();

                new Alert(Alert.AlertType.INFORMATION, "Student Added Successfully").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(title, nameField, emailField, deptField, saveBtn, backBtn);

        stage.setScene(new Scene(layout, 400, 350));
        backBtn.setOnAction(e -> start(stage));
    }

    // ---------------- VIEW / UPDATE / DELETE ----------------
    private void openViewStudentsScreen(Stage stage) {

        Label title = new Label("View Students");

        TableView<Student> table = new TableView<>();

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));

        table.getColumns().addAll(nameCol, emailCol, deptCol);

        ObservableList<Student> data = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                data.add(new Student(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department")
                ));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);

        Button updateBtn = new Button("Update Department");
        Button deleteBtn = new Button("Delete Student");
        Button backBtn = new Button("Back");

        // UPDATE
        updateBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s == null) return;

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Department");
            dialog.setHeaderText("Enter new department");

            dialog.showAndWait().ifPresent(newDept -> {
                try {
                    Connection conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE students SET department=? WHERE email=?"
                    );
                    ps.setString(1, newDept);
                    ps.setString(2, s.getEmail());
                    ps.executeUpdate();
                    conn.close();

                    s.setDepartment(newDept);
                    table.refresh();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        // DELETE
        deleteBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s == null) return;

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM students WHERE email=?"
                );
                ps.setString(1, s.getEmail());
                ps.executeUpdate();
                conn.close();

                table.getItems().remove(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(title, table, updateBtn, deleteBtn, backBtn);

        stage.setScene(new Scene(layout, 520, 450));
        backBtn.setOnAction(e -> start(stage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
