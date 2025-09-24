import android.os.CountDownTimer
import android.util.Log

class SportTimer(
    private val durationMinutes: Int,
    private val halfMinutes: Int
) {
    private var countDownTimer: CountDownTimer? = null
    private var secondsElapsed = 0
    private var isRunning = false

    fun startTimer() {
        countDownTimer = object : CountDownTimer(durationMinutes * 60_000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                Log.d("Timer", String.format("%02d:%02d", minutes, seconds))
                secondsElapsed++
            }

            override fun onFinish() {
                Log.d("Timer", "Half Time!")
            }
        }.start()

        isRunning = true
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        isRunning = false
    }

    fun reset() {
        pauseTimer()
        secondsElapsed = 0
    }

    fun getElapsedSeconds() = secondsElapsed
}
