package vote;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class VoterList {
    private final Map<Voter, Integer> votersToVisits = new HashMap<>();

    public void add(Voter newVoter) {
        votersToVisits.compute(newVoter, (existingVoter, visits) -> visits == null? 1: visits + 1);
    }

    public void printDuplicatedVoters(PrintStream ps) {
        for(Map.Entry<Voter, Integer> entry: votersToVisits.entrySet()) {
            Integer visits = entry.getValue();

            if(visits > 1) {
                ps.printf("%s - %d%n", entry.getKey(), visits);
            }
        }
    }
}
