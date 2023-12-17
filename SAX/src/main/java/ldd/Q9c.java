package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9c extends DefaultHandler {

    private XMLStreamWriter writer;
    private double sumOfPrices;
    private boolean isSOBook;
    private int soBookCount;
    private boolean isSOBookPrice;

    public Q9c() throws IOException, XMLStreamException {
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
        if (qName.equals("book") && attributes.getValue("category").equals("SO")) {
            isSOBook = true;
            soBookCount++;
        }

        if (isSOBook && qName.equals("price")) {
            isSOBookPrice = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isSOBookPrice) {
            sumOfPrices += Double.parseDouble(new String(ch, start, length));
            isSOBook = false;
            isSOBookPrice = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeCharacters(String.format("%.2f", sumOfPrices / soBookCount));
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
