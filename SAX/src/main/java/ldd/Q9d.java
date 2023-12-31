package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9d extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isYear;
    private int year;
    private boolean isPrice;
    private double price;
    private int bookCount;

    public Q9d() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
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
        if (qName.equals("year")) {
            isYear = true;
        } else if (qName.equals("price")) {
            isPrice = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isYear) {
            year = Integer.parseInt(new String(ch, start, length));
            isYear = false;
        } else if (isPrice) {
            price = Double.parseDouble(new String(ch, start, length));
            isPrice = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("book")) {
            if (year >= 2010 && price > 150) {
                bookCount++;
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeCharacters(Integer.toString(bookCount));
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
