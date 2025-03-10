$(document).ready(function () {
    let timerInterval;
    let minutes = 25;
    let seconds = 0;
    let isRunning = false;

    const timerDisplay = $("#timer");
    const timerStatus = $("#timer-status");
    const startBtn = $("#start-btn");
    const resetBtn = $("#reset-btn");

    function updateTimerDisplay() {
        const formattedMinutes = minutes < 10 ? "0" + minutes : minutes;
        const formattedSeconds = seconds < 10 ? "0" + seconds : seconds;
        timerDisplay.text(`${formattedMinutes}:${formattedSeconds}`);
    }

    function startTimer() {
        isRunning = true;
        timerStatus.text("Work Time");
        timerInterval = setInterval(function () {
            if (seconds === 0) {
                if (minutes === 0) {
                    clearInterval(timerInterval);
                    timerStatus.text("Time's Up! Take a break.");
                    isRunning = false;
                    return;
                }
                minutes--;
                seconds = 59;
            } else {
                seconds--;
            }
            updateTimerDisplay();
        }, 1000);
    }

    function resetTimer() {
        clearInterval(timerInterval);
        isRunning = false;
        minutes = 25;
        seconds = 0;
        timerStatus.text("Ready");
        updateTimerDisplay();
    }

    startBtn.on("click", function () {
        if (isRunning) {
            clearInterval(timerInterval);
            startBtn.text("Start");
            timerStatus.text("Paused");
        } else {
            startBtn.text("Pause");
            startTimer();
        }
    });

    resetBtn.on("click", function () {
        resetTimer();
        startBtn.text("Start");
    });

    // Initialize display
    updateTimerDisplay();
});
