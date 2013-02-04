package vsvteam.outsource.leanappandroid.exportcontrol;

import java.io.File;
import java.util.List;

import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class SendEmailController {
	private Context mContext;

	public SendEmailController(Context pContext) {
		// TODO Auto-generated constructor stub
		mContext = pContext;
	}

	public void upload(String pFilePath, int pTypeExport, boolean attachVideo,
			String pFileVideo) {
		initShareItent(pFilePath, pTypeExport, attachVideo, pFileVideo, "gmail");
	}

	private void initShareItent(String pFilePath, int typeExport,
			boolean attachVideo, String pFileVideo, String typeSend) {
		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		share.setType("text/plain");
//		if (typeExport == ActionExportActivity.FORMAT_EXCEL) {
//			MimeTypeMap mime = MimeTypeMap.getSingleton();
//			String mimeTypeForXLSFile = mime.getMimeTypeFromExtension(".xls");
//			share.setType(mimeTypeForXLSFile);
//		} else if (typeExport == ActionExportActivity.FORMAT_PDF) {
//			share.setType("application/pdf");
//		} else if (typeExport == ActionExportActivity.FORMAT_VIDEO) {
//			share.setType("video/3gp");
//		} else {
//			share.setType("text/plain");
//		}
		// gets the list of intents that can be loaded.
		List<ResolveInfo> resInfo = mContext.getPackageManager()
				.queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(
						typeSend)
						|| info.activityInfo.name.toLowerCase().contains(
								typeSend)) {
					share.putExtra(Intent.EXTRA_SUBJECT, "subject");
					share.putExtra(Intent.EXTRA_TEXT, "your text");
					if (pFilePath != null)
						share.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(pFilePath))); // class
					if (attachVideo && pFileVideo != null) {
						share.putExtra(Intent.EXTRA_STREAM,
								Uri.fromFile(new File(pFileVideo)));
					} // atrribute
					share.setPackage(info.activityInfo.packageName);
					found = true;
					break;
				}
			}
			if (!found)
				return;
			mContext.startActivity(Intent.createChooser(share, "Select"));
		}
	}
}
