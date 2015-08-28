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
package in.koyad.piston.app.appMgmt.actions;

import org.koyad.piston.core.model.Plugin;

import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.utils.PopulateFormUtil;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.servicedelegate.model.PistonModelCache;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = PluginDetailsPluginAction.ACTION_NAME
)
public class PluginDetailsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "pluginDetails";

	private static final LogUtil LOGGER = LogUtil.getLogger(PluginDetailsPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		String id = RequestContextUtil.getParameter("id");
		Plugin plugin = PistonModelCache.plugins.get(id);
		
		PluginDetailsPluginForm form = new PluginDetailsPluginForm();
		PopulateFormUtil.populatePluginDetails(form, plugin);
		RequestContextUtil.setRequestAttribute(PluginDetailsPluginForm.FORM_NAME, form);
		
		LOGGER.exitMethod("execute");
		return "/pages/pluginDetails.xml";
	}

}
