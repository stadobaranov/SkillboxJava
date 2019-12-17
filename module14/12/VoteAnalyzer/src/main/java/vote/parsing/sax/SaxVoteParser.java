package vote.parsing.sax;

import org.xml.sax.SAXException;
import vote.VoterList;
import vote.parsing.VoteParser;
import vote.parsing.VoteParserException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class SaxVoteParser implements VoteParser {
    @Override
    public VoterList parse(File file) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = parserFactory.newSAXParser();

            try {
                SaxVoteHandler handler = new SaxVoteHandler();
                parser.parse(file, handler);
                return handler.getVoters();
            }
            catch(IOException | SAXException exception) {
                throw new VoteParserException("Не удалось разобрать XML-документ", exception);
            }
        }
        catch(ParserConfigurationException | SAXException exception) {
            throw new VoteParserException("Не удалось создать SAX-парсер", exception);
        }
    }
}
