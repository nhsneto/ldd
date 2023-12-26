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

public class E {

    public static void main(String[] args) throws Exception {
        File inputFile = new File("employees.xml");
        EmployeesHandler handler = new EmployeesHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(inputFile, handler);
    }
}

class EmployeesHandler extends DefaultHandler {

    private XMLStreamWriter writer;
    private boolean isTitle;
    private boolean isSalary;
    private String title;
    private double salary;
    private int programmerCount;
    private double sumOfSalaries;

    public EmployeesHandler() throws Exception {
        writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream("E.xml"), "UTF-8");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("title")) {
            isTitle = true;
        } else if (qName.equals("salary")) {
            isSalary = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTitle) {
            title = new String(ch, start, length);
            isTitle = false;
        } else if (isSalary) {
            salary = Double.parseDouble(new String(ch, start, length));
            isSalary = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("employee")) {
            if (title.equals("Programação")) {
                programmerCount++;
                sumOfSalaries += salary;
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("average");
            writer.writeCharacters(String.format("%.2f", sumOfSalaries / programmerCount));
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
