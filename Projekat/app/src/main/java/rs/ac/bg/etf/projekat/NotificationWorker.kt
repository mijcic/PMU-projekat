package rs.ac.bg.etf.projekat
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.*
import java.util.concurrent.TimeUnit
import java.util.*

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        if (!isAppInForeground(applicationContext)) {
            showNotification(
                "Whodunit Reminder",
                "Vrati se i otkrij ko je ubica danas!"
            )
        }
        scheduleNextWork()
        return Result.success()
    }

    @SuppressLint("NotificationPermission")
    private fun showNotification(title: String, message: String) {
        val channelId = "daily_reminder_channel_v4"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val soundUri = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(false)
                setSound(soundUri, null)
                description = "Daily reminder to return to Whodunit"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun scheduleNextWork() {
        val nextWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWork)
    }
}

fun scheduleDailyReminder(context: Context) {
    val currentDate = Calendar.getInstance()
    val dueDate = Calendar.getInstance()

    dueDate.set(Calendar.HOUR_OF_DAY, 19)
    dueDate.set(Calendar.MINUTE, 57)
    dueDate.set(Calendar.SECOND, 0)

    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }

    val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

    val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context)
        .enqueueUniqueWork(
            "daily_notification",
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )
}

fun isAppInForeground(context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val processInfo = activityManager.runningAppProcesses?.firstOrNull {
            it.processName == context.packageName
        } ?: return false
        return processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
    } else {
        val runningTasks = activityManager.getRunningTasks(1)
        val topActivity = runningTasks[0].topActivity
        return topActivity?.packageName == context.packageName
    }
}