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

public class Q10g {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isTitle = false;
        boolean isPrice = false;
        boolean isYear = false;
        String title = "";
        String price = "";
        String year = "";
        HashMap<String, HashMap<String, String>> priceAndYearByTitle = new HashMap<>();
        List<Double> prices = new ArrayList<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("title")) {
                        isTitle = true;
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
                        HashMap<String, String> priceAndYear = new HashMap<>();
                        priceAndYear.put("price", price);
                        priceAndYear.put("year", year);
                        priceAndYearByTitle.put(title, priceAndYear);
                    }
                    break;
            }
        }

        double lowestPrice = Collections.min(prices);
        for (String titl : priceAndYearByTitle.keySet()) {
            double pric = Double.parseDouble(priceAndYearByTitle.get(titl).get("price"));
            if (pric == lowestPrice) {
                writer.writeStartElement("year");
                writer.writeCharacters(priceAndYearByTitle.get(titl).get("year"));
                writer.writeEndElement();
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
