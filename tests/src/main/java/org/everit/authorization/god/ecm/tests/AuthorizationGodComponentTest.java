/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.authorization.god.ecm.tests;

import org.everit.authorization.PermissionChecker;
import org.everit.authorization.qdsl.util.AuthorizationQdslUtil;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.junit.Assert;
import org.junit.Test;

import com.mysema.query.types.template.BooleanTemplate;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Test for Authorization God component and implementation.
 */
@Component(componentId = "AuthorizationGodComponentTest",
    configurationPolicy = ConfigurationPolicy.OPTIONAL)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "AuthorizationGodComponentTest") })
@Service(value = AuthorizationGodComponentTest.class)
public class AuthorizationGodComponentTest {

  private AuthorizationQdslUtil authorizationQdslUtil;

  private PermissionChecker permissionChecker;

  @ServiceRef(defaultValue = "")
  public void setAuthorizationQdslUtil(final AuthorizationQdslUtil authorizationQdslUtil) {
    this.authorizationQdslUtil = authorizationQdslUtil;
  }

  @ServiceRef(defaultValue = "")
  public void setPermissionChecker(final PermissionChecker permissionChecker) {
    this.permissionChecker = permissionChecker;
  }

  @Test
  public void testComplex() {
    Assert.assertEquals(BooleanTemplate.TRUE,
        authorizationQdslUtil.authorizationPredicate(0, null));

    Assert.assertTrue(permissionChecker.hasPermission(0, 0));

    Assert.assertEquals(-1, permissionChecker.getSystemResourceId());

    long[] authorizationScope = permissionChecker.getAuthorizationScope(0);
    Assert.assertEquals(1, authorizationScope.length);
    Assert.assertEquals(-1, authorizationScope[0]);
  }

}
