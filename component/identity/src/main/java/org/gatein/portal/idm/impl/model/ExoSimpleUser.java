package org.gatein.portal.idm.impl.model;

import org.picketlink.idm.impl.api.model.SimpleUser;

/**
 * Created by exo on 6/17/16.
 */
public class ExoSimpleUser extends SimpleUser {

    public ExoSimpleUser(String id) {
        super(id.toLowerCase());
    }
}
