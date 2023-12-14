package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9a extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isTitle;
    private String title;
    private int authorCount;

    public Q9a() throws IOException, XMLStreamException {
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
    public void endDocument() throws SAXException {
        try {
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
        } else if (qName.equals("author")) {
            authorCount++;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("book")) {
            if (authorCount > 1) {
                try {
                    writer.writeStartElement("title");
                    writer.writeCharacters(title);
                    writer.writeEndElement();
                } catch (XMLStreamException e) {
                    
                }
            }
            authorCount = 0;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            title = new String(ch, start, length);
            isTitle = false;
        }
    }
}
