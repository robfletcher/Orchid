package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.lexer.LexerImpl;
import com.mitchellbosecke.pebble.lexer.TokenStream;
import com.mitchellbosecke.pebble.node.RootNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.parser.ParserImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public final class PebbleCompiler extends OrchidCompiler implements OrchidEventListener {

    private Provider<OrchidContext> contextProvider;
    private ExecutorService executor;
    private PebbleEngine engine;

    @Inject
    public PebbleCompiler(Provider<OrchidContext> contextProvider, PebbleTemplateLoader loader, Set<Extension> extensions) {
        super(10000);

        Extension[] extensionArray = new Extension[extensions.size()];
        extensions.toArray(extensionArray);

        this.contextProvider = contextProvider;
        this.executor = Executors.newFixedThreadPool(10);
        this.engine = new PebbleEngine.Builder()
                .loader(loader)
                .executorService(executor)
                .extension(extensionArray)
                .newLineTrimming(false)
//                .cacheActive(false)
                .build();
    }

    @Override
    public String compile(String extension, String source, Map<String, Object> data) {
        try {
            LexerImpl lexer = new LexerImpl(
                    engine.getSyntax(),
                    engine.getExtensionRegistry().getUnaryOperators().values(),
                    engine.getExtensionRegistry().getBinaryOperators().values());
            TokenStream tokenStream = lexer.tokenize(new StringReader(source), "");

            Parser parser = new ParserImpl(
                    engine.getExtensionRegistry().getUnaryOperators(),
                    engine.getExtensionRegistry().getBinaryOperators(),
                    engine.getExtensionRegistry().getTokenParsers());
            RootNode root = parser.parse(tokenStream);

            PebbleTemplateImpl compiledTemplate = new PebbleTemplateImpl(engine, root, "");

            for (NodeVisitorFactory visitorFactory : engine.getExtensionRegistry().getNodeVisitors()) {
                visitorFactory.createVisitor(compiledTemplate).visit(root);
            }

            Writer writer = new StringWriter();
            compiledTemplate.evaluate(writer, data);

            return writer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"html", "peb", "pebble"};
    }

// Clean up executor on shutdown
//----------------------------------------------------------------------------------------------------------------------

    @On(Orchid.Lifecycle.Shutdown.class)
    public void onEndSession(Orchid.Lifecycle.Shutdown event) {
        executor.shutdown();
    }

    @On(Orchid.Lifecycle.ClearCache.class)
    public void onClearCache(Orchid.Lifecycle.ClearCache event) {
        engine.getTagCache().invalidateAll();
        engine.getTemplateCache().invalidateAll();
    }

}
