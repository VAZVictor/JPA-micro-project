package at.htl.workloads.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(
                        "select u from User u order by u.username",
                        User.class
                ).getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery(
                        "select u from User u where lower(u.username) = lower(:username)",
                        User.class
                )
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }
}
