package org.gatein.portal.idm.impl.model;

import org.picketlink.idm.impl.api.model.SimpleGroup;

/**
 * Created by exo on 6/17/16.
 */
public class ExoSimpleGroup extends SimpleGroup {

    public ExoSimpleGroup(String name, String groupType) {
        super(name.toLowerCase(), groupType); // lol
    }

    public ExoSimpleGroup(ExoGroupKey groupKey) {
        super(groupKey);
    }
}
