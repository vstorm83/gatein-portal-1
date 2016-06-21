package org.gatein.portal.idm.impl.api;

import org.picketlink.idm.api.IdentitySearchCriteria;
import org.picketlink.idm.api.UnsupportedCriterium;
import org.picketlink.idm.impl.api.IdentitySearchCriteriaImpl;
import org.picketlink.idm.spi.search.IdentityObjectSearchCriteria;

/**
 * Created by exo on 6/17/16.
 */
public class ExoIdentitySearchCriteriaImpl extends IdentitySearchCriteriaImpl {

    private String filter;

    public ExoIdentitySearchCriteriaImpl() {
    }

    public ExoIdentitySearchCriteriaImpl(IdentityObjectSearchCriteria criteria){
        super(criteria);
    }

    @Override
    public IdentitySearchCriteria nameFilter(String filter) throws UnsupportedCriterium
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("ID filter is null");
        }

        this.filter = filter.toLowerCase();

        return this;
    }
}
