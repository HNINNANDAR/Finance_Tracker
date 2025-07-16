package finance.tracker.service;

import finance.tracker.model.Category;
import finance.tracker.model.TransactionType;
import finance.tracker.repository.CategoryDAO;

import java.util.List;

public class CategoryService {
    private final CategoryDAO catDao;
    public CategoryService(CategoryDAO dao) { this.catDao = dao; }

    public boolean addCategory(Category c) { return catDao.insertCategory(c); }

    public List<Category> getAllByUser(int userId){
        return catDao.getAllByUser(userId);
    }
    public List<Category> getCategoriesForUser(int userId){
        return catDao.getCategoriesForUser(userId);
    }
    public Category getById(int categoryId) {
        return catDao.getById(categoryId);
    }
    public Category findByNameTypeAndUserId(String name, TransactionType type, int userId){
        return catDao.findByNameTypeAndUserId(name, type, userId);
    }
    public boolean updateCategory(Category category) {
        return catDao.updateCategory(category);
    }
    public boolean deleteCategory(int categoryId) {
        return catDao.deleteCategory(categoryId);
    }
}
