package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9n extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isYear;
    private boolean isTitle;
    private String title;
    private HashMap<Integer, Set<String>> booksByYear;

    public Q9n() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        booksByYear = new HashMap<>();
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
            NavigableSet<Integer> yearsInDescendingOrder = new TreeSet<>(booksByYear.keySet()).descendingSet();

            for (int year : yearsInDescendingOrder) {
                Set<String> titles = booksByYear.get(year);
                writer.writeStartElement("books");
                writer.writeAttribute("year", Integer.toString(year));
                writer.writeAttribute("count", Integer.toString(titles.size()));
                for (String title: titles) {
                    writer.writeStartElement("title");
                    writer.writeCharacters(title);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }

            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("title")) {
            isTitle = true;
        } else if (qName.equals("year")) {
            isYear = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            title = new String(ch, start, length);
            isTitle = false;
        }

        if (isYear) {
            Integer year = Integer.parseInt(new String(ch, start, length));
            if (booksByYear.containsKey(year)) {
                Set<String> titles = booksByYear.get(year);
                titles.add(title);
            } else {
                Set<String> titles = new TreeSet<>();
                titles.add(title);
                booksByYear.put(year, titles);
            }
            isYear = false;
        }
    }
}
