package ldd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class B {

    public static void main(String[] args) throws Exception {
        File inputFile = new File("albums.xml");
        AlbumsHandler handler = new AlbumsHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputFile, handler);
    }
}

class AlbumsHandler extends DefaultHandler {

    private XMLStreamWriter writer;
    private Map<String, String> lengthBySong;

    public AlbumsHandler() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("B.xml"), "UTF-8");
        lengthBySong = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("song")) {
            String title = attributes.getValue("title");
            String length = attributes.getValue("length");
            lengthBySong.put(title, length);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            List<Entry<String, String>> entries = new ArrayList<>(lengthBySong.entrySet());
            Comparator<Entry<String, String>> comparator = Entry.comparingByValue();
            entries.sort(comparator.reversed());

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("ul");
            for (Entry<String, String> entry : entries) {
                writer.writeStartElement("li");
                writer.writeCharacters(entry.getKey() + " (" + entry.getValue() + ")");
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
