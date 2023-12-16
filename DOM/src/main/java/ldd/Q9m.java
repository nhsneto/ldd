package ldd;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Q9m {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("bibliography.xml"));

        HashMap<Integer, Integer> bookCountByDecade = new HashMap<>();
        bookCountByDecade.put(2010, 0);
        bookCountByDecade.put(2000, 0);

        NodeList years = doc.getElementsByTagName("year");
        for (int i = 0; i < years.getLength(); i++) {
            int year = Integer.parseInt(years.item(i).getTextContent());
            if (year >= 2010 && year <= 2019) {
                int count = bookCountByDecade.get(2010);
                bookCountByDecade.put(2010, ++count);
            } else if (year >= 2000 && year <= 2009) {
                int count = bookCountByDecade.get(2000);
                bookCountByDecade.put(2000, ++count);
            }
        }

        Document out = db.newDocument();
        Element root = out.createElement("result");
        for (int decade : bookCountByDecade.keySet()) {
            Element books = out.createElement("books");
            books.setAttribute("decade", Integer.toString(decade));
            books.setAttribute("count", Integer.toString(bookCountByDecade.get(decade)));
            root.appendChild(books);
        }
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.transform(source, result);
    }
}
