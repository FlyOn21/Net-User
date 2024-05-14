package org.example.app.app.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.app.db_connect.IConnection;
import org.example.app.app.entity.User;
import org.example.app.app.exceptions.ConnectException;
import org.example.app.app.repository.interfaces.IRepository;
import org.example.app.app.utils.ActionAnswer;
import org.example.app.app.utils.UserFilters;
import org.example.app.app.utils.logging.LoggingErrorsMsg;
import org.example.app.app.utils.logging.LoggingSuccessMsg;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<User> {
    private final IConnection connection;
    private static final Logger CRUD_LOGGER =
            LogManager.getLogger(UserRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    public UserRepository(IConnection connection) {
        this.connection = connection;
    }

    @Override
    public ActionAnswer create(User obj) {
        Transaction transaction = null;
        ActionAnswer actionAnswer = new ActionAnswer();
        boolean isEmailExists = checkEmailExists(obj.getEmail());
        if (isEmailExists) {
            actionAnswer.addError("Email already exists");
            return actionAnswer;
        }
        try(Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(obj);
            transaction.commit();
            actionAnswer.setIsSuccess();
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_ADDED.getMsg(), "Entity User" ));
            actionAnswer.setSuccessMsg("User created successfully");
            return actionAnswer;
        } catch (HeadlessException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.addError(LoggingErrorsMsg.DB_INSERT_ERROR.getMsg());
            return actionAnswer;
        }
    }

    public boolean checkEmailExists(String emailValue) {
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).where(cb.equal(root.get("email"), emailValue)).distinct(true);
            long resultCount = session.createQuery(cr).stream().count();
            transaction.commit();

            return resultCount > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new ConnectException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }

    @Override
    public Optional<List<User>> readAll(List<String> excludeColumns, int limit, int offset) {
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            cq.from(User.class);
            Query<User> query = session.createQuery(cq);
            if (limit > 0) {
                query.setMaxResults(limit);
            }
            if (offset > 0) {
                query.setFirstResult(offset);
            }
            List<User> result = query.getResultList();
            if (result.isEmpty()) {
                return Optional.empty();
            }
            if (excludeColumns != null && !excludeColumns.isEmpty()) {
                List <User> filterResult = UserFilters.filterUsers(result, excludeColumns);
                return Optional.of(filterResult);
            }
            transaction.commit();
            return Optional.of(result);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new ConnectException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }

    @Override
    public ActionAnswer update(User obj) {
        ActionAnswer actionAnswer = new ActionAnswer();
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(obj);
            transaction.commit();
            actionAnswer.setIsSuccess();
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_UPDATED.getMsg(), "Entity User" ));
            actionAnswer.setSuccessMsg("User update successfully");
            return actionAnswer;
        } catch (HeadlessException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.addError(LoggingErrorsMsg.DB_UPDATE_ERROR.getMsg());
            return actionAnswer;
        }
    }

    @Override
    public ActionAnswer delete(Long id) {
        ActionAnswer actionAnswer = new ActionAnswer();
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
            actionAnswer.setIsSuccess();
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_DELETED.getMsg(), "Entity User" ));
            actionAnswer.setSuccessMsg("User deleted successfully");
            return actionAnswer;
        } catch (HeadlessException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.addError(LoggingErrorsMsg.DB_DELETE_ERROR.getMsg());
            return actionAnswer;
        }
    }

    @Override
    public Optional<List<User>> readById(Long id, List<String> excludeColumns) {
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);

            cq.where(cb.equal(root.get("id"), id));
            List <User> result = session.createQuery(cq).getResultList();
            if (result.isEmpty()) {
                return Optional.empty();
            }
            if (excludeColumns != null && !excludeColumns.isEmpty()) {
                List <User> filterResult = UserFilters.filterUsers(result, excludeColumns);
                return Optional.of(filterResult);
            }
            transaction.commit();
            return Optional.of(result);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new ConnectException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }

}
