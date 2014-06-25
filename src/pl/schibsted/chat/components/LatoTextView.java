package pl.schibsted.chat.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import pl.schibsted.chat.App;

/**
 * @author krzysztof.kosobudzki
 */
public class LatoTextView extends TextView {
    public LatoTextView(Context context) {
        super(context);
    }

    public LatoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LatoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (App.FONTS.get(style) == null) {
            super.setTypeface(tf, style);
        } else {
            setTypeface(App.getInstance().getTypeface(style));
        }
    }
}
