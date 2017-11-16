package baseball_elimination;
import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    
    private final int[] wins;
    private final int[] loss;
    private final int[] remain;
    private int[][] games;
    private ArrayList<String> teams = new ArrayList<>();
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        
        int teamCnt = 0;
        In in = new In(filename);
        teamCnt = in.readInt();
        wins = new int[teamCnt];
        loss = new int[teamCnt];
        remain = new int[teamCnt];
        games = new int[teamCnt][teamCnt];
        for (int i = 0; i < teamCnt; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            remain[i] = in.readInt();
            if (teamCnt > 1)
                for (int j = 0; j < teamCnt; j++)
                    games[i][j] = in.readInt();
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
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return wins[teams.indexOf(team)];
    }
    
    // number of losses for given team
    public int losses(String team) {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return loss[teams.indexOf(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return remain[teams.indexOf(team)];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teams.contains(team1) | !teams.contains(team2))
            throw new IllegalArgumentException();
        return games[teams.indexOf(team1)][teams.indexOf(team2)];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        
        int n = teams.indexOf(team);
        int wins_for_r = 0;
        int size = teams.size();
        int remaining = 0;
        // calculate wins for R
        for (String t : teams) {
            if (t != team){
                wins_for_r += this.wins[teams.indexOf(t)];
            }
        }
        // calculate remaining games among other teams
        for (int i = 0; i < size; i++) {
            if (i == n) continue;
            for (int j = i + 1; j < size; j++) {
                remaining += this.games[i][j];
//                this.games[i][j] = 100;
            }
        }
        // calculate the team average wins in R
        double avg = (wins_for_r + remaining) / (size>1 ? size-1 : 1);
        
        if (this.wins[n] < avg) return true;
        else return false;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        
        int team_vertices = teams.size();
        int game_vertices = team_vertices*(team_vertices-1)/2;
        int s_and_t = 2;
        int nodeID = 0;
        int V = game_vertices + team_vertices + s_and_t;
        FlowNetwork fn = new FlowNetwork(V);
        for (int i = 0; i < team_vertices; i++) {
            for (int j = i; j < team_vertices; j++) {
                if (i == j) continue;
                
                fn.addEdge(new FlowEdge(i, nodeID, this.games[i][j]));
                fn.addEdge(new FlowEdge(nodeID, game_vertices + i, Integer.MAX_VALUE));
                fn.addEdge(new FlowEdge(nodeID, game_vertices + j, Integer.MAX_VALUE));
            }
            fn.addEdge(new FlowEdge(game_vertices + i, w, capacity));
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }    
}
