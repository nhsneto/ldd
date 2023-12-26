package ldd;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class C {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("Copa2018.xml"), "UTF-8");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("C.xml"), "UTF-8");

        int golsCount = 0;
        int jogoCount = 0;

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("evento")) {
                        golsCount++;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("jogo")) {
                        if (golsCount == 0) {
                            jogoCount++;
                        }
                        golsCount = 0;
                    }
                    break;
            }
        }

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("jogos");
        writer.writeCharacters(Integer.toString(jogoCount));
        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
