package ldd;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q11c extends DefaultHandler {

    private XMLStreamWriter writer;
    private int paragraphCount;
    private String birthplace;
    private String id;
    private String born;
    private String died;

    public Q11c() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        paragraphCount = 0;
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
        if (qName.equals("entry"))  {
            birthplace = attributes.getValue("birthplace");
            id = attributes.getValue("id");
            born = attributes.getValue("born");
            died = attributes.getValue("died");
        } else if (qName.equals("p")) {
            paragraphCount++;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("entry")) {
            if (paragraphCount >= 3) {
                try {
                    writer.writeEmptyElement("entry");
                    writer.writeAttribute("birthplace", birthplace);
                    writer.writeAttribute("id", id);
                    writer.writeAttribute("born", born);
                    writer.writeAttribute("died", died);
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
            paragraphCount = 0;
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
