package ldd;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Q9b {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("bibliography.xml"));

        NodeList books = doc.getElementsByTagName("book");
        int authorCount = 0;
        int bookCount = 0;
        for (int i = 0; i < books.getLength(); i++) {
            Node book = books.item(i);
            NodeList bookChildren = book.getChildNodes();
            for (int j = 0; j < bookChildren.getLength(); j++) {
                if (bookChildren.item(j).getNodeName().equals("author")) {
                    authorCount++;
                }
            }

            if (authorCount > 1) {
                bookCount++;
            }
            authorCount = 0;
        }

        Document out = db.newDocument();
        Element root = out.createElement("result");
        root.setTextContent(Integer.toString(bookCount));
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
