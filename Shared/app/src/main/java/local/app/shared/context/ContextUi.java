package local.app.shared.context;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class ContextUi {
    public volatile WebView webView;
    public volatile WebViewClient client;

    public volatile EditText inputUrl;
    public volatile Button buttonUrl;
    public volatile Button buttonBack;
    public volatile Button buttonShared;
    public volatile Button buttonCookie;
    public volatile Button buttonExit;
}
