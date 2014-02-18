package net.karmats.sverigeleden.client.panel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;

public class NavigationPanel extends MenuBar {

    public NavigationPanel() {
        super();
        Command main = new Command() {

            @Override
            public void execute() {
                RootPanel.get("content").clear();
                RootPanel.get("content").add(new MainMapPanel());
            }
        };

        addItem(new MenuItem("Main", main));

        Command uploadPath = new Command() {

            @Override
            public void execute() {
//                RootPanel.get("content").clear();
//                RootPanel.get("content").add(new UploadPathPanel(map));
                Window.alert("You clicked upload");
            }
        };
        addItem(new MenuItem("Upload", uploadPath));

    }
}
