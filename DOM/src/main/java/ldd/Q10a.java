package ldd;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;

public class Q10a {

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(false);
        net.sf.saxon.s9api.DocumentBuilder dbu = processor.newDocumentBuilder();
        XdmNode node = dbu.build(new File("cd_catalog.xml"));
        XPathCompiler xpath2 = processor.newXPathCompiler();
        XPathExecutable exec = xpath2.compile("avg(/catalog/cd/price)");
        XPathSelector selector = exec.load();
        selector.setContextItem(node);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document out = db.newDocument();
        Element root = out.createElement("result");
        Double average = Double.parseDouble(selector.evaluateSingle().getStringValue());
        root.setTextContent(String.format("%.2f", average));
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.transform(source, result);
    }
}
