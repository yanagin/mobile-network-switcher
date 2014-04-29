package orz.yanagin.android.mns.configurations;

import java.io.Serializable;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * アプリケーション
 * @author k-yanagihara
 */
public class Application implements Serializable, Comparable<Application> {

	private static final long serialVersionUID = 1L;

	/** タイトル */
	private final String title;

	/** パッケージ名 */
	private final String packageName;

	/** 名称 */
	private final String name;

	public Application(String title, String packageName, String name) {
		this.title = title;
		this.packageName = packageName;
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return title + "|" + packageName + "|" + name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Application)) {
			return false;
		}
		return toString().equals(Application.class.cast(o).toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public int compareTo(Application another) {
		return getTitle().toLowerCase().compareTo(another.getTitle().toLowerCase());
	}

	/**
	 * アプリケーションのアイコンを読み込む
	 * @param packageManager PackageManager
	 * @return アイコン
	 */
	public Drawable loadIcon(PackageManager packageManager) {
		try {
			return packageManager.getActivityIcon(new ComponentName(packageName, name));
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * toString()で変換した文字列からアプリケーションを生成する
	 * @param str アプリケーションの文字列表現
	 * @return アプリケーション
	 */
	public static Application fromString(String str) {
		String[] array = str.split("\\|");
		if (array.length < 3) {
			return null;
		}
		return new Application(array[0], array[1], array[2]);
	}

}
