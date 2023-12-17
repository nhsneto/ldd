package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9m extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isYear;
    private int booksDecade2000Count;
    private int booksDecade2010Count;

    public Q9m() throws IOException, XMLStreamException {
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
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isYear) {
            int year = Integer.parseInt(new String(ch, start, length));
            if (year < 2010) {
                booksDecade2000Count++;
            } else {
                booksDecade2010Count++;
            }
            isYear = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeStartElement("books");
            writer.writeAttribute("decade", "2010");
            writer.writeAttribute("count", Integer.toString(booksDecade2010Count));
            writer.writeEndElement();

            writer.writeStartElement("books");
            writer.writeAttribute("decade", "2000");
            writer.writeAttribute("count", Integer.toString(booksDecade2000Count));
            writer.writeEndElement();
            
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
