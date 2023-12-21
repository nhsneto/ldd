package ldd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Q12a {

    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlif = XMLInputFactory.newFactory();
        XMLOutputFactory xmlof = XMLOutputFactory.newFactory();
        XMLStreamReader reader = xmlif.createXMLStreamReader(new FileInputStream("futebol.xml"), "UTF-8");
        XMLStreamWriter writer = xmlof.createXMLStreamWriter(new FileOutputStream("result.xml"), "UTF-8");

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("result");

        boolean isTime = false;
        boolean isMandante = false;
        boolean isVisitante = false;
        boolean isGols = false;
        String mandante = "";
        String visitante = "";
        String golsMandante = "";
        String golsVisitante = "";

        int jogoId = 0;
        int maxGols = 0;
        Map<Integer, Map<String, String>> jogosById = new HashMap<>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    String tagName = reader.getLocalName();

                    if (tagName.equals("mandante")) {
                        isMandante = true;
                    } else if (tagName.equals("visitante")) {
                        isVisitante = true;
                    } else if (tagName.equals("time")) {
                        isTime = true;
                    } else if (tagName.equals("gols")) {
                        isGols = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isTime && isMandante) {
                        mandante = reader.getText();
                        isTime = false;
                    } else if (isGols && isMandante) {
                        golsMandante = reader.getText();
                        isGols = false;
                    } else if (isTime && isVisitante) {
                        visitante = reader.getText();
                        isTime = false;
                    } else if (isGols && isVisitante) {
                        golsVisitante = reader.getText();
                        isGols = false;
                    } 
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    String tagNam = reader.getLocalName();

                    if (tagNam.equals("mandante")) {
                        isMandante = false;
                    } else if (tagNam.equals("visitante")) {
                        isVisitante = false;
                    } else if (reader.getLocalName().equals("jogo")) {
                        Map<String, String> jogo = new HashMap<>();
                        int totalGolsNoJogo = Integer.parseInt(golsMandante) + Integer.parseInt(golsVisitante);

                        jogo.put("mandante", mandante);
                        jogo.put("golsMandante", golsMandante);
                        jogo.put("visitante", visitante);
                        jogo.put("golsVisitante", golsVisitante);
                        jogo.put("totalGols", Integer.toString(totalGolsNoJogo));
                        jogoId += 1;
                        jogosById.put(jogoId, jogo);

                        if (totalGolsNoJogo > maxGols) {
                            maxGols = totalGolsNoJogo;
                        }
                    }
                    break;
            }
        }

        for (int id : jogosById.keySet()) {
            int totalGolsNoJogo = Integer.parseInt(jogosById.get(id).get("totalGols"));

            if (totalGolsNoJogo == maxGols) {
                Map<String, String> jogo = jogosById.get(id);

                writer.writeStartElement("jogo");

                writer.writeStartElement("mandante");
                writer.writeStartElement("time");
                writer.writeCharacters(jogo.get("mandante"));
                writer.writeEndElement();
                writer.writeStartElement("gols");
                writer.writeCharacters(jogo.get("golsMandante"));
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeStartElement("visitante");
                writer.writeStartElement("time");
                writer.writeCharacters(jogo.get("visitante"));
                writer.writeEndElement();
                writer.writeStartElement("gols");
                writer.writeCharacters(jogo.get("golsVisitante"));
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeEndElement();
            }
        }

        writer.writeEndDocument();
        writer.close();
        reader.close();
    }
}
