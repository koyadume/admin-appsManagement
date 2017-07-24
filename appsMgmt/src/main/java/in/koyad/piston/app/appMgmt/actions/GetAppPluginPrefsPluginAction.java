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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.koyad.piston.business.model.App;
import org.koyad.piston.business.model.Plugin;
import org.koyad.piston.business.model.Preference;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.model.Response;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.cache.store.PortalStaticCache;
import in.koyad.piston.client.api.PreferenceClient;
import in.koyad.piston.common.basic.ServiceLoaderUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.PreferenceScope;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.container.app.AppContext;
import in.koyad.piston.container.app.PrefScopes;
import in.koyad.piston.container.context.PortalContext;
import in.koyad.piston.core.sdk.impl.PreferenceClientImpl;
import in.koyad.piston.ui.utils.PortalContextUtil;

@AnnoPluginAction(
	name = GetAppPluginPrefsPluginAction.ACTION_NAME
)
public class GetAppPluginPrefsPluginAction extends BasePluginAction {
	
	public static final String ACTION_NAME = "getAppPluginPrefs";
	
	private static final LogUtil LOGGER = LogUtil.getLogger(GetAppPluginPrefsPluginAction.class);
	
	private final PreferenceClient prefClient = ServiceLoaderUtil.getInstance(PreferenceClient.class);
	
	@Override
	public String execute(Request req, Response resp) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		AppPluginPrefsPluginForm form = req.getPluginForm(AppPluginPrefsPluginForm.class);
		
		Set<Preference> filteredPreferences = new HashSet<>();
		String title = null;
		if(form.getType().equalsIgnoreCase(PreferenceScope.APP)) {
			//get app data
			String appId = form.getId();
			App app = PortalStaticCache.apps.get(appId);
			
			//read preferences from db
			List<Preference> prefsValues = prefClient.getAppPreferences(app.getId());
			Map<String, String> prefsMap = prefsValues.stream().collect(Collectors.toMap(Preference::getName, Preference::getValue)); 
			
			//read app preferences from app context
			Set<PrefScopes> appPrefs = PortalContextUtil.getPortalContext().getAppContextByName(app.getName()).getMetadata().getPrefs();
			
			// filter preferences which are editable at APP level
			appPrefs.stream()
				.filter(appPref -> null != appPref.getScopes() && appPref.getScopes().contains(PreferenceScope.APP))
				.forEach(appPref -> filteredPreferences.add(new Preference(appPref.getName(), appPref.getValue())));
				
			// update values
			filteredPreferences.forEach(pref -> pref.setValue(prefsMap.get(pref.getName())));
			
//			for(PrefScopes appPref : appPrefs) {
//				List<String> scopes = appPref.getScopes();
//				
//				//preference should be visible at APP level
//				if(null == scopes || scopes.contains(PreferenceScope.APP)) {
//					//add a preference ONLY if it does not exist already 
//					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(appPref.getName()))) {
//						Preference pref = new Preference();
//						pref.setName(appPref.getName());
//						pref.setApp(app);
//						preferences.add(pref);
//					}
//				}
//			}
			
			//set title
			title = app.getTitle();
		} else if(form.getType().equalsIgnoreCase(PreferenceScope.PLUGIN)) {
			//get plugin metadata
			String pluginId = form.getId();
			Plugin plugin = PortalStaticCache.plugins.get(pluginId);
			
			//read preferences from db
			List<Preference> prefsValues = prefClient.getPluginPreferences(plugin.getId());
			Map<String, String> prefsMap = prefsValues.stream().collect(Collectors.toMap(Preference::getName, Preference::getValue)); 
			
			//read plugin preferences from app context
			PortalContext portalCtx = PortalContextUtil.getPortalContext();
			ClassLoader clsLoader = portalCtx.getAppClassLoader(plugin.getApp().getName());
			AppContext appCtx = portalCtx.getAppContextByName(plugin.getApp().getName());
			Set<PrefScopes> pluginPrefs = appCtx.getPluginPrefs(appCtx.getPluginInstance(clsLoader, plugin.getName()));
			
			// filter preferences which are editable at PLUGIN level
			pluginPrefs.stream()
				.filter(pluginPref -> null != pluginPref.getScopes() && pluginPref.getScopes().contains(PreferenceScope.PLUGIN))
				.forEach(pluginPref -> filteredPreferences.add(new Preference(pluginPref.getName(), pluginPref.getValue())));
				
			//read app preferences from app context
			Set<PrefScopes> appPrefs = appCtx.getMetadata().getPrefs();
			
			// filter preferences which are editable at PLUGIN level
			appPrefs.stream()
				.filter(appPref -> null != appPref.getScopes() && appPref.getScopes().contains(PreferenceScope.PLUGIN))
				.forEach(appPref -> filteredPreferences.add(new Preference(appPref.getName(), appPref.getValue())));
			
			// update values
			filteredPreferences.forEach(pref -> pref.setValue(prefsMap.get(pref.getName())));
			
//			//read preferences from db
//			preferences = plugin.getPreferences();
//			
//			//read plugin preferences from app dictionary
//			Set<PrefScopes> pluginPrefs = RequestContextUtil.getAnnotationRegistry().getAppDictionary(plugin.getApp().getName()).getPluginPrefs(plugin.getName());
//			for(PrefScopes pluginPref : pluginPrefs) {
//				List<String> scopes = pluginPref.getScopes();
//				
//				//preference should be editable at PLUGIN level
//				if(null == scopes || scopes.contains(PreferenceScope.PLUGIN)) {
//					//add a preference ONLY if it does not exist already
//					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(pluginPref.getName()))) {
//						Preference pref = new Preference();
//						pref.setName(pluginPref.getName());
//						pref.setPlugin(plugin);
//						preferences.add(pref);
//					}
//				}
//			}
//			
//			//read app preferences from app dictionary
//			App app = PistonModelCache.apps.get(plugin.getApp().getId());
//			Set<PrefScopes> appPrefs = RequestContextUtil.getAnnotationRegistry().getAppDictionary(app.getName()).getAppPrefs();
//			for(PrefScopes appPref : appPrefs) {
//				List<String> scopes = appPref.getScopes();
//				
//				//preference should be editable at PLUGIN level
//				if(null != scopes && scopes.contains(PreferenceScope.PLUGIN)) {
//					//add a preference ONLY if it does not exist already
//					if(!preferences.stream().anyMatch(pref -> pref.getName().equalsIgnoreCase(appPref.getName()))) {
//						Preference pref = new Preference();
//						pref.setName(appPref.getName());
//						pref.setApp(app);
//						pref.setPlugin(plugin);
//						preferences.add(pref);
//					}
//				}
//			}
			
			//set title
			title = plugin.getTitle();
		} 
		
		req.setAttribute("preferences", filteredPreferences);

		LOGGER.exitMethod("execute");
		return "/appPluginPrefs.xml";
	}
	
}
