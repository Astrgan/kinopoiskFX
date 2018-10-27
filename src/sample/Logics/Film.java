package sample.Logics;

import java.util.Arrays;

public class Film
{

    public String year;
    public String[] countries;
    public String[] actors;
    public String[] genres;
    public String kinopoisk_rating;
    public String imdb_rating;
    public String title_ru;
    public String title_en;
    public String added_at;
    public String description;
    public String directors;

    public String kinopoisk_id;
    public String iframe_url;
    public boolean flag = true;

    @Override
    public String toString() {
        return "Film{" +
                "year='" + year + '\'' +
                ", countries=" + Arrays.toString(countries) +
                ", actors=" + Arrays.toString(actors) +
                ", genres=" + Arrays.toString(genres) +
                ", kinopoisk_rating='" + kinopoisk_rating + '\'' +
                ", imdb_rating='" + imdb_rating + '\'' +
                ", title_ru='" + title_ru + '\'' +
                ", title_en='" + title_en + '\'' +
                ", added_at='" + added_at + '\'' +
                ", description='" + description + '\'' +
                ", directors='" + directors + '\'' +
                ", kinopoisk_id='" + kinopoisk_id + '\'' +
                ", iframe_url='" + iframe_url + '\'' +
                '}';
    }

    public String getKinopoisk_id() {
        return kinopoisk_id;
    }
}
