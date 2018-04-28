package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import com.google.inject.Injector;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"}, dependsOnGroups = {"theme"})
public final class ThemeServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    private Injector injector;
    private OrchidContext context;
    private ThemeService underTest;
    private ThemeServiceImpl service;

    private GlobalAssetHolder globalAssetHolder;

    private JSONObject themeContextOptions;
    private JSONObject theme2ContextOptions;

    private Theme theme1;
    private Set<Theme> themes;

    @BeforeMethod
    public void testSetup() {
        themes = new HashSet<>();
        theme1 = mock(Theme.class);
        when(theme1.getKey()).thenReturn("theme1");
        themes.add(theme1);

        globalAssetHolder = mock(GlobalAssetHolder.class);

        themeContextOptions = new JSONObject();
        theme2ContextOptions = new JSONObject();

        // Mock Injector
        injector = mock(Injector.class);
        when(injector.getInstance((Class<Theme>) theme1.getClass())).thenReturn(theme1);

        // Mock Context
        context = mock(OrchidContext.class);
        when(context.getInjector()).thenReturn(injector);
        when(context.query("theme")).thenReturn(new JSONElement(themeContextOptions));
        when(context.query("theme2")).thenReturn(new JSONElement(theme2ContextOptions));

        // Create instance of Service Implementation
        service = new ThemeServiceImpl(globalAssetHolder, () -> themes, "theme1", "adminTheme1");
        service.initialize(context);

        // Create wrapper around the Implementation to verify it works in composition
        underTest = new ThemeService() {
            public void initialize(OrchidContext context) {
            }

            public <T extends OrchidService> T getService(Class<T> serviceClass) {
                return (T) service;
            }
        };
    }

}