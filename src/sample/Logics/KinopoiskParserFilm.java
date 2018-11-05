package sample.Logics;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class KinopoiskParserFilm {

    public String name = "";
    String fullName = "";
    public String writer = "";
    public String countries = "";
    public String year = "";
    public String genres = "";
    public Image image;
    public String URLPoster = "";
    public String description = "";
    public String rating = "";
    public String actors = "";
    public String premiere = "";
    Document doc = null;

    File dir;
    String iframeCode, kinopoiskID;

    public boolean start (String kinoURL, String iframe, String kinopoiskID) {
        this.iframeCode = iframe;
        this.kinopoiskID = kinopoiskID;
        if (kinopoiskID != null) return start(kinoURL);
        else return false;

    }

    public void setParam(String name, String writer, String countries, String year, String genres, Image image, String description, String rating, String actors) {
        this.name = name;
        this.writer = writer;
        this.countries = countries;
        this.year = year;
        this.genres = genres;
        this.image = image;
        this.description = description;
        this.rating = rating;
        this.actors = actors;
    }

    public boolean start (String kinoURL) {
        System.out.println("url: " + kinoURL);

        try {
            //set HTTP proxy host to 127.0.0.1 (localhost)
            System.setProperty("https.proxyHost", "127.0.0.1");

            //set HTTP proxy port to 8081
            System.setProperty("https.proxyPort", "8081");

            doc = Jsoup
                    .connect(kinoURL)
                    .userAgent("Mozilla/5.0")
                    .timeout(10 * 5000)
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(doc.title().equals("КиноПоиск.ru")){
            System.out.println("BAN !!!");
            return true;
        }

        Elements metaTags = doc.getElementsByTag("meta");
        for (Element metaTag : metaTags) {
            String content = metaTag.attr("property");
            if ("title".equals(content)) {
                fullName = metaTag.attr("content");
                System.out.println(fullName);
                break;
            }
        }

        System.out.println("fullName" + fullName);
        System.out.println(doc.title());

         int x1 = fullName.indexOf('(')-2;
         int x2 = fullName.lastIndexOf(',');

        name = fullName.substring(1, x1);
        if(x2 != -1 ){
            if (!name.equals(fullName.substring(x1+3, x2)))name += " / " + fullName.substring(x1+3, x2);
        }

        Element table = doc.select("table").first();

        Elements rows = table.select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i); //по номеру индекса получает строку
            Elements cols = row.select("td");// разбиваем полученную строку по тегу  на столбы
//            System.out.print(cols.get(0).text() + ": ");// первый столбец
//            System.out.print(cols.get(1).text());


            if(cols.get(0).text().contains("режиссер")) writer = cols.get(1).text();
            if(cols.get(0).text().contains("жанр")){
                if (!cols.get(1).text().contains("... слова"))genres = cols.get(1).text();
                else genres = cols.get(1).text().substring(0, cols.get(1).text().length()-11);
            }
            if(cols.get(0).text().contains("год") && cols.get(1).text().length() > 3) year = cols.get(1).text().substring(0, 4);
            if(cols.get(0).text().contains("страна") && cols.size() > 1) countries = cols.get(1).text();
            if(cols.get(0).text().contains("премьера")) premiere = cols.get(1).text();

//            System.out.println();
        }


        Elements elementsDescription = doc.select("div[itemprop=description]");
        if (elementsDescription.size() != 0) description = elementsDescription.get(0).text();
//        System.out.println("description: " + description);

        Elements elementsRating = doc.select("span[class=rating_ball]");
        System.out.println("elementsRating.size() " + elementsRating.size() );
        if (elementsRating.size() != 0 ) rating = elementsRating.get(0).text();


        Element link = doc.select("link[rel=\"image_src\"]").first();
        String relHref = link.attr("href");
//        System.out.println(relHref);


        Element divActors;
        Elements divActorsDoc = doc.select("div[id=actorList] > ul");
        if (divActorsDoc.size() != 0 ){
            divActors = divActorsDoc.get(0);


        Elements listActors = divActors.children();
        StringBuilder stringBuilder = new StringBuilder();
        for (Element elem : listActors) {

            if(!elem.text().equals("...")) {
                stringBuilder.append(elem.text());
                stringBuilder.append(", ");
            }
            //            System.out.println(elem.text());
        }
        actors = stringBuilder.toString();
        }



      /*  Element addDetails = doc.select("div.container > div.main-content > div.clearfix > div.col_7.post-info > ul.no-bullet").first();
        Elements divChildren = addDetails.children();
        for (Element elem : divChildren) {
            System.out.println(elem.text());
        }*/

        URLPoster = relHref;
        getImage(relHref);

//        doc = null;
        return false;
    }

    public void getImage(String strURL){
        try{

            boolean backgroundLoading = false;

            image = new Image(strURL, backgroundLoading);


          /*  URL url = new URL(strURL);
            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream("image.jpg"));

            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void save(String path){
        dir = new File(path, kinopoiskID);
        dir.mkdir();
        saveFilm();
    }

    private void saveFilm(){
        try (

                FileWriter factors = new FileWriter(dir.getAbsolutePath().toString() + "/actors.txt");
                FileWriter fcountries = new FileWriter(dir.getAbsolutePath().toString() + "/countries.txt");
                FileWriter fdescription = new FileWriter(dir.getAbsolutePath().toString() + "/description.txt");
                FileWriter fgenres = new FileWriter(dir.getAbsolutePath().toString() + "/genres.txt");
                FileWriter fnames = new FileWriter(dir.getAbsolutePath().toString() + "/names.txt");
                FileWriter fwriters = new FileWriter(dir.getAbsolutePath().toString() + "/writers.txt");
                FileWriter fyear = new FileWriter(dir.getAbsolutePath().toString() + "/year.txt");
                FileWriter iframe = new FileWriter(dir.getAbsolutePath().toString() + "/iframe.txt");
                FileWriter kinopoisk_id = new FileWriter(dir.getAbsolutePath().toString() + "/kinopoisk_id.txt");
                FileWriter fpremiere = new FileWriter(dir.getAbsolutePath().toString() + "/premiere.txt");
                FileWriter frating = new FileWriter(dir.getAbsolutePath().toString() + "/rating.txt");
                FileWriter fdocument = new FileWriter(dir.getAbsolutePath().toString() + "/document.html")

        ){
            frating.write(rating);
            //fpremiere.write(premiere);
            factors.write(actors);
            fcountries.write(countries);
            fdescription.write(description);
            fgenres.write(genres);
            fnames.write(name);
            fwriters.write(writer);
            fyear.write(year);
            iframe.write(iframeCode);
            //kinopoisk_id.write(kinopoiskID);
           // fdocument.write(doc.select("body").html());

            File image = new File(dir.getAbsolutePath().toString() + "/image.jpg");
            BufferedImage bImage = SwingFXUtils.fromFXImage(this.image, null);
            try {
                ImageIO.write(bImage, "jpg", image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
