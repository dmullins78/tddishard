package org.mullco.services;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.mullco.models.Team;
import org.mullco.repos.DatabaseConfig;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScoreCalculatorTest {

    private DataSourceDestination destination;
    public static final Operation DELETE_ALL =
            deleteAllFrom("GAMES", "TEAMS");
    private ScoreCalculator calculator;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new DatabaseConfig().getDataSource();
        destination = DataSourceDestination.with(dataSource);
        calculator = new ScoreCalculator();

        DbSetup dbSetup = new DbSetup(destination, DELETE_ALL);
        dbSetup.launch();
    }

    @Test
    public void shouldRankTeams() throws Exception {
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

        List<Team> standings = new ArrayList<>(calculator.getStandings());

        assertThat(standings.get(0).points, is(3));
        assertThat(standings.get(0).goalDifferential, is(3));
        assertThat(standings.get(1).points, is(0));
        assertThat(standings.get(1).goalDifferential, is(-3));

        Operation week2Games = sequenceOf(
                insertInto("GAMES")
                        .columns("ID", "HOME_TEAM", "HOME_TEAM_SCORE", "AWAY_TEAM", "AWAY_TEAM_SCORE")
                        .values(3, 2, 3, 3, 0)
                        .build());

        DbSetup dbSetup2 = new DbSetup(destination, week2Games);
        dbSetup2.launch();

        List<Team> week2Standings = new ArrayList<>(calculator.getStandings());

        assertThat(week2Standings.get(0).name, is("1"));
        assertThat(week2Standings.get(0).points, is(3));
        assertThat(week2Standings.get(0).goalDifferential, is(3));
        assertThat(week2Standings.get(1).name, is("2"));
        assertThat(week2Standings.get(1).points, is(3));
        assertThat(week2Standings.get(1).goalDifferential, is(0));
    }

}