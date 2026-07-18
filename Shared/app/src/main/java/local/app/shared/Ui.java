package local.app.shared;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import local.app.shared.context.ContextUi;
import local.app.shared.debug.LogCat;
import local.app.shared.debug.LogToast;
import local.app.shared.internet.Check;

public class Ui extends AppCompatActivity {
    private final ContextUi ui = new ContextUi();
    private Check internetCheck;
    private String urlBeforeQr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        restore(savedInstanceState);

        callbacks();
        welcome();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (ui.webView != null) {
            ui.webView.saveState(outState);
        }
        outState.putString("inputUrl", ui.inputUrl.getText().toString());
        outState.putString("urlBeforeQr", urlBeforeQr);
        new LogCat("onSaveInstanceState: saved");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ui.webView.restoreState(savedInstanceState);
            String savedUrl = savedInstanceState.getString("inputUrl", "");
            ui.inputUrl.setText(savedUrl);
            urlBeforeQr = savedInstanceState.getString("urlBeforeQr", null);
            new LogCat("onRestoreInstanceState: restored, url=" + savedUrl);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ui.webView.canGoBack()) {
            ui.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void restore(Bundle bundle) {
        if (bundle != null) {
            ui.webView.restoreState(bundle);
            ui.inputUrl.setText(bundle.getString("inputUrl", ""));
            urlBeforeQr = bundle.getString("urlBeforeQr", null);
            new LogCat("onCreate: restored from saved state");
        }
    }

    private void init() {
        internetCheck = new Check(this);

        ui.webView = findViewById(R.id.webView);
        ui.inputUrl = findViewById(R.id.inputUrl);

        ui.buttonUrl = findViewById(R.id.buttonUrl);
        ui.buttonBack = findViewById(R.id.buttonBack);
        ui.buttonShared = findViewById(R.id.buttonShared);
        ui.buttonCookie = findViewById(R.id.buttonCookie);
        ui.buttonExit = findViewById(R.id.buttonExit);

        init_webView();
        new LogCat("init done");
    }

    private void init_webView() {
        ui.client = new android.webkit.WebViewClient() {
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                if (!isQrPage(url)) {
                    runOnUiThread(() -> {
                        if (urlBeforeQr != null) {
                            ui.inputUrl.setText(urlBeforeQr);
                            new LogCat("Restored URL: " + urlBeforeQr);
                            urlBeforeQr = null;
                        } else {
                            ui.inputUrl.setText(url);
                            new LogCat("URL updated: " + url);
                        }
                    });
                }
            }
        };
        ui.webView.setWebViewClient(ui.client);

        android.webkit.WebSettings settings = ui.webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);

        ui.inputUrl.setText(R.string.app_github);
    }

    private void welcome() {
        new LogToast(this,
            getString(R.string.app_name)
                +"\n"
                +getString(R.string.app_help)
                +"\n"
                +getString(R.string.app_github),
            true);
    }

    private boolean isQrPage(String url) {
        return url.contains("qrserver")
            || url.contains("chart?cht=qr")
            || url.contains("/create-qr-code/");
    }

    private void callbacks() {
        ui.buttonUrl.setOnClickListener(this::onButtonUrl);
        ui.buttonBack.setOnClickListener(this::onButtonBack);
        ui.buttonShared.setOnClickListener(this::onButtonShared);
        ui.buttonCookie.setOnClickListener(this::onButtonCookie);
        ui.buttonExit.setOnClickListener(this::onButtonExit);
        new LogCat("callbacks done");
    }

    private void onButtonCookie(View v) {
        android.webkit.CookieManager cookieManager =
            android.webkit.CookieManager.getInstance();
        cookieManager.removeAllCookies(value ->
            runOnUiThread(() -> {
                cookieManager.flush();
                new LogToast(Ui.this,
                    "cookies\ncleared",
                    true);
                new LogCat("onButtonCookie: cookies cleared, success="
                    + value);
            })
        );
    }

    private void onButtonBack(View v) {
        if (ui.webView.canGoBack()) {
            ui.webView.goBack();
            new LogCat("onButtonBack: navigated back");
        } else {
            new LogToast(this,
                "origin reached",
                true);
            new LogCat("onButtonBack: no history, origin reached");
        }
    }

    private void onButtonUrl(View v) {
        if (!internetCheck.ok()) {
            new LogToast(this,
                "connect to internet first",
                true);
            new LogCat("onButtonUrl: no internet");
            return;
        }

        String url = ui.inputUrl.getText().toString();
        url = url.trim();

        if (!isLikelyUrl(url)) {
            url = "https://www.google.fr/search?q="
                + android.net.Uri.encode(url);
            new LogCat("onButtonUrl: treated as search query -> "
                + url);
        } else {
            new LogCat("onButtonUrl: " + url);
        }

        ui.webView.loadUrl(url);
    }

    private boolean isLikelyUrl(String input) {
        return input.contains(".") || input.contains("/");
    }

    private void onButtonShared(View v) {
        if (!internetCheck.ok()) {
            new LogToast(this,
                "connect to internet first",
                true);
            new LogCat("onButtonShared: no internet");
            return;
        }

        String currentUrl = ui.inputUrl.getText().toString();
        urlBeforeQr = currentUrl;  // ← save it here

        String qrImageUrl = "https://api.qrserver.com/v1/" +
                "create-qr-code/?color=000000&bgcolor=FFFFFF&data="
                + android.net.Uri.encode(currentUrl)
                + "&qzone=1&margin=0&size=400x400&ecc=L";

        ui.webView.loadUrl(qrImageUrl);
        new LogCat("onButtonShared: loaded QR image");
    }

    private void onButtonExit(View v) {
        finish();
    }
}