package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q11b extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isTitle;
    private boolean isParagraph;
    private String text;
    private String name;
    private List<String> names;

    public Q11b() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        text = "";
        name = "";
        names = new ArrayList<>();
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("ul");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("title")) {
            isTitle = true;
        } else if (qName.equals("p")) {
            isParagraph = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            String content = new String(ch, start, length);
            if (!content.isBlank()) {
                name += content + " ";
            }
        } else if (isParagraph) {
            String content = new String(ch, start, length);
            if (!content.isBlank()) {
                text += content;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("entry")) {
            if (text.contains("Oxford")) {
                names.add(formatName(name));
            }
            name = "";
            text = "";
        } else if (qName.equals("title")) {
            isTitle = false;
        } else if (qName.equals("body")) {
            isParagraph = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            for (String name : names) {
                writer.writeStartElement("li");
                writer.writeCharacters(name);
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private String formatName(String name) {
        String formatted = name.trim()
            .replace(" ,  ", ", ")
            .replace(" - ", "-");
        return formatted;
    }
}
