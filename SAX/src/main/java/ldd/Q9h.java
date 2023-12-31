package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q9h extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isBookInPortuguese;

    public Q9h() throws IOException, XMLStreamException {
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
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isBookInPortuguese) {
            try {
                writer.writeStartElement("title");
                writer.writeCharacters(new String(ch, start, length));
                writer.writeEndElement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isBookInPortuguese = false;
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
}
