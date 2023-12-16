package ldd;

import java.io.File;
import java.util.Collections;
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

public class Q9k {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("bibliography.xml"));

        HashMap<String, Integer> authors = new HashMap<>();
        NodeList authorsNodeList = doc.getElementsByTagName("author");
        for (int i = 0; i < authorsNodeList.getLength(); i++) {
            String author = authorsNodeList.item(i).getTextContent();
            if (authors.containsKey(author)) {
                int count = authors.get(author);
                authors.put(author, ++count);
            } else {
                authors.put(author, 1);
            }
        }

        Document out = db.newDocument();
        Element root = out.createElement("result");
        int maxCount = Collections.max(authors.values());
        for (String author : authors.keySet()) {
            if (authors.get(author) == maxCount) {
                Element authorElement = out.createElement("author");
                authorElement.setTextContent(author);
                root.appendChild(authorElement);
            }
        }
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.transform(source, result);
    }
}
