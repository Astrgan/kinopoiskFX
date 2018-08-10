package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Controller {

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
    private Button btnSave1;

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
        if(!URL.getText().equals("")) getFilm(URL.getText());
        if(!URLYear.getText().equals("")) getYear(URLYear.getText());
    }

    void getYear(String url){

        listYears.start(url);

        for (String path: listYears.listFilms) {
            KinopoiskParserFilm film = new KinopoiskParserFilm();
            film.start(path);
            listFilms.add(film);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        max = listFilms.size()-1;
        loadfilm(listFilms.get(index));
    }

    void loadfilm(KinopoiskParserFilm parser){

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
        parser.save();
    }

}
