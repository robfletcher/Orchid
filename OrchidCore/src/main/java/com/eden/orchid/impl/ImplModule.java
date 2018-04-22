package com.eden.orchid.impl;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.api.converters.TypeConverter;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.impl.compilers.frontmatter.FrontMatterPrecompiler;
import com.eden.orchid.impl.compilers.markdown.MarkdownCompiler;
import com.eden.orchid.impl.compilers.parsers.CSVParser;
import com.eden.orchid.impl.compilers.parsers.JsonParser;
import com.eden.orchid.impl.compilers.parsers.TOMLParser;
import com.eden.orchid.impl.compilers.parsers.YamlParser;
import com.eden.orchid.impl.compilers.pebble.PebbleCompiler;
import com.eden.orchid.impl.compilers.sass.SassCompiler;
import com.eden.orchid.impl.compilers.text.TextCompiler;
import com.eden.orchid.impl.events.ClogSetupListener;
import com.eden.orchid.impl.generators.AssetsGenerator;
import com.eden.orchid.impl.generators.HomepageGenerator;
import com.eden.orchid.impl.generators.SitemapGenerator;
import com.eden.orchid.impl.resources.CoreResourceSource;
import com.eden.orchid.impl.resources.LocalFileResourceSource;
import com.eden.orchid.impl.tasks.BuildCommand;
import com.eden.orchid.impl.tasks.BuildTask;
import com.eden.orchid.impl.tasks.HelpCommand;
import com.eden.orchid.impl.tasks.InteractiveTask;
import com.eden.orchid.impl.tasks.ServeTask;
import com.eden.orchid.impl.tasks.WatchTask;
import com.eden.orchid.impl.themes.DefaultTheme;
import com.eden.orchid.impl.themes.components.LicenseComponent;
import com.eden.orchid.impl.themes.components.PageContentComponent;
import com.eden.orchid.impl.themes.components.ReadmeComponent;
import com.eden.orchid.impl.themes.components.TemplateComponent;
import com.eden.orchid.impl.themes.functions.AnchorFunction;
import com.eden.orchid.impl.themes.functions.CompileAsFunction;
import com.eden.orchid.impl.themes.functions.FindAllFunction;
import com.eden.orchid.impl.themes.functions.FindFunction;
import com.eden.orchid.impl.themes.functions.FindTemplateFunction;
import com.eden.orchid.impl.themes.functions.LimitToFunction;
import com.eden.orchid.impl.themes.functions.LinkFunction;
import com.eden.orchid.impl.themes.functions.LoadFunction;
import com.eden.orchid.impl.themes.functions.LocalDateFunction;
import com.eden.orchid.impl.themes.menus.DividerMenuItem;
import com.eden.orchid.impl.themes.menus.DropdownMenuItem;
import com.eden.orchid.impl.themes.menus.IndexMenuItem;
import com.eden.orchid.impl.themes.menus.LinkMenuItem;
import com.eden.orchid.impl.themes.menus.PageMenuItem;
import com.eden.orchid.impl.themes.tags.LogTag;
import com.eden.orchid.utilities.ClogSpells;

import java.util.Collection;

@IgnoreModule
public final class ImplModule extends OrchidModule {

    @Override
    protected void configure() {
        // prepare empty sets for binding
        addToSet(OrchidService.class);

        addToSet(TypeConverter.class,
                AssetsGenerator.AssetDirectory.Converter.class);

        // Themes
        addToSet(Theme.class,
                DefaultTheme.class);

        addToSet(AdminTheme.class);

        // Resource Sources
        addToSet(FileResourceSource.class,
                LocalFileResourceSource.class);

        addToSet(PluginResourceSource.class,
                CoreResourceSource.class);

        // Compilers
        addToSet(OrchidCompiler.class,
                MarkdownCompiler.class,
                PebbleCompiler.class,
                SassCompiler.class,
                TextCompiler.class);

        // Parsers
        addToSet(OrchidParser.class,
                CSVParser.class,
                YamlParser.class,
                TOMLParser.class,
                JsonParser.class);

        // Precompilers
        addToSet(OrchidPrecompiler.class,
                FrontMatterPrecompiler.class);

        // Generators
        addToSet(OrchidGenerator.class,
                AssetsGenerator.class,
                HomepageGenerator.class,
                SitemapGenerator.class);

        // Tasks and Commands
        addToSet(OrchidTask.class,
                BuildTask.class,
                WatchTask.class,
                ServeTask.class,
                InteractiveTask.class);

        addToSet(OrchidCommand.class,
                HelpCommand.class,
                BuildCommand.class);

        // Menu Items
        addToSet(OrchidMenuItem.class,
                DividerMenuItem.class,
                DropdownMenuItem.class,
                LinkMenuItem.class,
                IndexMenuItem.class,
                PageMenuItem.class);

        // Component Types
        addToSet(OrchidComponent.class,
                LicenseComponent.class,
                ReadmeComponent.class,
                PageContentComponent.class,
                TemplateComponent.class);

        // Server
        addToSet(OrchidEventListener.class,
                TaskServiceImpl.class,
                ClogSetupListener.class);

        addToSet(OrchidController.class);

        // Template Functions
        addToSet(TemplateFunction.class,
                AnchorFunction.class,
                CompileAsFunction.class,
                FindAllFunction.class,
                FindFunction.class,
                FindTemplateFunction.class,
                LimitToFunction.class,
                LinkFunction.class,
                LoadFunction.class,
                LocalDateFunction.class
        );

        // Template Tags
        addToSet(TemplateTag.class,
                LogTag.class);

        ClogFormatter formatter = Clog.getInstance().getFormatter();
        if (formatter instanceof Parseltongue) {
            ((Parseltongue) formatter).findSpells(ClogSpells.class);
        }

        addToSet(AdminList.class, new AdminList() {
            @Override
            public String getKey() {
                return "services";
            }

            @Override
            public Class<?> getListClass() {
                return OrchidService.class;
            }

            @Override
            public Collection getItems() {
                return Orchid.getInstance().getContext().getServices();
            }

            @Override
            public boolean isImportantType() {
                return true;
            }
        });
    }
}
