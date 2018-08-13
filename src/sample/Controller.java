package sample;

import com.google.gson.JsonElement;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Logics.Film;
import sample.Logics.KinopoiskParserFilm;
import sample.Logics.KinopoiskParserListYears;
import sample.Logics.MoonwalkParserFilm;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView poster;

    @FXML
    private TextArea description;

    @FXML
    private TextField year;

    @FXML
    private TextField country;

    @FXML
    private TextField genres;

    @FXML
    private TextField writer;

    @FXML
    private TextField rating;

    @FXML
    private TextArea actors;

    @FXML
    private TextField URL;

    @FXML
    private TextField URLYear;

    @FXML
    private TextField names;

    @FXML
    private Button btnLoad;

    @FXML
    private TextField path;

    @FXML
    private TextField jsonPath;

    @FXML
    DirectoryChooser chooser = new DirectoryChooser();
    MoonwalkParserFilm parserJson;
    boolean flagLoad = true;
    boolean flagLoop = false;

    KinopoiskParserFilm parser = new KinopoiskParserFilm();
    KinopoiskParserListYears listYears = new KinopoiskParserListYears();
    ArrayList<KinopoiskParserFilm> listFilms = new ArrayList<>();

    int index = 0;
    int min = 0;
    int max;

    @FXML
    void PrevFilm(ActionEvent event) {
        if (index != min) {
            index--;
            loadfilm(listFilms.get(index));
        }
    }

    @FXML
    void load(ActionEvent event) {
        if (flagLoad) {

            flagLoad = false;
            flagLoop = false;
            btnLoad.setText("Stop");

   /*         Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            if (!URL.getText().equals("")) getFilm(URL.getText());
                            if (!URLYear.getText().equals("")) getYear(URLYear.getText());
                            if (!jsonPath.getText().equals("")) getJSON(jsonPath.getText());
                            return null;
                        }
                    };
                }
            };
            service.start();*/


            Platform.runLater(() -> {
                if (!URL.getText().equals("")) getFilm(URL.getText());
                if (!URLYear.getText().equals("")) getYear(URLYear.getText());
                if (!jsonPath.getText().equals("")) getJSON(jsonPath.getText());
            });

        }else {
            flagLoop = true;
            btnLoad.setText("Load");

        }

    }

    void getJSON(String path){
        parserJson = new MoonwalkParserFilm(path);

        for (Film film: parserJson.listFilms) {
            System.out.println("parse next");
            if (film.flag){
                KinopoiskParserFilm filmParser = new KinopoiskParserFilm();
                if (filmParser.start("https://www.kinopoisk.ru/film/" + film.kinopoisk_id, film.iframe_url, film.kinopoisk_id)){
                    System.out.println("Stop BAN!!!");
                    flagLoop = true;
                }else {
                    listFilms.add(filmParser);
                    film.flag = false;
                }
            }
            /*try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            if (flagLoop) break;
        }
        max = listFilms.size()-1;
         if(listFilms.size() > 0) loadfilm(listFilms.get(index));
        //parserJson.save();
        btnLoad.setText("Load");
        System.out.println("STOP getJSON");
    }

    void getYear(String url){

        listYears.start(url);

        for (String path: listYears.listFilms) {
            KinopoiskParserFilm film = new KinopoiskParserFilm();
            film.start(path);
            listFilms.add(film);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        max = listFilms.size()-1;
        loadfilm(listFilms.get(index));
    }

    void loadfilm(KinopoiskParserFilm parser){
        this.parser = parser;
        names.setText(parser.name);
        genres.setText(parser.genres);
        rating.setText(parser.rating);
        year.setText(parser.year);
        country.setText(parser.countries);
        writer.setText(parser.writer);
        description.setText(parser.description);
        actors.setText(parser.actors);
        poster.setImage(parser.image);
    }

    void getFilm(String url){

        parser.start(url);
        names.setText(parser.name);
        genres.setText(parser.genres);
        rating.setText(parser.rating);
        year.setText(parser.year);
        country.setText(parser.countries);
        writer.setText(parser.writer);
        description.setText(parser.description);
        actors.setText(parser.actors);
        poster.setImage(parser.image);
    }

    @FXML
    void nextFilm(ActionEvent event) {
        if (index != max) {
            index++;
            loadfilm(listFilms.get(index));
        }
    }

    @FXML
    void save(ActionEvent event) {
        parser.save(path.getText());
    }

    @FXML
    void saveAll(ActionEvent event) {
        for (KinopoiskParserFilm parser: listFilms) {
            parser.save(path.getText());
        }
    }

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {

        chooser.setTitle("Open Resource File");
        path.setText("/var/www/html/films");
        jsonPath.setText("moviJson.json");
    }

    @FXML
    void choose(ActionEvent event) {

        chooser.setTitle("Select folder");
        path.setText(chooser.showDialog(new Stage()).getAbsolutePath().toString());
    }

    @FXML
    void chooseJSON(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select folder");
        jsonPath.setText(chooser.showOpenDialog(new Stage()).getAbsolutePath().toString());
    }
}
