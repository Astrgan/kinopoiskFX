package sample;

import com.sun.deploy.util.StringUtils;

import com.sun.tools.javac.util.ArrayUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Logics.Film;
import sample.Logics.KinopoiskParserFilm;
import sample.Logics.MoonwalkParserFilm;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Controller2 {

    int num = 130;
    File dir;

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
    private TextField year1;

    @FXML
    private TextField country1;

    @FXML
    private TextField genres1;

    @FXML
    private TextField writer1;

    @FXML
    private TextField rating1;

    @FXML
    private TextField names1;

    @FXML
    private TextArea description1;

    @FXML
    private TextArea actors1;
    private MoonwalkParserFilm parserJson;
    private KinopoiskParserFilm filmParser;
    private Film film;

    @FXML
    void PrevFilm(ActionEvent event) {
        film = parserJson.listFilms.get(--num);
        showFilm(film);
    }

    @FXML
    void choose(ActionEvent event) {

    }

    @FXML
    void chooseJSON(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select folder");
        jsonPath.setText(chooser.showOpenDialog(new Stage()).getAbsolutePath().toString());
    }

    @FXML
    void load(ActionEvent event) {

        parserJson = new MoonwalkParserFilm(jsonPath.getText());

        film = parserJson.listFilms.get(num);
        showFilm(parserJson.listFilms.get(num));


        
    }
    
    void showFilm(Film film){



        names1.setText(film.title_ru + " / " + film.title_en);
        if(film.material_data != null && film.material_data.genres != null && film.material_data.genres.length != 0 ) genres1.setText(String.join(", ", film.material_data.genres));
        rating1.setText(film.material_data.kinopoisk_rating);
        year1.setText(film.material_data.year);
        if(film.material_data.countries != null && film.material_data.countries.length != 0 ) country1.setText(String.join(", ", film.material_data.countries));
        if(film.material_data.directors != null && film.material_data.directors.length != 0 ) writer1.setText(String.join(", ", film.material_data.directors));
        description1.setText(film.material_data.description);
        if(film.material_data.actors != null && film.material_data.actors.length != 0 ) actors1.setText(String.join(", ", film.material_data.actors));

        showSavedFilm(film);

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

    void showSavedFilm(Film film){
        System.out.println(film.getKinopoisk_id());
        System.out.println(path.getText()+"/"+film.getKinopoisk_id() + "/names.txt");
        dir = new File(path.getText(), film.kinopoisk_id);
        if(!Files.exists(Paths.get(dir.getAbsolutePath().toString())))return;
        System.out.println("существует");
        try
                /*FileReader factors = new FileReader(dir.getAbsolutePath().toString() + "/actors.txt");
                FileReader fcountries = new FileReader(dir.getAbsolutePath().toString() + "/countries.txt");
                FileReader fdescription = new FileReader(dir.getAbsolutePath().toString() + "/description.txt");
                FileReader fgenres = new FileReader(dir.getAbsolutePath().toString() + "/genres.txt");
                FileReader fnames = new FileReader(dir.getAbsolutePath().toString() + "/names.txt");
                FileReader fwriters = new FileReader(dir.getAbsolutePath().toString() + "/writers.txt");
                FileReader fyear = new FileReader(dir.getAbsolutePath().toString() + "/year.txt");
                FileReader iframe = new FileReader(dir.getAbsolutePath().toString() + "/iframe.txt");
                FileReader kinopoisk_id = new FileReader(dir.getAbsolutePath().toString() + "/kinopoisk_id.txt");
                FileReader fpremiere = new FileReader(dir.getAbsolutePath().toString() + "/premiere.txt");
                FileReader frating = new FileReader(dir.getAbsolutePath().toString() + "/rating.txt");
                FileReader fdocument = new FileReader(dir.getAbsolutePath().toString() + "/document.html")*/
                {

            names.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/names.txt") ), "UTF-8"));
          /*  genres.setText();
            rating.setText();
            year.setText();
            country.setText();
            writer.setText();
            description.setText();
            actors.setText();*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void KPUpdate(ActionEvent event) {
        KinopoiskParserFilm filmParser = new KinopoiskParserFilm();
        if (filmParser.start("https://www.kinopoisk.ru/film/" + film.kinopoisk_id, film.iframe_url, film.kinopoisk_id)) {
            System.out.println("Stop BAN!!!");
        }else {
            loadfilm(filmParser);
        }
    }

    @FXML
    void nextFilm(ActionEvent event) {
        film = parserJson.listFilms.get(++num);
        showFilm(film);
    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void saveAll(ActionEvent event) {

    }

}
