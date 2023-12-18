package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10b {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isTitle = false;
        boolean isYear = false;
        String title = "";
        int year = 0;
        HashMap<String, Integer> yearByTitle = new HashMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("title")) {
                        isTitle = true;
                    } else if (tagName.equals("year")) {
                        isYear = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isTitle) {
                        title = reader.getText();
                        isTitle = false;
                    } else if (isYear) {
                        year = Integer.parseInt(reader.getText());
                        isYear = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("cd")) {
                        yearByTitle.put(title, year);
                    }
                    break;
            }
        }

        int smallestYear = Collections.min(yearByTitle.values());
        for (String titl : yearByTitle.keySet()) {
            if (yearByTitle.get(titl) == smallestYear) {
                writer.writeStartElement("title");
                writer.writeCharacters(titl);
                writer.writeEndElement();
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
