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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;

public class F {

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(false);
        net.sf.saxon.s9api.DocumentBuilder dbu = processor.newDocumentBuilder();
        XdmNode node = dbu.build(new File("employees.xml"));

        XPathCompiler xpath2 = processor.newXPathCompiler();
        XPathExecutable exec = xpath2.compile("count(//employee[salary > avg(//salary)])");
        XPathSelector selector = exec.load();
        selector.setContextItem(node);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document out = db.newDocument();
        Element root = out.createElement("count");
        String count = selector.evaluateSingle().getStringValue();
        root.setTextContent(count);
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("F.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
