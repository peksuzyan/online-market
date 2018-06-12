package com.gmail.eksuzyan.pavel.education.market.model.util.suite;

import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.GenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.EntityManager;
import java.util.function.*;

import static java.util.Objects.requireNonNull;

public final class JpaDaoTestSuite<Pk extends Comparable<Pk>, Entity extends Identifiable<Pk>> {

    private Function<EntityManager, GenericDao<Pk, Entity>> daoGenerator;
    private Supplier<Pk> pkGenerator;
    private Supplier<Entity> entityGenerator;
    private Consumer<Entity> updaterGenerator;
    private Predicate<Entity> predicateGenerator;
    private UnaryOperator<Pk> pkIncrementor;

    public JpaDaoTestSuite() {
    }

    public JpaDaoTestSuite<Pk, Entity> setDaoGenerator(Function<EntityManager, GenericDao<Pk, Entity>> daoGenerator) {
        this.daoGenerator = requireNonNull(daoGenerator);
        return this;
    }

    public JpaDaoTestSuite<Pk, Entity> setPkGenerator(Supplier<Pk> pkGenerator) {
        this.pkGenerator = requireNonNull(pkGenerator);
        return this;
    }

    public JpaDaoTestSuite<Pk, Entity> setEntityGenerator(Supplier<Entity> entityGenerator) {
        this.entityGenerator = requireNonNull(entityGenerator);
        return this;
    }

    public JpaDaoTestSuite<Pk, Entity> setUpdaterGenerator(Consumer<Entity> updaterGenerator) {
        this.updaterGenerator = requireNonNull(updaterGenerator);
        return this;
    }

    public JpaDaoTestSuite<Pk, Entity> setPredicateGenerator(Predicate<Entity> predicateGenerator) {
        this.predicateGenerator = requireNonNull(predicateGenerator);
        return this;
    }

    public JpaDaoTestSuite<Pk, Entity> setPkIncrementor(UnaryOperator<Pk> pkIncrementor) {
        this.pkIncrementor = requireNonNull(pkIncrementor);
        return this;
    }

    public GenericDao<Pk, Entity> generateDao(EntityManager em) {
        return daoGenerator.apply(em);
    }

    public Pk generatePk() {
        return pkGenerator.get();
    }

    public Entity generateEntity() {
        return entityGenerator.get();
    }

    public Consumer<Entity> generateUpdater() {
        return updaterGenerator;
    }

    public Predicate<Entity> generatePredicate() {
        return predicateGenerator;
    }

    public Pk incrementPk(Pk pk) {
        return pkIncrementor.apply(pk);
    }
}
