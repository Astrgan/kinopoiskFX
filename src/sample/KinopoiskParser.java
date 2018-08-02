package sample;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class KinopoiskParser {

    public String writer;
    public Image image;
    public String URLPoster;

    public void start (String kinoURL) {
        Document doc = null;
        try {

            doc = Jsoup.connect(kinoURL).get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //String title = doc.title();
        //System.out.println("Title : " + title);


        Element table = doc.select("table").first();

        Elements rows = table.select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i); //по номеру индекса получает строку
            Elements cols = row.select("td");// разбиваем полученную строку по тегу  на столбы
            System.out.print(cols.get(0).text() + ": ");// первый столбец
            System.out.print(cols.get(1).text());
            if(cols.get(0).text().contains("режиссер")) writer = cols.get(1).text();
            System.out.println();
        }


        //System.out.println(doc.select("div[itemprop=description]").get(0).text());

        //System.out.println(doc.select("span[class=rating_ball]").get(0).text());

        Element link = doc.select("link[rel=\"image_src\"]").first();
        String relHref = link.attr("href");
        System.out.println(relHref);
        URLPoster = relHref;
        getImage(relHref);
    }

    public void getImage(String strURL){
        try{

            boolean backgroundLoading = true;

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
}
