package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10a {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isCd = false;
        boolean isPrice = false;
        int cdCount = 0;
        double sumOfPrices = 0d;

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("cd")) {
                        isCd = true;
                    } else if (tagName.equals("price")) {
                        isPrice = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isCd) {
                        cdCount++;
                        isCd = false;
                    } else if (isPrice) {
                        sumOfPrices += Double.parseDouble(reader.getText());
                        isPrice = false;
                    }
                    break;
            }
        }

        writer.writeCharacters(String.format("%.2f", sumOfPrices / cdCount));
        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
