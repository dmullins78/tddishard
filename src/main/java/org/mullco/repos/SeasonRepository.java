package org.mullco.repos;

import org.mullco.models.Game;
import org.mullco.models.Team;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class SeasonRepository {
    public List<Game> getGames() {
        Connection con = null;
        try {
            DataSource dataSource = new DatabaseConfig().getDataSource();

            con = dataSource.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT g.*, ht.name as home_team_name, at.name as away_team_name " +
                    "FROM games g " +
                    "INNER JOIN teams ht " +
                    "   ON ht.id = g.home_team " +
                    "INNER JOIN teams at " +
                    "   ON at.id = g.away_team";
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<Game> games = new ArrayList<>();
            while (rs.next()) {
                Team homeTeam = new Team(rs.getInt("home_team"), rs.getString("home_team_name"));
                int homeTeamScore = rs.getInt("home_team_score");

                Team awayTeam = new Team(rs.getInt("away_team"), rs.getString("away_team_name"));
                int awayTeamScore = rs.getInt("away_team_score");

                Game game = new Game(homeTeam, homeTeamScore, awayTeam, awayTeamScore);
                games.add(game);
            }

            return games;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
