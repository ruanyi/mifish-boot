package com.mifish.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2017-11-05 20:53
 */
public class PageList<T> extends ArrayList<T> implements Serializable {

    /***pageSize*/
    private int pageSize;

    /***page*/
    private int page;

    /***totalItems*/
    private int totalItems;

    public PageList() {

    }

    /**
     * PageList
     *
     * @param c
     */
    public PageList(Collection<? extends T> c) {
        super(c);
    }

    /**
     * PageList
     *
     * @param paginator
     */
    public PageList(Paginator paginator) {
        this.page = paginator.getPage();
        this.pageSize = paginator.getItemsPerPage();
        this.totalItems = paginator.getItems();
    }

    /**
     * PageList
     *
     * @param c
     * @param paginator
     */
    public PageList(Collection<? extends T> c, Paginator paginator) {
        super(c);
        this.page = paginator.getPage();
        this.pageSize = paginator.getItemsPerPage();
        this.totalItems = paginator.getItems();
    }

    /**
     * getPageSize
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * setPageSize
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getPage
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     * setPage
     *
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * getTotalItems
     *
     * @return
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * setTotalItems
     *
     * @param totalItems
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    /**
     * toPaginator
     *
     * @return
     */
    public Paginator toPaginator() {
        Paginator paginator = new Paginator();
        paginator.setPage(page);
        return paginator;
    }
}
