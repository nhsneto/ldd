package simulado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class B {

    public static void main(String[] args) throws Exception {
        File inputFile = new File("CaetanoVeloso.xml");
        CaetanoVelosoHandler handler = new CaetanoVelosoHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputFile, handler);
    }
}

class CaetanoVelosoHandler extends DefaultHandler {

    private XMLStreamWriter xsw;
    int albumCount;
    String year;
    private Map<String, Integer>  countByYear;

    public CaetanoVelosoHandler() throws XMLStreamException, IOException {
        xsw = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("b.xml"), "UTF-8");
        countByYear = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("album")) {
            year = attributes.getValue("year");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("album")) {
            if (countByYear.containsKey(year)) {
                int count = countByYear.get(year);
                countByYear.put(year, ++count);
            } else {
                countByYear.put(year, 1);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            xsw.writeStartDocument("UTF-8", "1.0");

            int maxCount = Collections.max(countByYear.values());
            for (String year : countByYear.keySet()) {
                int count = countByYear.get(year);
                
                if (count == maxCount) {
                    xsw.writeStartElement("year");
                    xsw.writeAttribute("count", Integer.toString(count));
                    xsw.writeCharacters(year);
                    xsw.writeEndElement();
                }
            }

            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
