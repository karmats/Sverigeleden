package net.karmats.sverigeleden.client;

import net.karmats.sverigeleden.client.panel.FooterPanel;
import net.karmats.sverigeleden.client.panel.HeaderPanel;
import net.karmats.sverigeleden.client.panel.MainMapPanel;
import net.karmats.sverigeleden.client.panel.NavigationPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sverigeleden implements EntryPoint {

    public static final String HEADER_ID = "header";
    public static final String MENU_ID = "menu";
    public static final String CONTENT_ID = "content";
    public static final String FOOTER_ID = "footer";

    // GWT module entry point method.
    public void onModuleLoad() {
        // Add header
        RootPanel.get(HEADER_ID).add(new HeaderPanel());
        RootPanel.get(MENU_ID).add(new NavigationPanel());
        RootPanel.get(CONTENT_ID).add(new MainMapPanel());
        RootPanel.get(FOOTER_ID).add(new FooterPanel());

        GWT.log("Done building UI");
    }

}