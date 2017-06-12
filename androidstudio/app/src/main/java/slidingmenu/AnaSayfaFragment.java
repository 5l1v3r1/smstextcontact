package slidingmenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Classlar.GecmisAramaSms;
import Classlar.Sms;
import adapter.AramaGecmisiAdapter;
import genel.stconst;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.devillord.smstextcontact.R;


public class AnaSayfaFragment extends Fragment implements AdapterView.OnItemClickListener {

	public AnaSayfaFragment() {
	}

	private List<GecmisAramaSms> items;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_anasayfa,
				container, false);

		btnsmsicerigi = (Button) rootView.findViewById(R.id.btnsmsicerigi);


		listem = (ListView) rootView.findViewById(R.id.listem);
		stconst.KisiListem.clear();



		// items.clear();

		if (items == null) {
			items = new ArrayList<GecmisAramaSms>();
		}

		listemadapter = new AramaGecmisiAdapter(getActivity(), items);
		listem.setAdapter(listemadapter);
		listem.setTextFilterEnabled(true);

		if (items.size() == 0) {
			getCallDetails(getActivity().getApplicationContext());
			stconst.SeciliButton = 0;
		}

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	Timer timer;

	@Override
	public void onResume() {
		super.onResume();



	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setActionBarPosition(1);
		((MainActivity) getActivity()).setActionBarTitle(getActivity()
				.getString(R.string.textnumarasorgulama));

		btnsmsicerigi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getAllSmsIcerigi();

				stconst.SeciliButton = 2;
				btnsmsicerigi.setBackgroundColor(getResources().getColor(
						R.color.acikmavi));


			}
		});
		btnsmsicerigi.setBackgroundColor(getResources().getColor(
				R.color.acikmavi));


		listem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView,
									int position, long id) {

				stconst.SorgulanacakNumara = items.get(position)
						.get_PhoneNumber();


			}
		});
	}

	protected List<Sms> getAllSmsIcerigi() {
		items.clear();
		List<Sms> lstSms = new ArrayList<Sms>();
		Sms objSms = new Sms();
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = getActivity().getContentResolver();

		Cursor c = cr.query(message, null, null, null, "DATE desc");
		getActivity().startManagingCursor(c);
		int totalSMS = c.getCount();
		int say = 0;
		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {
				GecmisAramaSms smsim = new GecmisAramaSms();
				objSms = new Sms();
				objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
				objSms.setAddress(c.getString(c
						.getColumnIndexOrThrow("address")));
				objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setReadState(c.getString(c.getColumnIndex("read")));
				objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

				Date date = new Date(c.getLong(c.getColumnIndexOrThrow("date")));

				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					objSms.setFolderName("INCOMING_SMS");
				} else {
					objSms.setFolderName("OUTGOING_SMS");
				}

				lstSms.add(objSms);
				smsim.set_Date(objSms.getTime().toString());
				try {
					smsim.set_PhoneNumber(objSms.getAddress().toString());
				} catch (Exception e) {
					smsim.set_PhoneNumber("-111");
				}
				smsim.set_Type(objSms.getFolderName().toString());
				smsim.set_OriginalDate(date);
				if (say < 10) {

					Pattern p = Pattern.compile("-?\\d+");
					Matcher m = p.matcher(objSms.getMsg());
					while (m.find()) {

						// Pattern pattern = Pattern
						// .compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
						// Matcher matcher = pattern.matcher(m.group());

						if (m.group().length() > 9) {
							smsim.set_PhoneNumber(m.group());

							smsim.set_DisplayName(getContactName(getActivity(),
									smsim.get_PhoneNumber()));

							items.add(smsim);
							say += 1;
						} else {

						}

					}

				}

				c.moveToNext();

			}
		}
		// else {
		// throw new RuntimeException("You have no SMS");
		// }
		if (c != null && !c.isClosed()) {
			getActivity().stopManagingCursor(c);
			c.close();
		}
		listemadapter.notifyDataSetChanged();
		return lstSms;
	}

	public void getCallDetails(Context context) {
		items.clear();
		StringBuffer stringBuffer = new StringBuffer();
		Cursor cursor = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");
		int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

		int say = 0;
		while (cursor.moveToNext()) {
			GecmisAramaSms smsim = new GecmisAramaSms();
			String phNumber = cursor.getString(number);
			String callType = cursor.getString(type);
			String callDate = cursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = cursor.getString(duration);
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
				case CallLog.Calls.OUTGOING_TYPE:
					dir = "OUTGOING_CALL";
					break;
				case CallLog.Calls.INCOMING_TYPE:
					dir = "INCOMING_CALL";
					break;

				case CallLog.Calls.MISSED_TYPE:
					dir = "MISSED_CALL";
					break;

				case 4:
					dir = "Voice_Mail";
					break;
				case 5:
					dir = "Rejected";
					break;
				case 6:
					dir = "Refused_List";
					break;
			}
			smsim.set_OriginalDate(callDayTime);
			smsim.set_Date(callDate);
			smsim.set_PhoneNumber(phNumber);
			smsim.set_Type(dir);
			if (say < 10) {

				smsim.set_DisplayName(getContactName(getActivity(),
						smsim.get_PhoneNumber()));

				items.add(smsim);
				say += 1;
			}
		}

		if (cursor != null && !cursor.isClosed()) {
			getActivity().stopManagingCursor(cursor);
			cursor.close();
		}
		listemadapter.notifyDataSetChanged();
	}

	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = "";
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}

	public List<Sms> getAllSms() {
		items.clear();
		List<Sms> lstSms = new ArrayList<Sms>();
		Sms objSms = new Sms();
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = getActivity().getContentResolver();

		Cursor c = cr.query(message, null, null, null, "DATE desc");
		getActivity().startManagingCursor(c);
		int totalSMS = c.getCount();
		int say = 0;
		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {
				GecmisAramaSms smsim = new GecmisAramaSms();
				objSms = new Sms();
				objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
				objSms.setAddress(c.getString(c
						.getColumnIndexOrThrow("address")));
				objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setReadState(c.getString(c.getColumnIndex("read")));
				objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));

				Date date = new Date(c.getLong(c.getColumnIndexOrThrow("date")));

				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					objSms.setFolderName("INCOMING_SMS");
				} else {
					objSms.setFolderName("OUTGOING_SMS");
				}

				lstSms.add(objSms);
				smsim.set_Date(objSms.getTime().toString());
				try {
					smsim.set_PhoneNumber(objSms.getAddress().toString());
				} catch (Exception e) {
					smsim.set_PhoneNumber("-111");
				}
				smsim.set_Type(objSms.getFolderName().toString());
				smsim.set_OriginalDate(date);
				if (say < 10) {

					smsim.set_DisplayName(getContactName(getActivity(),
							smsim.get_PhoneNumber()));

					items.add(smsim);
					say += 1;
				}

				c.moveToNext();

			}
		}
		// else {
		// throw new RuntimeException("You have no SMS");
		// }
		if (c != null && !c.isClosed()) {
			getActivity().stopManagingCursor(c);
			c.close();
		}
		listemadapter.notifyDataSetChanged();
		return lstSms;
	}

	ListView listem;
	public static AramaGecmisiAdapter listemadapter;
	Button  btnsmsicerigi;


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub

	}




}
