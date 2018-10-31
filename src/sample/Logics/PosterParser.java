package sample.Logics;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class PosterParser {

    private Elements elements = null;
    private Document doc;
    public Image image;


    public PosterParser() {

        try {
            //set HTTP proxy host to 127.0.0.1 (localhost)
            System.setProperty("https.proxyHost", "127.0.0.1");

            //set HTTP proxy port to 8081
            System.setProperty("https.proxyPort", "8081");

            doc = Jsoup.parse(new File("C:\\Users\\vasilev_av\\Documents\\MyJob\\д posters.html"), "UTF-8", "http://example.com/");

            elements = doc.select("div[class=posters-item posters-item-tile]");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPoster(String nameRu, String nameEn){

        for (Element element: elements) {
            Elements elementTitleEn =  element.getElementsByClass("poster-title-eng");
            Elements elementTitle =  element.getElementsByClass("poster-title");

            if (elementTitle.text().equals(nameRu)) {
                System.out.println(elementTitleEn.text());
                System.out.println(elementTitle.text());
                Elements elementTitleImage = element.getElementsByClass("image-shadow-poster posters__image");
                String strURL = "http:" + elementTitleImage.select("img").first().attr("src");
                System.out.println(strURL);



                return strURL;
            }



            if (elementTitleEn.text().contains("The Savages")) {
                System.out.println(elementTitleEn.text());
                Elements elementTitleImage = element.getElementsByClass("image-shadow-poster posters__image");
                System.out.println(elementTitleImage.select("img").first().attr("src"));
            }



        }
        return null;
    }
}
