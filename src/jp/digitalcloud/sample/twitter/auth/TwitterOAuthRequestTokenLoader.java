package jp.digitalcloud.sample.twitter.auth;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

/**
 * AsyncTaskLoader for get a request token.
 * 
 * @author R SATO
 *
 */
public class TwitterOAuthRequestTokenLoader extends
		AsyncTaskLoader<RequestToken> {

	private Twitter mTwitter;
	private RequestToken mResult;

	/**
	 * Initialize AsyncTaskLoader.
	 * 
	 * @param context
	 *            Context
	 * @param twitter
	 *            Twitter
	 */
	public TwitterOAuthRequestTokenLoader(Context context, Twitter twitter) {
		super(context);
		mTwitter = twitter;
	}

	@Override
	protected void onStartLoading() {
		if (mResult != null) {
			deliverResult(mResult);
		}

		if (takeContentChanged() || mResult == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void deliverResult(RequestToken data) {
		mResult = data;
		super.deliverResult(data);
	}

	@Override
	public RequestToken loadInBackground() {
		RequestToken requestToken = null;

		try {
			requestToken = mTwitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			requestToken = null;
		}

		return requestToken;
	}
}
