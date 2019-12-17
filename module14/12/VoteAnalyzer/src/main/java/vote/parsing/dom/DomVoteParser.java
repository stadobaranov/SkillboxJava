package vote.parsing.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import vote.BirthDayFormatException;
import vote.Voter;
import vote.VoterList;
import vote.parsing.VoteDocumentUtils;
import vote.parsing.VoteParser;
import vote.parsing.VoteParserException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomVoteParser implements VoteParser {
    @Override
    public VoterList parse(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            try {
                return parseDocument(db.parse(file));
            }
            catch(IOException | SAXException exception) {
                throw new VoteParserException("Не удалось разобрать XML-документ", exception);
            }
        }
        catch(ParserConfigurationException exception) {
            throw new VoteParserException("Не удалось создать парсер", exception);
        }
    }

    private VoterList parseDocument(Document document) {
        NodeList voterNodes = document.getElementsByTagName(VoteDocumentUtils.VOTER_TAG);
        VoterList voters = new VoterList();

        for(int i = 0, e = voterNodes.getLength(); i < e; i++) {
            Node voterNode = voterNodes.item(i);
            NamedNodeMap attributes = voterNode.getAttributes();
            String name = getAttribute(attributes, VoteDocumentUtils.VOTER_NAME_ATTR);
            String birthDay = getAttribute(attributes, VoteDocumentUtils.VOTER_BIRTH_DAY_ATTR);

            try {
                voters.add(new Voter(name, birthDay));
            }
            catch(BirthDayFormatException exception) {
                throw new VoteParserException("Не удалось разобрать избирателя", exception);
            }
        }

        return voters;
    }

    private static String getAttribute(NamedNodeMap attributes, String name) {
        Node node = attributes.getNamedItem(name);

        if(node == null) {
            throw new VoteParserException("Не удалось разобрать избирателя");
        }

        try {
            return node.getNodeValue();
        }
        catch(DOMException exception) {
            throw new VoteParserException("Не удалось разобрать избирателя", exception);
        }
    }
}
