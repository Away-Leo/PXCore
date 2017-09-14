package com.pingxundata.pxcore.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 *  处理APP操作的工具类
 */
public class AppUtils {

	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * @Description 判断某一apk是否被安装到设备上
	 * @param context
	 * @param apkPackageName
	 *            app包名
	 * @return boolean
	 * @throws
	 */
	public static boolean appInstalled(Context context, String apkPackageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(apkPackageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		return packageInfo != null;
	}

	/**
	 * 判断该APK是否正在运行
	 *
	 * @param apkPackageName
	 *            想要判断的应用包名
	 * @return true 正在运行 false 未运行
	 *
	 * */
	public static boolean appIsRun(Context context, String apkPackageName) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(apkPackageName) && info.baseActivity.getPackageName().equals(apkPackageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取安装应用的详细信息
	 *
	 * @param packageName
	 *            安装应用的包名
	 * @return AppInfo
	 */
	private static AppInfo getPackageInfo(Context context, String packageName) {
		AppInfo packages = new AppInfo();
		PackageInfo packageInfo = new PackageInfo();
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
			return null;
		}
		if (packageInfo != null) {
			packages.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
			packages.setPackageName(packageInfo.packageName);
			packages.setVersionName(packageInfo.versionName);
			packages.setVersionCode(packageInfo.versionCode);
			packages.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
		} else {
			packages = null;
		}
		return packages;
	}

	/**
	 * 获取机器安装软件信息（包名、版本号、版本code、icon）
	 *
	 * @return ArrayList<AppInfo>
	 */
	public static ArrayList<AppInfo> getPackagesInfo(Context context) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// 非系统应用才会添加至appList
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				AppInfo tmpInfo = new AppInfo();
				tmpInfo = getPackageInfo(context, packageInfo.packageName);
				appList.add(tmpInfo);
			}
		}
		return appList;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 *
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	/**
	 * [获取应用程序版本名称信息]
	 *
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * app详细信息，包括应用名称，包名，版本号，图标等
	 * */

	private static class AppInfo {

		private String appName = "";// 应用名称
		private String packageName = "";// 应用包名
		private String versionName = "";// 版本名称
		private int versionCode = 0;// 版本ID
		private Drawable appIcon = null;// 应用图标

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public Drawable getAppIcon() {
			return appIcon;
		}

		public void setAppIcon(Drawable appIcon) {
			this.appIcon = appIcon;
		}

	}
}
