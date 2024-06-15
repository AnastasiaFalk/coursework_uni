package com.example.coursework_uni;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MusicLibraryController implements Initializable {

    public Button addButton;
    public Button deleterecordButton;
    private Connection connection;

    @FXML
    private TableView<Music> tableView;

    @FXML
    private TableColumn<Music, String> artistColumn;

    @FXML
    private TableColumn<Music, String> titleColumn;

    @FXML
    private TableColumn<Music, String> trackColumn;

    @FXML
    private TableColumn<Music, Double> durationColumn;

    @FXML
    public TextField artistField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField trackField;

    @FXML
    private TextField durationField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ініціалізація стовпців
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        trackColumn.setCellValueFactory(new PropertyValueFactory<>("track"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // приєднання до бази даних PostgresSQL
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MusicLibrary", "anastasiafalk", "anastasiafalk");
            loadMusicData(); // завантаження даних з бази
        } catch (SQLException e) {
            System.out.println("Помилка під'єднання до бази даних.");
            e.printStackTrace();
        }

        deleterecordButton.setOnAction(e -> deleteRecord());
    }

    // завантаження музичних записів з бази даних до таблиці при повторному відкритті програми
    private void loadMusicData() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT artist, title, track, duration FROM music");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String artist = resultSet.getString("artist");
                String title = resultSet.getString("title");
                String track = resultSet.getString("track");
                double duration = resultSet.getDouble("duration");

                Music music = new Music(artist, title, track, duration);
                tableView.getItems().add(music);
            }
        } catch (SQLException e) {
            System.out.println("Error loading data from the database");
            e.printStackTrace();
        }
    }

    public void addComposition() {
        String artist = artistField.getText();
        String title = titleField.getText();
        String track = trackField.getText();
        double duration = Double.parseDouble(durationField.getText());

        Music newMusic = new Music(artist, title, track, duration);

        // додати нову композицію в таблицю
        tableView.getItems().add(newMusic);

        // додати нову композицію в базу даних
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO music (artist, title, track, duration) VALUES (?, ?, ?, ?)");
            statement.setString(1, artist);
            statement.setString(2, title);
            statement.setString(3, track);
            statement.setDouble(4, duration);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding data to the database");
            e.printStackTrace();
        }

        // очищення полів для вводу
        artistField.clear();
        titleField.clear();
        trackField.clear();
        durationField.clear();

        // Записати до файлу report.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("report.txt", true))) {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeString = dateTime.format(formatter);
            writer.println(dateTimeString + " - Додано композицію: " + newMusic.getArtist() + " - " + newMusic.getTrack() + ", тривалістю " + newMusic.getDuration() + "с.");
        } catch (IOException e) {
            System.out.println("Помилка запису до файлу report.txt");
            e.printStackTrace();
        }
    }

    public void deleteRecord() {
        Music selectedMusic = tableView.getSelectionModel().getSelectedItem();

        if (selectedMusic != null) {
            // Видалити запис з таблиці
            tableView.getItems().remove(selectedMusic);

            // Видалити запис з бази даних
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM music WHERE artist = ? AND title = ? AND track = ? AND duration = ?");
                statement.setString(1, selectedMusic.getArtist());
                statement.setString(2, selectedMusic.getTitle());
                statement.setString(3, selectedMusic.getTrack());
                statement.setDouble(4, selectedMusic.getDuration());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Помилка видалення даних з бази");
                e.printStackTrace();
            }
            // Записати до файлу report.txt
            try (PrintWriter writer = new PrintWriter(new FileWriter("report.txt", true))) {
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTimeString = dateTime.format(formatter);
                writer.println(dateTimeString + " - Видалено композицію: " + selectedMusic.getArtist() + " - " + selectedMusic.getTrack() + ", тривалістю " + selectedMusic.getDuration() + "с.");
            } catch (IOException e) {
                System.out.println("Помилка запису до файлу report.txt");
                e.printStackTrace();
            }
        }
    }

}
