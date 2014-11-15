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
 * @author Matti Tahvonen
 */
public interface FeatureDrawnListener {
    public static final Method drawnMethod = ReflectTools.findMethod(FeatureDrawnListener.class,
            "featureDrawn", FeatureDrawnEvent.class);

    public void featureDrawn(FeatureDrawnEvent event);
    
}
