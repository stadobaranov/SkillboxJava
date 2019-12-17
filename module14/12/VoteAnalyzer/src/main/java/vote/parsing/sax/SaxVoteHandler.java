package vote.parsing.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import vote.BirthDayFormatException;
import vote.Voter;
import vote.VoterList;
import vote.parsing.VoteDocumentUtils;

public class SaxVoteHandler extends DefaultHandler {
    private HandlingStatus currentStatus = HandlingStatus.START;
    private Voter currentVoter;
    private final VoterList voters = new VoterList();

    public VoterList getVoters() {
        return voters;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals(VoteDocumentUtils.VOTERS_TAG)) {
            checkCurrentStatus(HandlingStatus.START);
            currentStatus = HandlingStatus.VOTERS;
        }
        else if(qName.equals(VoteDocumentUtils.VOTER_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTERS);

            String name = attributes.getValue(VoteDocumentUtils.VOTER_NAME_ATTR);
            String birthDay = attributes.getValue(VoteDocumentUtils.VOTER_BIRTH_DAY_ATTR);

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
        else if(qName.equals(VoteDocumentUtils.VISIT_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTER);
            currentStatus = HandlingStatus.VISIT;
        }
        else {
            throwUnknownTagException(qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(VoteDocumentUtils.VOTERS_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTERS);
            currentStatus = HandlingStatus.END;
        }
        else if(qName.equals(VoteDocumentUtils.VOTER_TAG)) {
            checkCurrentStatus(HandlingStatus.VOTER);
            currentStatus = HandlingStatus.VOTERS;
        }
        else if(qName.equals(VoteDocumentUtils.VISIT_TAG)) {
            checkCurrentStatus(HandlingStatus.VISIT);
            voters.add(currentVoter);
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
