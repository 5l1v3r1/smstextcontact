package Classlar;

import java.util.Date;

public class GecmisAramaSms implements Comparable {

	@Override
	public int compareTo(Object arg0) {
		GecmisAramaSms country = (GecmisAramaSms) arg0;
		return this.get_OriginalDate().compareTo(country.get_OriginalDate());
	}

	private String _PhoneNumber;

	public String get_PhoneNumber() {
		return _PhoneNumber;
	}

	public void set_PhoneNumber(String _PhoneNumber) {
		this._PhoneNumber = _PhoneNumber;
	}

	public String get_Type() {
		return _Type;
	}

	public void set_Type(String _Type) {
		this._Type = _Type;
	}

	public String get_Date() {
		return _Date;
	}

	public void set_Date(String _Date) {
		this._Date = _Date;
	}

	public Date get_OriginalDate() {
		return _OriginalDate;
	}

	public void set_OriginalDate(Date _OriginalDate) {
		this._OriginalDate = _OriginalDate;
	}

	public String get_DisplayName() {
		return _DisplayName;
	}

	public void set_DisplayName(String _DisplayName) {
		this._DisplayName = _DisplayName;
	}

	private String _DisplayName = "";

	private String _Type = "";
	private String _Date = "";
	private Date _OriginalDate;

}
