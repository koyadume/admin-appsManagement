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
$(function() {
     $('.pseudo-appTitle').click(function() {
   		 var form = createPluginForm($(this), 'listPlugins');
   		 createHidden("appId", $(this).attr('id')).appendTo(form);
   		 form.appendTo('.pseudo-tile-content');
   		 form.submit();
   		 
     });
    
     $('.pseudo-enable-apps, .pseudo-enable-plugins').click(function() {
     	 var form = $(this).closest('form');
     	 form.find("input[name='action']").val('enable');
     	 form.submit();
     });
     
     $('.pseudo-disable-apps, .pseudo-disable-plugins').click(function() {
     	 var form = $(this).closest('form');
     	 form.find("input[name='action']").val('disable');
     	 form.submit();
     });
     
});