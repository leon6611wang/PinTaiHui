package com.zhiyu.quanzhu.ui.widget.rongplugins;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

public class MyExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        IPluginModule image = new ImagePlugin();
        IPluginModule location = new DefaultLocationPlugin();
//        IPluginModule audio = new AudioPlugin();
//        IPluginModule video = new VideoPlugin();
        IPluginModule mingpian=new MingPianPlugins();
//        IPluginModule mingpian = new OrderPlugins();
//        IPluginModule share = new SharePlugins();
        IPluginModule file = new FilePlugin();
        IPluginModule reply = new QuickReplyPlugins();

        if (conversationType.equals(Conversation.ConversationType.GROUP) ||
                conversationType.equals(Conversation.ConversationType.DISCUSSION) ||
                conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            pluginModuleList.add(image);
            pluginModuleList.add(location);
//            pluginModuleList.add(audio);
//            pluginModuleList.add(video);
            pluginModuleList.add(mingpian);
            pluginModuleList.add(file);
            pluginModuleList.add(reply);
        } else {
            pluginModuleList.add(image);
        }

        return pluginModuleList;
    }
}
