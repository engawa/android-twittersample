package twitter.sample.cliant;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import jp.digitalcloud.sample.twitter.auth.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TweetTimeline extends ListActivity {

	private TweetAdapter mAdapter;
    private Twitter mTwitter;

    // onCreateメソッド(画面初期表示イベント)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);


        mAdapter = new TweetAdapter(this);
        setListAdapter(mAdapter);

        mTwitter = getTwitterInstance();
        reloadTimeLine();
    }

    public Twitter getTwitterInstance() {
        String consumerKey = getString(R.string.twitter_consumer_key);
        String consumerSecret = getString(R.string.twitter_consumer_key_secret);

        // プリファレンスの準備 →トークン取得
        SharedPreferences pref = getSharedPreferences( getString(R.string.shared_pref_name), MODE_PRIVATE );
        String Token = pref.getString( getString(R.string.shared_pref_key_twitter_access_token), "" );
        String TokenSecret = pref.getString( getString(R.string.shared_pref_key_twitter_access_token_secret), "" );

        AccessToken at = new AccessToken(Token,TokenSecret);
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(at);

        return twitter;
    }

    private class TweetAdapter extends ArrayAdapter<String> {

        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }
    }

    private void reloadTimeLine() {
        AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    ResponseList<twitter4j.Status> timeline = mTwitter.getHomeTimeline();
                    ArrayList<String> list = new ArrayList<String>();
                    for (twitter4j.Status status : timeline) {
                        list.add(status.getText());
                    }
                    return list;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (String status : result) {
                        mAdapter.add(status);
                    }
                    getListView().setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
