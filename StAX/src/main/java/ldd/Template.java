package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Template {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    break;
                case XMLStreamConstants.CHARACTERS:
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    break;
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
