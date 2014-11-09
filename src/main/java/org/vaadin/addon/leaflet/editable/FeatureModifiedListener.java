/*
 * Copyright 2014 Vaadin Community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addon.leaflet.editable;

import com.vaadin.util.ReflectTools;
import java.lang.reflect.Method;

/**
 *
 * @author Matti Tahvonen <matti@vaadin.com>
 */
public interface FeatureModifiedListener {
    public static final Method modifiedMethod = ReflectTools.findMethod(FeatureModifiedListener.class,
            "featureModified", FeatureModifiedEvent.class);

    public void featureModified(FeatureModifiedEvent event);
    
}