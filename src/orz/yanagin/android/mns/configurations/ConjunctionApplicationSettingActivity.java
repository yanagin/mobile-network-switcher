package orz.yanagin.android.mns.configurations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import orz.yanagin.android.mns.pro.R;
import orz.yanagin.commons.Timer;
import orz.yanagin.commons.android.ActivityHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 連携するアプリケーションを選択するアクティビティ
 * @author k-yanagihara
 */
public class ConjunctionApplicationSettingActivity extends ListActivity {

	/** 連携するアプリケーション */
	private List<Application> applications;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conjunction_application_list);

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.application_scaning));
		progressDialog.show();

		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				applications = getInstalledApplications();

				Timer.sleep(2000);	// リストの構築に微妙に時間がかかるので少し待つ

				handler.post(new Runnable() {
					@Override
					public void run() {
						setListAdapter(new ApplicationAdapter(ConjunctionApplicationSettingActivity.this, applications));
						progressDialog.dismiss();
					}
				});
			}
		}).start();
	}

	List<Application> getInstalledApplications() {
		Intent intent = new Intent(Intent.ACTION_MAIN,null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,0);
		List<Application> result = new ArrayList<Application>();
		for (ResolveInfo resolveInfo : resolveInfos) {
			result.add(new Application(
					resolveInfo.activityInfo.loadLabel(packageManager).toString(),
					resolveInfo.activityInfo.packageName,
					resolveInfo.activityInfo.name));
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * アプリケーション一覧を表示するアダプタ
	 * @author k-yanagihara
	 */
	static class ApplicationAdapter extends BaseAdapter {

		private final Context context;

		private final List<Application> applications;

		private final LayoutInflater layoutInflater;

		private final PackageManager packageManager;

		public ApplicationAdapter(Context context, List<Application> applications) {
			this.context = context;
			this.applications = applications;
			layoutInflater = ActivityHelper.getSystemService(context, Context.LAYOUT_INFLATER_SERVICE);
			packageManager = context.getPackageManager();
		}

		@Override
		public int getCount() {
			return applications.size();
		}

		@Override
		public Object getItem(int i) {
			return applications.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null) {
				view = layoutInflater.inflate(R.layout.conjunction_application, null);
			}

			Application application = applications.get(i);

			CheckBox checkBox = CheckBox.class.cast(view.findViewById(R.id.application_check));

			ImageView.class.cast(view.findViewById(R.id.application_icon)).setImageDrawable(application.loadIcon(packageManager));
			TextView.class.cast(view.findViewById(R.id.application_name)).setText(application.getTitle());
			checkBox.setChecked(Configurations.isConjunctionApplication(context, application));

			view.setOnClickListener(new ListClickListener(context, applications.get(i), checkBox));
			checkBox.setOnClickListener(new CheckboxClickListener(context, application, checkBox));

			return view;
		}

	}

	static class ListClickListener implements OnClickListener {

		private final Context context;

		private final Application application;

		private final CheckBox checkBox;

		public ListClickListener(Context context, Application application, CheckBox checkBox) {
			this.context = context;
			this.application = application;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View v) {
			if (checkBox.isChecked()) {
				Configurations.removeConjunctionApplication(context, application);
				checkBox.setChecked(false);
			} else {
				Configurations.addConjunctionApplication(context, application);
				checkBox.setChecked(true);
			}
		}

	}

	static class CheckboxClickListener implements OnClickListener {

		private final Context context;

		private final Application application;

		private final CheckBox checkBox;

		public CheckboxClickListener(Context context, Application application, CheckBox checkBox) {
			this.context = context;
			this.application = application;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View v) {
			if (checkBox.isChecked()) {
				Configurations.addConjunctionApplication(context, application);
			} else {
				Configurations.removeConjunctionApplication(context, application);
			}
		}

	}

}
