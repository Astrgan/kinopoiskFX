package sample;

import com.sun.deploy.util.StringUtils;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Logics.Film;
import sample.Logics.KinopoiskParserFilm;
import sample.Logics.MoonwalkParserFilm;
import sample.Logics.PosterParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class Controller2 implements Initializable{

    int num = 130;
    File dir;
    PosterParser posterParser;

    @FXML
    private ImageView poster1;

    @FXML
    private CheckBox loadAll;

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
    DirectoryChooser chooser = new DirectoryChooser();

    @FXML
    void PrevFilm(ActionEvent event) {
        film = parserJson.listFilms.get(--num);
        showFilm(film);
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

    @FXML
    void load(ActionEvent event) {
        posterParser = new PosterParser();

        if(loadAll.isSelected()){
            superLoad();
        }else {
            try {
                num = Integer.parseInt(Files.readAllLines(Paths.get("num.txt")).get(0));
                System.out.println("num: " + num);
            } catch (IOException e) {
                e.printStackTrace();
            }
            parserJson = new MoonwalkParserFilm(jsonPath.getText());

            film = parserJson.listFilms.get(num);
            showFilm(parserJson.listFilms.get(num));

        }
        
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

        String url = posterParser.getPoster(film.title_ru, film.title_en);

        if (url != null){
            try(InputStream in = new URL(url).openStream()){
                Files.copy(in, Paths.get("images/image.jpg"));
            }catch (Exception e){
                e.printStackTrace();
            }
            File file = new File("images/image.jpg");
            try {
                poster1.setImage(new Image(file.toURI().toURL().toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }





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

    void superLoad(){
        for (Film film: parserJson.listFilms) {

        }
    }

    void showSavedFilm(Film film){
        System.out.println(film.getKinopoisk_id());
        System.out.println(path.getText()+"/"+film.getKinopoisk_id() + "/names.txt");
        dir = new File(path.getText(), film.kinopoisk_id);
        if(!Files.exists(Paths.get(dir.getAbsolutePath().toString())))return;
        System.out.println("существует");
        try{

            names.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/names.txt") ), "UTF-8"));
            genres.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/genres.txt") ), "UTF-8"));
            rating.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/rating.txt") ), "UTF-8"));
            year.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/year.txt") ), "UTF-8"));
            country.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/countries.txt") ), "UTF-8"));
            writer.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/writers.txt") ), "UTF-8"));
            description.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/description.txt") ), "UTF-8"));
            actors.setText(new String( Files.readAllBytes( Paths.get(path.getText()+"/"+film.getKinopoisk_id() + "/actors.txt") ), "UTF-8"));
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

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        path.setText("C:\\Apache24\\films\\films 7");
        jsonPath.setText("D:\\movies_foreign.json");
    }
}
