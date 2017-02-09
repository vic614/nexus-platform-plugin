/*
 * Copyright (c) 2016-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.nexus.ci.util

import com.cloudbees.plugins.credentials.CredentialsMatchers
import com.cloudbees.plugins.credentials.common.StandardListBoxModel
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials
import hudson.security.ACL
import hudson.util.FormValidation
import hudson.util.ListBoxModel
import jenkins.model.Jenkins

import static com.cloudbees.plugins.credentials.domains.URIRequirementBuilder.fromUri

@SuppressWarnings('FactoryMethodName')
// TODO ignore naming convention in existing code, refactor when convenient
class FormUtil
{
  final static String EMPTY_LIST_BOX_NAME = '-----------'

  final static String EMPTY_LIST_BOX_VALUE = ''

  static FormValidation validateUrl(String url) {
    try {
      if (url) {
        new URL(url)
      }

      return FormValidation.ok()
    }
    catch (MalformedURLException e) {
      return FormValidation.error('Malformed url (%s)', e.getMessage())
    }
  }

  static FormValidation validateNotEmpty(String value, String error) {
    if (!value) {
      return FormValidation.error(error)
    }
    return FormValidation.ok()
  }

  static FormValidation validateNoWhitespace(String value, String error) {
    if (value ==~ /.*\s+?.*/) {
      return FormValidation.error(error)
    }
    return FormValidation.ok()
  }

  static ListBoxModel buildCredentialsItems(final String serverUrl, final String credentialsId) {
    if (!Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER) || !serverUrl) {
      return new StandardListBoxModel().includeCurrentValue(credentialsId)
    }
    return new StandardListBoxModel()
        .includeEmptyValue()
        .includeMatchingAs(ACL.SYSTEM,
          Jenkins.getInstance(),
          StandardUsernamePasswordCredentials,
          fromUri(serverUrl).build(), CredentialsMatchers.always())
  }

  static ListBoxModel buildListBoxModel(Closure<String> nameSelector, Closure<String> valueSelector, List items)
  {
    def listBoxModel = buildListBoxModelWithEmptyOption()
    items.each { item ->
      listBoxModel.add(nameSelector(item), valueSelector(item))
    }
    return listBoxModel
  }

  static ListBoxModel buildListBoxModelWithEmptyOption() {
    def listBoxModel = new ListBoxModel()
    listBoxModel.add(EMPTY_LIST_BOX_NAME, EMPTY_LIST_BOX_VALUE)
    return listBoxModel
  }
}
