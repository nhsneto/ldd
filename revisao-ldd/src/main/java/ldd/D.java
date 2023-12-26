package ldd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class D {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("employees.xml"), "UTF-16");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("D.xml"), "UTF-16");

        boolean isFirstname = false;
        boolean isLastname = false;
        String ftname = "";
        String ltname = "";
        String firstname = "";
        String lastname = "";
        String admissionDate = "";
        String maxAdmissionDate = null;

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();
                    if (tagName.equals("employee")) {
                        admissionDate = reader.getAttributeValue(1);
                    } else if (tagName.equals("firstname")) {
                        isFirstname = true;
                    } else if (tagName.equals("lastname")) {
                        isLastname = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isFirstname) {
                        ftname = reader.getText();
                        isFirstname = false;
                    } else if (isLastname) {
                        ltname = reader.getText();
                        isLastname = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("employee")) {
                        if (maxAdmissionDate == null || (admissionDate.compareTo(maxAdmissionDate) == 1)) {
                            maxAdmissionDate = admissionDate;
                            firstname = ftname;
                            lastname = ltname;
                        }
                    }
                    break;
            }
        }

        writer.writeStartDocument("UTF-16", "1.0");
        writer.writeStartElement("employee");

        writer.writeStartElement("firstname");
        writer.writeCharacters(firstname);
        writer.writeEndElement();

        writer.writeStartElement("lastname");
        writer.writeCharacters(lastname);
        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
