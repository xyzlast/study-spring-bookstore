package me.xyzlast.bookstore.services;

import me.xyzlast.bookstore.constants.ActionType;
import me.xyzlast.bookstore.constants.BookStatus;
import me.xyzlast.bookstore.dao.BookDao;
import me.xyzlast.bookstore.dao.HistoryDao;
import me.xyzlast.bookstore.dao.UserDao;
import me.xyzlast.bookstore.entities.Book;
import me.xyzlast.bookstore.entities.History;
import me.xyzlast.bookstore.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ykyoon on 1/13/14.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private HistoryDao historyDao;
    @Autowired
    private UserLevelService userLevelService;

    @Override
    @Transactional
    public boolean rent(int userId, int bookId) {
        User user = userDao.getById(userId);
        user.setPoint(user.getPoint() + 1);

        Book book = bookDao.getById(bookId);
        if(book.getRentUserId() != null) {
            throw new IllegalArgumentException("It's already rent book");
        }
        book.setRentUserId(user.getId());
        book.setStatus(BookStatus.RentNow);

        History history = new History();
        history.setBookId(book.getId());
        history.setUserId(user.getId());
        history.setActionType(ActionType.Rent);
        history.setInsertTime(new Date());

        bookDao.update(book);
        historyDao.add(history);

        user.setLevel(userLevelService.getUserLevel(user.getPoint()));
        userDao.update(user);

        return true;
    }

    @Override
    @Transactional
    public boolean returnBook(int userId, int bookId) {
        User user = userDao.getById(userId);
        Book book = bookDao.getById(bookId);
        book.setRentUserId(null);
        book.setStatus(BookStatus.CanRent);

        History history = new History();
        history.setBookId(book.getId());
        history.setUserId(user.getId());
        history.setActionType(ActionType.Return);
        history.setInsertTime(new Date());

        bookDao.update(book);
        historyDao.add(history);

        return true;
    }

    @Override
    public List<User> listup() {
        return userDao.getAll();
    }

    @Override
    public List<History> getHistories(int userId) {
        return historyDao.listByUser(userId);
    }

    @Override
    public void setUserLevelService(UserLevelService userLevelService) {
        this.userLevelService = userLevelService;
    }
}
