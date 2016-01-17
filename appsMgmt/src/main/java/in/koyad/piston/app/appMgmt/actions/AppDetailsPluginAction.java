/*
 * Copyright (c) 2012-2016 Shailendra Singh <shailendra_01@outlook.com>
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

import org.koyad.piston.core.model.App;

import in.koyad.piston.app.appMgmt.forms.AppDetailsPluginForm;
import in.koyad.piston.app.appMgmt.utils.PopulateFormUtil;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.servicedelegate.model.PistonModelCache;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = AppDetailsPluginAction.ACTION_NAME
)
public class AppDetailsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "appDetails";

	private static final LogUtil LOGGER = LogUtil.getLogger(AppDetailsPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		String id = RequestContextUtil.getParameter("id");
		App app = PistonModelCache.apps.get(id); 
		
		AppDetailsPluginForm form = new AppDetailsPluginForm();
		PopulateFormUtil.populateAppDetails(form, app);
		RequestContextUtil.setRequestAttribute(AppDetailsPluginForm.FORM_NAME, form);
		
		LOGGER.exitMethod("execute");
		return "/pages/appDetails.xml";
	}

}
