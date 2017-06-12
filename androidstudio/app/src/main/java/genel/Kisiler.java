package genel;

import java.util.Comparator;

public class Kisiler {
	private String _ADI;
	private String _SOYADI;
	private String _GSMNO;

	public String get_ADI() {
		return _ADI;
	}

	public void set_ADI(String _ADI) {
		this._ADI = _ADI;
	}

	public String get_SOYADI() {
		return _SOYADI;
	}

	public void set_SOYADI(String _SOYADI) {
		this._SOYADI = _SOYADI;
	}

	public String get_GSMNO() {
		return _GSMNO;
	}

	public void set_GSMNO(String _GSMNO) {
		this._GSMNO = _GSMNO;
	}

	public String get_EKLEYEN() {
		return _EKLEYEN;
	}

	public void set_EKLEYEN(String _EKLEYEN) {
		this._EKLEYEN = _EKLEYEN;
	}

	public String get_SORGULAMATARIHI() {
		return _SORGULAMATARIHI;
	}

	public void set_SORGULAMATARIHI(String _SORGULAMATARIHI) {
		this._SORGULAMATARIHI = _SORGULAMATARIHI;
	}

	private String _SORGULAMATARIHI;
	private String _EKLEYEN;

	public static Comparator<Kisiler> FruitNameComparator = new Comparator<Kisiler>() {

		@Override
		public int compare(Kisiler fruit1, Kisiler fruit2) {

			String fruitName1 = fruit1.get_EKLEYEN().toUpperCase();
			String fruitName2 = fruit2.get_EKLEYEN().toUpperCase();

			// ascending order
			return fruitName1.compareTo(fruitName2);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};
}
