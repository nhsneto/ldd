package simulado;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class C {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("CaetanoVeloso.xml"), "UTF-8");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("c.xml"), "UTF-8");

        boolean isCompany = false;
        String company = "";
        Map<String, Integer> countByCompany = new HashMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("company")) {
                        isCompany = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isCompany) {
                        company = reader.getText();
                        isCompany = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (reader.getLocalName().equals("company")) {
                        if (countByCompany.containsKey(company)) {
                            int count = countByCompany.get(company);
                            countByCompany.put(company, ++count);
                        } else {
                            countByCompany.put(company, 1);
                        }
                    }
                    break;
            }
        }

        List<Entry<String, Integer>> entries = new ArrayList<>(countByCompany.entrySet());
        Comparator<Entry<String, Integer>> comparator = Entry.comparingByValue();
        entries.sort(comparator.reversed());

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("companies");
        for (Entry<String, Integer> entry : entries) {
            writer.writeEmptyElement("company");
            writer.writeAttribute("name", entry.getKey());
            writer.writeAttribute("count", Integer.toString(entry.getValue()));
        }
        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
