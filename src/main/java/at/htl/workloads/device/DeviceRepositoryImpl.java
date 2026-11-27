package at.htl.workloads.device;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DeviceRepositoryImpl implements DeviceRepository {

    private final EntityManager entityManager;

    public DeviceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Device> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Device.class, id));
    }

    @Override
    public List<Device> findByUser(Long userId) {
        return entityManager.createQuery(
                        "select d from Device d where d.user.id = :userId order by d.name",
                        Device.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void add(Device device) {
        entityManager.persist(device);
    }
}
