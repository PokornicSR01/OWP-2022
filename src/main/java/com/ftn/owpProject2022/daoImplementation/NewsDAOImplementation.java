package com.ftn.owpProject2022.daoImplementation;

import com.ftn.owpProject2022.dao.NewsDAO;
import com.ftn.owpProject2022.model.News;
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
public class NewsDAOImplementation implements NewsDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class NewsRowCallBackHandler implements RowCallbackHandler {

        private Map<String, News> listOfNews = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            String id = resultSet.getString(index++);
            String title = resultSet.getString(index++);
            String content = resultSet.getString(index++);
            LocalDate publicationDate = LocalDate.parse(resultSet.getString(index++));

            News news = listOfNews.get(id);
            if(news == null) {
                news = new News(id, title, content, publicationDate);
                listOfNews.put(news.getId(), news);
            }
        }

        public List<News> getListOfNews() {
            return new ArrayList<>(listOfNews.values());
        }
    }

    @Override
    public int save(News news) {
        String sql = "INSERT INTO news (id, title, content, publication_date) " +
                    "VALUES (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    news.getId(),
                    news.getTitle(),
                    news.getContent(),
                    news.getPublicationDate().toString()
            );
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public List<News> findTodaysNews() {
        LocalDate todaysDate = LocalDate.now();
        String sql =
                "SELECT * FROM news WHERE publication_date = ?";

        NewsDAOImplementation.NewsRowCallBackHandler rowCallbackHandler = new NewsDAOImplementation.NewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, todaysDate.toString());

        return rowCallbackHandler.getListOfNews();
    }

    @Override
    public List<News> findAll() {
        LocalDate todaysDate = LocalDate.now();
        String sql =
                "SELECT * FROM news";

        NewsDAOImplementation.NewsRowCallBackHandler rowCallbackHandler = new NewsDAOImplementation.NewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getListOfNews();
    }

}
