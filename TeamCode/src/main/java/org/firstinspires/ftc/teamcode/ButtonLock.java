package org.firstinspires.ftc.teamcode;

public class ButtonLock {
    private boolean flg;
    private final ButtonCallback callback;

    public ButtonLock(ButtonCallback button) {
        this.callback = button;
        this.flg = false;
    }

    public void option() {
        if (this.callback.getButton()) {
            if (!this.flg) {
                this.flg = true;
                callback.addButtonState();
            }
        } else this.flg = false;
    }


    public interface ButtonCallback {
        void addButtonState();

        boolean getButton();
    }
}
