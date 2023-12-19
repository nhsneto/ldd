package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10e {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isYear = false;
        int yearCount = 0;

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("year")) {
                        isYear = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isYear) {
                        int year = Integer.parseInt(reader.getText());
                        if (year >= 1980 && year <= 1989) {
                            yearCount++;
                        }
                        isYear = false;
                    }
                    break;
            }
        }

        writer.writeCharacters(Integer.toString(yearCount));
        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
