package vote.parsing;

import vote.VoterList;
import java.io.File;

public interface VoteParser {
    public abstract VoterList parse(File file);
}
