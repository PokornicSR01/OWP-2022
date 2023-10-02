package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.NewsAboutPatientsDAO;
import com.ftn.owpProject2022.model.NewsAboutPatients;
import com.ftn.owpProject2022.serviceImplementation.NewsAboutPatientsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class NewsAboutPatientsDAOImplementation implements NewsAboutPatientsDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class NewsRowCallBackHandler implements RowCallbackHandler {

        private Map<String, NewsAboutPatients> listOfNews = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            int numberOfPatientsOnVentilators = resultSet.getInt(index++);
            int numberOfTestedLast24hours = resultSet.getInt(index++);
            int totalNumberOfInfected = resultSet.getInt(index++);
            int numberOfHospitalized = resultSet.getInt(index++);
            int numberOfInfectedLast24hours = resultSet.getInt(index++);
            LocalDate publicationDate = LocalDate.parse(resultSet.getString(index++));

            NewsAboutPatients news = listOfNews.get(id);
            if(news == null) {
                news = new NewsAboutPatients(id, numberOfPatientsOnVentilators, numberOfTestedLast24hours,
                        totalNumberOfInfected,numberOfHospitalized, numberOfInfectedLast24hours, publicationDate );
                listOfNews.put(news.getId(), news);
            }
        }

        public List<NewsAboutPatients> getNews() {
            return new ArrayList<>(listOfNews.values());
        }
    }

    @Override
    public NewsAboutPatients getNewsAboutPatients() {
        String sql = "SELECT * FROM news_about_patients WHERE id = 1";

        NewsAboutPatientsDAOImplementation.NewsRowCallBackHandler rowCallbackHandler = new NewsAboutPatientsDAOImplementation.NewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getNews().get(0);

    }

    @Override
    public int update(NewsAboutPatients newsAboutPatients) {
        String sql = "UPDATE news_about_patients  SET number_of_patients_on_ventilators = ?, number_of_tested_last_24_hours = ?, total_number_of_infected = ?, " +
                "number_of_hospitalized = ?, number_of_infected_last_24_hours = ?, " +
                "publication_date = ? WHERE id = 1";

        try {
            boolean success = jdbcTemplate.update(sql,
                    newsAboutPatients.getNumberOfPatientsOnVentilators(),
                    newsAboutPatients.getNumberOfTestedLast24hours(),
                    newsAboutPatients.getTotalNumberOfInfected(),
                    newsAboutPatients.getNumberOfHospitalized(),
                    newsAboutPatients.getNumberOfInfectedLast24hours(),
                    newsAboutPatients.getPublicationDate().toString()
            ) == 1;

            return success ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
