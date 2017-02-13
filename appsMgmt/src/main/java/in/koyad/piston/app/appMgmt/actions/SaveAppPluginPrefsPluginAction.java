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

import org.koyad.piston.core.model.Preference;

import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.app.appMgmt.utils.ModelGenerator;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.Message;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.core.sdk.api.PreferenceService;
import in.koyad.piston.core.sdk.impl.PreferenceImpl;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = SaveAppPluginPrefsPluginAction.ACTION_NAME
)
public class SaveAppPluginPrefsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "saveAppPluginPrefs";
	
	private final PreferenceService prefService = PreferenceImpl.getInstance();

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveAppPluginPrefsPluginAction.class);
	
	@Override
	protected String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		List<Preference> preferences = null;
		try {
			AppPluginPrefsPluginForm form = FormUtils.createFormWithReqParams(AppPluginPrefsPluginForm.class);
			preferences = ModelGenerator.getPreferences(form);
			
			prefService.savePreferences(preferences);
			
			RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "Preferences are updated successfully."));
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating preferences."));
		}
		
		RequestContextUtil.setRequestAttribute("preferences", preferences);
		
		LOGGER.exitMethod("execute");
		return "/pages/appPluginPrefs.xml";
	}

}
