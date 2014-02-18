package net.karmats.sverigeleden.client.dialog;

import com.google.gwt.user.client.ui.DialogBox;

public class WaitDialog extends DialogBox {

    public WaitDialog() {
        super();
        setText("Laddar..");
        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
        setAutoHideEnabled(false);
    }
}
