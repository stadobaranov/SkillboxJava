package vote.parsing;

import org.xml.sax.SAXException;
import vote.database.VoterInserter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class VoteParser {
    private final VoterInserter voterInserter;

    public VoteParser(VoterInserter voterInserter) {
        this.voterInserter = voterInserter;
    }

    public void parse(File file) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = parserFactory.newSAXParser();

            try {
                VoteHandler handler = new VoteHandler(voterInserter);
                parser.parse(file, handler);
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
