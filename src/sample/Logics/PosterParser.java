package sample.Logics;

import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PosterParser {
    
    private ArrayList <Elements> divElements;
    private Document doc;
    public Image image;


    public PosterParser() {

        divElements = new ArrayList<>();
        try {
            //set HTTP proxy host to 127.0.0.1 (localhost)
            System.setProperty("https.proxyHost", "127.0.0.1");

            //set HTTP proxy port to 8081
            System.setProperty("https.proxyPort", "8081");
            File htmlFolder = new File("E:\\kinopoiskFX\\filmsSitePoster");

            for (File file: htmlFolder.listFiles() ) {
                
                doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
                divElements.add(doc.select("div[class=posters-item posters-item-tile]"));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPoster(String nameRu, String nameEn){

        for (Elements elements: divElements) {

            for (Element element: elements) {
                Elements elementTitleEn =  element.getElementsByClass("poster-title-eng");
                Elements elementTitle =  element.getElementsByClass("poster-title");

                if (elementTitle.text().equals(nameRu) || elementTitleEn.text().contains(nameEn)) {
                    System.out.println(elementTitleEn.text());
                    System.out.println(elementTitle.text());
                    Elements elementTitleImage = element.getElementsByClass("image-shadow-poster posters__image");
                    String strURL = "http:" + elementTitleImage.select("img").first().attr("src");
                    System.out.println(strURL);

                    return strURL;
                }
            }
        }

        return null;
    }
}
