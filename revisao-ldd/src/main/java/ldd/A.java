package ldd;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class A {

    public static void main(String[] args) throws Exception {
        File inputFile = new File("Copa2018.xml");
        Copa2018Handler handler = new Copa2018Handler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputFile, handler);
    }
}

class Copa2018Handler extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isMinuto;
    private String dat;
    private String man;
    private String vis;
    private String data;
    private String mandante;
    private String visitante;
    private Integer menorTempo;

    public Copa2018Handler() throws Exception  {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("A.xml"), "UTF-8");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("jogo")) {
            dat = attributes.getValue("data");
            man = attributes.getValue("mandante");
            vis = attributes.getValue("visitante");
        } else if (qName.equals("minuto")) {
            isMinuto = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isMinuto) {
            String minuto = new String(ch, start, length);
            if (!minuto.contains("+")) {
                int min = Integer.parseInt(minuto);
                if (menorTempo == null || min < menorTempo) {
                    menorTempo = min;
                    data = dat;
                    mandante = man;
                    visitante = vis;
                }
            }
            isMinuto = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeEmptyElement("jogo");
            writer.writeAttribute("data", data);
            writer.writeAttribute("mandante", mandante);
            writer.writeAttribute("visitante", visitante);
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
