package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9j extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isAuthor;
    private Set<String> authors;
    private boolean isYear;
    private String year;
    private int count;

    public Q9j() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        authors = new HashSet<>();
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
            writer.writeCharacters(Integer.toString(count));
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
        } else if (qName.equals("year")) {
            isYear = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("book")) {
            if (year.equals("2012") && authors.contains("Abraham Silberschatz")) {
                count++;
            }
            authors.clear();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isAuthor) {
            authors.add(new String(ch, start, length));
            isAuthor = false;
        } else if (isYear) {
            year = new String(ch, start, length);
            isYear = false;
        }
    }
}
