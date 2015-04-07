package org.mullco.services.one;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.mullco.models.Team;
import org.mullco.repos.DatabaseConfig;
import thirdparty.FifaSettings;

import javax.sql.DataSource;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScoreCalculatorTest {

    private DataSourceDestination destination;
    private ScoreCalculator calculator;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new DatabaseConfig().getDataSource();
        destination = DataSourceDestination.with(dataSource);

        FifaSettings.setUrl("http://localhost:4567");

        calculator = new ScoreCalculator();
    }

    @Test
    public void shouldRankTeams() throws Exception {
        // arrange
        Insert teams = insertInto("TEAMS")
                .columns("ID", "NAME")
                .values(1, "1")
                .values(2, "2")
                .values(3, "3")
                .build();
        Insert week1Games = insertInto("GAMES")
                .columns("ID", "HOME_TEAM", "HOME_TEAM_SCORE", "AWAY_TEAM", "AWAY_TEAM_SCORE")
                .values(1, 1, 3, 2, 0)
                .build();

        Operation setupOperation = sequenceOf(teams, week1Games);

        DbSetup dbSetup = new DbSetup(destination, setupOperation);
        dbSetup.launch();

        // act
        List<Team> standings = calculator.getStandings();
    }

}
