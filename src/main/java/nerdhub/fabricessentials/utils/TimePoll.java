package nerdhub.fabricessentials.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimePoll {

    private int votesYes;
    private int votesNo;
    private String time;
    private List<UUID> votedPlayers;

    public TimePoll(String time) {
        this.votesYes = 0;
        this.votesYes = 0;
        this.time = time;
        this.votedPlayers = new ArrayList<>();
    }

    public void addVote(UUID uuid, String timeArg) {
        if(timeArg.equals(time)) {
            this.addVoteYes();
        }else {
            this.addVoteNo();
        }

        this.votedPlayers.add(uuid);
    }

    public boolean hasPlayerVoted(UUID uuid) {
        return this.votedPlayers.contains(uuid);
    }

    public boolean didPollWin() {
        return votesYes > votesNo;
    }

    public void addVoteYes() {
        this.votesYes++;
    }

    public int getVotesYes() {
        return votesYes;
    }

    public void addVoteNo() {
        votesNo++;
    }

    public int getVoteNo() {
        return votesNo;
    }

    public String getTime() {
        return time;
    }
}
