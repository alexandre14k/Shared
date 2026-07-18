package local.app.shared.debug;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LogToast {
    public LogToast(Context context, String text, boolean showError) {
        if (text == null || text.isEmpty()) {
            text = "Error occurred";
        }

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        TextView textView = new TextView(context);
        textView.setLayoutParams(new FrameLayout
                .LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
        );
        textView.setPadding(32, 16, 32, 16);
        textView.setBackgroundColor(
                Color.parseColor("#CC000000")
        );
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);

        toast.setView(textView);
        toast.show();

        new LogCat("Show: " + text);
    }
}