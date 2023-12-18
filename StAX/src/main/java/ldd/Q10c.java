package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10c {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isTitle = false;
        boolean isArtist = false;
        boolean isCountry = false;
        boolean isCompany = false;
        boolean isPrice = false;
        boolean isYear = false;

        String title = null;
        String artist = null;
        String country = null;
        String company = null;
        String price = null;
        String year = null;

        HashMap<String, HashMap<String, String>> albumsByTitle = new HashMap<>();
        List<Double> prices = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("title")) {
                        isTitle = true;
                    } else if (tagName.equals("artist")) {
                        isArtist = true;
                    } else if (tagName.equals("country")) {
                        isCountry = true;
                    } else if (tagName.equals("company")) {
                        isCompany = true;
                    } else if (tagName.equals("price")) {
                        isPrice = true;
                    } else if (tagName.equals("year")) {
                        isYear = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isTitle) {
                        title = reader.getText();
                        isTitle = false;
                    } else if (isArtist) {
                        artist = reader.getText();
                        isArtist = false;
                    } else if (isCountry) {
                        country = reader.getText();
                        isCountry = false;
                    } else if (isCompany) {
                        company = reader.getText();
                        isCompany = false;
                    } else if (isPrice) {
                        price = reader.getText();
                        prices.add(Double.parseDouble(price));
                        isPrice = false;
                    } else if (isYear) {
                        year = reader.getText();
                        isYear = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("cd")) {
                        HashMap<String, String> album = new HashMap<>();
                        album.put("artist", artist);
                        album.put("country", country);
                        album.put("company", company);
                        album.put("price", price);
                        album.put("year", year);
                        albumsByTitle.put(title, album);
                    }
                    break;
            }
        }

        double highestPrice = Collections.max(prices);
        for (String titl : albumsByTitle.keySet()) {
            double albumPrice = Double.parseDouble(albumsByTitle.get(titl).get("price"));

            if (albumPrice == highestPrice) {
                writer.writeStartElement("cd");

                writer.writeStartElement("title");
                writer.writeCharacters(titl);
                writer.writeEndElement();

                String[] tags = {"artist", "country", "company", "price", "year"};
                for (String tag : tags) {
                    writer.writeStartElement(tag);
                    writer.writeCharacters(albumsByTitle.get(titl).get(tag));
                    writer.writeEndElement();
                }

                writer.writeEndElement();
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
