package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9i extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isBookInPortuguese;
    private boolean isBookInEnglish;
    private boolean isPortuguesePrice;
    private boolean isEnglishPrice;
    private double sumOfPricesPortuguese;
    private double sumOfPricesEnglish;
    private int portugueseCount;
    private int englishCount;

    public Q9i() throws IOException, XMLStreamException {
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
        if (qName.equals("title") && attributes.getValue("lang").equals("pt-br")) {
            isBookInPortuguese = true;
            portugueseCount++;
        }

        if (qName.equals("title") && attributes.getValue("lang").equals("en")) {
            isBookInEnglish = true;
            englishCount++;
        }

        if (isBookInPortuguese && qName.equals("price")) {
            isPortuguesePrice = true;
        }

        if (isBookInEnglish && qName.equals("price")) {
            isEnglishPrice = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isPortuguesePrice) {
            sumOfPricesPortuguese += Double.parseDouble(new String(ch, start, length));
            isPortuguesePrice = false;
        } else if (isEnglishPrice) {
            sumOfPricesEnglish += Double.parseDouble(new String(ch, start, length));
            isEnglishPrice = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("book")) {
            isBookInPortuguese = false;
            isBookInEnglish = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeCharacters(Boolean.toString((sumOfPricesPortuguese / portugueseCount) > (sumOfPricesEnglish / englishCount)));
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
