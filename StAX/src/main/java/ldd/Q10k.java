package ldd;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q10k {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileReader("cd_catalog.xml"));
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isCompany = false;
        Map<String, Integer> albumCountByCompany = new TreeMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("company")) {
                        isCompany = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isCompany) {
                        String company = reader.getText();
                        if (albumCountByCompany.containsKey(company)) {
                            int count = albumCountByCompany.get(company);
                            albumCountByCompany.put(company, ++count);
                        } else {
                            albumCountByCompany.put(company, 1);
                        }
                        isCompany = false;
                    }
                    break;
            }
        }

        for (String company : albumCountByCompany.keySet()) {
            writer.writeEmptyElement("company");
            writer.writeAttribute("name", company);
            writer.writeAttribute("count", Integer.toString(albumCountByCompany.get(company)));
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
