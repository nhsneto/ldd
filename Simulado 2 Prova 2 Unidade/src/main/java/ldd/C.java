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

public class C {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("albums.xml"), "UTF-8");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("C.xml"), "UTF-8");

        String albumTitle = "";
        int songCount = 0;
        Map<String, Integer> songCountByAlbum = new HashMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("album")) {
                        albumTitle = reader.getAttributeValue(0);
                    } else if (tagName.equals("song")) {
                        songCount++;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("album")) {
                        songCountByAlbum.put(albumTitle, songCount);
                        songCount = 0;
                    }
                    break;
            }
        }

        List<Entry<String, Integer>> entries = new ArrayList<>(songCountByAlbum.entrySet());
        Comparator<Entry<String, Integer>> comparator = Entry.comparingByValue();
        entries.sort(comparator.reversed());

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("table");
        for (Entry<String, Integer> entry : entries) {
            writer.writeStartElement("tr");

            writer.writeStartElement("td");
            writer.writeCharacters(entry.getKey());
            writer.writeEndElement();

            writer.writeStartElement("td");
            writer.writeCharacters(entry.getValue().toString());
            writer.writeEndElement();

            writer.writeEndElement();
        }
        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
