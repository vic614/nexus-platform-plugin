/*
 * Copyright (c) 2016-present Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://links.sonatype.com/products/clm/attributions.
 * "Sonatype" is a trademark of Sonatype, Inc.
 */
package com.sonatype.nexus.ci.iq.PolicyEvaluationHealthAction

import com.sonatype.nexus.ci.iq.Messages
import com.sonatype.nexus.ci.iq.PolicyEvaluationHealthAction

def t = namespace(lib.JenkinsTagLib)
def l = namespace(lib.LayoutTagLib)

def action = (PolicyEvaluationHealthAction) it

l.css(src: '/plugin/nexus-jenkins-plugin/css/nexus.css')
t.summary(icon: '/plugin/nexus-jenkins-plugin/images/48x48/nexus-iq.png') {
  a(href: "${action.getUrlName()}", Messages.IqPolicyEvaluation_ReportName())
  br()
  img(src: "${rootURL}/plugin/nexus-jenkins-plugin/images/16x16/governance-badge.png")
  if (action.criticalComponentCount) {
    span(class: 'iq-chiclet critical', action.criticalComponentCount)
  }
  if (action.severeComponentCount) {
    span(class: 'iq-chiclet severe', action.severeComponentCount)
  }
  if (action.moderateComponentCount) {
    span(class: 'iq-chiclet moderate', action.moderateComponentCount)
  }
  if (!action.criticalComponentCount && !action.severeComponentCount && !action.moderateComponentCount) {
    span(Messages.IqPolicyEvaluation_NoViolations())
  }
}