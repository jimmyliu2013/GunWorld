package com.lim.gunworld.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.interfaces.PBEKey;

import com.lim.gunworld.R;
import com.lim.gunworld.application.MyApplication;
import com.lim.gunworld.constant.GlobalConstant;
import com.lim.gunworld.domain.UpdateInfo;
import com.lim.gunworld.ui.ExpandAnimation;
import com.lim.gunworld.utils.DataUtils;
import com.lim.gunworld.utils.DownLoadManager;
import com.lim.gunworld.utils.UpdateInfoParser;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {

	private static final String TAG = "SettingsActivity";
	private TextView version;
	private boolean isCheckingUpdate;
	private Handler handler;
	private UpdateInfo info;
	private TextView cacheSize;
	private View scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		setContentView(R.layout.fragment_settings);
		scrollView = findViewById(R.id.sv_activity_settings);

		if (MyApplication.isDarkModel()) {
			scrollView.setBackgroundColor(Color.DKGRAY);
		}

		initClearCacheView();
		initChangeModeView();
		initChangeTextSizeView();
		initCheckUpdateView();
		initFeedbackView();
		initInfoView();

	}

	private void initChangeTextSizeView() {
		Spinner spinner = (Spinner) findViewById(R.id.sp_activity_settings_change_size);
		spinner.setSelection(MyApplication.getTextSize(), true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				MyApplication.setTextSize(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void initChangeModeView() {
		SwitchCompat switchBotton = (SwitchCompat) findViewById(R.id.btn_activity_settings_change_mode);
		if (MyApplication.isDarkModel()) {
			switchBotton.setChecked(true);
		}
		switchBotton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					MyApplication.setDarkModel(true);
					scrollView.setBackgroundColor(Color.DKGRAY);
				} else {

					MyApplication.setDarkModel(false);
					scrollView.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initClearCacheView() {
		cacheSize = (TextView) findViewById(R.id.tv_fragment_settings_cache_size);

		try {
			cacheSize.setText((DataUtils.getFolderSize(new File("/data/data/"
					+ getPackageName() + "/shared_prefs")) / 1024)
					+ " KB");
		} catch (Exception e) {
			cacheSize.setText("");
			e.printStackTrace();
		}

		ImageButton clearCache = (ImageButton) findViewById(R.id.btn_fragment_settings_clear_cache);

		clearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingsActivity.this);
				builder.setTitle("初始化设置？");
				builder.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								DataUtils
										.cleanSharedPreference(SettingsActivity.this);
								try {
									cacheSize.setText((DataUtils
											.getFolderSize(new File(
													"/data/data/"
															+ getPackageName()
															+ "/shared_prefs")) / 1024)
											+ " KB");
								} catch (Exception e) {
									cacheSize.setText("");
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.show();
			}
		});
	}

	private void initCheckUpdateView() {
		version = (TextView) findViewById(R.id.tv_fragment_settings_version);

		try {
			version.setText("当前版本：" + getVersion());
		} catch (Exception e) {
			version.setText("");
			e.printStackTrace();
		}

		ImageButton checkUpdate = (ImageButton) findViewById(R.id.btn_fragment_settings_check_update);

		checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isCheckingUpdate) {
					checkUpdate(version);
				}
			}
		});

	}

	private void initFeedbackView() {

		ImageButton feedback = (ImageButton) findViewById(R.id.btn_fragment_settings_feedback);

		final View feedbackDetailView = findViewById(R.id.ll_fragment_settings_feedback);
		final EditText contentView = (EditText) findViewById(R.id.et_fragment_settings_content);
		final EditText emailView = (EditText) findViewById(R.id.et_fragment_settings_email);

		ImageButton confirmButton = (ImageButton) findViewById(R.id.btn_fragment_settings_done_feedback);

		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = contentView.getText().toString();
				String email = emailView.getText().toString();
				ExpandAnimation animation = new ExpandAnimation(
						feedbackDetailView, 500);
				if (content != null) {

					try {
						feedback(content, email);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				feedbackDetailView.startAnimation(animation);
			}
		});

		ImageButton cancelButton = (ImageButton) findViewById(R.id.btn_fragment_settings_cancel_feedback);

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExpandAnimation animation = new ExpandAnimation(
						feedbackDetailView, 500);
				contentView.setText(null);
				emailView.setText(null);
				feedbackDetailView.startAnimation(animation);
			}
		});

		feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExpandAnimation animation = new ExpandAnimation(
						feedbackDetailView, 500);
				feedbackDetailView.startAnimation(animation);
			}
		});

	}

	private void initInfoView() {

		ImageButton info = (ImageButton) findViewById(R.id.btn_fragment_settings_info);
		final View infoDetailView = findViewById(R.id.ll_fragment_settings_info);
		TextView infoDetail = (TextView) findViewById(R.id.tv_fragment_settings_info_detail);

		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExpandAnimation animation = new ExpandAnimation(infoDetailView,
						500);
				infoDetailView.startAnimation(animation);
				//showInfo();
			}
		});
	}

	protected void checkUpdate(final TextView tv) {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GlobalConstant.CHECKUPDATE:
					tv.setText("正在检查更新...");
					break;

				case GlobalConstant.NO_NEED_UPDATE_CLIENT:
					tv.setText("已是最新版本");
					break;

				case GlobalConstant.UPDATE_CLIENT:
					showUpdataDialog();
					tv.setText("当前版本：" + getVersion());
					break;
				case GlobalConstant.GET_UPDATEINFO_ERROR:
					tv.setText("获取服务器更新信息失败");
					break;

				case GlobalConstant.INSERT_SDCARD:
					tv.setText("请插入sd卡");
					break;

				case GlobalConstant.DOWN_ERROR:
					tv.setText("下载新版本失败");
					break;
				default:
					break;
				}
			}
		};

		Thread checkUpdateThread = new Thread(new Runnable() {

			@Override
			public void run() {

				isCheckingUpdate = true;

				try {
					Message messageUpdate = new Message();
					messageUpdate.what = GlobalConstant.CHECKUPDATE;
					handler.sendMessage(messageUpdate);
					String path = GlobalConstant.BAIDU_SERVER_UPDATE_URL;// getResources().getString(R.string.serverurl);
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(3000);
					InputStream is = conn.getInputStream();
					info = UpdateInfoParser.getUpdateInfo(is);

					String versionName = getVersionName();
					if (info.getVersion().equals(versionName)) { // versionname������һ������
						Message messageNoNeedToUpdate = new Message();
						messageNoNeedToUpdate.what = GlobalConstant.NO_NEED_UPDATE_CLIENT;
						handler.sendMessage(messageNoNeedToUpdate);

					} else {
						Message msg = new Message();
						msg.what = GlobalConstant.UPDATE_CLIENT;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = GlobalConstant.GET_UPDATEINFO_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
				isCheckingUpdate = false;
			}
		});

		checkUpdateThread.start();

	}

	protected void feedback(final String content, final String email)
			throws IOException {

		final String urlString = GlobalConstant.ROOT_URL + "/feedback.action";

		new Thread(new Runnable() {

			@Override
			public void run() {
				URL url = null;
				try {
					String data = "content=" + " from gunworld "
							+ URLEncoder.encode(content, "UTF-8") + "&email="
							+ URLEncoder.encode(email, "UTF-8");
					url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.setRequestProperty("Content-Length",
							String.valueOf(data.getBytes().length));
					OutputStream os = conn.getOutputStream();
					os.write(data.getBytes());
					os.flush();
					InputStream is = conn.getInputStream();
					os.close();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setTitle("版本升级");
		if (info != null) {

			builer.setMessage(info.getDescription());
		}
		builer.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						downLoadApk();
					}
				});
		builer.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	protected void downLoadApk() {
		final ProgressDialog pd;
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							info.getUrl(), pd);
					sleep(1000);
					if (file != null) {

						installApk(file);
					} else {
						Message msg = new Message();
						msg.what = GlobalConstant.INSERT_SDCARD;
						handler.sendMessage(msg);
					}
					pd.dismiss();
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = GlobalConstant.DOWN_ERROR;
					handler.sendMessage(msg);
					pd.dismiss();
					e.printStackTrace();

				}
			}
		}.start();
	}

	protected void installApk(File file) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		this.finish();
	}

	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
