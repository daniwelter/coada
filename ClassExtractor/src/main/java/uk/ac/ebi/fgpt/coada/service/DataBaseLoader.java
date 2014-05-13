package uk.ac.ebi.fgpt.coada.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.fgpt.coada.model.CellAnnotation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by dwelter on 27/03/14.
 */
public class DataBaseLoader {


    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Collection<CellAnnotation> retrieveAllEntries() {

        Properties properties = new Properties();
        try{
            properties.load(getClass().getClassLoader().getResource("dbquery.properties").openStream());
            String setSchema = properties.getProperty("db.setSchema");

            getJdbcTemplate().execute(setSchema);

            String query = properties.getProperty("db.selectQuery");
            return getJdbcTemplate().query(query, new DataMapper());
        }
        catch (IOException e){
            throw new RuntimeException(
                    "Unable to create DataBaseLoader service: failed to read dbquery.properties resource", e);
        }
    }


    private class DataMapper implements RowMapper<CellAnnotation> {
        public CellAnnotation mapRow(ResultSet resultSet, int i) throws SQLException {
            String cellName = resultSet.getString(1);
            String cloID = resultSet.getString(2);
            String efoID = resultSet.getString(3);

            return new CellAnnotation(cellName, cloID, efoID);
        }
    }
}
