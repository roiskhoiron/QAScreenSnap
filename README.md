# ScreenSnap SDK

<p align="center">
  <img src="screenshots/1.png" width="200px">
  <img src="screenshots/2.png" width="200px">
  <img src="screenshots/3.png" width="200px">
  <img src="screenshots/4.png" width="200px">
  <img src="screenshots/5.png" width="200px">
</p>

**ScreenSnap SDK** adalah sebuah Android SDK yang powerful untuk tim QA dalam melakukan screen
recording dengan mudah untuk keperluan bug reporting dan testing. SDK ini dirancang khusus untuk
mode QA-staging dengan fitur yang sederhana namun lengkap.

## Fitur Utama

- **Single Video Recording** - Hanya merecord 1 video saja untuk menghindari kebingungan
- **Share atau Upload** - Setelah recording selesai, bisa langsung dishare atau diupload ke endpoint
- **Kualitas Video Configurable** - LOW (480p), MEDIUM (720p), HIGH (1080p)
- **Audio Recording Optional** - Bisa enable/disable audio recording
- **Modern UI** - Material Design 3 yang clean dan mudah digunakan
- **State Monitoring** - StateFlow untuk monitoring status recording dan upload
- **Auto Cleanup** - Otomatis membersihkan file setelah upload berhasil
- **Easy Integration** - Mudah diintegrasikan dengan Hilt DI

## Struktur Project

```
ScreenSnap/
├── app/                    # Original ScreenSnap app
├── screensnap-sdk/         # Main SDK module
├── sample-app/             # Sample app demonstrating SDK usage
├── core_*                  # Core modules (UI, Screen Recorder, etc.)
├── feature_*               # Feature modules
└── domain_*                # Domain modules
```

## Quick Start

### 1. Menambahkan SDK ke Project

Tambahkan dependency ke `build.gradle.kts` aplikasi Anda:

```kotlin
dependencies {
    implementation(project(":screensnap-sdk"))
    
    // Required dependencies
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
}
```

### 2. Setup Application Class

```kotlin
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize ScreenSnap SDK
        val config = ScreenSnapSDKConfig(
            enableUpload = true,
            uploadEndpoint = "https://your-qa-server.com/api/upload",
            videoQuality = VideoQuality.MEDIUM,
            enableAudio = true,
            appName = "Your App QA"
        )
        
        ScreenSnapSDK.initialize(this, config)
    }
}
```

### 3. Menambahkan Permissions

Tambahkan ke `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.INTERNET" />

<application>
    <!-- SDK Components -->
    <activity
        android:name="com.screensnap.sdk.ui.QARecordingActivity"
        android:exported="false"
        android:theme="@style/Theme.Material3.DayNight" />
        
    <service
        android:name="com.screensnap.sdk.service.QARecordingService"
        android:exported="false"
        android:foregroundServiceType="mediaProjection" />
        
    <!-- FileProvider for sharing -->
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
</application>
```

### 4. Menggunakan SDK

#### Basic Usage - Simple Button

```kotlin
@Composable
fun QAButton() {
    val screenSnapSDK = hiltViewModel<YourViewModel>().screenSnapSDK
    
    Button(
        onClick = { 
            screenSnapSDK.showQARecordingScreen(this@YourActivity)
        }
    ) {
        Text("Start QA Recording")
    }
}
```

#### Advanced Usage - Custom UI dengan State Monitoring

```kotlin
@Composable
fun AdvancedQAScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val recordingState by viewModel.recordingState.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    
    Column {
        // Display current state
        Text("Recording: ${recordingState}")
        Text("Upload: ${uploadState}")
        
        // Action buttons based on state
        when (recordingState) {
            is ScreenSnapSDK.RecordingState.Idle -> {
                Button(onClick = { viewModel.startRecording() }) {
                    Text("Start Recording")
                }
            }
            is ScreenSnapSDK.RecordingState.Recording -> {
                Button(onClick = { viewModel.stopRecording() }) {
                    Text("Stop Recording")
                }
            }
            is ScreenSnapSDK.RecordingState.Stopped -> {
                Row {
                    Button(onClick = { viewModel.shareRecording() }) {
                        Text("Share")
                    }
                    Button(onClick = { viewModel.uploadRecording() }) {
                        Text("Upload")
                    }
                }
            }
        }
    }
}
```

## Sample App

Project ini dilengkapi dengan sample app (`sample-app/`) yang mendemonstrasikan berbagai cara
menggunakan SDK:

1. **Basic Integration** - Simple button untuk launch QA recording
2. **Advanced Integration** - Custom UI dengan state monitoring
3. **Features Overview** - Menampilkan semua fitur SDK

Untuk menjalankan sample app:

```bash
./gradlew :sample-app:installDebug
```

## Konfigurasi SDK

```kotlin
data class ScreenSnapSDKConfig(
    val enableUpload: Boolean = true,              // Enable upload functionality
    val uploadEndpoint: String? = null,            // Your QA server endpoint
    val maxRecordingDurationMinutes: Int = 10,     // Max recording duration
    val videoQuality: VideoQuality = VideoQuality.HIGH, // Video quality
    val enableAudio: Boolean = true,               // Record audio
    val enableFloatingButton: Boolean = true,      // Show floating controls
    val uploadTimeout: Long = 60000L,              // Upload timeout (ms)
    val enableNotifications: Boolean = true,       // Show notifications
    val appName: String = "QA Recording"           // App name in UI
)
```

### Video Quality Options

- `VideoQuality.LOW` - 480p (file kecil, upload cepat)
- `VideoQuality.MEDIUM` - 720p (balanced)
- `VideoQuality.HIGH` - 1080p (kualitas terbaik, file besar)

## Server Integration

### Upload Endpoint Requirements

Server Anda harus menerima multipart POST request dengan field:

- `video` - File MP4 video
- `description` - Deskripsi optional
- `device_info` - Info device (model, OS, app version)
- `timestamp` - Timestamp recording

### Contoh Server Implementation (Node.js)

```javascript
const express = require('express');
const multer = require('multer');
const app = express();

const upload = multer({ dest: 'uploads/' });

app.post('/api/upload', upload.single('video'), (req, res) => {
    const { description, device_info, timestamp } = req.body;
    const videoFile = req.file;
    
    console.log('QA Recording Upload:');
    console.log('- Description:', description);
    console.log('- Device Info:', device_info);
    console.log('- Video File:', videoFile.filename);
    
    // Process video (save to storage, notify QA team, etc.)
    
    res.json({ 
        success: true, 
        message: 'QA recording uploaded successfully'
    });
});
```

## Testing

### Unit Testing

```kotlin
@Test
fun `should handle QA recording launch`() {
    val mockSDK = mockk<ScreenSnapSDK>()
    every { mockSDK.showQARecordingScreen(any()) } just Runs
    
    viewModel.launchQARecording(mockActivity)
    
    verify { mockSDK.showQARecordingScreen(mockActivity) }
}
```

## Build Commands

```bash
# Build semua modules
./gradlew build

# Install original app
./gradlew :app:installDebug

# Install sample app
./gradlew :sample-app:installDebug

# Build SDK AAR
./gradlew :screensnap-sdk:assembleRelease

# Run tests
./gradlew test

# Check code style
./gradlew ktlintCheck
```

## Documentation

- [Integration Guide](INTEGRATION_GUIDE.md) - Panduan lengkap integrasi SDK
- [SDK README](screensnap-sdk/README.md) - Dokumentasi API SDK detail

## Use Cases

### 1. Debug Mode Only

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            val config = ScreenSnapSDKConfig(
                uploadEndpoint = "https://qa-debug.yourapp.com/upload",
                appName = "${getString(R.string.app_name)} - Debug"
            )
            ScreenSnapSDK.initialize(this, config)
        }
    }
}
```

### 2. Feature Flag Integration

```kotlin
if (featureFlags.isQARecordingEnabled()) {
    screenSnapSDK.showQARecordingScreen(this)
}
```

### 3. Custom Debug Menu

```kotlin
@Composable
fun DebugMenu() {
    Column {
        DebugMenuButton("Clear Cache") { /* ... */ }
        DebugMenuButton("Performance") { /* ... */ }
        DebugMenuButton("QA Recording") { 
            screenSnapSDK.showQARecordingScreen(this@DebugActivity)
        }
    }
}
```

## Troubleshooting

### Common Issues

1. **"ScreenSnapSDK not initialized"**
    - Pastikan `ScreenSnapSDK.initialize()` dipanggil di Application class

2. **Recording tidak start**
    - Check permissions sudah di-grant
    - Verify MediaProjection permission

3. **Upload gagal**
    - Verify endpoint URL dan network connectivity
    - Check server logs untuk error details

4. **File size terlalu besar**
    - Gunakan `VideoQuality.LOW` atau `VideoQuality.MEDIUM`
    - Kurangi `maxRecordingDurationMinutes`

## License

This SDK is provided under the MIT License. See LICENSE file for details.

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

- Cek [Integration Guide](INTEGRATION_GUIDE.md) untuk dokumentasi lengkap
- Review sample app untuk contoh implementasi
- Contact development team untuk support project-specific

---
**Version:** 1.0.0  
**Last Updated:** November 2024
