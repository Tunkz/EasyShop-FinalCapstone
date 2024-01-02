package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = new ArrayList<>();
        try (
                Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        try (Connection connection = getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM +" +
                    "CATEGORIES where category_id = ?");
            preparedStatement.setInt(1,categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Category create(Category category) {
        // create a new category
        try (Connection connection  =getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO categories (name,description)+" +
                    "VALUES (?, ?) ");
            preparedStatement.setString(1,category.getName());
            preparedStatement.setString(2,category.getDescription());
            preparedStatement.executeUpdate();

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
       try (Connection connection = getConnection()){

           PreparedStatement preparedStatement = connection.prepareStatement("UPDATE categories SET name = ?, description = ? +" +
                   "WHERE category_id = ?");
           preparedStatement.setString(1,category.getName());
           preparedStatement.setString(2,category.getDescription());
           preparedStatement.setInt(3,categoryId);
           preparedStatement.executeUpdate();

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        try (Connection connection = getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE categpry_id = ?");
            preparedStatement.setInt(1,categoryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
