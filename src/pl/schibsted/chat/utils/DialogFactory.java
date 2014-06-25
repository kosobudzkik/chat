package pl.schibsted.chat.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogFactory {
    private boolean contextDead;

    public void showOkMessage(Context context, String message) {

        showOkMessage(context, null, message);
    }

    public void showOkMessage(Context context, String title, String message) {

        buildDialog(context, title, message, null, null);
    }

    public void showOkAndButtonMessage(Context context, String title, String message, String secondButtonText, DialogInterface.OnClickListener secondButtonCallback) {

        buildDialog(context, title, message,  secondButtonText,  secondButtonCallback);
    }

    private void buildDialog(Context context, String title, String message, String secondButtonText, DialogInterface.OnClickListener secondButtonCallback) {
        if (isContextDead(context)) return;
        AlertDialog ad = new AlertDialog.Builder(context).create();
        ad.setCancelable(false);
        if (title != null) ad.setTitle(title);
        ad.setMessage(message);
        if(secondButtonCallback != null && secondButtonText != null)
            ad.setButton(DialogInterface.BUTTON_NEUTRAL, secondButtonText, secondButtonCallback);
        ad.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    public boolean isContextDead(Context context) {
      if (context instanceof Activity) {
            try {
                Activity act = (Activity) context;
                if (act.isFinishing()) return true;
            }    catch (Exception ex) {
            }
        }
        return false;
    }
}
