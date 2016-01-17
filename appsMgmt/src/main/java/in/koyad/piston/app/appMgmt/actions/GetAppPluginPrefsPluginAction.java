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

import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.common.constants.PreferenceScope;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.servicedelegate.model.PistonModelCache;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.PrefScopes;
import in.koyad.piston.ui.utils.RequestContextUtil;

import java.util.List;
import java.util.Set;

import org.koyad.piston.core.model.App;
import org.koyad.piston.core.model.Plugin;
import org.koyad.piston.core.model.Preference;

@AnnoPluginAction(
	name = GetAppPluginPrefsPluginAction.ACTION_NAME
)
public class GetAppPluginPrefsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "getAppPluginPrefs";
	
	private static final LogUtil LOGGER = LogUtil.getLogger(GetAppPluginPrefsPluginAction.class);
	
	@Override
	protected String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		AppPluginPrefsPluginForm form = FormUtils.createFormWithReqParams(AppPluginPrefsPluginForm.class);
		
		List<Preference> preferences = null;
		String title = null;
		if(form.getType().equalsIgnoreCase(PreferenceScope.APP)) {
			//get app data
			String appId = form.getParentId();
			App app = PistonModelCache.apps.get(appId);
			
			//read preferences from db
			preferences = app.getPreferences();
			
			//read preferences from app dictionary
			Set<PrefScopes> appPrefs = RequestContextUtil.getAnnotationRegistry().getAppDictionary(app.getName()).getAppPrefs();
			for(PrefScopes appPref : appPrefs) {
				List<String> scopes = appPref.getScopes();
				
				//preference should be visible at APP level
				if(null == scopes || scopes.contains(PreferenceScope.APP)) {
					//add a preference ONLY if it does not exist already 
					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(appPref.getName()))) {
						Preference pref = new Preference();
						pref.setName(appPref.getName());
						pref.setAppId(appId);
						preferences.add(pref);
					}
				}
			}
			
			//set title
			title = app.getTitle();
		} else if(form.getType().equalsIgnoreCase(PreferenceScope.PLUGIN)) {
			//get plugin metadata
			String pluginId = form.getParentId();
			Plugin plugin = PistonModelCache.plugins.get(pluginId);
			
			//read preferences from db
			preferences = plugin.getPreferences();
			
			//read plugin preferences from app dictionary
			Set<PrefScopes> pluginPrefs = RequestContextUtil.getAnnotationRegistry().getAppDictionary(plugin.getApp().getName()).getPluginPrefs(plugin.getName());
			for(PrefScopes pluginPref : pluginPrefs) {
				List<String> scopes = pluginPref.getScopes();
				
				//preference should be editable at PLUGIN level
				if(null == scopes || scopes.contains(PreferenceScope.PLUGIN)) {
					//add a preference ONLY if it does not exist already
					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(pluginPref.getName()))) {
						Preference pref = new Preference();
						pref.setName(pluginPref.getName());
						pref.setPluginId(pluginId);
						preferences.add(pref);
					}
				}
			}
			
			//read app preferences from app dictionary
			App app = PistonModelCache.apps.get(plugin.getApp().getId());
			Set<PrefScopes> appPrefs = RequestContextUtil.getAnnotationRegistry().getAppDictionary(app.getName()).getAppPrefs();
			for(PrefScopes appPref : appPrefs) {
				List<String> scopes = appPref.getScopes();
				
				//preference should be editable at PLUGIN level
				if(null != scopes && scopes.contains(PreferenceScope.PLUGIN)) {
					//add a preference ONLY if it does not exist already
					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(appPref.getName()))) {
						Preference pref = new Preference();
						pref.setName(appPref.getName());
						pref.setAppId(app.getId());
						pref.setPluginId(pluginId);
						preferences.add(pref);
					}
				}
			}
			
			//set title
			title = plugin.getTitle();
		} 
		
		RequestContextUtil.setRequestAttribute("preferences", preferences);

		LOGGER.exitMethod("execute");
		return "/pages/appPluginPrefs.xml";
	}
	
}
