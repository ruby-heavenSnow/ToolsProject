package fragment.ruby.toolsproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.iaf.framework.app.BaseActivity;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void onBackPressed() {
    long current = System.currentTimeMillis();
    if (current - backTimeMillis < 2000) {
      exitApp();
    } else {
      backTimeMillis = current;
      showMsgToast("再按一次退出程序");
      return;
    }
  }
}
