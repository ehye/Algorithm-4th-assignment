package baseball_elimination;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    
    private final int[] wins;
    private final int[] loss;
    private final int[] left;
    private int[][] games;
    private ArrayList<String> teams = new ArrayList<>();
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        
        int teamCnt = 0;
        In in = new In(filename);
        teamCnt = in.readInt();
        wins = new int[teamCnt];
        loss = new int[teamCnt];
        left = new int[teamCnt];
        games = new int[teamCnt][teamCnt];
        for (int i = 0; i < teamCnt; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            left[i] = in.readInt();
            if (teamCnt > 1) {
                for (int j = 0; j < teamCnt; j++) {
                    games[i][j] = in.readInt();
                }
            }
        }
    }
    
    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }
    
    // all teams
    public Iterable<String> teams() {
        return teams;
    }
    
    // number of wins for given team
    public int wins(String team) {
        return wins[teams.indexOf(team)];
    }
    
    // number of losses for given team
    public int losses(String team) {
        return loss[teams.indexOf(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        return left[teams.indexOf(team)];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return games[teams.indexOf(team1)][teams.indexOf(team2)];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }    
}
