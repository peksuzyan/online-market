package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category;

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
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.junit.Assert.*;

public class JpaCategoryDaoTest {

    private static ConfigurationFactory configurationFactory;
    private static EntityManagerFactory entityManagerFactory;
    private static JdbcDatabaseHelper databaseHelper;

    private static EntityManager entityManager;
    private static JpaCategoryDao dao;

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

        assert databaseHelper.createSchema();
        entityManager = entityManagerFactory.createEntityManager();
        dao = new JpaCategoryDao(entityManager);
    }

    @Before
    public void setUp() {
        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown() {
        entityManager.getTransaction().rollback();
    }

    @AfterClass
    public static void destroy() throws Exception {
        entityManager.close();
        assert databaseHelper.dropSchema();

        if (entityManagerFactory != null)
            entityManagerFactory.close();

        if (configurationFactory != null)
            configurationFactory.close();
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteAllExNullPks() {
        dao.deleteAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteAllExEmptyPks() {
        dao.deleteAll(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAllExPksContainNull() {
        dao.deleteAll(Collections.singleton(null));
    }

    @Test
    public void testDeleteAllPos1() {
        Category e1 = dao.createOrUpdate(new Category());
        @SuppressWarnings("unused")
        Category e2 = dao.createOrUpdate(new Category());
        Category e3 = dao.createOrUpdate(new Category());

        int result = dao.deleteAll(
                of(e1, e3)
                        .map(Category::getPk)
                        .collect(toSet()));

        assertEquals(2, result);
    }

    @Test
    public void testDeleteAllPos2() {
        Category e1 = dao.createOrUpdate(new Category());
        Category e2 = dao.createOrUpdate(new Category());
        Category e3 = dao.createOrUpdate(new Category());

        dao.deleteAll(
                of(e1, e3)
                        .map(Category::getPk)
                        .collect(toSet()));

        List<Category> result = dao.readAll(
                of(e1, e2, e3)
                        .map(Category::getPk)
                        .collect(toSet()));

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteAllNeg1() {
        Category e1 = dao.createOrUpdate(new Category());
        Category e2 = dao.createOrUpdate(new Category());
        Category e3 = dao.createOrUpdate(new Category());

        long maxPk = of(e1, e2, e3)
                .mapToLong(Category::getPk)
                .max()
                .orElseThrow(AssertionError::new);

        int result = dao.deleteAll(new HashSet<>(
                Arrays.asList(
                        Long.sum(maxPk, 1L),
                        Long.sum(maxPk, 2L))));

        assertEquals(0, result);
    }

    @Test
    public void testDeleteAllNeg2() {
        Category e1 = dao.createOrUpdate(new Category());
        Category e2 = dao.createOrUpdate(new Category());
        Category e3 = dao.createOrUpdate(new Category());

        long maxPk = of(e1, e2, e3)
                .mapToLong(Category::getPk)
                .max()
                .orElseThrow(AssertionError::new);

        dao.deleteAll(new HashSet<>(
                Arrays.asList(
                        Long.sum(maxPk, 1L),
                        Long.sum(maxPk, 2L))));

        List<Category> result = dao.readAll(
                of(e1, e2, e3)
                        .map(Category::getPk)
                        .collect(toSet()));

        assertEquals(3, result.size());
    }

}
