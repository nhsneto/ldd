package ldd;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {

    public static void main(String[] args) {
        try {
            File inputFile = new File("bibliography.xml");
            // Template handler = new Template();
            // Q9a handler = new Q9a();
            // Q9b handler = new Q9b();
            // Q9c handler = new Q9c();
            // Q9d handler = new Q9d();
            // Q9e handler = new Q9e();
            // Q9f handler = new Q9f();
            Q9g handler = new Q9g();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputFile, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
