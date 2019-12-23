package vote;

public class VoterVisitInfo {
    private final Voter voter;
    private final int visitCount;

    public VoterVisitInfo(Voter voter, int visitCount) {
        this.voter = voter;
        this.visitCount = visitCount;
    }

    public Voter getVoter() {
        return voter;
    }

    public int getVisitCount() {
        return visitCount;
    }
}
