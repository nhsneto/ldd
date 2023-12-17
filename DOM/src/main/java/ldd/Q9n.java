package ldd;

import java.io.File;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Q9n {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("bibliography.xml"));

        HashMap<Integer, Set<String>> booksByYear = new HashMap<>();
        NodeList books = doc.getElementsByTagName("book");
        for (int i = 0; i < books.getLength(); i++) {
            NodeList bookChildren = books.item(i).getChildNodes();
            String title = "";
            Integer year = 0;

            for (int j = 0; j < bookChildren.getLength(); j++) {
                Node child = bookChildren.item(j);
                if (child.getNodeName().equals("title")) {
                    title = child.getTextContent();
                } else if (child.getNodeName().equals("year")) {
                    year = Integer.parseInt(child.getTextContent());
                }
            }

            if (booksByYear.containsKey(year)) {
                Set<String> titles = booksByYear.get(year);
                titles.add(title);
            } else {
                Set<String> titles = new TreeSet<>();
                titles.add(title);
                booksByYear.put(year, titles);
            }
        }

        Document out = db.newDocument();
        Element root = out.createElement("result");
        NavigableSet<Integer> years = new TreeSet<>(booksByYear.keySet()).descendingSet();

        for (int year : years) {
            Set<String> titles = booksByYear.get(year);
            Element booksElement = out.createElement("books");

            booksElement.setAttribute("year", Integer.toString(year));
            booksElement.setAttribute("count", Integer.toString(titles.size()));
            for (String title : titles) {
                Element bookElement = out.createElement("book");
                bookElement.setTextContent(title);
                booksElement.appendChild(bookElement);
            }
            root.appendChild(booksElement);
        }
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.transform(source, result);
    }
}
