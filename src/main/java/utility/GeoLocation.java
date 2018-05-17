/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author filip
 */
public class GeoLocation {

    public static String getCity() throws MalformedURLException, IOException {
        
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine();

        String myString = null;
        Document doc = Jsoup.connect("https://whatismyipaddress.com/ip/" + ip).get();
        Elements metaTags = doc.getElementsByTag("meta");

        for (Element metaTag : metaTags) {
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");

            if ("description".equals(name)) {
                myString = content.split("\\s+")[1];
                myString = myString.substring(0, myString.length() - 1);
            }

        }

        return myString;
    }

}
