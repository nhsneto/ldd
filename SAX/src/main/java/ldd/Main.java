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
            Q9b handler = new Q9b();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputFile, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
