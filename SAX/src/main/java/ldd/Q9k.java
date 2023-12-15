package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9k extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isAuthor;
    private HashMap<String, Integer> authors;

    public Q9k() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        authors = new HashMap<>();
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("result");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            int maxValue = Collections.max(authors.values());
            for (Entry<String, Integer> entry : authors.entrySet()) {
                if (entry.getValue() == maxValue) {
                    writer.writeStartElement("author");
                    writer.writeCharacters(entry.getKey());
                    writer.writeEndElement();
                }
            }
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("author")) {
            isAuthor = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isAuthor) {
            String author = new String(ch, start, length);
            if (authors.containsKey(author)) {
                Integer count = authors.get(author);
                authors.put(author, ++count);
            } else {
                authors.put(author, 1);
            }
            isAuthor = false;
        }
    }
}
