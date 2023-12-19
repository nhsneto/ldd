package ldd;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Q10c {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("cd_catalog.xml"));

        double highestPrice = 0d;
        NodeList priceNodeList = doc.getElementsByTagName("price");
        for (int i = 0; i < priceNodeList.getLength(); i++) {
            double price = Double.parseDouble(priceNodeList.item(i).getTextContent());
            if (highestPrice < price) {
                highestPrice = price;
            }
        }

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression expr = xpath.compile("//cd[price = " + highestPrice + "]");

        Document out = db.newDocument();
        Element root = out.createElement("result");

        NodeList cdNodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < cdNodeList.getLength(); i++) {
            NodeList cdChildren = cdNodeList.item(i).getChildNodes();
            Element cd = out.createElement("cd");

            for (int j = 0; j < cdChildren.getLength(); j++) {
                Node child = cdChildren.item(j);

                switch (child.getNodeName()) {
                    case "title":
                        Element title = out.createElement("title");
                        title.setTextContent(child.getTextContent());
                        cd.appendChild(title);
                        break;
                    case "artist":
                        Element artist = out.createElement("artist");
                        artist.setTextContent(child.getTextContent());
                        cd.appendChild(artist);
                        break;
                    case "country":
                        Element country = out.createElement("country");
                        country.setTextContent(child.getTextContent());
                        cd.appendChild(country);
                        break;
                    case "company":
                        Element company = out.createElement("company");
                        company.setTextContent(child.getTextContent());
                        cd.appendChild(company);
                        break;
                    case "price":
                        Element price = out.createElement("price");
                        price.setTextContent(child.getTextContent());
                        cd.appendChild(price);
                        break;
                    case "year":
                        Element year = out.createElement("year");
                        year.setTextContent(child.getTextContent());
                        cd.appendChild(year);
                        break;
                }
            }

            root.appendChild(cd);
        }
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
