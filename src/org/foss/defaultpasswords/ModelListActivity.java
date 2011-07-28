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

public class ModelListActivity extends ListActivity {
  private static final String TAG = "ModelListActivity";
  private PassDbAdapter mDbHelper;
  private long rowID;
  private Cursor modelCursor;

    /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Creating "+TAG);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.model_list);
    mDbHelper = new PassDbAdapter(this);
    mDbHelper.openDataBase();

    Bundle extras = getIntent().getExtras();

    if (extras != null) {
      TextView tvVendorName = (TextView)findViewById(R.id.vendor_name);
      tvVendorName.setText(extras.getString(PassDbAdapter.VENDOR));
      rowID = extras.getLong(PassDbAdapter.ID);
    } else {
      Toast.makeText(this, "Did not deliver to activity", Toast.LENGTH_LONG).show();
      throw new Error("Did not deliver to activity");
    }

    fillData();
  }


  private void fillData() {
    modelCursor = mDbHelper.fetchAllModelsForVendorAt(rowID);
    Toast.makeText(this, "Found "+modelCursor.getCount()+" Models", Toast.LENGTH_LONG).show();
    startManagingCursor(modelCursor);

    if (modelCursor.getCount() == 1) {
      modelCursor.moveToFirst();
      Intent i = new Intent(this, VersionListActivity.class);
      i.putExtra(PassDbAdapter.ID, modelCursor.getLong(modelCursor.getColumnIndexOrThrow(PassDbAdapter.ID)));
      i.putExtra(PassDbAdapter.MODEL, modelCursor.getString(modelCursor.getColumnIndexOrThrow(PassDbAdapter.MODEL)));
      startActivityForResult(i, 0);
      return;
    }

    String[] from = new String[] {PassDbAdapter.MODEL};
    int[] to = new int[] {R.id.text1};
     SimpleCursorAdapter models =
      new SimpleCursorAdapter(this, R.layout.vendor_row, modelCursor, from, to);
    setListAdapter(models);
  }

  @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor c = modelCursor;
		c.moveToPosition(position);
		Intent i = new Intent(this, VersionListActivity.class);
		i.putExtra(PassDbAdapter.ID,    c.getLong(c.getColumnIndexOrThrow(PassDbAdapter.ID)));
		i.putExtra(PassDbAdapter.MODEL, c.getString(c.getColumnIndexOrThrow(PassDbAdapter.MODEL)));
    startActivityForResult(i, 0);
	}
}