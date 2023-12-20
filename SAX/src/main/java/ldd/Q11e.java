package ldd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Q11e extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isTitle;
    private String birthplace;
    private String born;
    private String died;
    private String name;
    private Map<String, Map<String, Map<String, String>>> peopleByBirthplace;

    public Q11e() throws IOException, XMLStreamException {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");
        name = "";
        peopleByBirthplace = new TreeMap<>();
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("groups");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("entry")) {
            birthplace = attributes.getValue("birthplace");
            born = attributes.getValue("born");
            died = attributes.getValue("died");
        } else if (qName.equals("title")) {
            isTitle = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            String content = new String(ch, start, length);
            if (!content.isBlank()) {
                name += content + " ";
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("entry")) {
            if (birthplace != null) {
                if (peopleByBirthplace.containsKey(birthplace)) {
                   Map<String, Map<String, String>> people = peopleByBirthplace.get(birthplace);
                    Map<String, String> personData = new HashMap<>();

                    personData.put("born", born);
                    personData.put("died", died);
                    people.put(formatName(name), personData);
                } else {
                    Map<String, Map<String, String>> people = new TreeMap<>();
                    Map<String, String> personData = new HashMap<>();

                    personData.put("born", born);
                    personData.put("died", died);
                    people.put(formatName(name), personData);
                    peopleByBirthplace.put(birthplace, people);
                }
            }
            name = "";
        } else if (qName.equals("title")) {
            isTitle = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            for (String birthplace : peopleByBirthplace.keySet()) {
                writer.writeStartElement("group");
                writer.writeAttribute("birthplace", birthplace);

                Map<String, Map<String, String>> people = peopleByBirthplace.get(birthplace);
                for (String person : people.keySet()) {
                    Map<String, String> personData = people.get(person);

                    writer.writeStartElement("person");
                    writer.writeAttribute("born", personData.get("born"));
                    writer.writeAttribute("died", personData.get("died"));
                    writer.writeCharacters(person);
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

    private String formatName(String name) {
        String formatted = name.trim()
            .replace(" ,  ", ", ")
            .replace(" - ", "-");
        return formatted;
    }
}
