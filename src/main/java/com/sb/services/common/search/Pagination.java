/**
 * Pagination
 *
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.sb.services.common.search;

import org.hibernate.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic pagination data.
 *
 * @author prinaray
 */
public class Pagination {

    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final int MAX_PAGE_SIZE = 65535;

    private int pageNumber; // request page number, start with 1;
    private int pageSize;  // number of items per page
    private boolean avoidCount; // if true, we will not perform a COUNT(*) to get the total itemCount; improves response time
    private int itemCount = -1;      // number of total items
    private boolean allRowsInOnePage = false; // If true - pageSize = itemCount and all results will be returned
    // in one page

    private int resultItemCount;  // number of items on the current page

    /**
     * If true, paginate by calling Hibernate setFirstResult/setMaxResults, which uses "ROWNUM" WHERE clauses to select
     * the page instead of jumping to the row in ScrollableResults.
     */
    private boolean useFirstResultPagination = false;

    public Pagination() {
        // default page size, and use pageNumber = 1
        this.pageNumber = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pagination(int pageNumber) {
        this.pageNumber = pageNumber;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pagination(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        // so allRows="true" works
        if (pageSize == -1) {
            setAllRowsInOnePage(true);
            setAvoidCount(true); // No need to do a COUNT(*) if we're fetching all the results.
        }
    }

    public Pagination(int pageNumber, int pageSize, boolean avoidCount) {
        this(pageNumber, pageSize);
        setAvoidCount(avoidCount);
    }

    /**
     * Returns a pagination object that returns all results in a single page.
     * @return the pagination object
     */
    public static Pagination allRows() {
        Pagination p = new Pagination();
        p.setAllRowsInOnePage(true);
        p.setAvoidCount(true); // No need to do a COUNT(*) if we're fetching all the results.
        return p;
    }

    /**
     * Called by DAO layer
     *
     * @return the starting index to retrieve results, start with 0
     */
    public int getStartIndex() {
        if (isAllRowsInOnePage()) {
            return 0;   // discard request page number if allow all rows
        }
        if(getPageNumber()<1 || getPageSize() < 1){
            throw new HibernateException("Page number and/or limit parameters has invalid  value");
        }
        int start = getPageSize() * (getPageNumber() - 1);
        if ((start < 0 || start > getItemCount() - 1) && start != 0) {
            throw new HibernateException("Page number and/or limit parameters has invalid  value");
        }
        return start;
    }

    /**
     * @return the first item's position in the result (start with 1),
     *         0 means result is empty.
     */
    public int getResultFirst() {
        if (getResultItemCount() == 0) {
            return 0;
        }
        return getStartIndex() + 1;
    }

    /**
     * @return the last item's position in the result (starts with 1),
     *         0 means result is empty
     */
    public int getResultLast() {
        int last = getResultFirst() + getResultItemCount() - 1;
        if (last < 0)
            last = 0;
        return last;
    }

    /**
     * @return the page number of the result page, start with 1
     */
    public int getResultPageNumber() {
        return getStartIndex() / getPageSize() + 1;
    }

    public int getPreviousPageNumber() {
        int prev = getResultPageNumber() - 1;
        if (prev < 1)
            prev = 1;
        return prev;
    }

    public int getNextPageNumber() {
        int next = getResultPageNumber() + 1;
        if (next > getLastPageNumber())
            next = getLastPageNumber();
        return next;
    }

    /**
     * @return the first page's number, which is always 1
     */
    public int getFirstPageNumber() {
        return 1;
    }

    /**
     * @return the last page's number (first page number is 1), or -1 if the
     * total count is unknown
     */
    public int getLastPageNumber() {
        return getItemCount() == -1 ? -1 : (getItemCount() - 1) / getPageSize() + 1;
    }

    /**
     * @return number of total pages
     */
    public int getPageCount() {
        return itemCount / pageSize + 1;
    }

    public boolean isAvoidCount() {
        return avoidCount;
    }

    /**
     * Sets the avoidCount parameter. If true, we will not fetch the total count via COUNT(*) while executing this
     * search. This improves response times.
     * @param avoidCount whether to skip the COUNT(*)
     * @see #getItemCount
     */
    public void setAvoidCount(boolean avoidCount) {
        this.avoidCount = avoidCount;
    }

    /**
     * @return number of total items; -1 if avoidCount == true and not all records
     * have been fetched
     */
    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getPageSize() {
        int size = isAllRowsInOnePage() ? getItemCount() : pageSize;
        return (size == -1 ? MAX_PAGE_SIZE : size);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return Current page number, first page is 1
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Set desired page number, start with 1
     *
     * @param pageNumber
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return number of items on this page
     */
    public int getResultItemCount() {
        return resultItemCount;
    }

    /**
     * @param resultItemCount
     */
    public void setResultItemCount(int resultItemCount) {
        this.resultItemCount = resultItemCount;
    }

    /**
     * Returns all the rows returned by this query in one page
     * i.e pageSize = itemCount
     *
     * @return true if all rows need to be returned
     */
    public boolean isAllRowsInOnePage() {
        return allRowsInOnePage;
    }

    /**
     * Set to true if all rows need to be returned in one page
     * i.e pageSize = itemCount
     */
    public void setAllRowsInOnePage(boolean allRowsInOnePage) {
        this.allRowsInOnePage = allRowsInOnePage;
    }

    /**
     * Paginate a Criteria search
     *
     * @param criteria
     * @return list of results
     */
    public List paginate(Criteria criteria) {
        criteria.setCacheMode(CacheMode.IGNORE);
        if (useFirstResultPagination) {
            criteria.setFirstResult(getStartIndex());

            // Fetch one more row beyond the page. This is used below to figure out the total count.
            criteria.setMaxResults(getPageSize() + 1);
        }
        ScrollableResults sr = criteria.scroll();
        return paginate(sr, false, false, useFirstResultPagination);
    }

    /**
     * Paginate a Query search
     *
     * @param query
     * @return list of results
     */
    public List paginate(Query query) {
        List contents = query.list();
        setResultItemCount(contents.size());
        return contents;
    }

    /**
     * Paginate a Criteria search where each result row is an array of objects.
     *
     * @param criteria
     * @return list of Object[] results
     */
    public List paginateArrayResult(Criteria criteria) {
        criteria.setCacheMode(CacheMode.IGNORE);
        ScrollableResults sr = criteria.scroll();
        return paginate(sr, false, true, false);
    }

    private List paginate(ScrollableResults sr, boolean sharableMap, boolean arrayResult, boolean alreadyScrolled) {
        List res = new ArrayList();
        try {
            List skipped = new ArrayList();
            int startIndex = getStartIndex();
            int pageSize = getPageSize();

            // Scroll to the slot just before <startIndex>.
            sr.beforeFirst();
            if (startIndex > 0) {
                if (sharableMap) {
                    while (skipped.size() < startIndex && sr.next()) {
                        Object o = arrayResult ? sr.get() : sr.get(0);
//                        if (! containsTarget(skipped, (SharableMap) o)) {
//                            skipped.add(o);
//                        }
                    }
                } else if (!alreadyScrolled) {
                    sr.setRowNumber(startIndex - 1);
                }
            }

            // Fetch until we have <pageSize> results or we exhaust the set.
            while (res.size() < pageSize && sr.next()) {
                Object o = arrayResult ? sr.get() : sr.get(0);
                if (! res.contains(o)) {
                    res.add(o);
                }
            }

            // If all results have been fetched, then we know the true total count.

            if (res.size() < pageSize || sr.getRowNumber() == -1 || !sr.next()) {
                setItemCount(startIndex + res.size());
            }

            setResultItemCount(res.size());
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
        return res;
    }

    /**
     * @param list
     * @param obj
     * @return true if the list already contains the same target of SharableMap
     */
//    private boolean containsTarget(List list, SharableMap obj) {
//        for (Object l : list) {
//            SharableMap s = (SharableMap) l;
//            if (s.getTarget().equals(obj.getTarget())) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Returns true if the given page number is within the range of results, or false otherwise.
     * Also returns tree if the query has not been executed yet or if the number of results is unknown (if avoidCount
     * is enabled and results have not been fetched completely).
     * @param pageNumber the give page number
     */
    public boolean isValidPageNumber(int pageNumber) {
        int lastPageNumber = getLastPageNumber();
        return (pageNumber >= 1)
                && ((lastPageNumber == -1) // unknown
                || (pageNumber <= lastPageNumber));
    }

    public void setUseFirstResultPagination(boolean useFirstResultPagination) {
        this.useFirstResultPagination = useFirstResultPagination;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Pagination that = (Pagination) o;

        if (allRowsInOnePage != that.allRowsInOnePage) return false;
        if (itemCount != that.itemCount) return false;
        if (pageNumber != that.pageNumber) return false;
        if (pageSize != that.pageSize) return false;
        if (resultItemCount != that.resultItemCount) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = pageNumber;
        result = 29 * result + pageSize;
        result = 29 * result + itemCount;
        result = 29 * result + (allRowsInOnePage ? 1 : 0);
        result = 29 * result + resultItemCount;
        return result;
    }

    public String toString() {
        return "Pagination{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", itemCount=" + itemCount +
                ", allRowsInOnePage=" + allRowsInOnePage +
                ", resultItemCount=" + resultItemCount +
                '}';
    }
}

