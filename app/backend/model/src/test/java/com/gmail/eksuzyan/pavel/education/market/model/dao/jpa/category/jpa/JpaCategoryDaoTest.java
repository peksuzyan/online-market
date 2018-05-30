package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category.jpa;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Configurations;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;
import com.gmail.eksuzyan.pavel.education.market.model.util.DatabaseSettings;
import com.gmail.eksuzyan.pavel.education.market.model.util.JdbcDatabaseHelper;
import org.junit.*;

import javax.persistence.*;
import java.util.*;

import static com.gmail.eksuzyan.pavel.education.market.config.util.Settings.STORAGE_NAME;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class JpaCategoryDaoTest {

    private static ConfigurationFactory configurationFactory;
    private static EntityManagerFactory entityManagerFactory;
    private static JdbcDatabaseHelper databaseHelper;

    private EntityManager entityManager;

    @BeforeClass
    public static void init() {
        configurationFactory = Configurations.newSingleConfigurationFactory();

        Configuration configuration = configurationFactory.getConfiguration(new Properties() {{
            put(STORAGE_NAME, "config-test-db.xml");
        }});

        DatabaseSettings settings = new DatabaseSettings(configuration);

        databaseHelper = new JdbcDatabaseHelper(settings);
        entityManagerFactory =
                Persistence.createEntityManagerFactory("MarketDatabase", settings.getJpaProperties());

//        assert databaseHelper.createSchema();
//        entityManager = entityManagerFactory.createEntityManager();
    }

    @Before
    public void setUp() {
        assert databaseHelper.createSchema();
        entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown() {
//        entityManager.getTransaction().rollback();
        entityManager.close();
        assert databaseHelper.dropSchema();
    }

    @AfterClass
    public static void destroy() throws Exception {


        if (entityManagerFactory != null)
            entityManagerFactory.close();

        if (configurationFactory != null)
            configurationFactory.close();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExNullEntity() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateExEntityDetached() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Category entity = new Category();
        entity.setId(1L);

        dao.create(entity);
        dao.create(entity);
    }

    @Test(expected = TransactionRequiredException.class)
    public void testCreateExTransactionRequired() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.create(new Category());
    }

    @Test
    public void testCreatePos() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long result;
        try {
            entityManager.getTransaction().begin();
            result = dao.create(new Category());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadExNullPk() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.read(null);
    }

    @Test
    public void testReadNeg() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Optional<Category> result = dao.read(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testReadPos() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        Optional<Category> result = dao.read(pk);

        assertTrue(result.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateExNullPk() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.update(null, e -> e.setName("name"));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateExNullUpdater() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.update(1L, null);
    }

    @Test(expected = TransactionRequiredException.class)
    public void testUpdateExTransactionRequired() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        dao.update(pk, e -> e.setName("name"));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateExEntityAlreadyRemoved() {
        try {
            JpaCategoryDao dao = new JpaCategoryDao(entityManager);

            entityManager.getTransaction().begin();
            Long pk = dao.create(new Category());

            Category entity = new Category();
            entity.setId(pk);

            dao.delete(entity);

            entity = new Category();
            entity.setId(pk);

            dao.update(pk, e -> e.setName("name"));
            entityManager.getTransaction().commit();
        } catch (EntityNotFoundException ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }
    }

    @Test
    public void testUpdaterPos() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());

            dao.update(pk, e -> e.setName("name"));
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        Category entity = dao.read(pk).orElseThrow(AssertionError::new);

        assertEquals("name", entity.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteExNullEntity() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.delete((Category) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteExNullPk() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.delete((Long) null);
    }

    @Test(expected = TransactionRequiredException.class)
    public void testDeleteExTransactionRequired() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        dao.delete(pk);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteExEntityNotFound() {
        try {
            JpaCategoryDao dao = new JpaCategoryDao(entityManager);

            entityManager.getTransaction().begin();
            dao.delete(1L);
            entityManager.getTransaction().commit();
        } catch (EntityNotFoundException ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }
    }

    @Test
    public void testDeletePos() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());

            dao.delete(pk);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        assertFalse(dao.read(pk).isPresent());
    }

    @Test
    public void testCreateOrUpdatePos1() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Category entity;
        try {
            entityManager.getTransaction().begin();
            entity = dao.createOrUpdate(new Category());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        assertTrue(dao.read(entity.getPk()).isPresent());
    }

    @Test
    public void testCreateOrUpdatePos2() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        Long pk;
        try {
            entityManager.getTransaction().begin();
            pk = dao.create(new Category());

            Category entity = new Category();
            entity.setId(pk);
            entity.setName("name");

            dao.createOrUpdate(entity);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }

        Category updated = dao.read(pk).orElseThrow(AssertionError::new);

        assertEquals("name", updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOrUpdateExNullEntity() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.createOrUpdate(null);
    }

    @Test(expected = TransactionRequiredException.class)
    public void testCreateOrUpdateExTransactionRequired() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.createOrUpdate(new Category());
    }

    @Test(expected = NullPointerException.class)
    public void testReadAllExNullPks() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.readAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testReadAllExEmptyPks() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.readAll(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllExPksContainNull() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.readAll(Collections.singleton(null));
    }

    @Test
    public void testReadAllPos() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAll(new HashSet<>(Arrays.asList(1L, 3L)));

        assertEquals(2, result.size());
    }

    @Test
    public void testReadAllNeg() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAll(new HashSet<>(Arrays.asList(4L, 0L)));

        assertTrue(result.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteAllExNullPks() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.deleteAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteAllExEmptyPks() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.deleteAll(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAllExPksContainNull() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.deleteAll(Collections.singleton(null));
    }

    @Test
    public void testDeleteAllPos1() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());

        int result = dao.deleteAll(new HashSet<>(Arrays.asList(1L, 3L)));
        entityManager.getTransaction().commit();

        assertEquals(2, result);
    }

    @Test
    public void testDeleteAllPos2() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());

        dao.deleteAll(new HashSet<>(Arrays.asList(1L, 3L)));
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAll(new HashSet<>(Arrays.asList(1L, 2L, 3L)));

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteAllNeg1() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());

        int result = dao.deleteAll(new HashSet<>(Arrays.asList(4L, 0L)));
        entityManager.getTransaction().commit();

        assertEquals(0, result);
    }

    @Test
    public void testDeleteAllNeg2() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());

        dao.deleteAll(new HashSet<>(Arrays.asList(4L, 0L)));
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAll(new HashSet<>(Arrays.asList(1L, 2L, 3L)));

        assertEquals(3, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllFromRangeExStartNegative() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.readAllFromRange(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllFromRangeExEndNegative() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        dao.readAllFromRange(0, -1);
    }

    @Test
    public void testReadAllFromRangePos1() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAllFromRange(0, 1);

        assertEquals(1, result.size());
    }

    @Test
    public void testReadAllFromRangePos2() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAllFromRange(0, 4);

        assertEquals(3, result.size());
    }

    @Test
    public void testReadAllFromRangeNeg1() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAllFromRange(0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReadAllFromRangeNeg2() {
        JpaCategoryDao dao = new JpaCategoryDao(entityManager);

        entityManager.getTransaction().begin();
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        dao.createOrUpdate(new Category());
        entityManager.getTransaction().commit();

        List<Category> result = dao.readAllFromRange(3, 1);

        assertTrue(result.isEmpty());
    }

}
