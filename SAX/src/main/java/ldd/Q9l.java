package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9l extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isAuthor;
    private TreeMap<String, Set<String>> authorsByFirstLetter;

    public Q9l() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        authorsByFirstLetter = new TreeMap<>();
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
            String firstLetter = author.substring(0, 1);

            if (authorsByFirstLetter.containsKey(firstLetter)) {
                Set<String> authors = authorsByFirstLetter.get(firstLetter);
                authors.add(author);
            } else {
                Set<String> authors = new TreeSet<>();
                authors.add(author);
                authorsByFirstLetter.put(firstLetter, authors);
            }
            isAuthor = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            for (String letter : authorsByFirstLetter.keySet()) {
                Set<String> authors = authorsByFirstLetter.get(letter);
                writer.writeStartElement("authors");
                writer.writeAttribute("letter", letter);
                writer.writeAttribute("count", Integer.toString(authors.size()));
                for (String author : authors) {
                    writer.writeStartElement("author");
                    writer.writeCharacters(author);
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
}
