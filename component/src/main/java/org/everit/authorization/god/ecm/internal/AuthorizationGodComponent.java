/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
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
package org.everit.authorization.god.ecm.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.everit.authorization.PermissionChecker;
import org.everit.authorization.god.AuthorizationGod;
import org.everit.authorization.god.ecm.AuthorizationGodConstants;
import org.everit.authorization.qdsl.util.AuthorizationQdslUtil;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for {@link PermissionChecker} and {@link AuthorizationQdslUtil} interfaces based on
 * {@link AuthorizationGod}.
 */
@Component(componentId = AuthorizationGodConstants.SERVICE_FACTORYPID_AUTHORIZATION_GOD,
    configurationPolicy = ConfigurationPolicy.IGNORE, label = "Everit Authorization God",
    description = "Component that registers a PermissionChecker and AuthorizationQdslUtil OSGi "
        + "services. The component use 'GOD' implementation that allows everything.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = AuthorizationGodConstants.DEFAULT_SERVICE_DESCRIPTION, priority = 1,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualService({ PermissionChecker.class, AuthorizationQdslUtil.class })
public class AuthorizationGodComponent {

  private ServiceRegistration<?> serviceRegistration;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<AuthorizationGodComponent> componentContext) {
    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());

    serviceRegistration =
        componentContext.registerService(new String[] {
            PermissionChecker.class.getName(), AuthorizationQdslUtil.class.getName() },
            new AuthorizationGod(),
            serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

}
