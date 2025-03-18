package com.gms.entity.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vvthanh
 */
public class DataPage<E> {

    private int totalRecords;
    private int currentPage;
    private List<E> data;
    private int maxResult;
    private int totalPages;

    private int maxNavigationPage = 10;

    private List<Integer> navigationPages;

    public DataPage() {
    }

    private void calcNavigationPages() {
        this.navigationPages = new ArrayList<Integer>();
        int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;

        int begin = current - this.maxNavigationPage / 2;
        int end = current + this.maxNavigationPage / 2;

        navigationPages.add(1);
        if (begin > 2) {
            navigationPages.add(-1);
        }

        for (int i = begin; i < end; i++) {
            if (i > 1 && i < this.totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < this.totalPages - 2) {
            navigationPages.add(-1);
        }
        navigationPages.add(this.totalPages);
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getMaxNavigationPage() {
        return maxNavigationPage;
    }

    public void setMaxNavigationPage(int maxNavigationPage) {
        this.maxNavigationPage = maxNavigationPage;
    }

    public List<Integer> getNavigationPages() {
        if(navigationPages == null) {
            calcNavigationPages();
        }
        return navigationPages;
    }

    public void setNavigationPages(List<Integer> navigationPages) {
        this.navigationPages = navigationPages;
    }

}
