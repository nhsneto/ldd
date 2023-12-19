package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10i {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isCountry = false;
        Set<String> countries = new TreeSet<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("country")) {
                        isCountry = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isCountry) {
                        countries.add(reader.getText());
                        isCountry = false;
                    }
                    break;
            }
        }

        for (String country : countries) {
            writer.writeStartElement("country");
            writer.writeCharacters(country);
            writer.writeEndElement();
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
