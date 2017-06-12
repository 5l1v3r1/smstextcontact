package adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Classlar.GecmisAramaSms;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.devillord.smstextcontact.R;

public class AramaGecmisiAdapter extends ArrayAdapter<GecmisAramaSms> {
	private final Activity context;
	private ListItemRow itemRow;
	private final List<GecmisAramaSms> list;
	private LayoutInflater layoutInflater;

	public AramaGecmisiAdapter(Activity context, List<GecmisAramaSms> list) {
		super(context, R.layout.aramagecmisilistesi, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			itemRow = new ListItemRow();
			layoutInflater = context.getLayoutInflater();
			rowView = layoutInflater.inflate(R.layout.aramagecmisilistesi,
					null, true);

			itemRow.txt_phone_number = (TextView) rowView
					.findViewById(R.id.txt_phone_number);
			itemRow.txt_tarih = (TextView) rowView.findViewById(R.id.txt_tarih);
			itemRow.tablom = (TableLayout) rowView.findViewById(R.id.tablom);
		} else {
			itemRow = (ListItemRow) rowView.getTag();
		}
		GecmisAramaSms veriler = getItem(position);

		if (veriler.get_DisplayName().toString().intern() == "") {
			itemRow.txt_phone_number.setText(veriler.get_PhoneNumber()
					.toString());
		} else {
			itemRow.txt_phone_number.setText("(" + veriler.get_DisplayName()
					+ ") " + veriler.get_PhoneNumber().toString());
		}

		long val = Long.parseLong(veriler.get_Date().toString());
		Date date = new Date(val);
		SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String dateText = df2.format(date);

		itemRow.txt_tarih.setText(dateText);
		Resources resource = context.getResources();

		if (veriler != null && veriler.get_Type() != null) {

			if (veriler.get_Type().toString().intern() == "OUTGOING_CALL") {

				int iLeft = R.drawable.outcall;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);

			} else if (veriler.get_Type().toString().intern() == "INCOMING_CALL") {
				int iLeft = R.drawable.incall;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			} else if (veriler.get_Type().toString().intern() == "MISSED_CALL") {
				int iLeft = R.drawable.missincall;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			}

			else if (veriler.get_Type().toString().intern() == "Voice_Mail") {
				int iLeft = R.drawable.voicemail;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			} else if (veriler.get_Type().toString().intern() == "Rejected") {
				int iLeft = R.drawable.rejectedcall;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			} else if (veriler.get_Type().toString().intern() == "Refused_List") {
				int iLeft = R.drawable.rejectedlist;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			} else if (veriler.get_Type().toString().intern() == "OUTGOING_SMS") {
				int iLeft = R.drawable.outsms;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			} else if (veriler.get_Type().toString().intern() == "INCOMING_SMS") {
				int iLeft = R.drawable.insms;
				itemRow.txt_phone_number
						.setCompoundDrawablesWithIntrinsicBounds(0, 0, iLeft, 0);
			}
		}
		rowView.setTag(itemRow);
		return rowView;
	}


	private class ListItemRow {

		private TextView txt_phone_number;
		private TextView txt_tarih;
		private TableLayout tablom;

	}

}
