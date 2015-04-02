package org.mullco.repos;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.mullco.models.Game;

import javax.sql.DataSource;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;

public class SeasonRepositoryIntegrationTest {

    DataSourceDestination destination;
    SeasonRepository repository;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new DatabaseConfig().getDataSource();
        destination = DataSourceDestination.with(dataSource);

        repository = new SeasonRepository();
    }

    @Test
    public void shouldGetStuffFromDb() throws Exception {
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

        List<Game> games = repository.getGames();

        assertEquals(1, games.size());
    }

}