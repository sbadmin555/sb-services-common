package com.sb.services.common.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.sb.services.common.entity.model.SupplyByteException;
import com.sb.services.common.search.OrderBy;
import com.sb.services.common.search.SearchExpression;
import org.hibernate.criterion.Projection;

import com.sb.services.common.search.BetweenExpression;
import com.sb.services.common.search.Pagination;

/**
 * General-purpose DAO using Java Generics, providing common CRUD and search1
 * functionality.
 *
 * @param <T> the entity class supported by this DAO
 * @param <ID> the class of the entity's primary key (usually Long)
 *
 * @author Priti
 * @version $Id: //depot/jasper_dev/module/CorePrime/src/com/jasperwireless/core/dal/GenericDao.java#1 $
 */
public interface GenericDao<T, ID extends Serializable> {

    /**
     * Returns an instance of the entity given the primary key.
     * @param id the primary key
     * @return an instance of the entity
     * @throws SupplyByteException
     */
    public T get(ID id) throws SupplyByteException;

    /**
     * refreshes the given Persistent entity.
     * @param t the Persistent entity
     * @throws SupplyByteException 
     */
    public void refresh(T t) throws SupplyByteException;

    /**
     * Executes a search1 and returns a sorted, paginated list of matching entities.
     *
     * @param expressions the search1 criteria
     * @param orderBy specifies how the results will be sorted
     * @param pagination specifies which page of results to return
     * @return a list of entities matching the search1 criteria
     */
    public List<T> search(final List<SearchExpression> expressions,
                          final OrderBy orderBy,
                          final Pagination pagination) throws SupplyByteException;

    /**
     * Executes a search1 and returns a sorted,  count of matching entities.
     *
     * @param expressions the search1 criteria
     * @param bExpressions the "between" search1 criteria
     * @return a list of entities matching the search1 criteria
     */
    public Long searchCount(final List<SearchExpression> expressions,
                            final List<BetweenExpression> bExpressions) throws SupplyByteException;

    /**
     * Executes a search1 and returns a sorted, paginated list of matching entities.
     *
     * @param expressions the search1 criteria
     * @param bExpressions the "between" search1 criteria
     * @param orderBy specifies how the results will be sorted
     * @param pagination specifies which page of results to return
     * @return a list of entities matching the search1 criteria
     */
    public List<T> search(final List<SearchExpression> expressions,
                          final List<BetweenExpression> bExpressions,
                          final OrderBy orderBy,
                          final Pagination pagination) throws SupplyByteException;


    /**
     * Executes a search1 and returns a sorted, paginated list of IDs of the matching entities.
     *
     * @param expressions the search1 criteria
     * @param bExpressions the "between" search1 criteria
     * @param orderBy specifies how the results will be sorted
     * @param pagination specifies which page of results to return
     * @return a list of entities matching the search1 criteria
     */
    public List<ID> searchIds(final List<SearchExpression> expressions,
                              final List<BetweenExpression> bExpressions,
                              final OrderBy orderBy,
                              final Pagination pagination) throws SupplyByteException;

    /**
     * Adds the given entity instance to the persistent store.
     *
     * @param object
     */
    public void add(T object) throws SupplyByteException;

    /**
     * Updates the given entity instance in the persistent store.
     *
     * @param object
     */
    public void update(T object) throws SupplyByteException;

    /**
     * Deletes the given entity instance from the persistent store.
     *
     * @param object
     */
    public void delete(T object) throws SupplyByteException;

    void addOrUpdate(T object) throws SupplyByteException;

    void addOrUpdateAll(Collection<T> objects) throws SupplyByteException;

    void deleteAll(Collection<T> objects) throws SupplyByteException;

    @SuppressWarnings("unchecked")
    List<Object[]> searchProjection(List<SearchExpression> expressions,
                                    List<BetweenExpression> bExpressions,
                                    Projection projection,
                                    OrderBy orderBy,
                                    Pagination pagination) throws SupplyByteException;

    void flush() throws SupplyByteException;

    T lockWithNoWait(ID id) throws SupplyByteException;

    /**
     * Executes a search1 and returns a sorted, paginated list of matching entities.
     *
     * @param orderBy specifies how the results will be sorted
     * @param pagination specifies which page of results to return
     * @param queryName the named-query name
     * @param params parameters to be set into the named query
     * @return a list of entities matching the search1 criteria
     */
    List<T> searchNamedQuery(final OrderBy orderBy, final Pagination pagination,
                             final String queryName, final Object... params) throws SupplyByteException;

    /**
     * Exposes hibernate template for certain lower-level query manipulations
     *
     * @return
     */
    public org.springframework.orm.hibernate4.HibernateTemplate getHibernateTemplate();

    T lockWithWait(ID id) throws SupplyByteException;

    List<T> findAll() throws SupplyByteException;

    void setUseFirstResultPagination(boolean useFirstResultPagination);

    void setUseSecondarySortBy(boolean useSecondarySortByKey);
}

