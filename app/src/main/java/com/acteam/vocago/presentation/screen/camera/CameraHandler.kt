package com.acteam.vocago.presentation.screen.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.graphics.YuvImage
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Camera2Handler(
    private val context: Context,
    private val onImageCaptured: (Bitmap) -> Unit
) {
    var permissionGranted = false
    private var isCameraOpened = false

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var imageReader: ImageReader? = null

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val backgroundThread = HandlerThread("CameraBackground").apply { start() }
    private val backgroundHandler = Handler(backgroundThread.looper)

    private var textureView: TextureView? = null

    val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            tryOpenCamera(width, height, surface)
        }

        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            release()
            return true
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun tryOpenCamera(width: Int, height: Int, surfaceTexture: SurfaceTexture) {
        if (!permissionGranted || isCameraOpened) return

        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = manager.cameraIdList.first()
        surfaceTexture.setDefaultBufferSize(width, height)
        val previewSurface = Surface(surfaceTexture)

        imageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 2)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) return

        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                isCameraOpened = true

                val requestBuilder =
                    camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                        addTarget(previewSurface)
                    }

                val outputConfigs = listOf(
                    OutputConfiguration(previewSurface),
                    OutputConfiguration(imageReader!!.surface)
                )

                val sessionConfig = SessionConfiguration(
                    SessionConfiguration.SESSION_REGULAR,
                    outputConfigs,
                    executor,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            captureSession = session
                            session.setRepeatingRequest(requestBuilder.build(), null, null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {}
                    }
                )

                camera.createCaptureSession(sessionConfig)
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                camera.close()
                cameraDevice = null
            }
        }, backgroundHandler)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun bindTextureView(view: TextureView) {
        textureView = view
        if (view.isAvailable && permissionGranted && !isCameraOpened) {
            view.surfaceTexture?.let { surfaceTexture ->
                tryOpenCamera(view.width, view.height, surfaceTexture)
            }
        } else {
            view.surfaceTextureListener = surfaceTextureListener
        }
    }

    fun captureStillImage() {
        val reader = imageReader ?: return

        reader.setOnImageAvailableListener({ r ->
            val image = r.acquireLatestImage() ?: return@setOnImageAvailableListener
            try {
                val rawBitmap = image.toBitmap()
                val rotatedBitmap = rotateBitmapIfNeeded(rawBitmap)
                onImageCaptured(rotatedBitmap)
            } catch (e: Exception) {
            } finally {
                image.close()
            }
        }, backgroundHandler)

        val request =
            cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {
                addTarget(reader.surface)
            }?.build()

        if (request != null) {
            captureSession?.capture(request, null, backgroundHandler)
        }
    }

    fun release() {
        captureSession?.close()
        captureSession = null
        cameraDevice?.close()
        cameraDevice = null
        imageReader?.close()
        imageReader = null
        isCameraOpened = false
    }

    private fun rotateBitmapIfNeeded(bitmap: Bitmap): Bitmap {
        val rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display.rotation
        } else {
            @Suppress("DEPRECATION")
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.rotation
        }

        val degrees = when (rotation) {
            Surface.ROTATION_0 -> 90
            Surface.ROTATION_90 -> 0
            Surface.ROTATION_180 -> 270
            Surface.ROTATION_270 -> 180
            else -> 0
        }

        val matrix = Matrix().apply {
            postRotate(degrees.toFloat())
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun restartPreview() {
        if (permissionGranted && textureView != null && textureView!!.isAvailable) {
            release() // Giải phóng tài nguyên trước
            isCameraOpened = false // Reset trạng thái
            tryOpenCamera(textureView!!.width, textureView!!.height, textureView!!.surfaceTexture!!)
        }
    }
}

private fun Image.toBitmap(): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 90, out)
    val imageBytes = out.toByteArray()

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
