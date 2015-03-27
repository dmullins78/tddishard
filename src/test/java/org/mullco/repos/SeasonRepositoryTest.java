package org.mullco.repos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mullco.models.Game;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeasonRepositoryTest {

    @Mock
    DataSource dataSource;

    @Mock
    Connection connection;

    @Mock
    Statement statement;

    @Mock
    ResultSet resultSet;

    @InjectMocks
    SeasonRepository repository;

    @Test
    public void shouldGetSomethingFromDatabase() throws Exception {
        String sql = "SELECT g.*, ht.name as home_team_name, at.name as away_team_name " +
                "FROM games g " +
                "INNER JOIN teams ht " +
                "   ON ht.id = g.home_team " +
                "INNER JOIN teams at " +
                "   ON at.id = g.away_team";
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("home_team")).thenReturn(1);
        when(resultSet.getString("home_team_name")).thenReturn("home");
        when(resultSet.getInt("home_team_score")).thenReturn(1);
        when(resultSet.getInt("away_team")).thenReturn(1);
        when(resultSet.getString("away_team_name")).thenReturn("away");
        when(resultSet.getInt("away_team_score")).thenReturn(2);

        List<Game> games = repository.getGames();

        assertThat(games.get(0).home.id, is(1L));
    }

}