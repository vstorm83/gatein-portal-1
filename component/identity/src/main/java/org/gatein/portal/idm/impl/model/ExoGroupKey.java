package org.gatein.portal.idm.impl.model;

import org.picketlink.idm.impl.api.model.GroupKey;

/**
 * Created by exo on 6/17/16.
 */
public class ExoGroupKey extends GroupKey {

    private String name;

    public ExoGroupKey(String name, String type) {
        super(name, type);
        this.name = super.getName().toLowerCase();
    }

    public ExoGroupKey(String id) {
        super(id);
        this.name = super.getName().toLowerCase();
    }
}
