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

public class Q9g extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isAuthor;
    private Set<String> authors;

    public Q9g() throws IOException, XMLStreamException {
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
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("author")) {
            isAuthor = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isAuthor) {
            String author = new String(ch, start, length);
            if (author.startsWith("A")) {
                authors.add(author);
            }
            isAuthor = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            for (String author : authors) {
                writer.writeStartElement("author");
                writer.writeCharacters(author);
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
