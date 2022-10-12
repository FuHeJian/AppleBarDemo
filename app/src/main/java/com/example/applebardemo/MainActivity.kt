package com.example.applebardemo

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.ViewGroup.LayoutParams.*
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toFile
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.applebardemo.ui.theme.AppleBarDemoTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {

    val thisActivity = this


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, FloatingBarService::class.java))
//        println("MainActivity 创建成功")
        setContent {
            AppleBarDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        AppleBar()
                        Button(onClick = { requestSettingCanDrawOverlays() }) {
                            Text(text = "申请悬浮窗权限")
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppleBar() {

    val context = LocalContext.current
    var visible by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(targetState = visible)

    val widthAnimate by transition.animateFloat(transitionSpec = {
        if (!visible) tween(300) else
            tween(800)
    }) {
        if (it) 0.9f else 0.5f
    }

    val heightAnimate by transition.animateDp(transitionSpec = {
        if (!visible) tween(300) else
            tween(800)
    }) {
        if (it) 200.dp else 40.dp
    }

/*    var title by remember {
        NotificationListen.mTitle
    }

    var contentStr by remember {
        NotificationListen.mContentStr
    }*/
    Column(modifier = Modifier.fillMaxWidth()) {
        //UI
/*        Row(horizontalArrangement = Arrangement.Center) {
            floatingBar()
        }*/
        Button(onClick = {
            try {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalContext.context?.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }) {
            Text(text = "申请权限")
        }
        Button(onClick = {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }) {
            Text(text = "申请权限")
        }
        Button(onClick = {

        }) {
            Text(text = "qq群:670394787")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun floatingBar(lview: View? = null, windowManager: WindowManager? = null) {

    var visible by remember {
        mutableStateOf(false)
    }
    var currentTitle = remember {
        ""
    }
    var notificationInfo by remember {
        NotificationListen.notificationInfo
    }

    val transition = updateTransition(targetState = visible)

    val widthAnimate by transition.animateFloat(transitionSpec = {
        if (!visible) tween(300) else
            tween(800)
    }) {
        if (it) 0.9f else 0f
    }


    val floatingPadding by transition.animateDp(transitionSpec = {
        tween(700, if (visible) 0 else 600)
    }) {
        if (it) 32.dp else 1.5.dp
    }

    val heightAnimate by transition.animateDp(transitionSpec = {
        tween(500, if (visible) 700 else 0, easing = FastOutLinearInEasing)
    }) {
        if (it) 100.dp else 30.dp
    }
    val widthAnimate2 by transition.animateDp(transitionSpec = {
        tween(500, if (visible) 700 else 0, easing = FastOutLinearInEasing)
    }) {
        if (it) 500.dp else 30.dp
    }
    val widthScale by transition.animateFloat(transitionSpec = {
        tween(500, if (visible) 700 else 0, easing = LinearEasing)
    }) {
        if (it) 1f else 0.06f
    }
    val heightScale by transition.animateFloat(transitionSpec = {
        tween(500, if (visible) 700 else 0, easing = LinearEasing)
    }) {
        if (it) 1f else 0.3f
    }

    var currentColor = remember {
        Color.Red
    }

//    if(currentTitle != (notificationInfo.mTitle ?: "null"))
//    {
//
//    }
//    else
//    {
//
//    }

    LaunchedEffect(key1 = notificationInfo) {
        if (notificationInfo.mTitle != ""&&!notificationInfo.mTitle.contains("正在其他应用的上层")) {
            visible = true
            delay(3000)
            visible = false
            notificationInfo = GlobalContext.notificationNullInfo
        }
    }

    val rememberInfiniteTransition = rememberInfiniteTransition()
    val rotationx by rememberInfiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = InfiniteRepeatableSpec(tween(easing = LinearEasing))
    )

    val mModifier = if (widthAnimate2 == 30.dp) Modifier
        .graphicsLayer {
            clip = true
            shape = RoundedCornerShape(30.dp)
            if (floatingPadding == 1.5.dp) rotationZ = rotationx
        }
        .background(
            brush = Brush.linearGradient(arrayListOf(Color.Red, Color.Blue)), alpha = 0.6f
        ) else Modifier
    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        Card(
                modifier = Modifier
                    .padding(top = floatingPadding)
                    .then(mModifier)
                    .height(heightAnimate)
                    .width(widthAnimate2)
        ,
//            .clickable {
//                visible = false
//                notificationInfo = GlobalContext.notificationNullInfo
//            },
        shape = RoundedCornerShape(30.dp),
        backgroundColor = if (heightAnimate == 30.dp) Color.Transparent else Color.Black,
        elevation = 10.dp
//                .animateContentSize(animationSpec = tween(200))
        ) {
        if(!notificationInfo.mTitle.contains("正在其他应用的上层")&&widthAnimate2 == 500.dp)
        {
            Row {
                currentTitle = notificationInfo.mTitle ?: ""
//            if(notificationInfo.mIcon!=null)
//            {
//                Icon(bitmap = notificationInfo.mIcon!!, contentDescription = "")
//            }
//            if (widthAnimate2 == 500.dp)
                Text(
                    text = notificationInfo.appName ?: "null",
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(4F)
                ) {
                    Text(
                        text = notificationInfo.mTitle ?: "null",
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        maxLines = 2
                    )
                    Text(
                        text = notificationInfo.mContentStr ?: "null",
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        maxLines = 2
                    )
                }
            }
        }

    }
    }


}

private fun requestSettingCanDrawOverlays() {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt >= Build.VERSION_CODES.O) { //8.0以上
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        GlobalContext.context?.startActivity(intent)
    } else if (sdkInt >= Build.VERSION_CODES.M) { //6.0-8.0
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("package:" + GlobalContext.context?.packageName)
        GlobalContext.context?.startActivity(intent)
    } else { //4.4-6.0以下
        //无需处理了
    }
}