package com.screensnap.core.screen_recorder.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.MediaRecorder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default configuration values for the [MediaRecorder] and [SystemAudioRecorder].
 * These can be overriden by user settings.
 */
@Singleton
data class RecorderConfigValues(
    val screenWidth: Int,
    val screenHeight: Int,
    val screenDensity: Int,
    // Video settings
    val videoEncodingBitrate: Int = screenWidth * screenHeight * 5,
    val videoEncoder: Int = MediaRecorder.VideoEncoder.H264,
    val videoFrameRate: Int = 60,
    // Audio settings
    val audioEncoder: Int = MediaRecorder.AudioEncoder.AAC,
    val audioEncodingBitrate: Int = 128000,
    val audioSamplingRate: Int = 44100,
    val mediaRecorderOutputFormat: Int = MediaRecorder.OutputFormat.THREE_GPP,
    // AudioFormat settings
    val audioFormatEncoding: Int = AudioFormat.ENCODING_PCM_16BIT,
    val audioFormatSampleRate: Int = 44100, // 44.1[KHz] is guaranteed available
    val audioFormatChannelMask: Int = AudioFormat.CHANNEL_IN_MONO,
    // [AudioEncoder] settings
    val AUDIO_MIME_TYPE: String = MediaFormat.MIMETYPE_AUDIO_AAC,
    val AUDIO_BITRATE: Int = 64000, // 64 kbps
    // [SystemAudioRecorder] settings
    val AUDIO_BUFFER_SIZE: Int = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
    ),
    // [MediaMuxer] settings
    val mediaMuxerOutputFormat: Int = MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4,
    // [MediaCodec] settings
    val TIMEOUT: Long = 10000L,
    val VIDEO_MIME_TYPE: String = MediaFormat.MIMETYPE_VIDEO_AVC
) {
    companion object {
        fun createLowQualityConfig(
            screenWidth: Int,
            screenHeight: Int,
            screenDensity: Int
        ): RecorderConfigValues {
            return RecorderConfigValues(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                screenDensity = screenDensity,
                videoEncodingBitrate = (screenWidth * screenHeight * 2.5).toInt(),
                videoFrameRate = 30,
                audioEncodingBitrate = 64000,
                audioSamplingRate = 22050,
                AUDIO_BITRATE = 32000,
            )
        }

        fun createMediumQualityConfig(
            screenWidth: Int,
            screenHeight: Int,
            screenDensity: Int
        ): RecorderConfigValues {
            return RecorderConfigValues(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                screenDensity = screenDensity,
                videoEncodingBitrate = (screenWidth * screenHeight * 4).toInt(),
                videoFrameRate = 45,
                audioEncodingBitrate = 96000,
                audioSamplingRate = 44100,
                AUDIO_BITRATE = 64000,
            )
        }

        fun createHighQualityConfig(
            screenWidth: Int,
            screenHeight: Int,
            screenDensity: Int
        ): RecorderConfigValues {
            return RecorderConfigValues(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                screenDensity = screenDensity,
                videoEncodingBitrate = (screenWidth * screenHeight * 8).toInt(),
                videoFrameRate = 60,
                audioEncodingBitrate = 192000,
                audioSamplingRate = 48000,
                AUDIO_BITRATE = 128000,
            )
        }
    }
}