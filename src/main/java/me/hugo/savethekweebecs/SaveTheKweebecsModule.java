package me.hugo.savethekweebecs;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.Plugin;

public class SaveTheKweebecsModule extends AbstractModule {

    private final SaveTheKweebecs plugin;

    public SaveTheKweebecsModule(SaveTheKweebecs plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() { return Guice.createInjector(this); }

    @Override
    protected void configure() {
        bind(SaveTheKweebecs.class).toInstance(this.plugin);
        bind(Plugin.class).toInstance(this.plugin);
    }
}
