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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;

public class Q10c {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("cd_catalog.xml"));
        Processor processor = new Processor(false);
        XdmNode node = processor.newDocumentBuilder().wrap(doc);

        XPathCompiler xpath2 = processor.newXPathCompiler();
        XPathExecutable exec = xpath2.compile("//cd[price = max(//price)]");
        XPathSelector selector = exec.load();
        selector.setContextItem(node);

        Document out = db.newDocument();
        Element root = out.createElement("result");
        selector.forEach(item -> {
            XdmNode xdmNode = (XdmNode) item;
            Node domNode = (Node) xdmNode.getExternalNode();
            root.appendChild(out.importNode(domNode, true));
        });
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
