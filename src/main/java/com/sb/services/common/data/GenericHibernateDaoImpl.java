package com.sb.services.common.data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.sb.services.common.entity.model.SupplyByteException;
import com.sb.services.common.search.SearchExpression;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.sb.services.common.entity.model.Page;
import com.sb.services.common.search.BetweenExpression;
import com.sb.services.common.search.OrderBy;
import com.sb.services.common.search.Pagination;
import com.sb.services.common.util.CommonMessages;

/**
 * Hibernate-specific implementation of a general-purpose DAO using Java
 * Generics, providing common CRUD and search1 functionality.
 *
 * @author Priti
 * @version $Id: $
 * @param <T> the entity class supported by this DAO
 * @param <ID> the class of the entity's primary key (usually Long)
 */
public abstract class GenericHibernateDaoImpl<T, ID extends Serializable>
        extends HibernateDaoSupport implements GenericDao<T, ID> {
	
	private static final Logger LOG = LoggerFactory.getLogger(GenericHibernateDaoImpl.class);

    //    protected Logger log;
    private Class<T> persistentClass;
    private List<String> availableAliases = Arrays.asList(new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});

    @Autowired
    private HibernateTemplate hibernateTemplate;


    // Number of times to re-try a search1 after errors like "snapshot too old"
    private static final int MAX_SEARCH_TRIES = 3;

    /**
     * If true, paginate by calling Hibernate setFirstResult/setMaxResults, which uses "ROWNUM" WHERE clauses to select
     * the page instead of jumping to the row in ScrollableResults.
     */
    private boolean useFirstResultPagination = true;

    /**
     * If true, add a secondary sort by the entity's primary key, in order to make the sort order deterministic when
     * the primary sort uses a non-unique column.
     */
    private boolean useSecondarySortBy = false;


    @PostConstruct
    void init() {
        setHibernateTemplate(hibernateTemplate);
    }

    public Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public GenericHibernateDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
//        this.log = LogManager.getLogger(getClass());
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T get(ID id) throws SupplyByteException {
    	try {
    		return (T) getCurrentSession().get(getPersistentClass(), id);
    	} catch (Exception e){
    		LOG.error("Database error",e);
    		throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
    	}
    }

    public List<T> findAll() throws SupplyByteException {
    	try {
    		Query query = getCurrentSession().createQuery("from " + getPersistentClass().getSimpleName());
            return query.list();
    	} catch (Exception e){
    		LOG.error("Database error",e);
    		throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
    	}
    }

    public void refresh(T t) throws SupplyByteException {
		try {
			getCurrentSession().refresh(t);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    /**
     * Executes a search1 and returns a sorted, paginated list of matching entities.
     * <p/>
     * Note that this method will automatically add aliases to the Hibernate
     * Criteria object based on the properties referenced in the search1
     * expressions and orderBy.
     *
     * @param expressions the search1 criteria
     * @param orderBy     specifies how the results will be sorted
     * @param pagination  specifies which page of results to return
     * @return a list of entities matching the search1 criteria
     */
    public List<T> search(final List<SearchExpression> expressions,
                          final OrderBy orderBy,
                          final Pagination pagination) throws SupplyByteException {
    	try {
    		return search(expressions, null, orderBy, pagination);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public Long searchCount(final List<SearchExpression> expressions,
                            final List<BetweenExpression> bExpressions) throws SupplyByteException {
		try {
			Assert.notNull(expressions);
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, null);
					criteria.setProjection(Projections.rowCount());
					return criteria.uniqueResult();
				}
			};
			return (Long) executeWithRetry(callback);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    @SuppressWarnings("unchecked")
    public List<T> search(final List<SearchExpression> expressions,
                          final List<BetweenExpression> bExpressions,
                          final OrderBy orderBy,
                          final Pagination pagination) throws SupplyByteException {
		try {
			Assert.notNull(expressions);
			Assert.notNull(pagination);
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					List res = new ArrayList();

					// Get the total item count, for display in the pagination UI.
					pagination.setItemCount(-1);
					if (!pagination.isAvoidCount()) {
						Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, null);
						criteria.setProjection(Projections.rowCount());
						pagination.setItemCount(((Long) criteria.uniqueResult()).intValue());
					}

					// Now get the actual data.
					if (pagination.getItemCount() != 0) {
						Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, orderBy);
						criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
						pagination.setUseFirstResultPagination(useFirstResultPagination);
						res = pagination.paginate(criteria);
					}
					// This is added to get error if user provided page info is not under limit
					else if (useFirstResultPagination) {
						pagination.getStartIndex();
					}
					return res;

				}
			};

			return (List<T>) executeWithRetry(callback);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ID> searchIds(final List<SearchExpression> expressions,
                              final List<BetweenExpression> bExpressions,
                              final OrderBy orderBy,
                              final Pagination pagination) throws SupplyByteException {

		try {
			Assert.notNull(expressions);
			Assert.notNull(pagination);
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					List res = new ArrayList();

					// Get the total item count, for display in the pagination UI.
					pagination.setItemCount(-1);
					if (!pagination.isAvoidCount()) {
						Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, null);
						criteria.setProjection(Projections.rowCount());
						pagination.setItemCount((Integer) criteria.uniqueResult());
					}

					// Now get the actual data.
					if (pagination.getItemCount() != 0) {
						Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, orderBy);
						criteria.setResultTransformer(CriteriaSpecification.PROJECTION);
						criteria.setProjection(Projections.id());
						res = pagination.paginate(criteria);
					}
					return res;
				}
			};

			return (List<ID>) executeWithRetry(callback);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
	}


    /**
     * Executes a search1 using a projection, including aggregations like group-by,
     * min, max, etc.
     * <p>
     *
     * @param expressions the search1 expressions
     * @param bExpressions the between expressions
     * @param projection the projection to use
     * @param orderBy the ordering
     * @param pagination the pagination
     * @return a list of object arrays; each element of the array corresponds to an
     *   entry in the ProjectionList
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> searchProjection(final List<SearchExpression> expressions,
                                           final List<BetweenExpression> bExpressions,
                                           final Projection projection,
                                           final OrderBy orderBy,
                                           final Pagination pagination) throws SupplyByteException {
    	
		try {
			Assert.notNull(expressions);
			Assert.notNull(pagination);
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					// Hibernate does not support getting the total count of a projection,
					// so we will either return an item count of -1 (unknown) or the
					// actual item count if all items have been fetched.
					pagination.setItemCount(-1);
					Criteria criteria = createCriteriaForExpressions(session, expressions, bExpressions, orderBy);
					criteria.setProjection(projection);
					return pagination.paginateArrayResult(criteria);
				}
			};
			return (List<Object[]>) executeWithRetry(callback);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    /**
     * Create a Criteria object for the given expressions. Adds joins to the appropriate
     * entities for search1 fields and sort fields that use the "entity.field" notation.
     * @param session the current session
     * @param expressions the search1 expressions
     * @param bExpressions the between expressions
     * @param orderBy the ordering; if null, no ordering is used
     * @return the criteria
     */
    protected Criteria createCriteriaForExpressions(Session session,
                                                    List<SearchExpression> expressions,
                                                    List<BetweenExpression> bExpressions,
                                                    OrderBy orderBy) {
        Criteria criteria = session.createCriteria(getPersistentClass());
        Map<String, String> aliases = new HashMap<String, String>();
        List<String> availableAliases = getAvailableAliases();
        ClassMetadata classMD = getSessionFactory().getClassMetadata(getPersistentClass());
        if (classMD == null) {
            Assert.state(false, "Class is not defined in Hibernate (check the hibernateTemplate, the hbm.xml file, and/or the @Entity annotation): "+getPersistentClass());
        }

        for (SearchExpression expression : expressions) {
            // Use an inner join to search1 against associated entities.
            addAliasForEntity(criteria, aliases, availableAliases, classMD,
                    expression.getPropertyName(), CriteriaSpecification.INNER_JOIN);
            addExpression(criteria, aliases, expression);
        }
        if (bExpressions != null) {
            for (BetweenExpression bExpression : bExpressions) {
                addAliasForEntity(criteria, aliases, availableAliases, classMD,
                        bExpression.getPropertyName(), CriteriaSpecification.INNER_JOIN);
                addExpression(criteria, aliases, bExpression);
            }
        }

        if (orderBy != null) {
            // Use an outer join for ordering.
            addAliasForEntity(criteria, aliases, availableAliases, classMD,
                    orderBy.getPropertyName(), CriteriaSpecification.LEFT_JOIN);
            addOrderBy(criteria, aliases, orderBy);

            // When using first-result (ROWNUM) pagination, add a second Order By with the primary key for deterministic ordering.
            // See http://www.oracle.com/technetwork/issue-archive/2006/06-sep/o56asktom-086197.html
            if (useSecondarySortBy) {
                criteria.addOrder(Order.asc(classMD.getIdentifierPropertyName()));
            }
        }

        // Validate that all required aliases are created. Keep resolution option around for future.
        //resolveNestedAliases(criteria, aliases, availableAliases);
        validateNestedAliases(criteria, aliases);

        return criteria;
    }

    /**
     * Adds a Criteria alias for the given property's entity, if necessary.
     * For example, if the property is "acct.name", we will create an alias
     * for "acct".
     *
     * @param criteria          the Hibernate criteria
     * @param aliases           the aliases that have been defined on the criteria so far
     * @param availableAliases  the list of available aliases to create additional from
     * @param classMD           the metadata for the class
     * @param propertyPath      the name of the property, optionally prefixed with the
     *                          name of the entity, in the format "entity.property"
     * @param joinType          the type of join to use for the alias
     */
    protected void addAliasForEntity(Criteria criteria,
                                     Map<String, String> aliases,
                                     List<String> availableAliases,
                                     ClassMetadata classMD,
                                     String propertyPath,
                                     int joinType) {
        int separatorIndex = propertyPath.lastIndexOf('.');
        if (separatorIndex <= 0) {
            return;
        }

        String entityName = propertyPath.substring(0, separatorIndex);
        String propertyName = propertyPath.substring(separatorIndex + 1);

        // Don't create an alias if the entity is the root entity, or
        // if the alias has been defined already.
        if (entityName.equals(criteria.getAlias()) || (aliases.get(entityName) != null)) {
            return;
        }

        // this is the special sql property.
        if (entityName != null && entityName.startsWith("{alias}")) {
            return;
        }

        boolean multiLevelEntity = entityName.contains(".");

        // entityName can either be the direct entity reference, or a collection
        // multiLevelEntities can't be looked up in this manner so we have to skip and a join is required anyway
        if (!multiLevelEntity) {
            Type propType = classMD.getPropertyType(entityName);
            if (propType.isEntityType()) {
                // Optimization: Don't create an alias if we are just referencing the ID
                // property. This avoids an unnecessary join.
                String assocEntityName = ((EntityType) propType).getAssociatedEntityName();
                ClassMetadata assocClassMD = getSessionFactory().getClassMetadata(assocEntityName);
                if (propertyName.equals(assocClassMD.getIdentifierPropertyName())) {
                    return;
                }
            }
        }

        String alias = getAlias(entityName, availableAliases);

//        if (log.isDebugEnabled()) {
//            log.debug("Creating an alias for the entity: " + entityName +
//                    " (property: " + propertyPath + ") (alias: " + alias + ")");
//        }

        aliases.put(entityName, alias);
        criteria.createAlias(entityName, alias, joinType);
    }

    private void validateNestedAliases(Criteria criteria, Map<String, String> aliases) {
        for (String entityPathName : aliases.keySet())
            validateNestedAlias(entityPathName, criteria, aliases);
    }
    private void validateNestedAlias(String entityPathName, Criteria criteria, Map<String, String> aliases) {
        int separatorIndex = entityPathName.lastIndexOf('.');
        if (separatorIndex <= 0)
            return;

        String parentEntityPathName = entityPathName.substring(0, separatorIndex);
        if (!aliases.containsKey(parentEntityPathName))
            throw new IllegalArgumentException("No alias exists for entity '" + parentEntityPathName +
                    "'. Insufficient criteria supplied to join required tables.");

        validateNestedAlias(parentEntityPathName, criteria, aliases);
    }

    // Unused code but keeping it in case we want to allow for automatically generating aliases for
    // multi-level objects.
    private void resolveNestedAliases(Criteria criteria, Map<String, String> aliases, List<String> availableAliases) {
        for (String entityPathName : aliases.keySet())
            resolveNestedAlias(entityPathName, criteria, aliases, availableAliases);
    }
    private void resolveNestedAlias(String entityPathName, Criteria criteria, Map<String, String> aliases, List<String> availableAliases) {
        int separatorIndex = entityPathName.lastIndexOf('.');
        if (separatorIndex <= 0)
            return;

        String parentEntityPathName = entityPathName.substring(0, separatorIndex);
        if (!aliases.containsValue(parentEntityPathName)) {
            String alias = getAlias(parentEntityPathName, availableAliases);
            aliases.put(parentEntityPathName, alias);
            criteria.createAlias(parentEntityPathName, alias, CriteriaSpecification.INNER_JOIN);
        }

        resolveNestedAlias(parentEntityPathName, criteria, aliases, availableAliases);
    }

    // Couple of options for creating the alias.
    // Option #1 - use property's actual entity name
    //             negative of this is the rare possibility of alias name collisions
    // Option #2 - straight replacement of the . with a different character
    //             negative of this is trying to find a character that works (many don't)
    // Option #3 - start with a list of simple alias names and remove as we go
    //             negative if we use so many entities that we run out of aliases (very unlikely)
    protected String getAlias(String entityName, List<String> availableAliases) {
        if (availableAliases == null) return entityName;
        try {
            return availableAliases.remove(0);
        }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Could not get alias for " + entityName + ". There are too many joins.");
        }
        //return entityName; // doesn't work
        //return entityName.replaceAll(".", "!"); // doesn't work
        //int aliasSepIndex = entityName.lastIndexOf('.');  could work but collides
        //return entityName.substring(aliasSepIndex + 1);
    }

    /**
     * Executes the given Hibernate callback, with retry and error-handling support
     * for Oracle Text errors.
     * @param callback the callback
     * @return the result
     */
    protected Object executeWithRetry(HibernateCallback callback) {
        int tryNum = 0;
        while (true) {
            try {
                return getHibernateTemplate().execute(callback);
            } catch (UncategorizedSQLException e) {
                SQLException sqlE = e.getSQLException();
                if ((sqlE != null) && (sqlE.getErrorCode() == 29902)
                        && sqlE.getMessage().contains("DRG-51030")) {
                    throw new IllegalArgumentException("Query expansion resulted in too many terms", sqlE);
                } else if (tryNum+1 < MAX_SEARCH_TRIES && (sqlE != null) &&
                        (sqlE.getErrorCode() == 29902 || sqlE.getErrorCode() == 29903)) {
                    tryNum++; // Retry if there was an Oracle Text error like "parser error" or "snapshot too old"
//                    log.debug("retry #"+tryNum+": "+sqlE);
                } else {
//                    log.debug(e);
                    throw e;
                }
            } catch (RuntimeException e) {
//                log.debug(e);
                throw e;
            }
        }
    }

    private void checkForTx(){
        try{
            Session ss = getCurrentSession();
            if(ss != null && ss.getTransaction() != null){
                return;
            }
        }catch(Exception ignore){
        }
    }

    public void add(T object) throws SupplyByteException {
		try {
			Assert.notNull(object);
			checkForTx();
			getCurrentSession().save(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public void update(T object) throws SupplyByteException {
		try {
			Assert.notNull(object);
			checkForTx();
			getCurrentSession().update(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    public void addOrUpdate(T object) throws SupplyByteException {
		try {
			Assert.notNull(object);
			checkForTx();
			getCurrentSession().saveOrUpdate(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public void addOrUpdateAll(Collection<T> objects) throws SupplyByteException {
		try {
			Assert.notNull(objects);
			checkForTx();
			for (T object : objects)
				getCurrentSession().saveOrUpdate(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public void delete(T object) throws SupplyByteException {
		try {
			Assert.notNull(object);
			checkForTx();
			getCurrentSession().delete(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public void deleteAll(Collection<T> objects) throws SupplyByteException {
		try {
			Assert.notNull(objects);
			checkForTx();
			for (T object : objects)
				getCurrentSession().delete(object);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error);
		}
    }

    public void flush() throws SupplyByteException {
		try {
			getCurrentSession().flush();
			getCurrentSession().clear();
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    protected List<String> getAvailableAliases() {
        return new ArrayList<String>(availableAliases); // create a alterable copy
    }

    protected void addExpression(Criteria criteria, Map<String, String> aliases, SearchExpression expression) {
        criteria.add(expression.getCriterion(aliases));
    }

    protected void addExpression(Criteria criteria, Map<String, String> aliases, BetweenExpression expression) {
        criteria.add(expression.getCriterion(aliases));
    }

    protected void addOrderBy(Criteria criteria, Map<String, String> aliases, OrderBy orderBy) {
        criteria.addOrder(orderBy.getOrder(aliases));
    }

    /**
     * Lock the record. If another session is holding the lock, wait until the lock is available.
     * @param id the primary key
     * @return the record
     * @throws SupplyByteException
     */
    @Override
    public T lockWithWait(ID id) throws SupplyByteException {
		try {
			Assert.notNull(id);
			T t = get(id);
			getCurrentSession().flush();
			getCurrentSession().refresh(t, LockOptions.UPGRADE);
			return t;
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    /**
     * Lock the record. If another session is holding the lock, error out immediately.
     * @param id the primary key
     * @return the record
     * @throws SupplyByteException
     */
    public T lockWithNoWait(ID id) throws SupplyByteException {
		try {
			Assert.notNull(id);
			T t = get(id);
			getCurrentSession().flush();
			getCurrentSession().refresh(t, new LockOptions(LockMode.UPGRADE_NOWAIT));
			return t;
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    protected Integer executeUpdateNamedQuery(final String queryName){
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.getNamedQuery(queryName);
                return new Integer(query.executeUpdate());
            }
        };
        return (Integer)getHibernateTemplate().execute(callback);
    }

    public List<T> searchNamedQuery(final OrderBy orderBy, final Pagination pagination,
                                    final String queryName, final Object ...params) throws SupplyByteException {
		try {
			Assert.notNull(pagination);
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					List res = new ArrayList();

					Long totalNumberOfItems = getItemCount(session, queryName, params);

					pagination.setItemCount(-1);
					if (!pagination.isAvoidCount()) {
						pagination.setItemCount(totalNumberOfItems.intValue());
					}

					Query query = getNamedQuery(session, queryName, orderBy);

					setParameters(query, params);

					int firstResult = pagination.getStartIndex();
					int maxResults = pagination.getPageSize();

					query.setFirstResult(firstResult);
					query.setMaxResults(maxResults);

					// Now get the actual data.
					if (pagination.getItemCount() != 0) {
						res = pagination.paginate(query);
					}
					return res;

				}
			};

			return (List<T>) executeWithRetry(callback);
		} catch (Exception e) {
			LOG.error("Database error",e);
			throw new SupplyByteException(SupplyByteException.ErrorType.FAILURE, CommonMessages.database_error, e.getMessage());
		}
    }

    private final static String SELECT_COUNT_PREFIX="SELECT count(*) FROM (";
    private final static String END_PARANTESIS=")";

    private Long getItemCount(final Session session, final String queryName, final Object... params) {

        String rowCountQueryName = queryName + ".count";

        Query rowCountQuery = getNamedQuery(session, rowCountQueryName);
        if (rowCountQuery == null) {
            String namedQueryString = session.getNamedQuery(queryName).getQueryString();
            rowCountQuery = session.createQuery(SELECT_COUNT_PREFIX + namedQueryString + END_PARANTESIS);
        }
        setParameters(rowCountQuery, params);
        return (Long) rowCountQuery.uniqueResult();
    }

    private Query getNamedQuery(final Session session, final String queryName){
        return getNamedQuery(session, queryName, null);
    }

    private Query getNamedQuery(final Session session, final String queryName, final OrderBy orderBy) {

        try {
            if(orderBy == null)
                return session.getNamedQuery(queryName);

            StringBuilder sb = new StringBuilder();
            sb.append(OrderBy.ORDER_BY);
            sb.append(orderBy.getPropertyName());
            if(orderBy.isAscending()){
                sb.append(OrderBy.ASC);
            }else{
                sb.append(OrderBy.DESC);
            }

            String namedQueryString = session.getNamedQuery(queryName).getQueryString();

            return session.createQuery(namedQueryString + sb.toString());
        } catch (MappingException mappingException) {
            return null;
        }
    }

    private void setParameters(final Query query, final Object... params) {

        for (int index = 0; index < params.length; index++) {

            query.setParameter(index, params[index]);
        }
    }

    @Override
    public void setUseFirstResultPagination(boolean useFirstResultPagination) {
        this.useFirstResultPagination = useFirstResultPagination;
    }

    @Override
    public void setUseSecondarySortBy(boolean useSecondarySortBy) {
        this.useSecondarySortBy = useSecondarySortBy;
    }


    public Page<T> getPage(List<T> result, Pagination pagination) {
        Page<T> page = new Page<>();
        page.setItems(result);
        int count = pagination.getItemCount();
        int pageNum = pagination.getPageNumber();
        int pageSize = pagination.getPageSize();
        page.setTotalRows(count);
        if(count > 0) {
            int numberOfCompletePages = count / pageSize;
            int itemsInLastPage = count % pageSize;
            if (pageNum > 1) {
                page.setPreviousPageExists(true);
            }
            if ((pageNum < numberOfCompletePages) || ((pageNum == numberOfCompletePages) && (itemsInLastPage != 0))) {
                page.setNextPageExists(true);
            }
        }
        return page;
    }
}