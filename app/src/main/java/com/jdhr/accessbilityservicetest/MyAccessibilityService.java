package com.jdhr.accessbilityservicetest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;
import java.util.Map;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    private Map<Integer, Boolean> handleMap = new HashMap<>();

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo serviceInfo = getServiceInfo();
        //指定监听包名
        serviceInfo.packageNames = new String[]{"com.android.packageinstaller", "com.jdhr.accessbilityservicetest"};
        setServiceInfo(serviceInfo);
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //用getSource无法监听到安装完成的界面，改用 getRootInActiveWindow
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            int eventType = event.getEventType();
            if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (handleMap.get(event.getWindowId()) == null) {
                    boolean handled = iterateNodesAndHandle(nodeInfo);
                    if (handled) {
                        handleMap.put(event.getWindowId(), true);
                    }
                }
            }

        }
    }

    //遍历节点，模拟点击安装按钮
    private boolean iterateNodesAndHandle(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            String nodeContent = null;
            if (null != nodeInfo.getText()) {
                nodeContent = nodeInfo.getText().toString();
                Log.i(TAG, "iterateNodesAndHandle: " + nodeContent);
            }
            if (nodeInfo.isCheckable()) {//华为，勾选同意安装
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else if (!TextUtils.isEmpty(nodeContent) && (nodeContent.contains("安装") || nodeContent.contains("确定"))) {
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            } else if (!TextUtils.isEmpty(nodeContent) && nodeContent.contains("打开")) {//安装完成后打开
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
//            else if (!TextUtils.isEmpty(nodeContent) && nodeContent.equals("删除安装包")) {
//                Log.i(TAG, "iterateNodesAndHandle:delete " + nodeInfo.getChild(2).getText().toString());
//                nodeInfo.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }
            //遇到ScrollView的时候模拟滑动一下
            else if ("android.widget.ScrollView".equals(nodeInfo.getClassName())) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
                if (iterateNodesAndHandle(childNodeInfo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isCheckable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    @Override
    public void onInterrupt() {

    }
}
