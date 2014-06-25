package pl.schibsted.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import pl.schibsted.chat.App;
import pl.schibsted.chat.R;
import pl.schibsted.chat.async.DownloadAsyncTask;
import pl.schibsted.chat.components.LatoTextView;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author krzysztof.kosobudzki
 */
public class ArticlesListAdapter extends BaseAdapter {
    private final List<Article> mArticles;
    private final Context mContext;

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm yyyy-MM-dd");

    public ArticlesListAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    @Override
    public int getCount() {
        if (mArticles == null) {
            return 0;
        }

        return mArticles.size();
    }

    @Override
    public Object getItem(int position) {
        if (mArticles == null) {
            return null;
        }

        return mArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mArticles == null) {
            return 0;
        }

        return mArticles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.card_item, parent, false);

            ViewHolder vhh = new ViewHolder();
            vhh.titleTextView = (LatoTextView) v.findViewById(R.id.article_title_text_view);
            vhh.dateTextView = (LatoTextView) v.findViewById(R.id.article_date_text_view);
            vhh.coverView = (ImageView) v.findViewById(R.id.article_image_view);

            v.setTag(vhh);
        } else {
            v = convertView;
        }

        ViewHolder vh = (ViewHolder) v.getTag();
        Article article = mArticles.get(position);

        vh.titleTextView.setText(article.getTitle());
        vh.dateTextView.setText(
                TextUtils.setSpanBetweenTokens(
                        String.format(
                                mContext.getString(R.string.article_date),
                                DATE_FORMAT.format(article.getPublished() * 1000l)
                                ),
                        TextUtils.DEFAULT_TOKEN,
                        new TextAppearanceSpan(mContext, R.style.ChatTheme_Text_Big_Bold)
                ));

        if (article.getMainImages() != null) {
            Bitmap bmp = App.getInstance().getImage(article.getMainImages().getNormalUrl());

            if (bmp == null) {
                vh.coverView.setTag(article.getMainImages().getNormalUrl());

                new DownloadAsyncTask(vh.coverView).execute();
            } else {
                vh.coverView.setImageBitmap(bmp);
            }
        }

        return v;
    }

    public void addItems(List<Article> articles) {
        mArticles.addAll(articles);

        notifyDataSetChanged();
    }

    private class ViewHolder {
        public LatoTextView titleTextView;
        public LatoTextView dateTextView;
        public ImageView coverView;
    }
}
