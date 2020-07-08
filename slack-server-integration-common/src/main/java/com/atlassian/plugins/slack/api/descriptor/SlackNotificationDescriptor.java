package com.atlassian.plugins.slack.api.descriptor;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.descriptors.AbstractModuleDescriptor;
import com.atlassian.plugin.module.ModuleFactory;
import com.atlassian.plugins.slack.api.notification.SlackNotification;
import com.atlassian.util.concurrent.ResettableLazyReference;
import org.dom4j.Element;

public class SlackNotificationDescriptor extends AbstractModuleDescriptor<SlackNotification<Object>> {
    private String value;
    private String context;
    private String category;
    private int weight;
    private boolean activeByDefault;

    private final ResettableLazyReference<SlackNotification<Object>> moduleReference = new ResettableLazyReference<SlackNotification<Object>>() {
        @Override
        protected SlackNotification<Object> create() {
            return createModule();
        }
    };

    public SlackNotificationDescriptor(final ModuleFactory moduleFactory) {
        super(moduleFactory);
    }

    @Override
    public void init(final Plugin plugin, final Element element) throws PluginParseException {
        super.init(plugin, element);

        value = element.attributeValue("value");
        context = element.attributeValue("context");
        category = element.attributeValue("category");
        weight = Integer.parseInt(element.attributeValue("weight", "1000"));
        activeByDefault = Boolean.valueOf(element.attributeValue("activeByDefault"));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getValue() {
        return value;
    }

    public String getContext() {
        return context;
    }

    public String getCategory() {
        return category;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isActiveByDefault() {
        return activeByDefault;
    }

    @Override
    public SlackNotification<Object> getModule() {
        return moduleReference.get();
    }

    private SlackNotification<Object> createModule() {
        return moduleFactory.createModule(moduleClassName, this);
    }
}
