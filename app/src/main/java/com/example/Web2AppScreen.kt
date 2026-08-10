package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Web2AppScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State webview
    var currentUrl by remember { mutableStateOf(AppConfig.WEB_URL) }
    var pageTitle by remember { mutableStateOf(AppConfig.APP_TITLE) }
    var progress by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var isFirstLoadFinished by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showUrlDialog by remember { mutableStateOf(false) }

    // Observe network connectivity
    val isOnline by NetworkUtils.observeNetworkStatus(context).collectAsState(initial = NetworkUtils.isOnline(context))

    // Reference to WebView
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // File Upload Handler Callback
    var filePathCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

    // File Picker Launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (filePathCallback != null) {
            val results = if (uris.isNotEmpty()) uris.toTypedArray() else null
            filePathCallback?.onReceiveValue(results)
            filePathCallback = null
        }
    }

    // Fullscreen Video container state
    var customView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    // Back press handler for Web history
    BackHandler(enabled = customView != null || canGoBack) {
        if (customView != null) {
            customViewCallback?.onCustomViewHidden()
            customView = null
        } else if (canGoBack && webViewInstance != null) {
            webViewInstance?.goBack()
        }
    }

    Scaffold(
        topBar = {
            if (AppConfig.SHOW_TOP_BAR) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = pageTitle.ifEmpty { AppConfig.APP_TITLE },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = currentUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { webViewInstance?.goBack() },
                            enabled = canGoBack
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali"
                            )
                        }
                    },
                    actions = {
                        if (canGoForward) {
                            IconButton(onClick = { webViewInstance?.goForward() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Maju"
                                )
                            }
                        }

                        IconButton(onClick = {
                            if (isError || !isOnline) {
                                isError = false
                                webViewInstance?.loadUrl(currentUrl)
                            } else {
                                webViewInstance?.reload()
                            }
                        }) {
                            Icon(
                                imageVector = if (isLoading) Icons.Default.Close else Icons.Default.Refresh,
                                contentDescription = "Muat Ulang"
                            )
                        }

                        IconButton(onClick = {
                            webViewInstance?.loadUrl(AppConfig.WEB_URL)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Halaman Utama"
                            )
                        }

                        IconButton(onClick = { showUrlDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Ganti URL"
                            )
                        }

                        IconButton(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, pageTitle)
                                putExtra(Intent.EXTRA_TEXT, currentUrl)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Link Website"))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Bagikan"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Web View
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        configureWebSettings(this, ctx)

                        webViewClient = object : WebViewClient() {
                            private fun injectSharePolyfill(view: WebView?) {
                                if (AppConfig.ENABLE_WEB_SHARE) {
                                    val polyfill = """
                                        if (typeof window.AndroidShare !== 'undefined') {
                                            navigator.share = function(data) {
                                                return new Promise(function(resolve, reject) {
                                                    try {
                                                        var title = (data && data.title) ? data.title : '';
                                                        var text = (data && data.text) ? data.text : '';
                                                        var url = (data && data.url) ? data.url : '';
                                                        window.AndroidShare.share(title, text, url);
                                                        resolve();
                                                    } catch(e) {
                                                        reject(e);
                                                    }
                                                });
                                            };
                                        }
                                    """.trimIndent()
                                    view?.evaluateJavascript(polyfill, null)
                                }
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                isError = false
                                if (url != null) currentUrl = url
                                injectSharePolyfill(view)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                isFirstLoadFinished = true
                                if (url != null) currentUrl = url
                                canGoBack = view?.canGoBack() == true
                                canGoForward = view?.canGoForward() == true
                                injectSharePolyfill(view)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    isError = true
                                    errorMessage = error?.description?.toString()
                                        ?: "Gagal terhubung ke server."
                                }
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val urlStr = request?.url?.toString() ?: return false

                                // Handle external schemes (WhatsApp, Phone, Email, Intent, etc.)
                                if (AppConfig.ALLOW_EXTERNAL_INTENTS && isExternalScheme(urlStr)) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlStr))
                                        ctx.startActivity(intent)
                                        return true
                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, "Tidak ada aplikasi untuk membuka link ini", Toast.LENGTH_SHORT).show()
                                        return true
                                    }
                                }

                                return false // Load in WebView
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                progress = newProgress
                                isLoading = newProgress < 100
                                if (newProgress >= 100) {
                                    isFirstLoadFinished = true
                                }
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                if (!title.isNullOrEmpty()) {
                                    pageTitle = title
                                }
                            }

                            override fun onShowFileChooser(
                                webView: WebView?,
                                filePathCallbackIn: ValueCallback<Array<Uri>>?,
                                fileChooserParams: FileChooserParams?
                            ): Boolean {
                                filePathCallback?.onReceiveValue(null)
                                filePathCallback = filePathCallbackIn

                                try {
                                    filePickerLauncher.launch("*/*")
                                } catch (e: Exception) {
                                    filePathCallback = null
                                    return false
                                }
                                return true
                            }

                            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                                customView = view
                                customViewCallback = callback
                            }

                            override fun onHideCustomView() {
                                customView = null
                                customViewCallback = null
                            }

                            override fun onCreateWindow(
                                view: WebView?,
                                isDialog: Boolean,
                                isUserGesture: Boolean,
                                resultMsg: android.os.Message?
                            ): Boolean {
                                if (AppConfig.ENABLE_GOOGLE_LOGIN_FIX) {
                                    val popupWebView = WebView(view?.context ?: ctx)
                                    configureWebSettings(popupWebView, ctx)
                                    popupWebView.webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(v: WebView?, req: WebResourceRequest?): Boolean {
                                            val url = req?.url?.toString() ?: return false
                                            view?.loadUrl(url)
                                            return true
                                        }
                                    }
                                    val transport = resultMsg?.obj as? WebView.WebViewTransport
                                    transport?.webView = popupWebView
                                    resultMsg?.sendToTarget()
                                    return true
                                }
                                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
                            }
                        }

                        loadUrl(AppConfig.WEB_URL)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top Progress Bar
            AnimatedVisibility(
                visible = isLoading && progress in 1..99 && (!AppConfig.SHOW_SPLASH_SCREEN || isFirstLoadFinished),
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Full Screen Splash Screen / Initial Loading Overlay
            if (AppConfig.SHOW_SPLASH_SCREEN && !isFirstLoadFinished) {
                AnimatedVisibility(
                    visible = !isFirstLoadFinished,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                modifier = Modifier.size(96.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shadowElevation = 6.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "App Logo",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(52.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = pageTitle.ifEmpty { AppConfig.APP_TITLE },
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Memuat aplikasi...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            }

            // Offline / Error Overlay
            if (!isOnline || isError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (!isOnline) Icons.Default.WifiOff else Icons.Default.Warning,
                                    contentDescription = "Error Icon",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = if (!isOnline) "Tidak Ada Koneksi Internet" else "Gagal Memuat Website",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (!isOnline)
                                "Pastikan Wi-Fi atau Data Seluler Anda dalam keadaan aktif untuk mengakses aplikasi ini."
                            else
                                errorMessage.ifEmpty { "Website sedang tidak bisa diakses. Periksa URL atau koneksi Anda." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = {
                                isError = false
                                webViewInstance?.reload()
                            },
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Coba Lagi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Fullscreen Custom View overlay (for videos)
            if (customView != null) {
                AndroidView(
                    factory = {
                        FrameLayout(it).apply {
                            addView(
                                customView,
                                FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim)
                )
            }
        }
    }

    // Dialog Ganti URL (Untuk testing cepat)
    if (showUrlDialog) {
        var tempUrl by remember { mutableStateOf(currentUrl) }

        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = { Text(text = "Ganti Link Website", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Masukkan URL website yang ingin Anda buka:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempUrl,
                        onValueChange = { tempUrl = it },
                        singleLine = true,
                        label = { Text("URL Website") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showUrlDialog = false
                    var formattedUrl = tempUrl.trim()
                    if (!formattedUrl.startsWith("http://") && !formattedUrl.startsWith("https://")) {
                        formattedUrl = "https://$formattedUrl"
                    }
                    currentUrl = formattedUrl
                    isError = false
                    webViewInstance?.loadUrl(formattedUrl)
                }) {
                    Text("Buka URL")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun configureWebSettings(webView: WebView, context: Context) {
    if (AppConfig.CLEAR_CACHE_ON_START && !AppConfig.ENABLE_OFFLINE_PWA_MODE) {
        webView.clearCache(true)
    }

    if (AppConfig.ENABLE_WEB_SHARE) {
        webView.addJavascriptInterface(AndroidShareBridge(context), "AndroidShare")
    }

    if (AppConfig.ENABLE_GOOGLE_LOGIN_FIX) {
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
    }

    webView.settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = true
        useWideViewPort = true
        loadWithOverviewMode = true
        setSupportZoom(true)
        builtInZoomControls = true
        displayZoomControls = false
        allowFileAccess = true
        allowContentAccess = true
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        
        if (AppConfig.ENABLE_GOOGLE_LOGIN_FIX) {
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
        }

        if (AppConfig.ENABLE_OFFLINE_PWA_MODE) {
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        } else {
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        if (AppConfig.DESKTOP_MODE) {
            userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        } else if (!AppConfig.CUSTOM_USER_AGENT.isNullOrEmpty()) {
            userAgentString = AppConfig.CUSTOM_USER_AGENT
        } else if (AppConfig.ENABLE_GOOGLE_LOGIN_FIX) {
            // Strip '; wv' and 'Version/X.X' so Google Auth endpoint doesn't reject standard Android WebView User-Agent
            val defaultUa = userAgentString ?: WebSettings.getDefaultUserAgent(context)
            val cleanUa = defaultUa.replace("; wv", "")
                .replace(Regex("Version/\\d+\\.\\d+\\s?"), "")
            userAgentString = cleanUa
        }
    }
}

private fun isExternalScheme(url: String): Boolean {
    return url.startsWith("whatsapp://") ||
            url.startsWith("https://wa.me") ||
            url.startsWith("tel:") ||
            url.startsWith("mailto:") ||
            url.startsWith("intent:") ||
            url.startsWith("market:") ||
            url.startsWith("tg:")
}

class AndroidShareBridge(private val context: Context) {
    @JavascriptInterface
    fun share(title: String?, text: String?, url: String?) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val parts = listOfNotNull(title, text, url).filter { it.isNotBlank() }
                val fullText = parts.joinToString("\n")
                putExtra(Intent.EXTRA_TEXT, fullText)
                if (!title.isNullOrBlank()) {
                    putExtra(Intent.EXTRA_SUBJECT, title)
                }
            }
            val chooser = Intent.createChooser(shareIntent, "Bagikan via")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
