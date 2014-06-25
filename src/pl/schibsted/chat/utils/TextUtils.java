package pl.schibsted.chat.utils;

import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;

/**
 * @author krzysztof.kosobudzki
 */
public final class TextUtils {
    public static final String DEFAULT_TOKEN = "*";

    public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... cs) {
        int tokenLen = token.length();
        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        if (start > -1 && end > -1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

            if (cs.length > 0) {
                for (CharacterStyle c : cs) {
                    spannableStringBuilder.setSpan(c, start, end, 0);
                }
            }

            spannableStringBuilder.delete(end, end + tokenLen);
            spannableStringBuilder.delete(start - tokenLen, start);

            text = spannableStringBuilder;
        }

        return text;
    }
}
