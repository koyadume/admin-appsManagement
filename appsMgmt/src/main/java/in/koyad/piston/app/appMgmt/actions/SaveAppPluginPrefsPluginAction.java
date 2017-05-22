/*
 * Copyright (c) 2012-2017 Shailendra Singh <shailendra_01@outlook.com>
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

import java.util.List;

import org.koyad.piston.business.model.Preference;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.app.appMgmt.utils.ModelGenerator;
import in.koyad.piston.client.api.PreferenceClient;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.constants.PreferenceScope;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.common.util.Message;
import in.koyad.piston.core.sdk.impl.PreferenceClientImpl;

@AnnoPluginAction(
	name = SaveAppPluginPrefsPluginAction.ACTION_NAME
)
public class SaveAppPluginPrefsPluginAction extends BasePluginAction {
	
	public static final String ACTION_NAME = "saveAppPluginPrefs";
	
	private final PreferenceClient prefClient = PreferenceClientImpl.getInstance();

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveAppPluginPrefsPluginAction.class);
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
		List<Preference> preferences = null;
		try {
			AppPluginPrefsPluginForm form = req.getPluginForm(AppPluginPrefsPluginForm.class);
			preferences = ModelGenerator.getPreferences(form);
			
			if(form.getType().equalsIgnoreCase(PreferenceScope.APP)) {
				prefClient.updateAppPreferences(form.getId(), preferences);
			} else if(form.getType().equalsIgnoreCase(PreferenceScope.PLUGIN)) {
				prefClient.updatePluginPreferences(form.getId(), preferences);
			}
			
			req.setAttribute("msg", new Message(MsgType.INFO, "Preferences are updated successfully."));
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating preferences."));
		}
		
		req.setAttribute("preferences", preferences);
		
		LOGGER.exitMethod("execute");
		return "/pages/appPluginPrefs.xml";
	}

}
