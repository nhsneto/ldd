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

public class Q11e {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("chalmers-biography-extract.xml"));
        Processor processor = new Processor(false);
        XdmNode node = processor.newDocumentBuilder().wrap(doc);

        XQueryCompiler xquery = processor.newXQueryCompiler();
        String query = "let $birthplaces :=" +
                       " for $bp in distinct-values(//entry/@birthplace)" +
                       " order by $bp" +
                       " return $bp" +
                       " for $b in $birthplaces" +
                       " return <group birthplace=\"{$b}\">{" +
                       " for $e in //entry[@birthplace = $b]" +
                       " return <person born='{$e/@born}' died='{$e/@died}'>{normalize-space($e/title/string())}</person>}</group>";
        XQueryExecutable exec = xquery.compile(query);
        XQueryEvaluator evaluator = exec.load();
        evaluator.setContextItem(node);

        Document out = db.newDocument();
        Element root = out.createElement("result");
        evaluator.run(new DOMDestination(root));
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("result.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
