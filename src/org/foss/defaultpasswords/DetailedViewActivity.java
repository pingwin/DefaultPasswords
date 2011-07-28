package org.foss.defaultpasswords;

import org.foss.defaultpasswords.R;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class DetailedViewActivity extends Activity {
  private static final String TAG = "DetailedViewActivity";
  private PassDbAdapter mDbHelper;
  private long rowID;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Creating "+TAG);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detailed_view);
    mDbHelper = new PassDbAdapter(this);
    mDbHelper.openDataBase();

    Bundle extras = getIntent().getExtras();
    rowID = extras.getLong(PassDbAdapter.ID);
    fillData();
  }

  private void fillData() {
    Cursor c = mDbHelper.at(rowID);
    c.moveToFirst();
    TextView tv = (TextView)findViewById(R.id.tv_vendor);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.VENDOR)));
    tv = (TextView)findViewById(R.id.tv_model);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.MODEL)));
    tv = (TextView)findViewById(R.id.tv_version);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.VERSION)));
    tv = (TextView)findViewById(R.id.tv_ac);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.ACCESSTYPE)));
    tv = (TextView)findViewById(R.id.tv_user);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.USERNAME)));
    tv = (TextView)findViewById(R.id.tv_pass);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.PASSWORD)));
    tv = (TextView)findViewById(R.id.tv_priv);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.PRIVILEGES)));
    tv = (TextView)findViewById(R.id.tv_notes);
    tv.setText(c.getString(c.getColumnIndexOrThrow(PassDbAdapter.NOTES)));
  }  

}
