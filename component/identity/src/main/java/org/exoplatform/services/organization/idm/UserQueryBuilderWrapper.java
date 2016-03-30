package org.exoplatform.services.organization.idm;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.SortOrder;
import org.picketlink.idm.api.query.UnsupportedQueryCriterium;
import org.picketlink.idm.api.query.UserQuery;
import org.picketlink.idm.api.query.UserQueryBuilder;

import org.exoplatform.services.organization.Membership;

public class UserQueryBuilderWrapper implements UserQueryBuilder {
  private UserQueryBuilder delegate;
  
  private Set<Membership> memberships = new LinkedHashSet<Membership>();

  public UserQueryBuilderWrapper(UserQueryBuilder delegate) {
    this.delegate = delegate;
  }

  public UserQueryBuilder addAssociatedGroup(Group group) {
    return delegate.addAssociatedGroup(group);
  }

  public UserQueryBuilder addAssociatedGroup(String id) {
    return delegate.addAssociatedGroup(id);
  }

  public UserQueryBuilder addAssociatedGroups(Collection<Group> groups) {
    return delegate.addAssociatedGroups(groups);
  }

  public UserQueryBuilder addAssociatedGroupsKeys(Collection<String> ids) {
    return delegate.addAssociatedGroupsKeys(ids);
  }

  public UserQueryBuilder addGroupConnectedWithRole(Group group) {
    return delegate.addGroupConnectedWithRole(group);
  }

  public UserQueryBuilder addGroupConnectedWithRole(String id) {
    return delegate.addGroupConnectedWithRole(id);
  }

  public UserQueryBuilder addGroupsConnectedWithRole(Collection<Group> groups) {
    return delegate.addGroupsConnectedWithRole(groups);
  }

  public UserQueryBuilder addGroupsKeysConnectedWithRole(Collection<String> ids) {
    return delegate.addGroupsKeysConnectedWithRole(ids);
  }

  public UserQueryBuilder addRelatedGroup(Group group) {
    return delegate.addRelatedGroup(group);
  }

  public UserQueryBuilder addRelatedGroup(String id) {
    return delegate.addRelatedGroup(id);
  }

  public UserQueryBuilder addRelatedGroups(Collection<Group> groups) {
    return delegate.addRelatedGroups(groups);
  }

  public UserQueryBuilder addRelatedGroupsKeys(Collection<String> ids) {
    return delegate.addRelatedGroupsKeys(ids);
  }

  public UserQueryBuilder attributeValuesFilter(String attributeName, String[] attributeValues) throws UnsupportedQueryCriterium {
    return delegate.attributeValuesFilter(attributeName, attributeValues);
  }

  public UserQuery createQuery() {
    return delegate.createQuery();
  }

  public UserQueryBuilder idFilter(String idFilter) throws UnsupportedQueryCriterium {
    return delegate.idFilter(idFilter);
  }

  public UserQueryBuilder page(int firstResult, int maxResult) throws UnsupportedQueryCriterium {
    return delegate.page(firstResult, maxResult);
  }

  public UserQueryBuilder reset() {
    return delegate.reset();
  }

  public UserQueryBuilder sort(SortOrder order) throws UnsupportedQueryCriterium {
    return delegate.sort(order);
  }

  public UserQueryBuilder sortAttributeName(String name) throws UnsupportedQueryCriterium {
    return delegate.sortAttributeName(name);
  }

  public UserQueryBuilder withUserId(String id) {
    return delegate.withUserId(id);
  }

  public Set<Membership> getMemberships() {
    return memberships;
  }

  public void addMemberships(Set<Membership> memberships) {
    for (Membership mem : memberships) {
      this.delegate.addRelatedGroup(mem.getGroupId());
    }
    this.memberships.addAll(memberships);
  }
  
}
