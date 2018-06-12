package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Configurations;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.GenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;
import com.gmail.eksuzyan.pavel.education.market.model.util.DatabaseSettings;
import com.gmail.eksuzyan.pavel.education.market.model.util.JdbcDatabaseHelper;
import com.gmail.eksuzyan.pavel.education.market.model.util.data.JpaDaoTestData;
import com.gmail.eksuzyan.pavel.education.market.model.util.suite.JpaDaoTestSuite;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.persistence.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.gmail.eksuzyan.pavel.education.market.config.util.Settings.STORAGE_NAME;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.junit.Assert.*;
import static org.junit.runners.Parameterized.*;

@SuppressWarnings("Duplicates")
@RunWith(Parameterized.class)
public class JpaGenericDaoTest<Pk extends Comparable<Pk>, Entity extends Identifiable<Pk>> {

    private static ConfigurationFactory configurationFactory;
    private static EntityManagerFactory entityManagerFactory;
    private static JdbcDatabaseHelper databaseHelper;

    private static EntityManager entityManager;

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

    @Parameters
    public static Collection<JpaDaoTestSuite> data() {
        return JpaDaoTestData.generateTestData();
    }

    public JpaGenericDaoTest(JpaDaoTestSuite<Pk, Entity> suite) {
        this.suite = suite;
        this.dao = suite.generateDao(entityManager);
        this.updater = suite.generateUpdater();
        this.predicate = suite.generatePredicate();
    }

    private JpaDaoTestSuite<Pk, Entity> suite;
    private GenericDao<Pk, Entity> dao;
    private Consumer<Entity> updater;
    private Predicate<Entity> predicate;

    @Test(expected = NullPointerException.class)
    public void testCreateExNullEntity() {
        dao.create(null);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateExEntityDetached() {
        Entity entity = suite.generateEntity();

        entity.setPk(suite.generatePk());

        dao.create(entity);
        dao.create(entity);
    }

    @Test
    public void testCreatePos() {
        Pk result = dao.create(suite.generateEntity());

        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadExNullPk() {
        dao.read(null);
    }

    @Test
    public void testReadNeg() {
        Optional<Entity> result = dao.read(suite.generatePk());

        assertFalse(result.isPresent());
    }

    @Test
    public void testReadPos() {
        Pk pk = dao.create(suite.generateEntity());

        Optional<Entity> result = dao.read(pk);

        assertTrue(result.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateExNullPk() {
        dao.update(null, updater);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateExNullUpdater() {
        dao.update(suite.generatePk(), null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateExEntityAlreadyRemoved() {
        Pk pk = dao.create(suite.generateEntity());

        Entity entity = suite.generateEntity();
        entity.setPk(pk);

        dao.delete(entity);

        entity = suite.generateEntity();
        entity.setPk(pk);

        dao.update(pk, updater);
    }

    @Test
    public void testUpdaterPos() {
        Pk pk = dao.create(suite.generateEntity());

        dao.update(pk, updater);

        Entity entity = dao.read(pk).orElseThrow(AssertionError::new);

        assertTrue(predicate.test(entity));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteExNullEntity() {
        dao.delete((Entity) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteExNullPk() {
        dao.delete((Pk) null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteExEntityNotFound() {
        dao.delete(suite.generatePk());
    }

    @Test
    public void testDeletePos() {
        Pk pk = dao.create(suite.generateEntity());

        dao.delete(pk);

        assertFalse(dao.read(pk).isPresent());
    }

    @Test
    public void testCreateOrUpdatePos1() {
        Entity entity = dao.createOrUpdate(suite.generateEntity());

        assertTrue(dao.read(entity.getPk()).isPresent());
    }

    @Test
    public void testCreateOrUpdatePos2() {
        Pk pk = dao.create(suite.generateEntity());

        Entity entity = suite.generateEntity();
        entity.setPk(pk);
        updater.accept(entity);

        dao.createOrUpdate(entity);

        Entity updated = dao.read(pk).orElseThrow(AssertionError::new);

        assertTrue(predicate.test(updated));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOrUpdateExNullEntity() {
        dao.createOrUpdate(null);
    }

    @Test(expected = NullPointerException.class)
    public void testReadAllExNullPks() {
        dao.readAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testReadAllExEmptyPks() {
        dao.readAll(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllExPksContainNull() {
        dao.readAll(Collections.singleton(null));
    }

    @Test
    public void testReadAllPos() {
        Entity e1 = dao.createOrUpdate(suite.generateEntity());
        Entity e2 = dao.createOrUpdate(suite.generateEntity());
        Entity e3 = dao.createOrUpdate(suite.generateEntity());

        List<Entity> result = dao.readAll(
                of(e1, e3).map(Entity::getPk).collect(toSet()));

        assertEquals(2, result.size());
    }

    @Test
    public void testReadAllNeg() {
        Entity e1 = dao.createOrUpdate(suite.generateEntity());
        Entity e2 = dao.createOrUpdate(suite.generateEntity());
        Entity e3 = dao.createOrUpdate(suite.generateEntity());

        Pk maxPk = of(e1, e2, e3)
                .map(Entity::getPk)
                .max(Comparator.naturalOrder())
                .orElseThrow(AssertionError::new);

        List<Entity> result = dao.readAll(new HashSet<>(
                Arrays.asList(
                        suite.incrementPk(maxPk),
                        suite.incrementPk(suite.incrementPk(maxPk)))));

        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllFromRangeExStartNegative() {
        dao.readAll(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAllFromRangeExEndNegative() {
        dao.readAll(0, -1);
    }

    @Test
    public void testReadAllFromRangePos1() {
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());

        List<Entity> result = dao.readAll(0, 1);

        assertEquals(1, result.size());
    }

    @Test
    public void testReadAllFromRangePos2() {
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());

        List<Entity> result = dao.readAll(0, 4);

        assertEquals(3, result.size());
    }

    @Test
    public void testReadAllFromRangeNeg1() {
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());

        List<Entity> result = dao.readAll(0, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReadAllFromRangeNeg2() {
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());
        dao.createOrUpdate(suite.generateEntity());

        List<Entity> result = dao.readAll(3, 1);

        assertTrue(result.isEmpty());
    }
}
