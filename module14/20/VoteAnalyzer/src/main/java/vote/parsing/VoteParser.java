package vote.parsing;

import org.xml.sax.SAXException;
import vote.database.VoterVisitRegistrar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class VoteParser {
    private final VoterVisitRegistrar visitRegistrar;

    public VoteParser(VoterVisitRegistrar visitRegistrar) {
        this.visitRegistrar = visitRegistrar;
    }

    public void parse(File file) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = parserFactory.newSAXParser();

            try {
                VoteHandler handler = new VoteHandler(visitRegistrar);
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

    public static void parse(Path path, VoterVisitRegistrar visitRegistrar) {
        parse(path.toFile(), visitRegistrar);
    }

    public static void parse(File file, VoterVisitRegistrar visitRegistrar) {
        VoteParser parser = new VoteParser(visitRegistrar);
        parser.parse(file);
    }
}
