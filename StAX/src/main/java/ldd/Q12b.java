package ldd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q12b {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("futebol.xml"), "UTF-8");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("table");

        boolean isGols = false;
        int totalGolsNoJogo = 0;
        String rodada = "";
        Map<String, Integer> golsByRodada = new HashMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();

                    if (tagName.equals("rodada")) {
                        rodada = reader.getAttributeValue(0);
                    } else if (tagName.equals("gols")) {
                        isGols = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isGols) {
                        totalGolsNoJogo += Integer.parseInt(reader.getText());
                        isGols = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("rodada")) {
                        golsByRodada.put(rodada, totalGolsNoJogo);
                        totalGolsNoJogo = 0;
                    }
                    break;
            }
        }

        List<Entry<String, Integer>> entries = new ArrayList<>(golsByRodada.entrySet());
        Comparator<Entry<String, Integer>> comparator = Entry.comparingByValue();
        entries.sort(comparator.reversed());

        writer.writeStartElement("tr");

        writer.writeStartElement("th");
        writer.writeCharacters("Rodada");
        writer.writeEndElement();

        writer.writeStartElement("th");
        writer.writeCharacters("Gols");
        writer.writeEndElement();

        writer.writeEndElement();

        for (Entry<String, Integer> entry : entries) {
            writer.writeStartElement("tr");

            writer.writeStartElement("td");
            writer.writeCharacters(entry.getKey());
            writer.writeEndElement();

            writer.writeStartElement("td");
            writer.writeCharacters(Integer.toString(entry.getValue()));
            writer.writeEndElement();

            writer.writeEndElement();
        }

        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
