/*
 * Copyright (c) 2016-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.nexus.ci.util

import com.sonatype.nexus.api.repository.RepositoryInfo
import com.sonatype.nexus.ci.config.GlobalNexusConfiguration
import com.sonatype.nexus.ci.config.Nxrm2Configuration
import com.sonatype.nexus.ci.config.NxrmConfiguration

import hudson.util.FormValidation
import hudson.util.ListBoxModel

class NxrmUtil
{
  static boolean hasNexusRepositoryManagerConfiguration() {
    GlobalNexusConfiguration.all().get(GlobalNexusConfiguration.class).nxrmConfigs.size() > 0;
  }

  static FormValidation doCheckNexusInstanceId(final String value) {
    return FormUtil.validateNotEmpty(value, "Nexus Instance is required")
  }

  static ListBoxModel doFillNexusInstanceIdItems() {
    def globalConfiguration = GlobalNexusConfiguration.all().get(GlobalNexusConfiguration.class);
    return FormUtil.
        buildListBoxModel({ NxrmConfiguration it -> it.displayName }, { NxrmConfiguration it -> it.id },
            globalConfiguration.nxrmConfigs)
  }

  static FormValidation doCheckNexusRepositoryId(final String value) {
    return FormUtil.validateNotEmpty(value, "Nexus Repository is required")
  }

  static ListBoxModel doFillNexusRepositoryIdItems(final String nexusInstanceId) {
    if (!nexusInstanceId) {
      return FormUtil.buildListBoxModelWithEmptyOption()
    }
    def repositories = getRepositories(nexusInstanceId)
    return FormUtil.buildListBoxModel({ it.name }, { it.id }, repositories)
  }

  static List<RepositoryInfo> getRepositories(final String nexusInstanceId) {
    def globalConfiguration = GlobalNexusConfiguration.all().get(GlobalNexusConfiguration.class);
    def configuration = globalConfiguration.nxrmConfigs.find { Nxrm2Configuration config ->
      config.id == nexusInstanceId
    }

    def client = RepositoryManagerClientUtil.buildRmClient(configuration.serverUrl, configuration.credentialsId)
    return client.getRepositoryList().findAll { it.format =~ /maven/ }
  }
}
