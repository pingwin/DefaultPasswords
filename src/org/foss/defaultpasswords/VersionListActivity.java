package org.foss.defaultpasswords;

import org.foss.defaultpasswords.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import java.io.IOException;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class VersionListActivity extends ListActivity {
  private static final String TAG = "VersionListActivity";
  private PassDbAdapter mDbHelper;
  private long rowID;
  private Cursor versionCursor;

    /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Creating "+TAG);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.version_list);
    mDbHelper = new PassDbAdapter(this);
    mDbHelper.openDataBase();

    Bundle extras = getIntent().getExtras();

    if (extras != null) {
      rowID = extras.getLong(PassDbAdapter.ID);
      TextView tvVendorName = (TextView)findViewById(R.id.vendor_name);
      tvVendorName.setText(mDbHelper.vendorNameAt(rowID));

      TextView tvModelName = (TextView)findViewById(R.id.model_name);
      tvModelName.setText(extras.getString(PassDbAdapter.MODEL));

    } else {
      Toast.makeText(this, "Did not deliver to activity", Toast.LENGTH_LONG).show();
      throw new Error("Did not deliver to activity");
    }

    fillData();
  }


  private void fillData() {
    versionCursor = mDbHelper.fetchAllVersionsForModelAt(rowID);
    Toast.makeText(this, "Found "+versionCursor.getCount()+" Models", Toast.LENGTH_LONG).show();
    startManagingCursor(versionCursor);

    if (versionCursor.getCount() == 1) {
      versionCursor.moveToFirst();
      Intent i = new Intent(this, DetailedViewActivity.class);
      i.putExtra(PassDbAdapter.ID, versionCursor.getLong(versionCursor.getColumnIndexOrThrow(PassDbAdapter.ID)));
      startActivityForResult(i, 0);
      return;
    }

    String[] from = new String[] {PassDbAdapter.VERSION};
    int[] to = new int[] {R.id.text1};
     SimpleCursorAdapter versions =
      new SimpleCursorAdapter(this, R.layout.vendor_row, versionCursor, from, to);
    setListAdapter(versions);
  }

  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
 		Cursor c = versionCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, DetailedViewActivity.class);
		i.putExtra(PassDbAdapter.ID, c.getLong(c.getColumnIndexOrThrow(PassDbAdapter.ID)));
    startActivityForResult(i, 0);
	}
}