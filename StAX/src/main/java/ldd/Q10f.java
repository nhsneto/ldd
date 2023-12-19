package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10f {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isArtist = false;
        boolean isCompany = false;
        String artist = "";
        String company = "";

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("artist")) {
                        isArtist = true;
                    } else if (tagName.equals("company")) {
                        isCompany = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isArtist) {
                        artist = reader.getText();
                        isArtist = false;
                    } else if (isCompany) {
                        company = reader.getText();
                        isCompany = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("cd")) {
                        if (company.equals("Polydor")) {
                            writer.writeStartElement("artist");
                            writer.writeCharacters(artist);
                            writer.writeEndElement();
                        }
                    }
                    break;
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
