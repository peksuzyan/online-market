package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Configurations;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaBookDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaMagazineDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaPictureDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Magazine;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Picture;
import com.gmail.eksuzyan.pavel.education.market.model.util.DatabaseSettings;
import com.gmail.eksuzyan.pavel.education.market.model.util.JdbcDatabaseHelper;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import static com.gmail.eksuzyan.pavel.education.market.config.util.Settings.STORAGE_NAME;

@SuppressWarnings("Duplicates")
public class JpaProductDaoTest {

    private static ConfigurationFactory configurationFactory;
    private static EntityManagerFactory entityManagerFactory;
    private static JdbcDatabaseHelper databaseHelper;

    private static EntityManager entityManager;
//    private static JpaProductDao dao;

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
//        dao = new JpaCategoryDao(entityManager);
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

    @Test
    public void testReadAllBooksInRangePos() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        bookDao.create(new Book());
        bookDao.create(new Book());
        bookDao.create(new Book());

        productDao.create(new Product());
        productDao.create(new Product());
        productDao.create(new Product());

        List<Book> result = bookDao.readAll(0, 6);

        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testReadAllBooksByPkPos() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long bookPk1 = bookDao.create(new Book());
        Long bookPk2 = bookDao.create(new Book());
        Long bookPk3 = bookDao.create(new Book());

        Long productPk1 = productDao.create(new Product());
        Long productPk2 = productDao.create(new Product());
        Long productPk3 = productDao.create(new Product());

        List<Book> result = bookDao.readAll(new HashSet<>(
                Arrays.asList(bookPk1, bookPk3, productPk2)));

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testReadAllMagazinesInRangePos() {
        JpaMagazineDao magazineDao = new JpaMagazineDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        magazineDao.create(new Magazine());
        magazineDao.create(new Magazine());
        magazineDao.create(new Magazine());

        productDao.create(new Product());
        productDao.create(new Product());
        productDao.create(new Product());

        List<Magazine> result = magazineDao.readAll(0, 6);

        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testReadAllMagazinesByPkPos() {
        JpaMagazineDao magazineDao = new JpaMagazineDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long magazinePk1 = magazineDao.create(new Magazine());
        Long magazinePk2 = magazineDao.create(new Magazine());
        Long magazinePk3 = magazineDao.create(new Magazine());

        Long productPk1 = productDao.create(new Product());
        Long productPk2 = productDao.create(new Product());
        Long productPk3 = productDao.create(new Product());

        List<Magazine> result = magazineDao.readAll(new HashSet<>(
                Arrays.asList(magazinePk1, magazinePk3, productPk2)));

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testReadAllPicturesInRangePos() {
        JpaPictureDao pictureDao = new JpaPictureDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        pictureDao.create(new Picture());
        pictureDao.create(new Picture());
        pictureDao.create(new Picture());

        productDao.create(new Product());
        productDao.create(new Product());
        productDao.create(new Product());

        List<Picture> result = pictureDao.readAll(0, 6);

        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testReadAllPicturesByPkPos() {
        JpaPictureDao pictureDao = new JpaPictureDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long picturePk1 = pictureDao.create(new Picture());
        Long picturePk2 = pictureDao.create(new Picture());
        Long picturePk3 = pictureDao.create(new Picture());

        Long productPk1 = productDao.create(new Product());
        Long productPk2 = productDao.create(new Product());
        Long productPk3 = productDao.create(new Product());

        List<Picture> result = pictureDao.readAll(new HashSet<>(
                Arrays.asList(picturePk1, picturePk3, productPk2)));

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testReadAllProductsInRangePos() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaMagazineDao magazineDao = new JpaMagazineDao(entityManager);
        JpaPictureDao pictureDao = new JpaPictureDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        bookDao.create(new Book());
        magazineDao.create(new Magazine());
        pictureDao.create(new Picture());

        productDao.create(new Product());
        productDao.create(new Product());
        productDao.create(new Product());

        List<Product> result = productDao.readAll(0, 6);

        Assert.assertEquals(6, result.size());
    }

    @Test
    public void testReadAllProductsByPkPos() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaMagazineDao magazineDao = new JpaMagazineDao(entityManager);
        JpaPictureDao pictureDao = new JpaPictureDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long bookPk = bookDao.create(new Book());
        Long magazinePk = magazineDao.create(new Magazine());
        Long picturePk = pictureDao.create(new Picture());

        Long productPk1 = productDao.create(new Product());
        Long productPk2 = productDao.create(new Product());
        Long productPk3 = productDao.create(new Product());

        List<Product> result = productDao.readAll(new HashSet<>(
                Arrays.asList(bookPk, picturePk, productPk2)));

        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testDeleteBookAsProductByPkPos() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long pk = bookDao.create(new Book());

        productDao.delete(pk);

        Assert.assertFalse(productDao.read(pk).isPresent());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteBookAsMagazineByPkExEntityNotFound() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaMagazineDao magazineDao = new JpaMagazineDao(entityManager);

        Long pk = bookDao.create(new Book());

        magazineDao.delete(pk);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteProductAsBookByPkExEntityNotFound() {
        JpaBookDao bookDao = new JpaBookDao(entityManager);
        JpaProductDao productDao = new JpaProductDao(entityManager);

        Long pk = productDao.create(new Product());

        bookDao.delete(pk);
    }
}
