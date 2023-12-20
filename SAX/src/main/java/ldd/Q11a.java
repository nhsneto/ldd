package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q11a extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isTitle;
    private String name;
    private int year;
    private Map<String, Integer> dudes;

    public Q11a() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        name = "";
        dudes = new HashMap<>();
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("results");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("entry")) {
            year = Integer.parseInt(attributes.getValue("died"));
        } else if (qName.equals("title")) {
            isTitle = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            String content = new String(ch, start, length);
            if (!content.isBlank()) {
                name += content + " ";
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("title")) {
            isTitle = false;
        } else if (qName.equals("entry")) {
            if (year < 1600) {
                dudes.put(formatName(name), year);
            }
            name = "";
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            List<Entry<String, Integer>> entries = new ArrayList<>(dudes.entrySet());
            entries.sort(Entry.comparingByValue());
            for (Entry<String, Integer> entry : entries) {
                writer.writeStartElement("dude");
                writer.writeCharacters(entry.getKey() + " (" + entry.getValue() + ")");
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private String formatName(String name) {
        String formatted = name.trim()
            .replace(" ,  ", ", ")
            .replace(" - ", "-");
        return formatted;
    }
}
