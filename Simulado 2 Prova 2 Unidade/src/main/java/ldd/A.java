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

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmNode;

public class A {

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(false);
        net.sf.saxon.s9api.DocumentBuilder dbu = processor.newDocumentBuilder();
        XdmNode node = dbu.build(new File("albums.xml"));

        XQueryCompiler xquery = processor.newXQueryCompiler();
        String query = "for $album in //album" +
                       " return <table>" +
                       "<caption>{$album/@title/string()}</caption>{" +
                       "for $song in $album/song" +
                       " return <tr><td>{$song/@title/string()}</td><td>{$song/@length/string()}</td></tr>" +
                       "}</table>";
        XQueryExecutable exec = xquery.compile(query);
        XQueryEvaluator evaluator = exec.load();
        evaluator.setContextItem(node);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document out = db.newDocument();
        Element root = out.createElement("html");
        evaluator.run(new DOMDestination(root));
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("A.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
