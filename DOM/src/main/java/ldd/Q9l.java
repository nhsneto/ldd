package ldd;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Q9l {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("bibliography.xml"));

        TreeMap<String, TreeSet<String>> authorsByFirstLetter = new TreeMap<>();
        NodeList authorNodeList = doc.getElementsByTagName("author");
        for (int i = 0; i < authorNodeList.getLength(); i++) {
            String author = authorNodeList.item(i).getTextContent();
            String firstLetter = author.substring(0, 1);
            
            if (authorsByFirstLetter.containsKey(firstLetter)) {
                TreeSet<String> authors = authorsByFirstLetter.get(firstLetter);
                authors.add(author);
            } else {
                TreeSet<String> authors = new TreeSet<>();
                authors.add(author);
                authorsByFirstLetter.put(firstLetter, authors);
            }
        }

        Document out = db.newDocument();
        Element root = out.createElement("result");
        for (String letter : authorsByFirstLetter.keySet()) {
            Set<String> authors = authorsByFirstLetter.get(letter);
            Element authorsElement = out.createElement("authors");

            authorsElement.setAttribute("letter", letter);
            authorsElement.setAttribute("count", Integer.toString(authors.size()));
            for (String author : authors) {
                Element authorElement = out.createElement("author");
                authorElement.setTextContent(author);
                authorsElement.appendChild(authorElement);
            }
            root.appendChild(authorsElement);
        }
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.transform(source, result);
    }
}
