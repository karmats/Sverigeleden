package net.karmats.sverigeleden.client.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class HeaderPanel extends HorizontalPanel {

    public HeaderPanel() {
        super();
        add(new Label("Sverigeleden"));
        Image image = new Image();
        // Point the image at a real URL.
        image.setUrl("http://www.google.com/images/logo.gif");
        add(image);
    }

}
