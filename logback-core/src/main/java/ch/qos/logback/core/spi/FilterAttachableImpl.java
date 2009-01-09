/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 2000-2008, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.core.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.qos.logback.core.filter.Filter;

/**
 * Implementation of FilterAttachable.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
final public class FilterAttachableImpl<E> implements FilterAttachable<E> {

  CopyOnWriteArrayList<Filter<E>> filterList = new CopyOnWriteArrayList<Filter<E>>();

  /**
   * Add a filter to end of the filter list.
   */
  public void addFilter(Filter<E> newFilter) {
    filterList.add(newFilter);
  }

  /**
   * Get first filter in the chain.
   */
  public Filter<E> getFirstFilter() {
    if (filterList.size() > 0) {
      return filterList.get(0);
    } else {
      return null;
    }
  }

  /**
   * Clear the filter chain
   */
  public void clearAllFilters() {
    filterList.clear();
  }

  /**
   * Loop through the filters in the list. As soon as a filter decides on
   * ACCEPT or DENY, then that value is returned. If all of the filters return
   * NEUTRAL, then NEUTRAL is returned.
   */
  public FilterReply getFilterChainDecision(E event) {
    for (Filter<E> f : filterList) {
      final FilterReply r = f.decide(event);
      if (r == FilterReply.DENY || r == FilterReply.ACCEPT) {
        return r;
      }
    }
    return FilterReply.NEUTRAL;
  }

  public List<Filter<E>> getCopyOfAttachedFiltersList() {
    return new ArrayList<Filter<E>>(filterList);
  }
}