package slidingmenu;

import genel.stconst;

import java.util.ArrayList;

import slidingmenu.adapter.NavDrawerListAdapter;
import slidingmenu.model.NavDrawerItem;
import Listener.PhoneStateMonitor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devillord.smstextcontact.R;
import com.startapp.android.publish.StartAppAd;

public class MainActivity extends Activity {

	TelephonyManager manager;
	PhoneStateMonitor phoneStateListener;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private Object fragment;
	private final StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, TimeService.class));
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home

		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
		// .getResourceId(7, -1), true, "50+"));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button

		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			@Override
			public void onDrawerClosed(View view) {

				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {

				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getFragmentManager().popBackStack();
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		} else {
			if (getFragmentManager().getBackStackEntryCount() == 0) {
				displayView(0);
			}
		}

		phoneStateListener = new PhoneStateMonitor(this);
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String data = extras.getString("notificationId");
			if (data != null) {
				stconst.SorgulanacakNumara = extras.getString("notificationId");
				stconst.sayac = 100;
				Fragment yeniFragment = null;

				yeniFragment = new AnaSayfaFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.frame_container, yeniFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null); //geri tusu icin
				ft.commit();
			}
		}

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item

			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			openDrawer();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (menu.findItem(R.id.action_settings) != null) {
			menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		}

		return super.onPrepareOptionsMenu(menu);

	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		// Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();

		if (position == 0) {

			Fragment fragment = null;
			switch (position) {
			case 0:

				fragment = new AnaSayfaFragment();
				stconst.sayac += 1;
				break;
			case 1:
				stconst.SeciliKisi = null;

				break;
			case 2:
				stconst.SeciliKisi = null;

				break;

			case 3:

				fragment = new AnaSayfaFragment();
				stconst.sayac += 1;
				break;
			case 4:

				fragment = new AnaSayfaFragment();
				stconst.sayac += 1;
				break;
			case 5:

				fragment = new AnaSayfaFragment();
				stconst.sayac += 1;
				break;
			case 6:

				stconst.sayac += 1;
				break;
				case 7:

				stconst.sayac += 1;
				break;
			default:
				fragment = new AnaSayfaFragment();
				stconst.sayac += 1;
				break;
			}

			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment)
						.addToBackStack(null).commit();

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);

			} else {

				Log.e("MainActivity", "Error in creating fragment");
			}
		} else {

			Builder builder = new AlertDialog.Builder(this);
			AlertDialog dialog = builder.create();
			dialog.setTitle(this.getString(R.string.Uyari));
			dialog.setMessage(this
					.getString(R.string.OncelikleNumaraniziDogrulayiniz));
			dialog.setButton(this.getString(R.string.Tamam), listenerAccept2);

			dialog.setCancelable(false);
			dialog.show();
		}
	}

	DialogInterface.OnClickListener listenerAccept2 = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

	@Override
	public void onBackPressed() {
		int fragments = getFragmentManager().getBackStackEntryCount();
		if (fragments == 1) {

			finish();
		}
		super.onBackPressed();

	}

	public void setActionBarPosition(int position) {

		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void setActionBarTitle(String title) {

		getActionBar().setTitle(title);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;

		getActionBar().setTitle(mTitle);

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.

		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls

		mDrawerToggle.onConfigurationChanged(newConfig);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	//
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}
}
