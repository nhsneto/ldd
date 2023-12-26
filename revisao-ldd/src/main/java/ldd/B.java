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

public class B {

    public static void main(String[] args) throws Exception {
        Processor processor = new Processor(false);
        net.sf.saxon.s9api.DocumentBuilder dbu = processor.newDocumentBuilder();
        XdmNode node = dbu.build(new File("Copa2018.xml"));

        XQueryCompiler xquery = processor.newXQueryCompiler();
        XQueryExecutable exec = xquery.compile("let $gols := " +
                " for $j in //jogo" +
                " return <gols data=\"{$j/@data}\">{" +
                "abs(count($j//evento[@time = 'mandante']) - count($j//evento[@time = 'visitante']))" +
                "}</gols>" +
                " for $j in //jogo" +
                " where $j/@data = $gols[. = max($gols)]/@data" +
                " return $j");
        XQueryEvaluator evaluator = exec.load();
        evaluator.setContextItem(node);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document out = db.newDocument();
        Element root = out.createElement("jogos");
        evaluator.run(new DOMDestination(root));
        out.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(out);
        StreamResult result = new StreamResult(new File("B.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
