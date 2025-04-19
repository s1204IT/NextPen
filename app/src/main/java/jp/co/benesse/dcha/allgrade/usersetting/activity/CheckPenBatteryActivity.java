package jp.co.benesse.dcha.allgrade.usersetting.activity;

import android.app.Activity;
import android.os.BenesseExtension;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.s1204.benesse.touch.checkpen.R;

public class CheckPenBatteryActivity extends Activity {

    private static final int BATTERY_UNACQUIRED = 0;
    private static final int BATTERY_EXTREMELY_LOW = 1;
    private static final int BATTERY_LOW = 2;
    private static final int BATTERY_MEDIUM = 3;
    private static final int BATTERY_FULL = 4;
    private static final String BC_FTS_PEN_BATTERY = "bc:pen:battery";

    @Deprecated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_check_pen_battery);
        findViewById(R.id.back).setOnClickListener(view -> finishAndRemoveTask());
        Button batteryTouchArea = findViewById(R.id.batteryTouchArea);
        batteryTouchArea.setOnTouchListener((v, event) -> {
            try {
                if (event.getAction() == 0) {
                    int battery = BenesseExtension.getInt(BC_FTS_PEN_BATTERY);
                    checkBatteryImageChange(event, battery);
                    return false;
                }
                return false;
            } catch (NoClassDefFoundError ignored) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void checkBatteryImageChange(MotionEvent event, int battery) {
        TextView batteryLevel = findViewById(R.id.batteryLevel);
        TextView batteryAnnotationText = findViewById(R.id.batteryAnnotationText);
        int type = event.getToolType(0);
        if (type == 2) {
            switch (battery) {
                case BATTERY_UNACQUIRED:
                    batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_default);
                    return;
                case BATTERY_EXTREMELY_LOW:
                    batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_extremely_low);
                    batteryAnnotationText.setBackgroundResource(R.drawable.check_pen_battery_annotation_text_extremely_low);
                    findViewById(R.id.batteryAnnotationText).setVisibility(View.VISIBLE);
                    return;
                case BATTERY_LOW:
                    batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_low);
                    batteryAnnotationText.setBackgroundResource(R.drawable.check_pen_battery_annotation_text_medium);
                    findViewById(R.id.batteryAnnotationText).setVisibility(View.VISIBLE);
                    return;
                case BATTERY_MEDIUM:
                    batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_medium);
                    batteryAnnotationText.setBackgroundResource(R.drawable.check_pen_battery_annotation_text_full);
                    findViewById(R.id.batteryAnnotationText).setVisibility(View.VISIBLE);
                    return;
                case BATTERY_FULL:
                    batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_full);
                    batteryAnnotationText.setBackgroundResource(R.drawable.check_pen_battery_annotation_text_full);
                    findViewById(R.id.batteryAnnotationText).setVisibility(View.VISIBLE);
                    return;
                default:
                    return;
            }
        }
        batteryLevel.setBackgroundResource(R.drawable.check_pen_battery_level_default);
        batteryAnnotationText.setBackgroundResource(R.drawable.check_pen_battery_annotation_text_other_pen);
        findViewById(R.id.batteryAnnotationText).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == 0) {
                int battery = BenesseExtension.getInt(BC_FTS_PEN_BATTERY);
                checkBatteryImageChange(event, battery);
                return false;
            }
            return false;
        } catch (NoClassDefFoundError ignored) {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        findViewById(R.id.back).setOnClickListener(null);
    }
}
