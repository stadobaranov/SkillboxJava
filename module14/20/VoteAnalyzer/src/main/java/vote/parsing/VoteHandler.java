package vote.parsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import vote.BirthDayFormatException;
import vote.Voter;
import vote.database.VoterVisitRegistrar;
import java.sql.SQLException;

public class VoteHandler extends DefaultHandler {
    private static final String VOTERS_TAG = "voters";
    private static final String VOTER_TAG = "voter";
    private static final String VISIT_TAG = "visit";

    private HandlingStatus currentStatus = HandlingStatus.START;
    private Voter currentVoter;
    private final VoterVisitRegistrar visitRegistrar;

    public VoteHandler(VoterVisitRegistrar visitRegistrar) {
        this.visitRegistrar = visitRegistrar;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals(VOTERS_TAG)) {
            checkCurrentStatus(HandlingStatus.START);
            currentStatus = HandlingStatus.VOTERS;
        }
        else if(qName.equals(VOTER_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTERS);

            String name = attributes.getValue("name");
            String birthDay = attributes.getValue("birthDay");

            if(name == null || birthDay == null) {
                throw new SAXException("Не удалось разобрать избирателя");
            }

            try {
                currentVoter = new Voter(name, birthDay);
            }
            catch(BirthDayFormatException exception) {
                throw new SAXException("Не удалось разобрать избирателя", exception);
            }

            currentStatus = HandlingStatus.VOTER;
        }
        else if(qName.equals(VISIT_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTER);
            currentStatus = HandlingStatus.VISIT;
        }
        else {
            throwUnknownTagException(qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(VOTERS_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTERS);
            currentStatus = HandlingStatus.END;
        }
        else if(qName.equals(VOTER_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTER);
            currentStatus = HandlingStatus.VOTERS;
        }
        else if(qName.equals(VISIT_TAG)) {
            checkCurrentStatus(HandlingStatus.VISIT);

            try {
                visitRegistrar.register(currentVoter);
            }
            catch(SQLException exception) {
                throw new SAXException("Не удалось добавить визит избирателя в БД", exception);
            }

            currentStatus = HandlingStatus.VOTER;
        }
        else {
            throwUnknownTagException(qName);
        }
    }

    private void checkCurrentStatus(HandlingStatus status) throws SAXException {
        if(currentStatus != status) {
            throw new SAXException("Некорретный формат документа");
        }
    }

    private static void throwUnknownTagException(String name) throws SAXException {
        throw new SAXException(String.format("Найден неизвестный тэг - \"%s\"", name));
    }

    private static enum HandlingStatus {
        START, VOTERS, VOTER, VISIT, END
    }
}
