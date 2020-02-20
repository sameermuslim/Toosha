package com.sameer.tashbih.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.getkeepsafe.taptargetview.TapTargetView.Listener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sameer.tashbih.R;

public final class GuidUtils {
    private static boolean isGuid;

    public static void addNew(final Context context) {
        if (isGuid()) {
            Activity activity = (Activity) context;

            final FloatingActionButton floatingActionButton = (FloatingActionButton) activity.findViewById(R.id.addTasbihBtn);

            TapTargetView.showFor(activity, TapTarget.forView(floatingActionButton, context.getString(R.string.newـ), context.getString(R.string.new_desc)).transparentTarget(true).targetRadius(35).cancelable(true), new Listener() {

                public void onTargetCancel(TapTargetView tapTargetView) {
                    super.onTargetCancel(tapTargetView);
                    GuidUtils.isGuid = false;

                }

                public void onTargetClick(TapTargetView tapTargetView) {
                    super.onTargetClick(tapTargetView);
                    LocalUtils.setOnceGuid(context, false);
                    floatingActionButton.performClick();

                }
            });
        }
    }

//    public static void am_pmDialog(Context context) {
//        View inflate = LayoutInflater.from(context).inflate(R.layout.am_pm_guid_dialog, null);
//        final AlertDialog create = new Builder(context).setView(inflate).create();
//        inflate.findViewById(R.id.cl_ok).setOnClickListener(new OnClickListener() {
//
//            public void onClick(View view) {
//                create.hide();
//
//            }
//        });
//        create.show();
//
//    }
//
//    public static void addActivity(final Context context) {
//        if (isGuid()) {
//
//            new TapTargetSequence((Activity) context).targets(TapTarget.forView(r0.findViewById(R.id.tp_silent), context.getString(R.string.volume_off), context.getString(R.string.volume_off_desc)).transparentTarget(true).targetRadius(82).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.tp_tone), context.getString(R.string.volume_up), context.getString(R.string.volume_up_desc)).transparentTarget(true).targetRadius(82).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.tv_Mon), context.getString(R.string.repeat), context.getString(R.string.repeat_desc)).transparentTarget(true).targetRadius(56).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.img_help), context.getString(R.string.help), context.getString(R.string.help_desc)).transparentTarget(true).targetRadius(18).outerCircleColor(R.color.colorPrimaryDark).cancelable(false)).listener(new TapTargetSequence.Listener() {
//                public void onSequenceCanceled(TapTarget tapTarget) {
//
//                }
//
//                public void onSequenceStep(TapTarget tapTarget, boolean z) {
//
//                }
//
//                public void onSequenceFinish() {
//                    GuidUtils.setIsGuid(false);
//                    GuidUtils.am_pmDialog(context);
//
//                }
//            }).start();
//        }
//    }

//    public static void datePicker(Context context) {
//
//        new TapTargetSequence((Activity) context).targets(TapTarget.forView(r0.findViewById(R.id.tp_silent), context.getString(R.string.volume_off), context.getString(R.string.volume_off_desc)).transparentTarget(true).targetRadius(82).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.tp_tone), context.getString(R.string.volume_up), context.getString(R.string.volume_up_desc)).transparentTarget(true).targetRadius(82).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.tv_Mon), context.getString(R.string.repeat), context.getString(R.string.repeat_desc)).transparentTarget(true).targetRadius(56).outerCircleColor(R.color.colorPrimaryDark).cancelable(false), TapTarget.forView(r0.findViewById(R.id.img_help), context.getString(R.string.help), context.getString(R.string.help_desc)).transparentTarget(true).targetRadius(18).outerCircleColor(R.color.colorPrimaryDark).cancelable(false)).listener(new TapTargetSequence.Listener() {
//                public void onSequenceCanceled(TapTarget tapTarget) {
//
//                }
//
//                public void onSequenceStep(TapTarget tapTarget, boolean z) {
//
//                }
//
//                public void onSequenceFinish() {
//                    GuidUtils.setIsGuid(false);
//
//                }
//            }).start();
//    }
    private static boolean isGuid() {
        return isGuid;
    }

    public static void setIsGuid(boolean z) {
        isGuid = z;
    }
}