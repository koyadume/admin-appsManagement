/*
 * Copyright (c) 2012-2015 Shailendra Singh <shailendra_01@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.koyad.piston.app.appMgmt.plugins;

import in.koyad.piston.app.appMgmt.actions.ListAppsPluginAction;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.Plugin;
import in.koyad.piston.controller.plugin.annotations.AnnoPlugin;

@AnnoPlugin(name = "appsManager", title = "Apps Manager", defaultAction = ListAppsPluginAction.ACTION_NAME)
public class AppsManagerPlugin extends Plugin {

	private static final LogUtil LOGGER = LogUtil.getLogger(AppsManagerPlugin.class);
	
	@Override
	public void preProcess() {
		LOGGER.enterMethod("execute");
	}
	
	@Override
	public void postProcess() {
		LOGGER.exitMethod("execute");
	}
	
}
